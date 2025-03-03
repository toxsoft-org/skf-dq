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
      // health
      int i = 0;
      while( true ) {
        int index = i++;
        IAtomicValue avHealthOutput = configs.findValue( NETNODE_HEALTH_OUTPUT_PREFIX + index );
        IAtomicValue avHealthInputs = configs.findValue( NETNODE_HEALTH_INPUTS_PREFIX + index );
        IAtomicValue avHealthWeights = configs.findValue( NETNODE_HEALTH_WEIGHTS_PREFIX + index );
        if( avHealthOutput == null && avHealthInputs == null && avHealthWeights == null ) {
          break;
        }
        if( avHealthOutput == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_OUTPUT_PREFIX + index );
        }
        if( avHealthInputs == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_INPUTS_PREFIX + index );
        }
        if( avHealthWeights == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_WEIGHTS_PREFIX + index );
        }
        Gwid healthOutput = avHealthOutput.asValobj();
        IGwidList healthInputs = avHealthInputs.asValobj();
        IIntList healthWeights = avHealthWeights.asValobj();
        // В качестве выходного параметра может быть использован аналог
        // if( !coreApi.sysdescr().hierarchy().isAssignableFrom( ISkNetNode.CLASS_ID, healthOutput.classId() ) ) {
        // throw new TsIllegalArgumentRtException();
        // }
        writers.add( new SkNetNodeRtdHealthWriter( coreApi, healthOutput, healthInputs, healthWeights ) );
      }
      // online
      i = 0;
      while( true ) {
        int index = i++;
        IAtomicValue avOnlineOutput = configs.findValue( NETNODE_ONLINE_OUTPUT_PREFIX + index );
        IAtomicValue avOnlineInputs = configs.findValue( NETNODE_ONLINE_INPUTS_PREFIX + index );
        if( avOnlineOutput == null && avOnlineInputs == null ) {
          break;
        }
        if( avOnlineOutput == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_OUTPUT_PREFIX + index );
        }
        if( avOnlineInputs == null ) {
          throw new TsItemNotFoundRtException( ERR_NOT_FOUND, NETNODE_HEALTH_INPUTS_PREFIX + index );
        }
        Gwid onlineOutput = avOnlineOutput.asValobj();
        IGwidList onlineInputs = avOnlineInputs.asValobj();
        // В качестве выходного параметра может быть использован аналог
        // if( !coreApi.sysdescr().hierarchy().isAssignableFrom( ISkNetNode.CLASS_ID, healthOutput.classId() ) ) {
        // throw new TsIllegalArgumentRtException();
        // }
        writers.add( new SkNetNodeRtdOnlineWriter( coreApi, onlineOutput, onlineInputs ) );
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
