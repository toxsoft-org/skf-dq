package org.toxsoft.skf.dq.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.events.msg.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Реализация службы {@link ISkDataQualityService}.
 *
 * @author mvk
 */
public class SkDataQualityService
    extends AbstractSkService
    implements ISkDataQualityService {

  /**
   * Service creator singleton.
   */
  public static final ISkServiceCreator<AbstractSkService> CREATOR = SkDataQualityService::new;

  /**
   * Класс для реализации {@link ISkDataQualityService#eventer()}.
   */
  class Eventer
      extends AbstractTsEventer<ISkDataQualityChangeListener> {

    private boolean         onTicketChangedFlag;
    private IStringListEdit onResourcesStateChangedList = null;

    @Override
    protected boolean doIsPendingEvents() {
      return false;
    }

    @Override
    protected void doFirePendingEvents() {
      reallyFire();
    }

    private void reallyFire() {
      if( onTicketChangedFlag ) {
        for( ISkDataQualityChangeListener l : listeners() ) {
          try {
            l.onTicketsChanged( SkDataQualityService.this );
          }
          catch( Exception ex ) {
            LoggerUtils.errorLogger().error( ex );
          }
        }
        onTicketChangedFlag = false;
      }
      if( onResourcesStateChangedList != null ) {
        for( ISkDataQualityChangeListener l : listeners() ) {
          for( String resourceId : onResourcesStateChangedList ) {
            try {
              l.onResourcesStateChanged( SkDataQualityService.this, resourceId );
            }
            catch( Exception ex ) {
              LoggerUtils.errorLogger().error( ex );
            }
          }
        }
        onResourcesStateChangedList = null;
      }
    }

    @Override
    protected void doClearPendingEvents() {
      onTicketChangedFlag = false;
      onResourcesStateChangedList = null;
    }

    void fireTickesChangedEvent() {
      onTicketChangedFlag = true;
      if( !isPendingEvents() ) {
        reallyFire();
      }
    }

    void fireResourcesStateChangedEvent( String aResourceId ) {
      if( onResourcesStateChangedList == null ) {
        onResourcesStateChangedList = new StringArrayList();
      }
      onResourcesStateChangedList.add( aResourceId );
      if( !isPendingEvents() ) {
        reallyFire();
      }
    }
  }

  private final Eventer eventer = new Eventer();

  /**
   * Конструктор службы.
   *
   * @param aCoreApi {@link IDevCoreApi} API ядра uskat
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SkDataQualityService( IDevCoreApi aCoreApi ) {
    super( ISkDataQualityService.SERVICE_ID, aCoreApi );
    // backend = aCoreApi.getBackendAddon( S5_BACKEND_DATAQUALITY_ID, ISkBackendAddonDataQuality.class );
    // // Регистрация обработчиков сообщений бекенда
    // aCoreApi.registerMessageHandler( WHEN_DATAQUALITY_TICKETS_CHANGED, new SkMessageWhenDataQualityTicketsChanged() {
    //
    // @Override
    // protected void doWhenDataQualityTicketsChanged() {
    // eventer.pauseFiring();
    // try {
    // eventer.addOnTickesListChanged();
    // }
    // finally {
    // eventer.resumeFiring( true );
    // }
    // }
    // } );
    // aCoreApi.registerMessageHandler( WHEN_DATAQUALITY_RESOURCES_STATE_CHANGED,
    // new SkMessageWhenDataQualityResourcesStateChanged() {
    //
    // @Override
    // protected void doWhenDataQualityResourcesStateChanged( String aResourceId ) {
    // eventer.pauseFiring();
    // try {
    // eventer.addOnResourcesStateChangedList( aResourceId );
    // }
    // finally {
    // eventer.resumeFiring( true );
    // }
    // }
    // } );
  }

  // ------------------------------------------------------------------------------------
  // Реализация шаблонных методов класса AbstractSkService
  //
  @Override
  protected void doInit( ITsContextRo aArgs ) {
    // nop
  }

  @Override
  protected void doClose() {
    // nop
  }

  @Override
  protected boolean onBackendMessage( GenericMessage aMessage ) {
    return switch( aMessage.messageId() ) {
      case SkDataQualityMsgResourceChanged.MSG_ID -> {
        String resourceId = SkDataQualityMsgResourceChanged.INSTANCE.getResourceId( aMessage );
        eventer.fireResourcesStateChangedEvent( resourceId );
        yield true;
      }
      case SkDataQualityMsgTicketsChanged.MSG_ID -> {
        eventer.fireTickesChangedEvent();
        yield true;
      }
      default -> false;
    };
  }

  @Override
  protected void onBackendActiveStateChanged( boolean aIsActive ) {
    // Установка/разрыв связи с сервером
    eventer.fireResourcesStateChangedEvent( TICKET_ID_NO_CONNECTION );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса ISkDataQualityService
  //
  @Override
  public IOptionSet getResourceMarks( Gwid aResource ) {
    TsNullArgumentRtException.checkNull( aResource );
    if( !coreApi().backend().isActive() ) {
      // Бекенд недоступен.
      IOptionSetEdit options = new OptionSet();
      // По умолчанию TICKET_ID_NO_CONNECTION = true
      options.setValue( TICKET_ID_NO_CONNECTION, AV_TRUE );
      return options;
    }
    return backend().getResourceMarks( aResource );
  }

  @Override
  public IMap<Gwid, IOptionSet> getResourcesMarks( IGwidList aResources ) {
    if( coreApi().backend().isActive() ) {
      return backend().getResourcesMarks( aResources );
    }
    if( aResources == null ) {
      return IMap.EMPTY;
    }
    IMapEdit<Gwid, IOptionSet> retValue = new ElemMap<>();
    // Бекенд недоступен
    IOptionSetEdit options = new OptionSet();
    // По умолчанию TICKET_ID_NO_CONNECTION = true
    options.setValue( TICKET_ID_NO_CONNECTION, AV_TRUE );
    for( Gwid gwid : aResources ) {
      retValue.put( gwid, options );
    }
    return retValue;
  }

  @Override
  public IMap<Gwid, IOptionSet> queryMarkedUgwies( ITsCombiFilterParams aQueryParams ) {
    TsNullArgumentRtException.checkNull( aQueryParams );
    return backend().queryMarkedUgwies( aQueryParams );
  }

  @Override
  public IGwidList getConnectedResources() {
    return backend().getConnectedResources();
  }

  @Override
  public void addConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    backend().addConnectedResources( aResources );
  }

  @Override
  public void removeConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    backend().removeConnectedResources( aResources );
  }

  @Override
  public IGwidList setConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    return backend().setConnectedResources( aResources );
  }

  @Override
  public void setMarkValue( String aTicketId, IAtomicValue aValue, IGwidList aResources ) {
    TsNullArgumentRtException.checkNulls( aTicketId, aValue, aResources );
    backend().setMarkValue( aTicketId, aValue, aResources );
  }

  @Override
  public void setConnectedAndMarkValues( String aTicketId, IMap<Gwid, IAtomicValue> aValues ) {
    TsNullArgumentRtException.checkNulls( aTicketId, aValues );
    backend().setConnectedAndMarkValues( aTicketId, aValues );
  }

  @Override
  public IStridablesList<ISkDataQualityTicket> listTickets() {
    // TODO: хранить список тикетов
    return backend().listTickets();
  }

  @Override
  public ISkDataQualityTicket defineTicket( String aTicketId, String aName, String aDescription, IDataType aDataType ) {
    TsNullArgumentRtException.checkNulls( aTicketId, aName, aDescription, aDataType );
    return backend().defineTicket( aTicketId, aName, aDescription, aDataType );
  }

  @Override
  public void removeTicket( String aTicketId ) {
    TsNullArgumentRtException.checkNull( aTicketId );
    backend().removeTicket( aTicketId );
  }

  @Override
  public ITsEventer<ISkDataQualityChangeListener> eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  private IBaDataQuality backend() {
    return coreApi().backend().findBackendAddon( IBaDataQuality.ADDON_ID, IBaDataQuality.class );
  }
}
