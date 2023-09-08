package fi.tatu.spring6r2dbc.controllers;

import fi.tatu.spring6r2dbc.model.CustomerDto;
import fi.tatu.spring6r2dbc.repositories.CustomerRepositoryTest;
import fi.tatu.spring6r2dbc.repositories.CustomerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void testListCustomers() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .expectBody().jsonPath("$.size()").isEqualTo(2);
    }

    @Test
    @Order(2)
    void testGetCustomerById() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .expectBody(CustomerDto.class);
    }

    @Test
    @Order(3)
    void testCreateCustomer() {
        final String NEW_BEER_URL = "http://localhost:8080/api/v2/customer/3";

        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDto.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location(NEW_BEER_URL);
    }

    @Test
    @Order(4)
    void testUpdateCustomer() {

        webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDto.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    void testDeleteCustomer() {

        webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }


    @Test
    @Order(6)
    void testGetCustomerByIdNotFound() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNotFound();
    }

}