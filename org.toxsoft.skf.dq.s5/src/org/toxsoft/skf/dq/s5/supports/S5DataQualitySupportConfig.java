package org.toxsoft.skf.dq.s5.supports;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.dq.s5.supports.IS5Resources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.s5.utils.*;

/**
 * Конфигурация поддержки расширения бекенда для службы качества данных {@link ISkDataQualityService}
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
public class S5DataQualitySupportConfig
    extends S5RegisteredConstants {

  /**
   * Префикс идентфикаторов подсистемы
   */
  public static final String SYBSYSTEM_ID_PREFIX = ISkDataQualityService.SERVICE_ID;

  /**
   * Список зарегистрированных тикетов
   * <p>
   * Тип: {@link EAtomicType#VALOBJ} содержит {@link S5DataQualityTicketList}
   */
  public static final IDataDef DQ_TICKETS = register( SYBSYSTEM_ID_PREFIX + ".tickets", EAtomicType.VALOBJ, //
      TSID_NAME, STR_N_TICKETS, //
      TSID_DESCRIPTION, STR_D_TICKETS, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_IS_MANDATORY, AV_FALSE, //
      TSID_DEFAULT_VALUE, avValobj( new S5DataQualityTicketList() ) );

  /**
   * Все параметры подсистемы.
   */
  public static final IStridablesList<IDataDef> ALL_DQ_OPDEFS = new StridablesList<>( //
      DQ_TICKETS //
  );
}
