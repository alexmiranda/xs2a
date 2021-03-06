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

package de.adorsys.psd2.validator.certificate;

import de.adorsys.psd2.validator.certificate.util.CertificateUtils;
import org.junit.Assert;
import org.junit.Test;

import de.adorsys.psd2.validator.certificate.util.CertificateExtractorUtil;
import de.adorsys.psd2.validator.certificate.util.TppCertificateData;
import no.difi.certvalidator.api.CertificateValidationException;

import static org.hamcrest.core.IsEqual.equalTo;

public class CertificateExtractorUtilTest {

	@Test
	public void testExtractCertData() throws CertificateValidationException {
		
		String encodedCert = "-----BEGIN CERTIFICATE-----MIIEBjCCAu6gAwIBAgIEejybVTANBgkqhkiG9w0BAQsFADCBlDELMAkGA1UEBhMCREUxDzANBgNVBAgMBkhlc3NlbjESMBAGA1UEBwwJRnJhbmtmdXJ0MRUwEwYDVQQKDAxBdXRob3JpdHkgQ0ExCzAJBgNVBAsMAklUMSEwHwYDVQQDDBhBdXRob3JpdHkgQ0EgRG9tYWluIE5hbWUxGTAXBgkqhkiG9w0BCQEWCmNhQHRlc3QuZGUwHhcNMTgwNzAyMTMzMDUyWhcNMTgwNzE5MTQxMTIxWjB6MRMwEQYDVQQDDApkb21haW5OYW1lMQwwCgYDVQQKDANvcmcxCzAJBgNVBAsMAm91MRAwDgYDVQQGEwdHZXJtYW55MQ8wDQYDVQQIDAZCYXllcm4xEjAQBgNVBAcMCU51cmVtYmVyZzERMA8GA1UEYQwIMTIzNDU5ODcwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCSYxs2Fhpkt0zweiLxiJ3+QwpENoiIyxtNKehU/oqc/V6mns8c2N1BgxDEUixAugf257zkwNn39PZKuV8S4T61GeT4UUvf5DySYvgI63XpbRF3hj0KJk/oZDanidFZ86X7kW7TcTetPX+AqGiCLQEIKbnyISh1qeij6SmMr2j6IXVBzSP9dnxif+nf2poj0kCAz69eCI2zHufz8VJuIf9RhFychuuPO/NCQXuKL7KFeEf5uLhDGPeGqeWs1EBCAECuBOSAOwX+uFwyn/Jw/HfzH0ZQtod7qaNs4/fvjtZOekOtBGYGFOaZlqNvmadiR9mMJTbSXmBY1A5DMdBGcPAzAgMBAAGjeTB3MHUGCCsGAQUFBwEDBGkwZwYGBACBmCcCMF0wTDARBgcEAIGYJwEBDAZQU1BfQVMwEQYHBACBmCcBAgwGUFNQX1BJMBEGBwQAgZgnAQMMBlBTUF9BSTARBgcEAIGYJwEEDAZQU1BfSUMMBEF1dGgMB0dlcm1hbnkwDQYJKoZIhvcNAQELBQADggEBAKXgO8FvKBmTOGn2gWlasZtSsIWbzexR0IMU1AuGNmkMF2BGEkBTQJ1bGpCd5+eBVUkbIQkxP0Oqyb/moNty/7c9gym1GpEzZ5Cs1Zgtne8EnOKh8sGtI9L0AFVSTFHPHt9qj4o5TMgzdU9Q4cq4bq9bSZiwo3btwgIkBn7yetvfo8fSADBSYwOlqUAv+mROjEID772owEOgn0fLZmlIdZl8rpYQlzbHpcvlTTdnQiidjdO4Xln3+NIp7ifIfBUoCei0AP8rAFhq14GIE1YweY9efa1QPEm/a9EN3f22Psyd/jrQqnWVLsIJIEPVth3gdish6tHDfED1PpZVtMxKHqE=-----END CERTIFICATE-----";
		
		TppCertificateData extract = CertificateExtractorUtil.extract(encodedCert);

        Assert.assertThat(extract.getPspRoles().size(), equalTo(4));
	}

    @Test
    public void testExtractCertDataValidCert() throws CertificateValidationException {

        String encodedCert = CertificateUtils.getCertificateByName("certificateValid.crt");

        TppCertificateData extract = CertificateExtractorUtil.extract(encodedCert);

        Assert.assertThat(extract.getPspRoles().size(), equalTo(3));
    }

    @Test(expected = CertificateValidationException.class)
    public void testExtractCertDataInvalidCert() throws CertificateValidationException {
        String encodedCert = CertificateUtils.getCertificateByName("certificateInvalid.crt");

        CertificateExtractorUtil.extract(encodedCert);
    }

}
