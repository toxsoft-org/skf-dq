package org.toxsoft.skf.dq.skadmin.dq;

import static org.toxsoft.skf.dq.skadmin.dq.IAdminHardConstants.*;
import static org.toxsoft.skf.dq.skadmin.dq.IAdminHardResources.*;
import static org.toxsoft.uskat.skadmin.core.EAdminCmdContextNames.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.legacy.plexy.*;
import org.toxsoft.uskat.skadmin.core.*;
import org.toxsoft.uskat.skadmin.core.impl.*;

/**
 * Команда s5admin: удалить {@link Gwid}-идентификаторы ресурсов из списка ресурсов подключенных к текущей сессии
 *
 * @author mvk
 */
public class AdminCmdRemoveConnected
    extends AbstractAdminCmd {

  /**
   * Конструктор
   */
  public AdminCmdRemoveConnected() {
    // Контекст: API ISkConnection
    addArg( CTX_SK_CORE_API );
    // Идентификатор моста
    addArg( ARG_GWIDS );
    // Имя моста
  }

  // ------------------------------------------------------------------------------------
  // Реализация абстрактных методов AbstractAdminCmd
  //
  @Override
  public String id() {
    return CMD_REMOVE_CONNECTED_ID;
  }

  @Override
  public String alias() {
    return CMD_ADD_CONNECTED_ALIAS;
  }

  @Override
  public String nmName() {
    return CMD_ADD_CONNECTED_NAME;
  }

  @Override
  public String description() {
    return CMD_ADD_CONNECTED_DESCR;
  }

  @Override
  public IPlexyType resultType() {
    return IPlexyType.NONE;
  }

  @Override
  public IStringList roles() {
    return IStringList.EMPTY;
  }

  @Override
  public void doExec( IStringMap<IPlexyValue> aArgValues, IAdminCmdCallback aCallback ) {
    // API сервера
    ISkCoreApi coreApi = argSingleRef( CTX_SK_CORE_API );
    ISkDataQualityService service =
        (ISkDataQualityService)coreApi.services().getByKey( ISkDataQualityService.SERVICE_ID );

    IList<IAtomicValue> argGwids = argValueList( ARG_GWIDS );
    try {
      long startTime = System.currentTimeMillis();
      GwidList gwids = new GwidList();
      for( IAtomicValue argGwid : argGwids ) {
        gwids.add( argGwid.asValobj() );
      }
      // Передача конфигурации на сервер
      service.removeConnectedResources( gwids );

      long delta = (System.currentTimeMillis() - startTime) / 1000;
      addResultInfo( MSG_CMD_TIME, Long.valueOf( delta ) );
      resultOk();
    }
    finally {
      // nop
    }
  }

  @Override
  protected IList<IPlexyValue> doPossibleValues( String aArgId, IStringMap<IPlexyValue> aArgValues ) {
    return IList.EMPTY;
  }
}
