package com.modsen.beershop.service;

import com.modsen.beershop.controller.dto.BuyBeerDto;
import com.modsen.beershop.controller.request.BuyBeerRequest;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.repository.UserRepository;
import com.modsen.beershop.repository.UserTransactionRepository;
import com.modsen.beershop.service.exceprion.ValidateException;
import com.modsen.beershop.service.validator.Validator;
import com.modsen.beershop.service.validator.verifier.Verifier;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BuyBeerService {

    private final List<Verifier<BuyBeerRequest, String>> validators;
    private final List<Verifier<BuyBeerRequest, List<BuyBeerDto>>> verifiers;

    public void buy(BuyBeerRequest buyBeerRequest, Object uuid) {
        final String errorMessage = validators.stream()
                .filter(v -> v.filter(buyBeerRequest))
                .map(v -> v.verify(buyBeerRequest))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (errorMessage != null) {
            throw new ValidateException(errorMessage);
        }
        verifiers.stream().filter(v -> v.filter(buyBeerRequest)).map(v -> v.verify(buyBeerRequest)).flatMap(Collection::stream).forEach(buyBeerDto -> {
            BeerRepository.INSTANCE.updateBeer(buyBeerDto.getBeer());
            buyBeerDto.setUserId(UserRepository.INSTANCE.readUserIdByUuid(uuid));
            UserTransactionRepository.INSTANCE.save(buyBeerDto);
        });
    }
}
