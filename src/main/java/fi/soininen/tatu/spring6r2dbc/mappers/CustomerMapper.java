package fi.soininen.tatu.spring6r2dbc.mappers;

import fi.soininen.tatu.spring6r2dbc.domain.Customer;
import fi.soininen.tatu.spring6r2dbc.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDto dto);
    CustomerDto customerToCustomerDto(Customer customer);
}
