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

package de.adorsys.psd2.xs2a.service.mapper.consent;

import de.adorsys.psd2.consent.api.CmsAddress;
import de.adorsys.psd2.consent.api.ais.CmsAccountReference;
import de.adorsys.psd2.consent.api.pis.PisPayment;
import de.adorsys.psd2.consent.api.pis.proto.PisCommonPaymentResponse;
import de.adorsys.psd2.xs2a.core.profile.AccountReference;
import de.adorsys.psd2.xs2a.domain.Xs2aAmount;
import de.adorsys.psd2.xs2a.domain.address.Xs2aAddress;
import de.adorsys.psd2.xs2a.domain.address.Xs2aCountryCode;
import de.adorsys.psd2.xs2a.domain.code.Xs2aFrequencyCode;
import de.adorsys.psd2.xs2a.domain.pis.BulkPayment;
import de.adorsys.psd2.xs2a.domain.pis.CommonPayment;
import de.adorsys.psd2.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.psd2.xs2a.domain.pis.SinglePayment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CmsToXs2aPaymentMapper {

    public PeriodicPayment mapToPeriodicPayment(PisPayment payment) {
        return Optional.ofNullable(payment)
                   .map(p -> {
                       PeriodicPayment periodic = new PeriodicPayment();
                       periodic.setPaymentId(p.getPaymentId());
                       periodic.setEndToEndIdentification(p.getEndToEndIdentification());
                       periodic.setDebtorAccount(mapToAccountReferenceEntity(p.getDebtorAccount()));
                       periodic.setCreditorAccount(mapToAccountReferenceEntity(p.getCreditorAccount()));
                       periodic.setInstructedAmount(new Xs2aAmount(p.getCurrency(), p.getAmount().toPlainString()));
                       periodic.setCreditorAgent(p.getCreditorAgent());
                       periodic.setCreditorName(p.getCreditorName());
                       periodic.setCreditorAddress(mapToXs2aAddress(p.getCreditorAddress()));
                       periodic.setRemittanceInformationUnstructured(p.getRemittanceInformationUnstructured());
                       String frequency = p.getFrequency();
                       if (StringUtils.isNotBlank(frequency)) {
                           periodic.setFrequency(Xs2aFrequencyCode.valueOf(frequency));
                       }
                       return periodic;
                   }).orElse(null);
    }


    public SinglePayment mapToSinglePayment(PisPayment payment) {
        return Optional.ofNullable(payment)
                   .map(p -> {
                       SinglePayment single = new SinglePayment();
                       single.setPaymentId(p.getPaymentId());
                       single.setEndToEndIdentification(p.getEndToEndIdentification());
                       single.setInstructedAmount(new Xs2aAmount(p.getCurrency(), p.getAmount().toPlainString()));
                       single.setDebtorAccount(mapToAccountReferenceEntity(p.getDebtorAccount()));
                       single.setCreditorAccount(mapToAccountReferenceEntity(p.getCreditorAccount()));
                       single.setCreditorAgent(p.getCreditorAgent());
                       single.setCreditorName(p.getCreditorName());
                       single.setCreditorAddress(mapToXs2aAddress(p.getCreditorAddress()));
                       single.setRemittanceInformationUnstructured(p.getRemittanceInformationUnstructured());
                       return single;
                   }).orElse(null);
    }

    public BulkPayment mapToBulkPayment(List<PisPayment> payments) {
        BulkPayment bulk = new BulkPayment();
        bulk.setPaymentId(payments.get(0).getPaymentId());
        bulk.setBatchBookingPreferred(false);
        bulk.setDebtorAccount(mapToAccountReferenceEntity(payments.get(0).getDebtorAccount()));
        bulk.setRequestedExecutionDate(payments.get(0).getRequestedExecutionDate());
        List<SinglePayment> paymentList = payments.stream()
                                              .map(this::mapToSinglePayment)
                                              .collect(Collectors.toList());
        bulk.setPayments(paymentList);
        return bulk;
    }

    public CommonPayment mapToXs2aCommonPayment(PisCommonPaymentResponse response) {
        return Optional.ofNullable(response)
                   .map(r -> {
                            CommonPayment commonPayment = new CommonPayment();
                            commonPayment.setPaymentId(r.getExternalId());
                            commonPayment.setPaymentProduct(r.getPaymentProduct());
                            commonPayment.setPaymentType(r.getPaymentType());
                            commonPayment.setPaymentData(r.getPaymentData());
                            commonPayment.setTppInfo(r.getTppInfo());
                            return commonPayment;
                        }
                   )
                   .orElse(null);
    }

    private Xs2aAddress mapToXs2aAddress(CmsAddress address) {
        return Optional.ofNullable(address)
                   .map(a -> {
                       Xs2aAddress xs2aAddress = new Xs2aAddress();
                       xs2aAddress.setStreet(a.getStreet());
                       xs2aAddress.setBuildingNumber(a.getBuildingNumber());
                       xs2aAddress.setCity(a.getCity());
                       xs2aAddress.setPostalCode(a.getPostalCode());
                       xs2aAddress.setCountry(new Xs2aCountryCode(a.getCountry()));
                       return xs2aAddress;
                   }).orElseGet(Xs2aAddress::new);
    }

    private AccountReference mapToAccountReferenceEntity(CmsAccountReference cmsAccountReference) {
        return Optional.ofNullable(cmsAccountReference)
                   .map(ref -> {
                       AccountReference accountReference = new AccountReference();
                       accountReference.setIban(ref.getIban());
                       accountReference.setBban(ref.getBban());
                       accountReference.setPan(ref.getPan());
                       accountReference.setMaskedPan(ref.getMaskedPan());
                       accountReference.setMsisdn(ref.getMsisdn());
                       accountReference.setCurrency(ref.getCurrency());
                       accountReference.setAspspAccountId(ref.getAspspAccountId());

                       return accountReference;
                   }).orElse(null);
    }
}
