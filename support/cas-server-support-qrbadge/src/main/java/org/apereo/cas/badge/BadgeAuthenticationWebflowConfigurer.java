package org.apereo.cas.qrbadge;

import module java.base;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.AbstractCasWebflowConfigurer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * This is {@link BadgeAuthenticationWebflowConfigurer}.
 *
 * @author Ben Winston
 * @since 8.0.0
 */
@Slf4j
public class BadgeAuthenticationWebflowConfigurer extends AbstractCasWebflowConfigurer {

    //static final String STATE_ID_VALIDATE_QR_TOKEN = "validateQRToken";

    public BadgeAuthenticationWebflowConfigurer(final FlowBuilderServices flowBuilderServices,
                                             final FlowDefinitionRegistry flowDefinitionRegistry,
                                             final ConfigurableApplicationContext applicationContext,
                                             final CasConfigurationProperties casProperties) {
        super(flowBuilderServices, flowDefinitionRegistry, applicationContext, casProperties);
    }

    @Override
    protected void doInitialize() {
        val flow = getLoginFlow();
        if (flow != null) {
            val state = getState(flow, CasWebflowConstants.STATE_ID_INIT_LOGIN_FORM, ActionState.class);
            val setAction = createSetAction("flowScope.badgeAuthenticationEnabled", "true");
            state.getEntryActionList().add(setAction);

            //availableAuthenticationHandlerNames

            val badgeSubmission = getState(flow, CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM);
            LOGGER.warn("badge submission: " + badgeSubmission.toString());
//            createTransitionForState(qrSubmission, CasWebflowConstants.TRANSITION_ID_VALIDATE, STATE_ID_VALIDATE_QR_TOKEN);
//
//            val validateAction = createActionState(flow, STATE_ID_VALIDATE_QR_TOKEN, CasWebflowConstants.ACTION_ID_QR_AUTHENTICATION_VALIDATE_CHANNEL);
//            createTransitionForState(validateAction, CasWebflowConstants.TRANSITION_ID_FINALIZE, CasWebflowConstants.STATE_ID_REAL_SUBMIT);
//            state.getEntryActionList().add(createEvaluateAction(CasWebflowConstants.ACTION_ID_QR_AUTHENTICATION_GENERATE_CODE));
        }
    }
}
