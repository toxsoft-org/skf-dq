package org.toxsoft.skf.dq.virtdata.netnode.skatlet;

/**
 * Локализуемые ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  String STR_N_SKATLET = "Виртуальные данные проекта VAL (AnalogChannel)";
  String STR_D_SKATLET = "Формирование виртуальных данных проекта VAL (AnalogChannel)";

  String STR_CONNECTED   = "Ресурсы";
  String STR_CONNECTED_D = "Список идентификаторов проверяемых подключенных к системе ресурсов.";

  String STR_RTD_HEALTH_GWID   = "Состояние";
  String STR_RTD_HEALTH_GWID_D =
      "Идентификатор текущего данного представляющего интегральную оценку состояния подключенных ресурсов.";

  String ERR_NOT_FOUND = "%s is not found";
}
