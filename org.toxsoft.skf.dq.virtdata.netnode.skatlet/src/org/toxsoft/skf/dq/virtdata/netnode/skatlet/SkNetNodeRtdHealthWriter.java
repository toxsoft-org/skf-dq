package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.skf.dq.virtdata.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Писатель виртуального данного: {@link ISkNetNode#RTDID_HEALTH}
 *
 * @author mvk
 */
final class SkNetNodeRtdHealthWriter
    extends SkAbstractVirtDataCurrDataWriter {

  private SkVirtDataDataQualityReader dataQuality;

  /**
   * Конструктор.
   *
   * @param aCoreApi {@link ISkCoreApi} API соединения.
   * @param aNetNodeId {@link Skid} идентификатор сетевого узла.
   * @param aResourceIds {@link IGwidList} список идентификаторов ресурсов подключаемых к сетевому узлу.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  SkNetNodeRtdHealthWriter( ISkCoreApi aCoreApi, Skid aNetNodeId, IGwidList aResourceIds ) {
    super( aCoreApi, Gwid.createRtdata( aNetNodeId.classId(), aNetNodeId.strid(), ISkNetNode.RTDID_HEALTH ) );
    dataQuality = new SkVirtDataDataQualityReader( aCoreApi, aResourceIds, this );
  }

  // ------------------------------------------------------------------------------------
  // SkAbstractVirtDataCurrDataWriter
  //
  @Override
  protected IAtomicValue doCalculateValue() {
    int linkedNodeQtty = dataQuality.resourceIds().size();
    if( linkedNodeQtty == 0 ) {
      // Нет подключенных узлов
      return avInt( 100 );
    }
    IMap<Gwid, IOptionSet> marks = dataQuality.getResourcesMarks();
    int counter = 0;
    for( Gwid gwid : marks.keys() ) {
      IAtomicValue notConnected = marks.findByKey( gwid ).findByKey( ISkDataQualityService.TICKET_ID_NO_CONNECTION );
      if( !notConnected.asBool() ) {
        counter++;
      }
    }
    int retValue = (counter == linkedNodeQtty ? 100 : (counter * 100) / linkedNodeQtty);
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
