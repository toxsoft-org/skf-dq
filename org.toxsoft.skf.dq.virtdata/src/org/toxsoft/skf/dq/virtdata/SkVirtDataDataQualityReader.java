package org.toxsoft.skf.dq.virtdata;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.skf.dq.lib.impl.*;
import org.toxsoft.uskat.core.*;

/**
 * {@link ISkDataQualityService} reader for a virtual data.
 *
 * @author mvk
 */
public class SkVirtDataDataQualityReader
    implements ICloseable, ISkDataQualityChangeListener {

  private final ISkDataQualityService  dataQualityService;
  private final IGwidList              resourceIds;
  private final IGenericChangeListener changeListener;

  /**
   * Constructor.
   *
   * @param aCoreApi {@link ISkCoreApi} connection API.
   * @param aResourceIds {@link IStringList} list of read resource IDs.
   * @param aChangeListener {@link IGenericChangeListener} input data change listener
   * @throws TsNullArgumentRtException any argument = null.
   * @throws TsIllegalArgumentRtException object of another class.
   */
  public SkVirtDataDataQualityReader( ISkCoreApi aCoreApi, IGwidList aResourceIds,
      IGenericChangeListener aChangeListener ) {
    TsNullArgumentRtException.checkNulls( aCoreApi, aChangeListener );
    // register dataquality service if necessary
    if( !aCoreApi.services().hasKey( ISkDataQualityService.SERVICE_ID ) ) {
      aCoreApi.addService( SkDataQualityService.CREATOR );
    }
    dataQualityService = aCoreApi.getService( ISkDataQualityService.SERVICE_ID );
    resourceIds = new GwidList( aResourceIds );
    changeListener = aChangeListener;
    // register change-state listener
    dataQualityService.eventer().addListener( this );
  }

  // ------------------------------------------------------------------------------------
  // Public API
  //
  /**
   * Returns list of read resource IDs.
   *
   * @return {@link IGwidList} resource IDs
   */
  public IGwidList resourceIds() {
    return resourceIds;
  }

  /**
   * Возвращает значения пометок для нескольких ресурсов сразу.
   * <p>
   * {@link Gwid} ресурсов должны представлять данные объектов. Абстрактные {@link Gwid#isAbstract()} (без объекта(ов))
   * не допускаются.
   * <p>
   * ДОПУСКАЮТСЯ групповые ({@link Gwid#isMulti()} == true, адресация нескольких данных) идентификаторы.
   * <p>
   * Примеры возможных {@link Gwid}:
   * <ul>
   * <li>CtPot[potObj1]$rtdata( alive ).</li>
   * <li>CtPot[potObj1]$rtdata( * ).</li>
   * <li>CtPot[*]$rtdata( alive ).</li>
   * <li>CtPot[*]$rtdata( * ).</li>
   * </ul>
   *
   * @return IMap&lt;{@link Gwid},{@link IOptionSet}&gt; карта "ресурс" - "значения пометок"
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException запрет абстрактных {@link Gwid} - должен быть указан объект или объекты(*)
   * @throws TsIllegalArgumentRtException {@link Gwid} не представляют данное {@link EGwidKind#GW_RTDATA}
   * @throws TsIllegalArgumentRtException {@link Gwid} несуществующего класса, объекта или данного
   */
  public IMap<Gwid, IOptionSet> getResourcesMarks() {
    return getResourcesMarks( resourceIds );
  }

  /**
   * Возвращает значения пометок для нескольких ресурсов сразу.
   * <p>
   * {@link Gwid} ресурсов должны представлять данные объектов. Абстрактные {@link Gwid#isAbstract()} (без объекта(ов))
   * не допускаются.
   * <p>
   * ДОПУСКАЮТСЯ групповые ({@link Gwid#isMulti()} == true, адресация нескольких данных) идентификаторы.
   * <p>
   * Примеры возможных {@link Gwid}:
   * <ul>
   * <li>CtPot[potObj1]$rtdata( alive ).</li>
   * <li>CtPot[potObj1]$rtdata( * ).</li>
   * <li>CtPot[*]$rtdata( alive ).</li>
   * <li>CtPot[*]$rtdata( * ).</li>
   * </ul>
   *
   * @param aResources {@link IGwidList} список запрашиваемых ресурсов
   * @return IMap&lt;{@link Gwid},{@link IOptionSet}&gt; карта "ресурс" - "значения пометок"
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException запрет абстрактных {@link Gwid} - должен быть указан объект или объекты(*)
   * @throws TsIllegalArgumentRtException {@link Gwid} не представляют данное {@link EGwidKind#GW_RTDATA}
   * @throws TsIllegalArgumentRtException {@link Gwid} несуществующего класса, объекта или данного
   */
  IMap<Gwid, IOptionSet> getResourcesMarks( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    return dataQualityService.getResourcesMarks( aResources );
  }

  // ------------------------------------------------------------------------------------
  // ISkDataQualityChangeListener
  //
  @Override
  public void onResourcesStateChanged( ISkDataQualityService aSource, String aTicketId ) {
    changeListener.onGenericChangeEvent( this );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //
  @Override
  public void close() {
    // deregister change-state listener
    dataQualityService.eventer().removeListener( this );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

}
