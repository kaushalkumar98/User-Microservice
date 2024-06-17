package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

	public TransferRequest(String string, String string2, BigDecimal ten, String string3) {
		// TODO Auto-generated constructor stub
	}
	private String password;
	private String sourceAccountNumber;
	private String destinationAccountNumber;
	private BigDecimal amount;
}
