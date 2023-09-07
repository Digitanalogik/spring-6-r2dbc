package fi.tatu.spring6r2dbc.controllers;

import fi.tatu.spring6r2dbc.model.BeerDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";

    @GetMapping(BEER_PATH)
    Flux<BeerDto> listBeers() {
        BeerDto beer1 = BeerDto.builder().id(1).build();
        BeerDto beer2 = BeerDto.builder().id(2).build();
        return Flux.just(beer1, beer2);
    }

}
