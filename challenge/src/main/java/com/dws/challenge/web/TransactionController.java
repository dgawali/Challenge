package com.dws.challenge.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dws.challenge.domain.TransferMoney;
import com.dws.challenge.exception.InsufficientAmountException;
import com.dws.challenge.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/transactions")
@Slf4j
public class TransactionController {

	// private final MoneyTransferAsyncService moneyTransferAsyncService;
	private final TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	public ResponseEntity<Object> transferMoney(@RequestBody @Valid TransferMoney transferMoney) {

		log.info("Transferring money started");
		try {
			this.transactionService.transferMoney(transferMoney);
		} catch (InsufficientAmountException exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
