package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.s5.server.backend.supports.skatlets.*;

/**
 * Параметры конфигурации подсистемы
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
public final class SkNetNodeSkatletConfig {

  /**
   * Префикс идентфикаторов подсистемы
   * <p>
   * Хранение значений параметров конфигурации возможно во внешнем хранилище подсистемы скатлетов поэтому префикс имеет
   * предопределенное значение.
   */
  public static final String SYBSYSTEM_ID_PREFIX = S5BackendSkatletsConfig.SYBSYSTEM_ID_PREFIX + ".virtdata.netnode";

  /**
   * Префикс имени параметра конкретного ({@link Gwid#isAbstract()}=false) идентификатора ресурса представляющего
   * параметр "Интегральная оценка состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его аналога в
   * другом классе) у которого устанавливается значение параметра {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link Gwid}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[@GwidList[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.weights0=@IntList[ 35, 65 ]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output0=@Gwid[sk.Server[valcom.server.local1]$rtdata(online)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * </code>
   */
  public static final String NETNODE_HEALTH_OUTPUT_PREFIX = SYBSYSTEM_ID_PREFIX + ".health.output";

  /**
   * Префикс имен списка параметров конкретных ({@link Gwid#isAbstract()}=false) идентификаторов подключенных к сетевому
   * узлу ресурсов и представляющих параметр "Интегральная оценка состояния подключенных узлов"
   * {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) с помощью которого формируется значение параметра
   * {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IGwidList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[@GwidList[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.weights0=@IntList[ 35, 65 ]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output0=@Gwid[sk.Server[valcom.server.local1]$rtdata(online)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * </code>
   */
  public static final String NETNODE_HEALTH_INPUTS_PREFIX = SYBSYSTEM_ID_PREFIX + ".health.inputs";

  /**
   * Префикс имен параметров списка весов параметра {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) у
   * подключенных к сетевому узлу ресурсов при рассчете собственного {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IIntList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[@GwidList[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.weights0=@IntList[ 35, 65 ]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output0=@Gwid[sk.Server[valcom.server.local1]$rtdata(online)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * </code>
   */
  public static final String NETNODE_HEALTH_WEIGHTS_PREFIX = SYBSYSTEM_ID_PREFIX + ".health.weights";

  /**
   * Префикс имени параметра конкретного ({@link Gwid#isAbstract()}=false) идентификатора ресурса представляющего
   * параметр "Включен" {@link ISkNetNode#RTDID_ONLINE} (или его аналога в другом классе) у которого устанавливается
   * значение параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link Gwid}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[@GwidList[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.weights0=@IntList[ 35, 65 ]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output0=@Gwid[sk.Server[valcom.server.local1]$rtdata(online)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * </code>
   */
  public static final String NETNODE_ONLINE_OUTPUT_PREFIX = SYBSYSTEM_ID_PREFIX + ".online.output";

  /**
   * Префикс имен списка параметров конкретных ({@link Gwid#isAbstract()}=false) идентификаторов любых подключенных к
   * сетевому узлу ресурсов с помощью которого формируется значение параметра {@link ISkNetNode#RTDID_ONLINE}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IGwidList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.output0=@Gwid[@GwidList[sk.Server[valcom.server.main]$rtdata(health)]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.inputs0=@GwidList[sk.Server[valcom.server.local1]$rtdata(health),sk.Server[valcom.server.local2]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.health.weights0=@IntList[ 35, 65 ]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.output0=@Gwid[sk.Server[valcom.server.local1]$rtdata(online)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.online.input0=@Gwid[sk.Server[valcom.server.local1]$rtdata(health)]]
   * </code>
   */
  public static final String NETNODE_ONLINE_INPUTS_PREFIX = SYBSYSTEM_ID_PREFIX + ".online.inputs";
}
