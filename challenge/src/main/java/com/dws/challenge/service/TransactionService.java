package com.dws.challenge.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.TransferMoney;
import com.dws.challenge.exception.InsufficientAmountException;

@Service
public class TransactionService {

	@Autowired
	private MoneyTransferAsyncService moneyTransferAsyncService;

	public void transferMoney(TransferMoney transferMoney) throws InsufficientAmountException, Exception {
		CompletableFuture<Boolean> check = moneyTransferAsyncService.transferMoney(transferMoney.getAccountFromId(),
				transferMoney.getAccountToId(), transferMoney.getAmount());
		if (!check.get()) {
			throw new InsufficientAmountException("Account id : " + transferMoney.getAccountFromId()
					+ " InsufficientAmount : " + transferMoney.getAmount());
		}
	}
}
