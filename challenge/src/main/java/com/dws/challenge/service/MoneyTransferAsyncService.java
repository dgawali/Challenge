package com.dws.challenge.service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MoneyTransferAsyncService {

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Async("multithreadingbean")
	public CompletableFuture<Boolean> transferMoney(String accountFromId, String accountToId, BigDecimal amount) {
		log.info("Transfering money : {} from account : {} to account :{} ", amount, accountFromId, accountToId);
		Account accountFrom = accountsService.getAccount(accountFromId);
		Account accountTo = accountsService.getAccount(accountToId);

		if (accountFrom.getBalance().signum() > 0 && (accountFrom.getBalance().compareTo(amount)) >= 0) {
			accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
			accountTo.setBalance(accountTo.getBalance().add(amount));
			String creditTransferDescription = "Credited amount : " + amount + " Available balance : "
					+ accountTo.getBalance();
			String debitTransferDescription = "Debited amount : " + amount + " Available balance : "
					+ accountFrom.getBalance();

			emailNotificationService.notifyAboutTransfer(accountTo, creditTransferDescription);
			emailNotificationService.notifyAboutTransfer(accountFrom, debitTransferDescription);

		} else {
			return CompletableFuture.completedFuture(false);
		}
		return CompletableFuture.completedFuture(true);
	}
}
