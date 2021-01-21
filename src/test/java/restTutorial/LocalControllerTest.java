/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package restTutorial;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.Thread.sleep;

public class LocalControllerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9999);

    @Before
    public void setUp() {
        mockSuccessService();
        mockRedirectService();
        mockForbidenService();
        mockUnauthorizedService();
        mockServerFailure();
        mockJsonMatch();
        mockHeaderMatch();
    }

    @Test
    public void testLocalServiceWithMockedRemoteService() throws Exception {
        sleep(100000000);
    }

    private void mockSuccessService() {
        stubFor(get(urlEqualTo("/success"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("remoteServiceResponse.json")));
    }

    private void mockRedirectService() {
        stubFor(get(urlEqualTo("/redirect"))
                .willReturn(aResponse()
                        .withStatus(301)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("remoteServiceResponse.json")));
    }

    private void mockUnauthorizedService() {
        stubFor(put(urlEqualTo("/authorize"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withStatusMessage("You have no Rights to do this")
                ));
    }

    private void mockForbidenService() {
        stubFor(options(urlEqualTo("/forbiden"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withStatusMessage("Not really")
                ));
    }

    private void mockServerFailure() {
        stubFor(post(urlEqualTo("/server"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")
                ));
    }

    private void mockJsonMatch() {
        stubFor(patch(urlEqualTo("/json"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson("{\"message\": \"Hello World!\"}"))
                .willReturn(aResponse()
                        .withStatus(200)
                ));
    }

    private void mockHeaderMatch() {
        stubFor(delete(urlEqualTo("/header"))
                .withHeader("CoookieMaster", equalTo("something"))
                .willReturn(aResponse()
                        .withStatus(200)
                ));
    }

}
