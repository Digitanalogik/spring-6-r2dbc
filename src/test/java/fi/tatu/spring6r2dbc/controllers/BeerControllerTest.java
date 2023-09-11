package fi.tatu.spring6r2dbc.controllers;

import fi.tatu.spring6r2dbc.domain.Beer;
import fi.tatu.spring6r2dbc.model.BeerDto;
import fi.tatu.spring6r2dbc.repositories.BeerRepositoryTest;
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
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void testListBeers() {
        webTestClient.get().uri(BeerController.BEER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(2)
    void testGetBeerById() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID, 1)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .expectBody(BeerDto.class);
    }

    @Test
    @Order(3)
    void testGetBeerByIdNotFound() {

        webTestClient.get().uri(BeerController.BEER_PATH_ID, 999)
            .exchange()
            .expectStatus().isNotFound();
    }


    @Test
    @Order(4)
    void testCreateBeer() {
        final String NEW_BEER_URL = "http://localhost:8080/api/v2/beer/4";

        webTestClient.post()
            .uri(BeerController.BEER_PATH)
            .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().location(NEW_BEER_URL);
    }

    @Test
    @Order(5)
    void testCreateBeerBadName() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("");

        webTestClient.post()
            .uri(BeerController.BEER_PATH)
            .body(Mono.just(testBeer), BeerDto.class)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(6)
    void testUpdateBeer() {

        webTestClient.put()
            .uri(BeerController.BEER_PATH_ID, 1)
            .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(7)
    void testUpdateBeerBadRequest() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerStyle("");

        webTestClient.put()
            .uri(BeerController.BEER_PATH_ID, 1)
            .body(Mono.just(testBeer), BeerDto.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(8)
    void testUpdateBeerNotFound() {

        webTestClient.put()
            .uri(BeerController.BEER_PATH_ID, 999)
            .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(9)
    void testDeleteBeer() {
        webTestClient.delete()
            .uri(BeerController.BEER_PATH_ID, 1)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(9)
    void testDeleteBeerNotFound() {
        webTestClient.delete()
                .uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    @Order(10)
    void testPatchBeer() {

        webTestClient.patch()
            .uri(BeerController.BEER_PATH_ID, 2)
            .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @Order(11)
    void testPatchBeerNotFound() {

        webTestClient.patch()
            .uri(BeerController.BEER_PATH_ID, 999)
            .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
            .exchange()
            .expectStatus().isNotFound();
    }

}
