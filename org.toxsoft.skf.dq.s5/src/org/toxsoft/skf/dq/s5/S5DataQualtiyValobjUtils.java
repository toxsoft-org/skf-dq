package org.toxsoft.skf.dq.s5;

import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;
import org.toxsoft.skf.dq.lib.impl.SkDataQualityTicket;
import org.toxsoft.skf.dq.s5.supports.S5DataQualityTicketList;

/**
 * Регистрация хранителей данных подсистемы.
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
