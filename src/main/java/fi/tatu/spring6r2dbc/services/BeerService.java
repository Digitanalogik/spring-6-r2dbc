package fi.tatu.spring6r2dbc.services;

import fi.tatu.spring6r2dbc.model.BeerDto;
import reactor.core.publisher.Flux;

public interface BeerService {

    Flux<BeerDto> listBeers();
}
