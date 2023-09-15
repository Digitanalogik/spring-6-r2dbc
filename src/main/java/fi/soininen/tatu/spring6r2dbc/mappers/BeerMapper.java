package fi.soininen.tatu.spring6r2dbc.mappers;

import fi.soininen.tatu.spring6r2dbc.domain.Beer;
import fi.soininen.tatu.spring6r2dbc.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDto dto);
    BeerDto beerToBeerDto(Beer beer);
}
