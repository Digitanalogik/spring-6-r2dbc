package fi.tatu.spring6r2dbc.controllers;

import fi.tatu.spring6r2dbc.model.BeerDto;
import fi.tatu.spring6r2dbc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BeerController {

    private static final String BASE_URL = "http://localhost:8080";
    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    Flux<BeerDto> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId);
    }

    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDto beerDto) {

        AtomicInteger atomicInteger = new AtomicInteger();

        beerService.saveNewBeer(beerDto)
            .subscribe(savedDto -> {
                atomicInteger.set(savedDto.getId());
            });

        return Mono.just(ResponseEntity.created(UriComponentsBuilder
            .fromHttpUrl(BASE_URL + BEER_PATH + "/" + atomicInteger.get())
            .build().toUri())
            .build());
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                                  @Validated @RequestBody BeerDto beerDto) {

        beerService.updateBeer(beerId, beerDto)
            .subscribe();

        return Mono.just(ResponseEntity.noContent().build());
    }

    @PatchMapping (BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingBeer(@PathVariable("beerId") Integer beerId,
                                                 @Validated @RequestBody BeerDto beerDto) {

        beerService.patchBeer(beerId, beerDto)
                .subscribe();

        return Mono.just(ResponseEntity.ok().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable Integer beerId) {
        return beerService.deleteById(beerId)
            .thenReturn(ResponseEntity.noContent().build());
    }
}
