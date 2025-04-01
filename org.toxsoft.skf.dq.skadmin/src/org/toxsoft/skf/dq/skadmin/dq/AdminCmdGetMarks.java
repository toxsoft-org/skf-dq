package org.toxsoft.skf.dq.skadmin.dq;

import static org.toxsoft.core.tslib.bricks.validator.ValidationResult.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.skf.dq.skadmin.dq.IAdminHardConstants.*;
import static org.toxsoft.skf.dq.skadmin.dq.IAdminHardResources.*;
import static org.toxsoft.uskat.skadmin.core.EAdminCmdContextNames.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.legacy.plexy.*;
import org.toxsoft.uskat.skadmin.core.*;
import org.toxsoft.uskat.skadmin.core.impl.*;

/**
 * Команда s5admin: возвращает метки ресурсов установленные у ресурсов.
 *
 * @author mvk
 */
public class AdminCmdGetMarks
    extends AbstractAdminCmd {

  /**
   * Конструктор
   */
  public AdminCmdGetMarks() {
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
    return CMD_GET_MARKS_ID;
  }

  @Override
  public String alias() {
    return CMD_GET_MARKS_ALIAS;
  }

  @Override
  public String nmName() {
    return CMD_GET_MARKS_NAME;
  }

  @Override
  public String description() {
    return CMD_GET_MARKS_DESCR;
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
  @SuppressWarnings( { "boxing", "nls" } )
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
      IMap<Gwid, IOptionSet> allMarks = service.getResourcesMarks( gwids.size() > 0 ? gwids : null );
      int index = 0;
      for( Gwid g : allMarks.keys() ) {
        IOptionSet marks = allMarks.getByKey( g );
        print( aCallback, "[%d] %s = \n%s", index++, g, OptionSetUtils.humanReadable( marks ) );
        print( aCallback, EMPTY_STRING );
      }

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

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  /**
   * Вывести сообщение в callback клиента
   *
   * @param aMessage String - текст сообщения
   * @param aArgs Object[] - аргументы сообщения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static void print( IAdminCmdCallback aCallback, String aMessage, Object... aArgs ) {
    aCallback.onNextStep( new ElemArrayList<>( info( aMessage, aArgs ) ), 0, 0, false );
  }
}
