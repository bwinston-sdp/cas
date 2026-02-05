package org.apereo.cas.qrbadge.authentication;

import module java.base;
import org.apereo.cas.authentication.credential.BasicIdentifiableCredential;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

/**
 * This is {@link BadgeAuthenticationTokenCredential}.
 *
 * @author Ben Winston
 * @since 8.0.0
 */

@Slf4j
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class BadgeAuthenticationTokenCredential extends BasicIdentifiableCredential {
    @Serial
    private static final long serialVersionUID = -8234522701132144037L;

    private String deviceId;

    public BadgeAuthenticationTokenCredential(final String token) {
        super(token);
        LOGGER.warn("Badge Auth!!!");
    }
}