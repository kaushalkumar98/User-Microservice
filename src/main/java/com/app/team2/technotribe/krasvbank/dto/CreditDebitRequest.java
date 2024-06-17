package com.app.team2.technotribe.krasvbank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDebitRequest {
	public CreditDebitRequest(String string, BigDecimal ten, String string2) {
		// TODO Auto-generated constructor stub
	}
	private String password;
private String accountNumber;
private BigDecimal amount;
}
