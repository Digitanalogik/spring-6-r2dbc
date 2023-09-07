package fi.tatu.spring6r2dbc.services;

import fi.tatu.spring6r2dbc.model.BeerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDto> listBeers();

    Mono<BeerDto> getBeerById(Integer beerId);
}
