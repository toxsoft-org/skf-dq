package org.toxsoft.skf.dq.lib.impl;

import org.toxsoft.core.tslib.bricks.events.msg.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.core.backend.api.*;

/**
 * {@link IBaDataQuality} message builder: change tickets notification.
 *
 * @author mvk
 */
public class SkDataQualityMsgTicketsChanged
    extends AbstractBackendMessageBuilder {

  /**
   * ID of the message.
   */
  public static final String MSG_ID = "QualityDataTicketsChanged"; //$NON-NLS-1$

  /**
   * Singletone intance.
   */
  public static final SkDataQualityMsgTicketsChanged INSTANCE = new SkDataQualityMsgTicketsChanged();

  SkDataQualityMsgTicketsChanged() {
    super( ISkDataQualityService.SERVICE_ID, MSG_ID );
  }

  /**
   * Creates the message instance.
   *
   * @return {@link GtMessage} - created instance of the message
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public GtMessage makeMessage() {
    return makeMessageVarargs();
  }
}
