package com.modsen.beershop.controller.response;

import com.modsen.beershop.model.UserTransaction;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AllUsersHistoryResponse {
   List<UserTransaction> userTransactions;
}
