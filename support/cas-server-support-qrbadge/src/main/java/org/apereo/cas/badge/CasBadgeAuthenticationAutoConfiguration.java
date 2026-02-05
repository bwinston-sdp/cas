package org.apereo.cas.config;

import module java.base;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.handler.ByCredentialTypeAuthenticationHandlerResolver;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalFactoryUtils;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.features.CasFeatureModule;
import org.apereo.cas.qrbadge.authentication.BadgeAuthenticationTokenCredential;
import org.apereo.cas.qrbadge.authentication.BadgeAuthenticationTokenAuthenticationHandler;
import org.apereo.cas.qrbadge.BadgeAuthenticationWebflowConfigurer;
import org.apereo.cas.qrbadge.BadgeAuthenticationScanBadgeAction;
//import org.apereo.cas.qr.QRAuthenticationConstants;
//import org.apereo.cas.qr.authentication.JsonResourceQRAuthenticationDeviceRepository;
//import org.apereo.cas.qr.authentication.QRAuthenticationDeviceRepository;
//import org.apereo.cas.qr.authentication.QRAuthenticationTokenAuthenticationHandler;
//import org.apereo.cas.qr.authentication.QRAuthenticationTokenCredential;
//import org.apereo.cas.qr.validation.DefaultQRAuthenticationTokenValidatorService;
//import org.apereo.cas.qr.validation.QRAuthenticationTokenValidatorService;
//import org.apereo.cas.qr.web.QRAuthenticationChannelController;
//import org.apereo.cas.qr.web.QRAuthenticationDeviceRepositoryEndpoint;
//import org.apereo.cas.qr.web.flow.QRAuthenticationGenerateCodeAction;
//import org.apereo.cas.qr.web.flow.QRAuthenticationValidateTokenAction;
//import org.apereo.cas.qr.web.flow.QRAuthenticationWebflowConfigurer;
//import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.ticket.registry.TicketRegistry;
//import org.apereo.cas.token.JwtBuilder;
import org.apereo.cas.util.spring.boot.ConditionalOnFeatureEnabled;
//import org.apereo.cas.web.CasWebSecurityConfigurer;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.CasWebflowExecutionPlanConfigurer;
import org.apereo.cas.web.flow.actions.WebflowActionBeanSupplier;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.Action;

/**
 * This is {@link CasBadgeAuthenticationAutoConfiguration}.
 *
 * @author Ben Winston
 * @since 8.0.0
 */
//@EnableWebSocketMessageBroker
@EnableConfigurationProperties(CasConfigurationProperties.class)
@ConditionalOnFeatureEnabled(feature = CasFeatureModule.FeatureCatalog.Authentication, module = "badge")
@AutoConfiguration
@Slf4j
public class CasBadgeAuthenticationAutoConfiguration {

//    @Configuration(value = "BadgeAuthenticationServiceConfiguration", proxyBeanMethods = false)
//    @EnableConfigurationProperties(CasConfigurationProperties.class)
//    static class BadgeAuthenticationServiceConfiguration {
//
//        @Bean
//        @ConditionalOnMissingBean(name = "qrAuthenticationTokenValidatorService")
//        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
//        public QRAuthenticationTokenValidatorService qrAuthenticationTokenValidatorService(
//                final CasConfigurationProperties casProperties,
//                @Qualifier("qrAuthenticationDeviceRepository")
//                final QRAuthenticationDeviceRepository qrAuthenticationDeviceRepository,
//                @Qualifier(JwtBuilder.TICKET_JWT_BUILDER_BEAN_NAME)
//                final JwtBuilder tokenTicketJwtBuilder,
//                @Qualifier(TicketRegistry.BEAN_NAME)
//                final TicketRegistry ticketRegistry) {
//            return new DefaultQRAuthenticationTokenValidatorService(tokenTicketJwtBuilder,
//                    ticketRegistry, casProperties, qrAuthenticationDeviceRepository);
//        }
//
//        @Bean
//        @ConditionalOnMissingBean(name = "qrAuthenticationDeviceRepository")
//        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
//        public QRAuthenticationDeviceRepository qrAuthenticationDeviceRepository(final CasConfigurationProperties casProperties) {
//            val qr = casProperties.getAuthn().getQr();
//            if (qr.getJson().getLocation() != null) {
//                return new JsonResourceQRAuthenticationDeviceRepository(qr.getJson().getLocation());
//            }
//            return QRAuthenticationDeviceRepository.permitAll();
//        }
//    }

