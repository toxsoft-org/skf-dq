package org.toxsoft.skf.dq.skadmin;

import org.toxsoft.skf.dq.skadmin.dq.*;
import org.toxsoft.uskat.skadmin.core.plugins.*;

/**
 * Плагин s5admin: команды управления конфигурациями s5-шлюзов
 *
 * @author mvk
 */
public class AdminPluginDq
    extends AbstractPluginCmdLibrary {

  /**
   * ИД-путь команд которые находятся в плагине
   */
  public static final String CMD_PATH = "sk.ext.dq."; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Реализация абстрактных методов AbstractPluginCmdLibrary
  //
  @Override
  public String getName() {
    return getClass().getName();
  }

  @Override
  protected void doInit() {
    // Качество данных
    addCmd( new AdminCmdAddConnected() );
    addCmd( new AdminCmdRemoveConnected() );
    addCmd( new AdminCmdGetMarks() );
  }

  @Override
  protected void doClose() {
    // nop
  }
}
