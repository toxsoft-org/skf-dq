package org.toxsoft.skf.dq.s5.addons;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.msg.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.skf.dq.s5.supports.*;
import org.toxsoft.uskat.s5.server.backend.addons.*;

/**
 * Local {@link IBaDataQuality} implementation.
 *
 * @author mvk
 */
public final class S5BaDataQualityLocal
    extends S5AbstractBackendAddonLocal
    implements IBaDataQuality {

  /**
   * Поддержка бекенда службы качества данных
   */
  private final IS5BackendDataQualitySingleton dataQualitySupport;

  /**
   * Данные конфигурации фронтенда для {@link IBaDataQuality}
   */
  private final S5BaDataQualityData baData = new S5BaDataQualityData();

  /**
   * Constructor.
   *
   * @param aOwner {@link IS5BackendLocal} - the owner backend
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public S5BaDataQualityLocal( IS5BackendLocal aOwner ) {
    super( aOwner, ISkDataQualityServiceHardConstants.BAINF_DATA_QUALITY );
    dataQualitySupport = aOwner.backendSingleton().findSupport( S5BackendDataQualitySingleton.BACKEND_DATA_QUALITY_ID,
        IS5BackendDataQualitySingleton.class );
    // Установка конфигурации фронтенда
    frontend().frontendData().setBackendAddonData( IBaDataQuality.ADDON_ID, baData );
  }

  // ------------------------------------------------------------------------------------
  // BackendAddonBase
  //
  @Override
  public void onBackendMessage( GtMessage aMessage ) {
    // if( aMessage.messageId().equals( S5BaAfterInitMessages.MSG_ID ) ) {
    // }
  }

  @Override
  public void close() {
    // // Список идентификаторов открытых запросов
    // IStringList queryIds;
    // synchronized (baData) {
    // queryIds = new StringArrayList( baData.openQueries.keys() );
    // }
    // // Завершение работы открытых запросов
    // for( String queryId : queryIds ) {
    // queriesSupport.close( frontend(), queryId );
    // }
  }

  // ------------------------------------------------------------------------------------
  // Реализация IBaDataQuality
  //
  @Override
  public IOptionSet getResourceMarks( Gwid aResource ) {
    TsNullArgumentRtException.checkNull( aResource );
    return dataQualitySupport.getResourceMarks( aResource );
  }

  @Override
  public IMap<Gwid, IOptionSet> getResourcesMarks( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    return dataQualitySupport.getResourcesMarks( aResources );
  }

  @Override
  public IMap<Gwid, IOptionSet> queryMarkedUgwies( ITsCombiFilterParams aQueryParams ) {
    TsNullArgumentRtException.checkNull( aQueryParams );
    return dataQualitySupport.queryMarkedUgwies( aQueryParams );
  }

  @Override
  public IGwidList getConnectedResources() {
    return dataQualitySupport.getConnectedResources( Skid.NONE );
  }

  @Override
  public void addConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    dataQualitySupport.addConnectedResources( sessionID(), aResources );
  }

  @Override
  public void removeConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    dataQualitySupport.removeConnectedResources( sessionID(), aResources );
  }

  @Override
  public IGwidList setConnectedResources( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    return dataQualitySupport.setConnectedResources( sessionID(), aResources );
  }

  @Override
  public void setMarkValue( String aTicketId, IAtomicValue aValue, IGwidList aResources ) {
    TsNullArgumentRtException.checkNulls( aTicketId, aValue, aResources );
    dataQualitySupport.setMarkValue( aTicketId, aValue, aResources );
  }

  @Override
  public void setMarkValues( String aTicketId, IMap<Gwid, IAtomicValue> aValues ) {
    TsNullArgumentRtException.checkNulls( aTicketId, aValues );
    dataQualitySupport.setMarkValues( aTicketId, aValues );
  }

  @Override
  public IStridablesList<ISkDataQualityTicket> listTickets() {
    return dataQualitySupport.listTickets();
  }

  @Override
  public ISkDataQualityTicket defineTicket( String aTicketId, String aName, String aDescription, IDataType aDataType ) {
    TsNullArgumentRtException.checkNulls( aTicketId, aName, aDescription, aDataType );
    return dataQualitySupport.defineTicket( aTicketId, aName, aDescription, aDataType );
  }

  @Override
  public void removeTicket( String aTicketId ) {
    TsNullArgumentRtException.checkNull( aTicketId );
    dataQualitySupport.removeTicket( aTicketId );
  }
}
