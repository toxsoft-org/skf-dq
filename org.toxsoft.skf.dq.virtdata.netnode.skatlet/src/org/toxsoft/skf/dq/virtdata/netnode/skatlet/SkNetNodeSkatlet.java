package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.dq.virtdata.netnode.skatlet.ISkResources.*;
import static org.toxsoft.skf.dq.virtdata.netnode.skatlet.SkNetNodeSkatletConfig.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.threadexec.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.virtdata.*;

/**
 * Virtual Data Skatlet Writer for {@link ISkNetNode}.
 *
 * @author mvk
 */
public class SkNetNodeSkatlet
    extends SkatletBase {

  private final IListEdit<SkAbstractVirtDataCurrDataWriter> writers = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public SkNetNodeSkatlet() {
    super( SkNetNodeSkatlet.class.getSimpleName(), OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_SKATLET, //
        TSID_DESCRIPTION, STR_D_SKATLET //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // SkatletBase
  //
  @Override
  public void start() {
    super.start();
    IOptionSet configs = environ().params();
    ISkCoreApi coreApi = getSharedConnection().coreApi();
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( coreApi );
    threadExecutor.syncExec( () -> {
      int i = 0;
      while( true ) {
        int index = i++;
        IAtomicValue id = configs.findValue( NETNODE_ID_PREFIX + index );
        IAtomicValue healths = configs.findValue( NETNODE_HEALTHS_PREFIX + index );
        IAtomicValue weights = configs.findValue( NETNODE_WEIGHTS_PREFIX + index );
        if( id == null && healths == null && weights == null ) {
          break;
        }
        if( id == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_ID_PREFIX + index );
        }
        if( healths == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTHS_PREFIX + index );
        }
        if( weights == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_WEIGHTS_PREFIX + index );
        }
        Skid nodeId = id.asValobj();
        IGwidList nodeHealths = healths.asValobj();
        IIntList nodeWeights = weights.asValobj();
        if( !coreApi.sysdescr().hierarchy().isAssignableFrom( ISkNetNode.CLASS_ID, nodeId.classId() ) ) {
          throw new TsIllegalArgumentRtException();
        }
        writers.add( new SkNetNodeRtdHealthWriter( coreApi, nodeId, nodeHealths, nodeWeights ) );
        writers.add( new SkNetNodeRtdOnlineWriter( coreApi, nodeId ) );
      }
      // Register dataquality list
      GwidList writeDataIds = new GwidList();
      for( SkAbstractVirtDataCurrDataWriter writer : writers ) {
        writeDataIds.add( writer.writeDataId() );
      }
      if( writeDataIds.size() > 0 ) {
        ISkDataQualityService dataQualityService = coreApi.getService( ISkDataQualityService.SERVICE_ID );
        dataQualityService.addConnectedResources( writeDataIds );
      }
    } );
    logger().info( "%s: start(). writers count = %d", id(), Integer.valueOf( writers.size() ) ); //$NON-NLS-1$
  }

  @Override
  public boolean queryStop() {
    super.queryStop();
    ISkCoreApi coreApi = getSharedConnection().coreApi();
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( coreApi );
    threadExecutor.syncExec( () -> {
      // Deregister dataquality list
      GwidList writeDataIds = new GwidList();
      for( SkAbstractVirtDataCurrDataWriter writer : writers ) {
        writeDataIds.add( writer.writeDataId() );
        writer.close();
      }
      if( writeDataIds.size() > 0 ) {
        ISkDataQualityService dataQualityService = coreApi.getService( ISkDataQualityService.SERVICE_ID );
        dataQualityService.removeConnectedResources( writeDataIds );
      }
    } );
    logger().info( "%s: queryStop(). virtual data writers (%d) are closed", id(), Integer.valueOf( writers.size() ) ); //$NON-NLS-1$

    return true;
  }

}
