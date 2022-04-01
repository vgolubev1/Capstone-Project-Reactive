package com.griddynamics.mainservice.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.griddynamics.mainservice.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock
@AutoConfigureWebTestClient
@DirtiesContext
@WireMockTest
class OrderSearchServiceTest {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getByPhone() {
        String testPhone = "88005553535";


        var uri = UriComponentsBuilder.fromUriString("/orderSearchService/order/phone")
                .queryParam("phoneNumber", testPhone)
                .toUriString();

        wireMockServer.stubFor(
                WireMock.get(uri)
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", MediaType.APPLICATION_NDJSON_VALUE)
                                .withBody("{\"orderNumber\":\"228756\",\"userName\":\"userUsernameTest\",\"phoneNumber\":\"" + testPhone + "\", \"productCode\" : \"123456789\", \"productName\": \"productNameTest\", \"productId\": \"productIdTest1\"}")));

        StepVerifier.create(this.webTestClient.get()
                        .uri("http://localhost:" + wireMockServer.getPort() + "/orderSearchService/order/phone?phoneNumber=" + testPhone)
                        .accept(MediaType.APPLICATION_NDJSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_NDJSON_VALUE)
                        .returnResult(Order.class)
                        .getResponseBody()
                )
                .expectNextCount(1)
                .verifyComplete();
    }
}