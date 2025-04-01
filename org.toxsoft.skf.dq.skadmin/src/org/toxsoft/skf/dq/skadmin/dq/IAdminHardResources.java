package org.toxsoft.skf.dq.skadmin.dq;

/**
 * Локализуемые ресурсы пакета.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface IAdminHardResources {

  // ------------------------------------------------------------------------------------
  // AdminCmdAddConnected
  //
  String STR_CMD_ADD_CONNECTED =
      "Добавить {@link Gwid}-идентификаторы ресурсов из списка ресурсов подключенных к текущей сессии.";

  // ------------------------------------------------------------------------------------
  // AdminCmdRemoveConnected
  //
  String STR_CMD_REMOVE_CONNECTED =
      "Удалить {@link Gwid}-идентификаторы ресурсов из списка ресурсов подключенных к текущей сессии.";

  // ------------------------------------------------------------------------------------
  // AdminCmdGetMarks
  //
  String STR_CMD_GET_MARKS = "Возвращает метки ресурсов.";

  String STR_GWIDS = "Список GWID-идентификаторов ресурсов";

  String MSG_CMD_TIME         = "Время выполнения команды: %d (мсек).";
  String ERR_READ_GWID_CONFIG =
      "Ошибка чтения значения аргумента %s. Значение должно быть в формате SkGatewayGwids.KEEPER. Причина: %s";
}
