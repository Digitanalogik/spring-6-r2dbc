package fi.tatu.spring6r2dbc.services;

import fi.tatu.spring6r2dbc.model.BeerDto;
import reactor.core.publisher.Flux;

public class BeerServiceImpl implements BeerService {
    @Override
    public Flux<BeerDto> listBeers() {
        return null;
    }
}
