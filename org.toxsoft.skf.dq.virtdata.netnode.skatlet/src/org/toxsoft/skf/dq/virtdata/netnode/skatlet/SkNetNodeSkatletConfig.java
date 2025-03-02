package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
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
   * Префикс имен параметров "идентификатор сетевого узла ISkNetNode".
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link Skid}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.skid0=@Skid[sk.Server[valcom.main]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.resources0=@GwidList[sk.Server[valcom.local0]$rtdata(online),sk.Server[valcom.local1]$rtdata(online)]]
   * </code>
   */
  public static final String NETNODE_ID_PREFIX = SYBSYSTEM_ID_PREFIX + ".skid";

  /**
   * Префикс имен параметров списка конкретных ({@link Gwid#isAbstract()}=false) идентификаторов ресурсов представляющих
   * параметр "Интегральная оценка состояния подключенных узлов" {@link ISkNetNode#RTDID_HEALTH} (или его аналога в
   * другом классе) у подключенных к сетевому узлу ресурсов.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IGwidList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.skid0=@Skid[sk.Server[valcom.main]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.healths=@GwidList[sk.Server[valcom.local0]$rtdata(health),sk.Server[valcom.local1]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.weights=@IntList[ 35, 65 ]
   * </code>
   */
  public static final String NETNODE_HEALTHS_PREFIX = SYBSYSTEM_ID_PREFIX + ".healths";

  /**
   * Префикс имен параметров списка весов параметра {@link ISkNetNode#RTDID_HEALTH} (или его аналога в другом классе) у
   * подключенных к сетевому узлу ресурсов при рассчете собственного {@link ISkNetNode#RTDID_HEALTH}.
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IIntList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.skid0=@Skid[sk.Server[valcom.main]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.healths=@GwidList[sk.Server[valcom.local0]$rtdata(health),sk.Server[valcom.local1]$rtdata(health)]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.weights=@IntList[ 35, 65 ]
   * </code>
   */
  public static final String NETNODE_WEIGHTS_PREFIX = SYBSYSTEM_ID_PREFIX + ".weights";

}
