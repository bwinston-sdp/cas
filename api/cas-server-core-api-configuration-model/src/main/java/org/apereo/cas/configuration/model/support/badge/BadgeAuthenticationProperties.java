package org.apereo.cas.configuration.model.support.badge;

import module java.base;
import org.apereo.cas.configuration.support.RequiresModule;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * This is {@link org.apereo.cas.configuration.model.support.badge.BadgeAuthenticationProperties}.
 *
 * @author Ben Winston
 * @since 8.0.0
 */
@RequiresModule(name = "cas-server-support-qrbadge")
@Getter
@Setter
@Accessors(chain = true)
public class BadgeAuthenticationProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = 8726382874579042118L;
}
