package fi.tatu.spring6r2dbc.repositories;

import fi.tatu.spring6r2dbc.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
