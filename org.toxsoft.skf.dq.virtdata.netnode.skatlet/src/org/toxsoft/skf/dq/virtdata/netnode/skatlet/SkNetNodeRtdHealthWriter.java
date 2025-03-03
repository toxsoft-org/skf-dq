package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.dq.virtdata.netnode.skatlet.ISkResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.skf.dq.virtdata.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Писатель виртуального данного: {@link ISkNetNode#RTDID_HEALTH} или его аналога.
 *
 * @author mvk
 */
final class SkNetNodeRtdHealthWriter
    extends SkAbstractVirtDataCurrDataWriter {

  private final SkVirtDataDataQualityReader dataQuality;
  private final SkVirtDataCurrDataReader    currdata;
  private final IMap<Gwid, Integer>         weights;
  private int                               weightTotal = 0;

  /**
   * Конструктор.
   *
   * @param aCoreApi {@link ISkCoreApi} API соединения.
   * @param aHealthOutput {@link Gwid} конкретный ({@link Gwid#isAbstract()}=false) идентификатор ресурса
   *          представляющего параметр "Интегральная оценка состояния подключенных узлов"
   *          {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) у которого устанавливается значение
   *          параметра {@link ISkNetNode#RTDID_HEALTH}.
   * @param aHealthInputs {@link IGwidList} список параметров конкретных ({@link Gwid#isAbstract()}=false)
   *          идентификаторов подключенных к сетевому узлу ресурсов и представляющих параметр "Интегральная оценка
   *          состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) с помощью
   *          которого формируется значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * @param aInputWeigths {@link IIntList} список весов параметра {@link ISkNetNode#RTDID_HEALTH} (или его аналога в
   *          другом классе) у подключенных к сетевому узлу ресурсов при рассчете собственного
   *          {@link ISkNetNode#RTDID_HEALTH}.
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException неодинаковый размер списков идентикаторы параметра состояния не могут быть
   *           абстрактным {@link Gwid}.
   * @throws TsIllegalArgumentRtException неодинаковый размер списков состояния и весов.
   * @throws TsIllegalArgumentRtException в системе не найден параметр подключенного ресурса.
   */
  SkNetNodeRtdHealthWriter( ISkCoreApi aCoreApi, Gwid aHealthOutput, IGwidList aHealthInputs, IIntList aInputWeigths ) {
    super( aCoreApi, aHealthOutput );
    TsNullArgumentRtException.checkNulls( aHealthInputs, aInputWeigths );
    if( aHealthInputs.size() != aInputWeigths.size() ) {
      Integer h = Integer.valueOf( aHealthInputs.size() );
      Integer w = Integer.valueOf( aInputWeigths.size() );
      throw new TsIllegalArgumentRtException( ERR_DIMENSION_IS_NOT_EQUAL, aHealthOutput, h, w );
    }
    for( Gwid health : aHealthInputs ) {
      TsIllegalArgumentRtException.checkTrue( health.isAbstract() );
    }
    dataQuality = new SkVirtDataDataQualityReader( aCoreApi, aHealthInputs, this );
    currdata = new SkVirtDataCurrDataReader( aCoreApi, Skid.NONE, IStringList.EMPTY, this );
    currdata.addReadData( aHealthInputs );
    int wt = 0;
    IMapEdit<Gwid, Integer> w = new ElemMap<>();
    for( int index = 0, n = aHealthInputs.size(); index < n; index++ ) {
      Integer weigth = aInputWeigths.get( index );
      w.put( aHealthInputs.get( index ), weigth );
      wt += weigth.intValue();
    }
    weights = w;
    weightTotal = wt;
  }

  // ------------------------------------------------------------------------------------
  // SkAbstractVirtDataCurrDataWriter
  //
  @Override
  protected IAtomicValue doCalculateValue() {
    int inputQtty = dataQuality.resourceIds().size();
    if( inputQtty == 0 ) {
      // Нет подключенных узлов
      return avInt( 100 );
    }
    IMap<Gwid, IOptionSet> marks = dataQuality.getResourcesMarks();
    int retValue = 0;
    for( Gwid gwid : dataQuality.resourceIds() ) {
      IAtomicValue health = currdata.get( gwid );
      if( !health.isAssigned() ) {
        continue;
      }
      IAtomicValue notConnected = marks.findByKey( gwid ).findByKey( ISkDataQualityService.TICKET_ID_NO_CONNECTION );
      if( notConnected.asBool() ) {
        continue;
      }
      int weight = weights.getByKey( gwid ).intValue();
      retValue += (health.asInt() * weight) / weightTotal;
    }
    return avInt( retValue );
  }

  @Override
  protected void doClose() {
    dataQuality.close();
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
}
