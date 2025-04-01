package org.toxsoft.skf.dq.skadmin.dq;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.skf.dq.skadmin.dq.IAdminHardResources.*;

import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.dq.skadmin.*;
import org.toxsoft.uskat.legacy.plexy.impl.*;
import org.toxsoft.uskat.skadmin.core.*;
import org.toxsoft.uskat.skadmin.core.impl.*;

/**
 * Константы пакета.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface IAdminHardConstants {

  /**
   * Префикс идентификаторов команд и их алиасов плагина
   */
  String CMD_PATH_PREFIX = AdminPluginDq.CMD_PATH;

  // ------------------------------------------------------------------------------------
  // AdminCmdRemoveConnected
  //
  String CMD_REMOVE_CONNECTED_ID    = CMD_PATH_PREFIX + "removeConnected";
  String CMD_REMOVE_CONNECTED_ALIAS = EMPTY_STRING;
  String CMD_REMOVE_CONNECTED_NAME  = EMPTY_STRING;
  String CMD_REMOVE_CONNECTED_DESCR = STR_CMD_REMOVE_CONNECTED;

  // ------------------------------------------------------------------------------------
  // AdminCmdAddConnected
  //
  String CMD_ADD_CONNECTED_ID    = CMD_PATH_PREFIX + "addConnected";
  String CMD_ADD_CONNECTED_ALIAS = EMPTY_STRING;
  String CMD_ADD_CONNECTED_NAME  = EMPTY_STRING;
  String CMD_ADD_CONNECTED_DESCR = STR_CMD_ADD_CONNECTED;

  // ------------------------------------------------------------------------------------
  // AdminCmdAddConnected
  //
  String CMD_GET_MARKS_ID    = CMD_PATH_PREFIX + "getMarks";
  String CMD_GET_MARKS_ALIAS = EMPTY_STRING;
  String CMD_GET_MARKS_NAME  = EMPTY_STRING;
  String CMD_GET_MARKS_DESCR = STR_CMD_GET_MARKS;

  /**
   * Аргумент : список {@link Gwid} идентификаторов
   */
  IAdminCmdArgDef ARG_GWIDS = new AdminCmdArgDef( "gwids", PlexyValueUtils.PT_LIST_VALOBJ, STR_GWIDS );

}
