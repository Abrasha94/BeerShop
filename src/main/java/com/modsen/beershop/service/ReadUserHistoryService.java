package com.modsen.beershop.service;

import com.modsen.beershop.controller.response.UserHistoryResponse;
import com.modsen.beershop.model.UserTransaction;
import com.modsen.beershop.repository.UserTransactionRepository;
import com.modsen.beershop.service.validator.PageValidator;

import java.util.List;
import java.util.UUID;

public enum ReadUserHistoryService {
    INSTANCE;

    public UserHistoryResponse read(Integer page, Integer size, UUID uuid) {
        PageValidator.INSTANCE.validatePage(page);
        final Integer pageSize = PageValidator.INSTANCE.validatePageSize(size);
        List<UserTransaction> userTransactions = UserTransactionRepository.INSTANCE.readByUserUuid(uuid, pageSize, page);
        return new UserHistoryResponse(userTransactions);
    }
}
