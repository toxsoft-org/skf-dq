package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
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
   * Префикс имен параметров "список идентификаторов ресурсов подключаемых к сетевому узлу ISkNetNode".
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} ({@link IGwidList}).
   * <p>
   * Пример: <code>
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.skid0=@Skid[sk.Server[valcom.main]]
   * -Dorg.toxsoft.uskat.skatlets.virtdata.netnode.resources0=@GwidList[sk.Server[valcom.local0]$rtdata(online),sk.Server[valcom.local1]$rtdata(online)]]
   * </code>
   */
  public static final String NETNODE_RESOURCES_PREFIX = SYBSYSTEM_ID_PREFIX + ".resources";

  // /**
  // * Требование обновить системное описание согласно указанной конфигурации узлов.
  // * <p>
  // * Тип: {@link EAtomicType}({@link IGwidList}).
  // */
  // public static final IDataDef UPDATE_SYSDESCR = create( SYBSYSTEM_ID_PREFIX + ".updateSysdescr", BOOLEAN, //
  // TSID_NAME, STR_UPDATE_SYSDESCR, //
  // TSID_DESCRIPTION, STR_UPDATE_SYSDESCR_D, //
  // TSID_DEFAULT_VALUE, avBool( false ), //
  // TSID_IS_MANDATORY, AV_FALSE //
  // );

}
