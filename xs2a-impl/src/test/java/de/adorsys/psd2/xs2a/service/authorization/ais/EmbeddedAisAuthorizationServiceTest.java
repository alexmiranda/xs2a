/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.psd2.xs2a.service.authorization.ais;

import de.adorsys.psd2.xs2a.config.factory.AisScaStageAuthorisationFactory;
import de.adorsys.psd2.xs2a.core.psu.PsuIdData;
import de.adorsys.psd2.xs2a.core.sca.ScaStatus;
import de.adorsys.psd2.xs2a.domain.consent.*;
import de.adorsys.psd2.xs2a.service.authorization.ais.stage.AisScaStartAuthorisationStage;
import de.adorsys.psd2.xs2a.service.consent.Xs2aAisConsentService;
import de.adorsys.psd2.xs2a.service.mapper.consent.Xs2aAisConsentMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static de.adorsys.psd2.xs2a.config.factory.AisScaStageAuthorisationFactory.SERVICE_PREFIX;
import static de.adorsys.psd2.xs2a.domain.consent.ConsentAuthorizationResponseLinkType.START_AUTHORISATION_WITH_PSU_AUTHENTICATION;
import static de.adorsys.psd2.xs2a.domain.consent.ConsentAuthorizationResponseLinkType.START_AUTHORISATION_WITH_PSU_IDENTIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmbeddedAisAuthorizationServiceTest {
    private static final ScaStatus STARTED_SCA_STATUS = ScaStatus.STARTED;
    private static final ScaStatus STARTED_XS2A_SCA_STATUS = ScaStatus.STARTED;
    private static final String PSU_ID = "Test psuId";
    private static final PsuIdData PSU_DATA = new PsuIdData(PSU_ID, null, null, null);
    private static final String CONSENT_ID = "Test consentId";
    private static final String WRONG_CONSENT_ID = "Wrong consent id";
    private static final String AUTHORISATION_ID = "Test authorisationId";
    private static final String WRONG_AUTHORISATION_ID = "Wrong authorisation id";
    private static final ScaStatus SCA_STATUS = ScaStatus.RECEIVED;

    @InjectMocks
    private EmbeddedAisAuthorizationService authorizationService;

    @Mock
    private Xs2aAisConsentService aisConsentService;
    @Mock
    private Xs2aAisConsentMapper aisConsentMapper;
    @Mock
    private AisScaStageAuthorisationFactory scaStageAuthorisationFactory;
    @Mock
    private AisScaStartAuthorisationStage startAuthorisationStage;
    @Mock
    private UpdateConsentPsuDataReq updateConsentPsuDataRequest;
    @Mock
    private AccountConsentAuthorization consentAuthorization;
    @Mock
    private UpdateConsentPsuDataResponse updateConsentPsuDataResponse;
    @Mock
    private AccountConsent consent;

    @Before
    public void setUp() {
        when(consentAuthorization.getScaStatus())
            .thenReturn(STARTED_SCA_STATUS);

        when(scaStageAuthorisationFactory.getService(SERVICE_PREFIX + STARTED_SCA_STATUS.name()))
            .thenReturn(startAuthorisationStage);

        when(startAuthorisationStage.apply(updateConsentPsuDataRequest))
            .thenReturn(updateConsentPsuDataResponse);

        when(aisConsentService.getAuthorisationScaStatus(CONSENT_ID, AUTHORISATION_ID))
            .thenReturn(Optional.of(SCA_STATUS));
        when(aisConsentService.getAuthorisationScaStatus(WRONG_CONSENT_ID, WRONG_AUTHORISATION_ID))
            .thenReturn(Optional.empty());

    }

    @Test
    public void createConsentAuthorization_Success() {
        when(aisConsentService.createAisConsentAuthorization(CONSENT_ID, STARTED_XS2A_SCA_STATUS, PSU_DATA))
            .thenReturn(Optional.of(AUTHORISATION_ID));
        when(aisConsentService.getAccountConsentById(CONSENT_ID)).thenReturn(consent);

        Optional<CreateConsentAuthorizationResponse> actualResponseOptional = authorizationService.createConsentAuthorization(PSU_DATA, CONSENT_ID);

        assertThat(actualResponseOptional.isPresent()).isTrue();

        CreateConsentAuthorizationResponse actualResponse = actualResponseOptional.get();

        assertThat(actualResponse.getConsentId()).isEqualTo(CONSENT_ID);
        assertThat(actualResponse.getAuthorizationId()).isEqualTo(AUTHORISATION_ID);
        assertThat(actualResponse.getScaStatus()).isEqualTo(STARTED_SCA_STATUS);
        assertThat(actualResponse.getResponseLinkType()).isEqualTo(START_AUTHORISATION_WITH_PSU_AUTHENTICATION);
    }

    @Test
    public void createConsentAuthorizationNoPsuAuthentification_Success() {
        when(aisConsentService.createAisConsentAuthorization(CONSENT_ID, STARTED_XS2A_SCA_STATUS, PSU_DATA))
            .thenReturn(Optional.of(AUTHORISATION_ID));
        when(aisConsentService.getAccountConsentById(CONSENT_ID)).thenReturn(consent);
        when(consent.getPsuData()).thenReturn(PSU_DATA);
        PsuIdData psuIdData = new PsuIdData(null, null, null, null);
        Optional<CreateConsentAuthorizationResponse> actualResponseOptional = authorizationService.createConsentAuthorization(psuIdData, CONSENT_ID);

        assertThat(actualResponseOptional.isPresent()).isTrue();

        CreateConsentAuthorizationResponse actualResponse = actualResponseOptional.get();

        assertThat(actualResponse.getConsentId()).isEqualTo(CONSENT_ID);
        assertThat(actualResponse.getAuthorizationId()).isEqualTo(AUTHORISATION_ID);
        assertThat(actualResponse.getScaStatus()).isEqualTo(STARTED_SCA_STATUS);
        assertThat(actualResponse.getResponseLinkType()).isEqualTo(START_AUTHORISATION_WITH_PSU_AUTHENTICATION);
    }

    @Test
    public void createConsentAuthorizationNoPsuIdentification_Success() {
        PsuIdData psuIdData = new PsuIdData(null, null, null, null);
        when(aisConsentService.createAisConsentAuthorization(CONSENT_ID, STARTED_XS2A_SCA_STATUS, psuIdData))
            .thenReturn(Optional.of(AUTHORISATION_ID));
        when(aisConsentService.getAccountConsentById(CONSENT_ID)).thenReturn(consent);
        when(consent.getPsuData()).thenReturn(psuIdData);
        Optional<CreateConsentAuthorizationResponse> actualResponseOptional = authorizationService.createConsentAuthorization(psuIdData, CONSENT_ID);

        assertThat(actualResponseOptional.isPresent()).isTrue();

        CreateConsentAuthorizationResponse actualResponse = actualResponseOptional.get();

        assertThat(actualResponse.getConsentId()).isEqualTo(CONSENT_ID);
        assertThat(actualResponse.getAuthorizationId()).isEqualTo(AUTHORISATION_ID);
        assertThat(actualResponse.getScaStatus()).isEqualTo(STARTED_SCA_STATUS);
        assertThat(actualResponse.getResponseLinkType()).isEqualTo(START_AUTHORISATION_WITH_PSU_IDENTIFICATION);
    }

    @Test
    public void updateConsentPsuData_Failure_ResponseWithError() {
        when(updateConsentPsuDataResponse.hasError())
            .thenReturn(true);

        authorizationService.updateConsentPsuData(updateConsentPsuDataRequest, consentAuthorization);

        verify(aisConsentService, times(0)).updateConsentAuthorization(any(UpdateConsentPsuDataReq.class));
        assertThat(updateConsentPsuDataResponse).isNotNull();
    }

    @Test
    public void updateConsentPsuData_Success_ResponseWithoutError() {
        when(updateConsentPsuDataResponse.hasError())
            .thenReturn(false);

        when(aisConsentMapper.mapToSpiUpdateConsentPsuDataReq(updateConsentPsuDataResponse, updateConsentPsuDataRequest))
            .thenReturn(updateConsentPsuDataRequest);

        doNothing()
            .when(aisConsentService).updateConsentAuthorization(updateConsentPsuDataRequest);

        authorizationService.updateConsentPsuData(updateConsentPsuDataRequest, consentAuthorization);

        verify(aisConsentService).updateConsentAuthorization(updateConsentPsuDataRequest);
        assertThat(updateConsentPsuDataResponse).isNotNull();
    }

    @Test
    public void getAuthorisationScaStatus_success() {
        // When
        Optional<ScaStatus> actual = authorizationService.getAuthorisationScaStatus(CONSENT_ID, AUTHORISATION_ID);

        // Then
        assertThat(actual.isPresent()).isTrue();
        //noinspection OptionalGetWithoutIsPresent
        assertThat(actual.get()).isEqualTo(SCA_STATUS);
    }

    @Test
    public void getAuthorisationScaStatus_failure_wrongIds() {
        // When
        Optional<ScaStatus> actual = authorizationService.getAuthorisationScaStatus(WRONG_CONSENT_ID, WRONG_AUTHORISATION_ID);

        // Then
        assertThat(actual.isPresent()).isFalse();
    }
}
