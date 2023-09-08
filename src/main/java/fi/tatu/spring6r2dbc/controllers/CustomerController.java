package fi.tatu.spring6r2dbc.controllers;

import fi.tatu.spring6r2dbc.model.CustomerDto;
import fi.tatu.spring6r2dbc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private static final String BASE_URL = "http://localhost:8080";
    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDto> listCustomers() {
        log.info("List all Customers");
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<CustomerDto>> getCustomerById(@PathVariable("customerId") Integer customerId) {
        return customerService.getCustomerById(customerId)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> createNewCustomer(@Validated @RequestBody CustomerDto CustomerDto) {

        AtomicInteger atomicInteger = new AtomicInteger();

        customerService.saveNewCustomer(CustomerDto)
            .subscribe(savedDto -> {
                atomicInteger.set(savedDto.getId());
            });

        return Mono.just(ResponseEntity.created(UriComponentsBuilder
                        .fromHttpUrl(BASE_URL + CUSTOMER_PATH + "/" + atomicInteger.get())
                        .build().toUri())
                .build());
    }

    @PutMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> updateExistingCustomer(@PathVariable("customerId") Integer customerId,
                                                  @Validated @RequestBody CustomerDto CustomerDto) {

        customerService.updateCustomer(customerId, CustomerDto)
            .subscribe();

        return Mono.just(ResponseEntity.noContent().build());
    }

    @PatchMapping (CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingCustomer(@PathVariable("customerId") Integer customerId,
                                                 @Validated @RequestBody CustomerDto CustomerDto) {

        customerService.patchCustomer(customerId, CustomerDto)
            .subscribe();

        return Mono.just(ResponseEntity.ok().build());
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable Integer customerId) {
        return customerService.deleteCustomerById(customerId)
            .thenReturn(ResponseEntity.noContent().build());
    }

}
