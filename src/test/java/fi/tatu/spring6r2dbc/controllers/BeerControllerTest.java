package fi.tatu.spring6r2dbc.controllers;

import fi.tatu.spring6r2dbc.model.BeerDto;
import fi.tatu.spring6r2dbc.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;


    @Test
    void testListBeers() {
        webTestClient.get().uri(BeerController.BEER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    void testGetBeerById() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .expectBody(BeerDto.class);
    }

    @Test
    void testCreateBeer() {
        final String NEW_BEER_URL = "http://localhost:8080/api/v2/beer/4";

        webTestClient.post().uri(BeerController.BEER_PATH)
            .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().location(NEW_BEER_URL);
    }

    @Test
    void testUpdateBeer() {

        webTestClient.put().uri(BeerController.BEER_PATH_ID, 1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDto.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteBeer() {

        webTestClient.delete().uri(BeerController.BEER_PATH_ID, 1)
            .exchange()
            .expectStatus().isNoContent();
    }
}