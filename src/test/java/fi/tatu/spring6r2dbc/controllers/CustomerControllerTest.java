package fi.tatu.spring6r2dbc.controllers;

import fi.tatu.spring6r2dbc.domain.Customer;
import fi.tatu.spring6r2dbc.model.CustomerDto;
import fi.tatu.spring6r2dbc.repositories.CustomerRepositoryTest;
import org.junit.jupiter.api.*;
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
        webTestClient.get()
            .uri(CustomerController.CUSTOMER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .expectBody().jsonPath("$.size()").isEqualTo(2);
    }

    @Test
    @Order(2)
    void testGetCustomerById() {
        webTestClient.get()
            .uri(CustomerController.CUSTOMER_PATH_ID, 1)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .expectBody(CustomerDto.class);
    }

    @Test
    @Order(3)
    void testGetCustomerByIdNotFound() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 999)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testCreateCustomer() {
        final String NEW_CUSTOMER_URL = "http://localhost:8080/api/v2/customer/3";

        webTestClient.post()
            .uri(CustomerController.CUSTOMER_PATH)
            .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDto.class)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().location(NEW_CUSTOMER_URL);
    }

    @Test
    @Order(5)
    void testCreateCustomerBadName() {
        Customer testCustomer = CustomerRepositoryTest.getTestCustomer();
        testCustomer.setName("");

        webTestClient.post()
            .uri(CustomerController.CUSTOMER_PATH)
            .body(Mono.just(testCustomer), CustomerDto.class)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isBadRequest();
    }


    @Test
    @Order(6)
    void testUpdateCustomer() {
        webTestClient.put()
            .uri(CustomerController.CUSTOMER_PATH_ID, 1)
            .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDto.class)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(7)
    void testUpdateCustomerBadRequest() {
        Customer testCustomer = CustomerRepositoryTest.getTestCustomer();
        testCustomer.setName("");

        webTestClient.put()
            .uri(CustomerController.CUSTOMER_PATH_ID, 1)
            .body(Mono.just(testCustomer), CustomerDto.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(8)
    void testUpdateCustomerNotFound() {
        webTestClient.put()
            .uri(CustomerController.CUSTOMER_PATH_ID, 999)
            .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDto.class)
            .exchange()
            .expectStatus().isNotFound();
    }


    @Test
    @Order(9)
    void testDeleteCustomer() {
        webTestClient.delete()
            .uri(CustomerController.CUSTOMER_PATH_ID, 1)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(10)
    void testDeleteCustomerNotFound() {
        webTestClient.delete()
            .uri(CustomerController.CUSTOMER_PATH_ID, 999)
            .exchange()
            .expectStatus().isNotFound();
    }

}