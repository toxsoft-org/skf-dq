package org.toxsoft.skf.dq.s5;

import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.dq.lib.impl.*;
import org.toxsoft.skf.dq.s5.supports.*;

/**
 * Регистрация хранителей данных подсистемы.
 * <p>
 *
 * @author mvk
 */
public class S5DataQualtiyValobjUtils {

  /**
   * Регистрация известных хранителей
   */
  public static void registerS5Keepers() {
    TsValobjUtils.registerKeeperIfNone( SkDataQualityTicket.KEEPER_ID, SkDataQualityTicket.KEEPER );
    TsValobjUtils.registerKeeperIfNone( S5DataQualityTicketList.KEEPER_ID, S5DataQualityTicketList.KEEPER );
  }
}
