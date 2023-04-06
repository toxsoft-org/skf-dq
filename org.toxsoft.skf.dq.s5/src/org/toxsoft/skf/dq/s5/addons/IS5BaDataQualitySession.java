package org.toxsoft.skf.dq.s5.addons;

import javax.ejb.Remote;

import org.toxsoft.skf.dq.lib.IBaDataQuality;
import org.toxsoft.uskat.s5.server.backend.addons.IS5BackendAddonSession;

/**
 * Сессия расширения backend {@link IBaDataQuality}
 *
 * @author mvk
 */
@Remote
public interface IS5BaDataQualitySession
    extends IBaDataQuality, IS5BackendAddonSession {
  // nop
}
