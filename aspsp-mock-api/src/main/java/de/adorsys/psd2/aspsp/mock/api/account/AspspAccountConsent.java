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

package de.adorsys.psd2.aspsp.mock.api.account;

import de.adorsys.psd2.aspsp.mock.api.consent.AspspAccountAccess;
import de.adorsys.psd2.aspsp.mock.api.consent.AspspConsentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AspspAccountConsent {
    @Id
    private String id;
    private AspspAccountAccess access;
    private boolean recurringIndicator;
    private LocalDate validUntil;
    private int frequencyPerDay;
    private LocalDate lastActionDate;
    private AspspConsentStatus consentStatus;
    private boolean withBalance;
    private boolean tppRedirectPreferred;
    private String psuId;
    private String tppId;
}
