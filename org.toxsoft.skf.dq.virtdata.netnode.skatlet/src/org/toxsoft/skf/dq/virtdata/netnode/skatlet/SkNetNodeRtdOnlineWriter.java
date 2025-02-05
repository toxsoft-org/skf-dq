package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.classes.ISkNetNode.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Писатель виртуального данного: {@link ISkNetNode#RTDID_ONLINE}.
 *
 * @author mvk
 */
final class SkNetNodeRtdOnlineWriter
    extends SkAbstractVirtDataCurrDataWriter {

  private final SkVirtDataCurrDataReader currdata;

  /**
   * Конструктор.
   *
   * @param aCoreApi {@link ISkCoreApi} API соединения.
   * @param aNetNodeId {@link Skid} идентификатор сетевого узла.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  SkNetNodeRtdOnlineWriter( ISkCoreApi aCoreApi, Skid aNetNodeId ) {
    super( aCoreApi, Gwid.createRtdata( aNetNodeId.classId(), aNetNodeId.strid(), RTDID_ONLINE ) );
    currdata = new SkVirtDataCurrDataReader( aCoreApi, aNetNodeId, new StringArrayList( RTDID_HEALTH ), this );
  }

  // ------------------------------------------------------------------------------------
  // SkAbstractVirtDataCurrDataWriter
  //
  @Override
  protected IAtomicValue doCalculateValue() {
    IAtomicValue health = currdata.get( RTDID_HEALTH );
    return avValobj( (health.isAssigned() && health.asInt() > 0) ? EConnState.ONLINE : EConnState.OFFLINE );
  }

  @Override
  public void doClose() {
    currdata.close();
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //
}
