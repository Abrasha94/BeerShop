package com.modsen.beershop.service;

import com.modsen.beershop.controller.response.AllUsersHistoryResponse;
import com.modsen.beershop.model.UserTransaction;
import com.modsen.beershop.repository.UserTransactionRepository;
import com.modsen.beershop.service.validator.PageValidator;

import java.util.List;

public enum ReadAllUsersHistoryService {
    INSTANCE;

    public AllUsersHistoryResponse read(Integer page, Integer size) {
        PageValidator.INSTANCE.validatePage(page);
        final Integer pageSize = PageValidator.INSTANCE.validatePageSize(size);
        final List<UserTransaction> usersTransactions = UserTransactionRepository.INSTANCE.readAll(pageSize, page);
        return new AllUsersHistoryResponse(usersTransactions);
    }
}
