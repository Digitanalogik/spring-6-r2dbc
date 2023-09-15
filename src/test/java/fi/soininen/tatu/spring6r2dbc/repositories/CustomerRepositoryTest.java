package fi.soininen.tatu.spring6r2dbc.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.soininen.tatu.spring6r2dbc.config.DatabaseConfiguration;
import fi.soininen.tatu.spring6r2dbc.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;


@Slf4j
@DataR2dbcTest
@Import(DatabaseConfiguration.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    public static Customer getTestCustomer() {
        return Customer.builder()
            .name("Test Customer")
            .build();
    }

    @Test
    void saveNewCustomer() {
        customerRepository.save(getTestCustomer())
            .subscribe(beer -> {
                System.out.println(beer.toString());
            });
    }

    @Test
    void testCreateJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(objectMapper.writeValueAsString(getTestCustomer()));
    }
}