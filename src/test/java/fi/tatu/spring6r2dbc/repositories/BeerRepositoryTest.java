package fi.tatu.spring6r2dbc.repositories;

import fi.tatu.spring6r2dbc.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("1234567890")
                .build();
    }

    @Test
    void saveNewBeer() {
        beerRepository.save(getTestBeer())
            .subscribe(beer -> {
                System.out.println(beer.toString());
            });
    }
}