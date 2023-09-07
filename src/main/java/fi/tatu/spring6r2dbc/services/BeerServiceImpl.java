package fi.tatu.spring6r2dbc.services;

import fi.tatu.spring6r2dbc.mappers.BeerMapper;
import fi.tatu.spring6r2dbc.model.BeerDto;
import fi.tatu.spring6r2dbc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDto> listBeers() {
        return beerRepository.findAll()
            .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> getBeerById(Integer beerId) {
        return beerRepository.findById(beerId)
            .map(beerMapper::beerToBeerDto);
}

    @Override
    public Mono<BeerDto> saveNewBeer(BeerDto beerDto) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beerDto))
            .map(beerMapper::beerToBeerDto);
    }
}
