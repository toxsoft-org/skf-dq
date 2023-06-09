package org.toxsoft.skf.dq.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.metainfo.IDataType;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.bricks.events.AbstractTsEventer;
import org.toxsoft.core.tslib.bricks.events.ITsEventer;
import org.toxsoft.core.tslib.bricks.events.msg.GenericMessage;
import org.toxsoft.core.tslib.bricks.filter.ITsCombiFilterParams;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.coll.IMap;
import org.toxsoft.core.tslib.coll.IMapEdit;
import org.toxsoft.core.tslib.coll.impl.ElemMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.gwid.IGwidList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.core.ISkServiceCreator;
import org.toxsoft.uskat.core.devapi.IDevCoreApi;
import org.toxsoft.uskat.core.impl.AbstractSkService;

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
    switch( aMessage.messageId() ) {
      case SkDataQualityMsgResourceChanged.MSG_ID: {
        String resourceId = SkDataQualityMsgResourceChanged.INSTANCE.getResourceId( aMessage );
        eventer.fireResourcesStateChangedEvent( resourceId );
        return true;
      }
      case SkDataQualityMsgTicketsChanged.MSG_ID: {
        eventer.fireTickesChangedEvent();
        return true;
      }
      default:
        return false;
    }
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
    TsNullArgumentRtException.checkNull( aResources );
    if( !coreApi().backend().isActive() ) {
      // Бекенд недоступен.
      IOptionSetEdit options = new OptionSet();
      // По умолчанию TICKET_ID_NO_CONNECTION = true
      options.setValue( TICKET_ID_NO_CONNECTION, AV_TRUE );
      IMapEdit<Gwid, IOptionSet> retValue = new ElemMap<>();
      for( Gwid gwid : aResources ) {
        retValue.put( gwid, options );
      }
      return retValue;
    }
    return backend().getResourcesMarks( aResources );
  }

  @Override
  public IMap<Gwid, IOptionSet> queryMarkedUgwies( ITsCombiFilterParams aQueryParams ) {
    TsNullArgumentRtException.checkNull( aQueryParams );
    return backend().queryMarkedUgwies( aQueryParams );
  }

  @Override
  public IGwidList getConnectedResources() {
    // TODO: отработка если нет связи, локальное сохранение списка ресурсов
    return backend().getConnectedResources();
  }

  @Override
  public void addConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    // TODO: отработка если нет связи, локальное сохранение списка ресурсов
    backend().addConnectedResources( aResources );
  }

  @Override
  public void removeConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    // TODO: отработка если нет связи, локальное сохранение списка ресурсов
    backend().removeConnectedResources( aResources );
  }

  @Override
  public IGwidList setConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    // TODO: отработка если нет связи, локальное сохранение списка ресурсов
    return backend().setConnectedResources( aResources );
  }

  @Override
  public void setMarkValue( String aTicketId, IAtomicValue aValue, IGwidList aResources ) {
    TsNullArgumentRtException.checkNulls( aTicketId, aValue, aResources );
    // TODO: отработка если нет связи, локальное сохранение списка ресурсов со значением метки
    backend().setMarkValue( aTicketId, aValue, aResources );
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
