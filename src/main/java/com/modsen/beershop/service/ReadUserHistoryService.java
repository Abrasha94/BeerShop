package com.modsen.beershop.service;

import com.modsen.beershop.controller.response.UserHistoryResponse;
import com.modsen.beershop.model.UserTransaction;
import com.modsen.beershop.repository.UserRepository;
import com.modsen.beershop.repository.UserTransactionRepository;
import com.modsen.beershop.service.validator.PageValidator;

import java.util.List;

public enum ReadUserHistoryService {
    INSTANCE;

    public UserHistoryResponse read(Integer page, Integer size, Object uuid) {
        PageValidator.INSTANCE.validatePage(page);
        final Integer pageSize = PageValidator.INSTANCE.validatePageSize(size);
        final Integer id = UserRepository.INSTANCE.readUserIdByUuid(uuid);
        List<UserTransaction> userTransactions = UserTransactionRepository.INSTANCE.readByUserId(id, pageSize, page);
        return new UserHistoryResponse(userTransactions);
    }
}
