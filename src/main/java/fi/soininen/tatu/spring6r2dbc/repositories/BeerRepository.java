package fi.soininen.tatu.spring6r2dbc.repositories;

import fi.soininen.tatu.spring6r2dbc.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
}