    @Configuration(value = "BadgeAuthenticationWebflowPlanConfiguration", proxyBeanMethods = false)
    @EnableConfigurationProperties(CasConfigurationProperties.class)
    static class BadgeAuthenticationWebflowPlanConfiguration {

        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        @ConditionalOnMissingBean(name = "badgeAuthenticationCasWebflowExecutionPlanConfigurer")
        public CasWebflowExecutionPlanConfigurer badgeAuthenticationCasWebflowExecutionPlanConfigurer(
                @Qualifier("badgeAuthenticationWebflowConfigurer")
                final CasWebflowConfigurer badgeAuthenticationWebflowConfigurer) {
            return plan -> plan.registerWebflowConfigurer(badgeAuthenticationWebflowConfigurer);
        }

    }

    @Configuration(value = "BadgeAuthenticationWebflowConfiguration", proxyBeanMethods = false)
    @EnableConfigurationProperties(CasConfigurationProperties.class)
    static class BadgeAuthenticationWebflowConfiguration {
        @ConditionalOnMissingBean(name = "badgeAuthenticationWebflowConfigurer")
        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public CasWebflowConfigurer badgeAuthenticationWebflowConfigurer(
                final CasConfigurationProperties casProperties, final ConfigurableApplicationContext applicationContext,
                @Qualifier(CasWebflowConstants.BEAN_NAME_FLOW_DEFINITION_REGISTRY)
                final FlowDefinitionRegistry flowDefinitionRegistry,
                @Qualifier(CasWebflowConstants.BEAN_NAME_FLOW_BUILDER_SERVICES)
                final FlowBuilderServices flowBuilderServices) {
            return new BadgeAuthenticationWebflowConfigurer(flowBuilderServices, flowDefinitionRegistry, applicationContext, casProperties);
        }

    }

    @Configuration(value = "BadgeAuthenticationHandlerPlanConfiguration", proxyBeanMethods = false)
    @EnableConfigurationProperties(CasConfigurationProperties.class)
    static class BadgeAuthenticationHandlerPlanConfiguration {
        @ConditionalOnMissingBean(name = "badgeAuthenticationEventExecutionPlanConfigurer")
        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public AuthenticationEventExecutionPlanConfigurer badgeAuthenticationEventExecutionPlanConfigurer(
                @Qualifier("badgeAuthenticationTokenAuthenticationHandler")
                final AuthenticationHandler badgeAuthenticationTokenAuthenticationHandler) {
            LOGGER.warn("badge auth plan configuration");
            return plan -> {
                plan.registerAuthenticationHandler(badgeAuthenticationTokenAuthenticationHandler);
                plan.registerAuthenticationHandlerResolver(new ByCredentialTypeAuthenticationHandlerResolver(BadgeAuthenticationTokenCredential.class));
            };
        }

    }

    @Configuration(value = "BadgeAuthenticationHandlerConfiguration", proxyBeanMethods = false)
    @EnableConfigurationProperties(CasConfigurationProperties.class)
    static class BadgeAuthenticationHandlerConfiguration {
        @ConditionalOnMissingBean(name = "badgeAuthenticationPrincipalFactory")
        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public PrincipalFactory badgeAuthenticationPrincipalFactory() {
            return PrincipalFactoryUtils.newPrincipalFactory();
        }

