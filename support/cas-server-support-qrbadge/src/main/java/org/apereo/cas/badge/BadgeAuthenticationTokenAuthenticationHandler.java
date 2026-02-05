package org.apereo.cas.qrbadge.authentication;

import module java.base;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

/**
 * This is {@link BadgeAuthenticationTokenAuthenticationHandler}.
 *
 * @author Ben Winston
 * @since 8.0.0
 */
@Slf4j
public class BadgeAuthenticationTokenAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

//    private final QRAuthenticationTokenValidatorService tokenValidatorService;
//
//    public QRAuthenticationTokenAuthenticationHandler(
//            final PrincipalFactory principalFactory,
//            final QRAuthenticationTokenValidatorService tokenValidatorService) {
//
//        super(StringUtils.EMPTY, principalFactory, 0);
//        this.tokenValidatorService = tokenValidatorService;
//    }

    public BadgeAuthenticationTokenAuthenticationHandler(final PrincipalFactory principalFactory) {
        super(StringUtils.EMPTY, principalFactory, null);
        LOGGER.warn("in BaseAuthenticationTokenAuthenticationHandler");
    }

    @Override
    public boolean supports(final Credential credential) {
        return BadgeAuthenticationTokenCredential.class.isAssignableFrom(credential.getClass());
    }

    @Override
    public boolean supports(final Class<? extends Credential> clazz) {
        return BadgeAuthenticationTokenCredential.class.isAssignableFrom(clazz);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(final Credential credential, final Service service) throws GeneralSecurityException {
        val tokenCredential = (BadgeAuthenticationTokenCredential) credential;
        try {
            LOGGER.debug("Received token [{}]", tokenCredential.getId());

//            val request = QRAuthenticationTokenValidationRequest.builder()
//                    .token(tokenCredential.getId())
//                    .registeredService(Optional.empty())
//                    .deviceId(tokenCredential.getDeviceId())
//                    .build();
//
//            val result = tokenValidatorService.validate(request);
            //val principal = result.getAuthentication().getPrincipal();
            Principal principal = getPrincipalFactory().createPrincipal("badge-principal", null);
            return createHandlerResult(tokenCredential, principal);
        } catch (final Throwable t) {
            LOGGER.error(t.toString());
//        } catch (final Exception e) {
//            LOGGER.error(e.toString());
////            LoggingUtils.error(LOGGER, e);
        }
        throw new FailedLoginException("Unable to verify QR code " + tokenCredential.getId());
    }
}