package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.skf.dq.virtdata.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Писатель виртуального данного: {@link ISkNetNode#RTDID_ONLINE} или его аналога.
 *
 * @author mvk
 */
final class SkNetNodeRtdOnlineWriter
    extends SkAbstractVirtDataCurrDataWriter {

  private final SkVirtDataDataQualityReader dataQuality;

  /**
   * Конструктор.
   *
   * @param aCoreApi {@link ISkCoreApi} API соединения.
   * @param aOnlineOutput {@link Gwid} конкретный ({@link Gwid#isAbstract()}=false) идентификатор ресурса
   *          представляющего параметр "Интегральная оценка состояния подключенных узлов"
   *          {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) у которого устанавливается значение
   *          параметра {@link ISkNetNode#RTDID_HEALTH}.
   * @param aOnlineInputs {@link IGwidList} список параметров конкретных ({@link Gwid#isAbstract()}=false)
   *          идентификаторов подключенных к сетевому узлу ресурсов и представляющих параметр "Интегральная оценка
   *          состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) с помощью
   *          которого формируется значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  SkNetNodeRtdOnlineWriter( ISkCoreApi aCoreApi, Gwid aOnlineOutput, IGwidList aOnlineInputs ) {
    super( aCoreApi, aOnlineOutput );
    dataQuality = new SkVirtDataDataQualityReader( aCoreApi, aOnlineInputs, this );
  }

  // ------------------------------------------------------------------------------------
  // SkAbstractVirtDataCurrDataWriter
  //
  @Override
  protected IAtomicValue doCalculateValue() {
    IMap<Gwid, IOptionSet> marks = dataQuality.getResourcesMarks();
    for( Gwid gwid : dataQuality.resourceIds() ) {
      IAtomicValue notConnected = marks.findByKey( gwid ).findByKey( ISkDataQualityService.TICKET_ID_NO_CONNECTION );
      if( !notConnected.asBool() ) {
        return avValobj( EConnState.ONLINE );
      }
    }
    return avValobj( EConnState.OFFLINE );
  }

  @Override
  public void doClose() {
    dataQuality.close();
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
}
