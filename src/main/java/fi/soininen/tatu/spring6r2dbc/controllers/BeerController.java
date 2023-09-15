package fi.soininen.tatu.spring6r2dbc.controllers;

import fi.soininen.tatu.spring6r2dbc.model.BeerDto;
import fi.soininen.tatu.spring6r2dbc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    Mono<ResponseEntity<BeerDto>> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDto beerDto) {

        AtomicInteger atomicInteger = new AtomicInteger();

        beerService.saveNewBeer(beerDto)
            .subscribe(savedDto -> atomicInteger.set(savedDto.getId()));

        return Mono.just(ResponseEntity.created(UriComponentsBuilder
            .fromHttpUrl(BASE_URL + BEER_PATH + "/" + atomicInteger.get())
            .build().toUri())
            .build());
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                                  @Validated @RequestBody BeerDto beerDto) {
        return beerService.updateBeer(beerId, beerDto)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(saveDto -> ResponseEntity.noContent().build());
    }

    @PatchMapping (BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingBeer(@PathVariable("beerId") Integer beerId,
                                                 @Validated @RequestBody BeerDto beerDto) {
        return beerService.patchBeer(beerId, beerDto)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(updatedDto -> ResponseEntity.ok().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable Integer beerId) {
        return beerService.getBeerById(beerId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(beerDto -> beerService.deleteById(beerDto.getId()))
            .thenReturn(ResponseEntity.noContent().build());
    }
}
