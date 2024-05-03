package com.dws.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransferMoney {

	@NotNull
	@NotEmpty
	private String accountFromId;

	@NotNull
	@NotEmpty
	private String accountToId;

	@NotNull
	private BigDecimal amount;

	@JsonCreator
	public TransferMoney(@JsonProperty("accountFromId") String accountFromId, @JsonProperty("accountToId") String accountToId,
			@JsonProperty("amount") BigDecimal amount) {
		this.accountFromId = accountFromId;
		this.accountToId = accountToId;
		this.amount = amount;
	}
}
