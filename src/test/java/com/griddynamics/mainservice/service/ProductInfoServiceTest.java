package com.griddynamics.mainservice.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.griddynamics.mainservice.domain.ProductInfo;
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
class ProductInfoServiceTest {

    public static final String TEST_PRODUCT_CODE = "213";
    public static final String TEST_RESPONSE_BODY = "{\"productId\":\"228756\",\"productCode\":\"" + TEST_PRODUCT_CODE + "\",\"productName\":\"testProductName\",\"score\":\"10.2\"}";

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getByProductCode() {
        var uri = UriComponentsBuilder.fromUriString("/productInfoService/product/names")
                .queryParam("productCode", TEST_PRODUCT_CODE)
                .toUriString();

        wireMockServer.stubFor(
                WireMock.get(uri)
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", MediaType.APPLICATION_NDJSON_VALUE)
                                .withBody(TEST_RESPONSE_BODY)));

        StepVerifier.create(this.webTestClient.get()
                        .uri("http://localhost:" + wireMockServer.getPort() + "/productInfoService/product/names?productCode=" + TEST_PRODUCT_CODE)
                        .accept(MediaType.APPLICATION_NDJSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_NDJSON_VALUE)
                        .returnResult(ProductInfo.class)
                        .getResponseBody()
                )
                .expectNextCount(1)
                .verifyComplete();
    }
}