        @ConditionalOnMissingBean(name = "badgeAuthenticationTokenAuthenticationHandler")
        @Bean
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public AuthenticationHandler badgeAuthenticationTokenAuthenticationHandler(
                @Qualifier("badgeAuthenticationPrincipalFactory")
                final PrincipalFactory badgeAuthenticationPrincipalFactory //,
//                @Qualifier("qrAuthenticationTokenValidatorService")
//                final QRAuthenticationTokenValidatorService qrAuthenticationTokenValidatorService,
//                @Qualifier(ServicesManager.BEAN_NAME)
//                final ServicesManager servicesManager
            ) {
            LOGGER.warn("creating badge auth handler");
            return new BadgeAuthenticationTokenAuthenticationHandler(badgeAuthenticationPrincipalFactory);
        }

    }

//    @Configuration(value = "QRAuthenticationWebConfiguration", proxyBeanMethods = false)
//    @EnableConfigurationProperties(CasConfigurationProperties.class)
//    static class QRAuthenticationWebConfiguration {
//
//        @Bean
//        @ConditionalOnAvailableEndpoint
//        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
//        public QRAuthenticationDeviceRepositoryEndpoint qrAuthenticationDeviceRepositoryEndpoint(
//                final CasConfigurationProperties casProperties,
//                @Qualifier("qrAuthenticationDeviceRepository")
//                final ObjectProvider<@NonNull QRAuthenticationDeviceRepository> qrAuthenticationDeviceRepository) {
//            return new QRAuthenticationDeviceRepositoryEndpoint(casProperties, qrAuthenticationDeviceRepository);
//        }
//    }

//    @Configuration(value = "QRAuthenticationMvcConfiguration", proxyBeanMethods = false)
//    @EnableConfigurationProperties(CasConfigurationProperties.class)
//    static class QRAuthenticationMvcConfiguration {
//        private static final String ENDPOINT_QR_WEBSOCKET = "/qr-websocket";
//
//        @Bean
//        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
//        @ConditionalOnMissingBean(name = "qrAuthenticationEndpointConfigurer")
//        public CasWebSecurityConfigurer<Void> qrAuthenticationEndpointConfigurer() {
//            return new CasWebSecurityConfigurer<>() {
//                @Override
//                public List<String> getIgnoredEndpoints() {
//                    return List.of("/qr-websocket");
//                }
//            };
//        }
//
//        @Bean
//        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
//        public WebSocketMessageBrokerConfigurer qrAuthenticationWebSocketMessageBrokerConfigurer(
//                final CasConfigurationProperties casProperties) {
//            return new WebSocketMessageBrokerConfigurer() {
//                @Override
//                public void registerStompEndpoints(
//                        @NonNull
//                        final StompEndpointRegistry registry) {
//                    registry.addEndpoint(ENDPOINT_QR_WEBSOCKET)
//                            .setAllowedOrigins(casProperties.getAuthn().getQr().getAllowedOrigins().toArray(ArrayUtils.EMPTY_STRING_ARRAY))
//                            .addInterceptors(new HttpSessionHandshakeInterceptor())
//                            .withSockJS();
//                }
//
//                @Override
//                public void configureMessageBroker(
//                        @NonNull
//                        final MessageBrokerRegistry config) {
//                    config.enableSimpleBroker(QRAuthenticationConstants.QR_SIMPLE_BROKER_DESTINATION_PREFIX);
//                    config.setApplicationDestinationPrefixes("/qr");
//                }
//            };
//        }
//    }
//
//    @Configuration(value = "QRAuthenticationControllerConfiguration", proxyBeanMethods = false)
//    @EnableConfigurationProperties(CasConfigurationProperties.class)
//    static class QRAuthenticationControllerConfiguration {
//        @Bean
//        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
//        public QRAuthenticationChannelController qrAuthenticationChannelController(
//                @Qualifier("brokerMessagingTemplate")
//                final SimpMessagingTemplate template,
//                @Qualifier("qrAuthenticationTokenValidatorService")
//                final QRAuthenticationTokenValidatorService qrAuthenticationTokenValidatorService) {
//            return new QRAuthenticationChannelController(template, qrAuthenticationTokenValidatorService);
//        }
//
//    }
//
    @Configuration(value = "BadgeAuthenticationWebflowActionConfiguration", proxyBeanMethods = false)
    @EnableConfigurationProperties(CasConfigurationProperties.class)
    static class BadgeAuthenticationWebflowActionConfiguration {
//        @Bean
//        @ConditionalOnMissingBean(name = CasWebflowConstants.ACTION_ID_QR_AUTHENTICATION_VALIDATE_CHANNEL)
//        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
//        public Action qrAuthenticationValidateWebSocketChannelAction(
//                final ConfigurableApplicationContext applicationContext,
//                final CasConfigurationProperties casProperties) {
//            return WebflowActionBeanSupplier.builder()
//                    .withApplicationContext(applicationContext)
//                    .withProperties(casProperties)
//                    .withAction(QRAuthenticationValidateTokenAction::new)
//                    .withId(CasWebflowConstants.ACTION_ID_QR_AUTHENTICATION_VALIDATE_CHANNEL)
//                    .build()
//                    .get();
//        }

        @Bean
        @ConditionalOnMissingBean(name = CasWebflowConstants.ACTION_ID_BADGE_AUTHENTICATION_SCAN_BADGE)
        @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
        public Action badgeAuthenticationScanBadgeAction(final ConfigurableApplicationContext applicationContext,
                                                         final CasConfigurationProperties casProperties) {
            return WebflowActionBeanSupplier.builder()
                    .withApplicationContext(applicationContext)
                    .withProperties(casProperties)
                    .withAction(BadgeAuthenticationScanBadgeAction::new)
                    .withId(CasWebflowConstants.ACTION_ID_BADGE_AUTHENTICATION_SCAN_BADGE)
                    .build()
                    .get();
        }

    }
}
