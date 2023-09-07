package fi.tatu.spring6r2dbc.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.tatu.spring6r2dbc.config.DatabaseConfiguration;
import fi.tatu.spring6r2dbc.domain.Beer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataR2dbcTest
@Import(DatabaseConfiguration.class)
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

    @Test
    void testCreateJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(objectMapper.writeValueAsString(getTestBeer()));
    }
}