package fi.tatu.spring6r2dbc.services;

import fi.tatu.spring6r2dbc.mappers.CustomerMapper;
import fi.tatu.spring6r2dbc.model.CustomerDto;
import fi.tatu.spring6r2dbc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDto> listCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> saveNewCustomer(CustomerDto customerDto) {
        return customerRepository.save(customerMapper.customerDtoToCustomer(customerDto))
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> updateCustomer(Integer customerId, CustomerDto customerDto) {
        return customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    foundCustomer.setName(customerDto.getName());

                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> patchCustomer(Integer customerId, CustomerDto customerDto) {

        return customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    if(StringUtils.hasText(customerDto.getName())) {
                        foundCustomer.setName(customerDto.getName());
                    }
                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<Void> deleteCustomerById(Integer customerId) {
        log.debug("In the service implementation, trying to delete customer with ID={}", customerId);
        return customerRepository.deleteById(customerId);
    }
}
