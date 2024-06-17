package com.app.team2.technotribe.krasvbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.team2.technotribe.krasvbank.dto.TransactionReportDto;
import com.app.team2.technotribe.krasvbank.external.services.ExternalReportingService;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

	@Autowired
	ExternalReportingService externalReportingService;

//	@GetMapping("getTransactionFromAcc")
//	public TransactionReportDto getAllTransactions(RequestParam String accountNumber) {
//        return externalReportingService.getAllTransactions(accountNumber);
//    }
	@GetMapping("/transactions/all")
	public TransactionReportDto getAllTransactions(@RequestParam String accountNumber) {
		return externalReportingService.getAllTransactions(accountNumber);
	}

	@GetMapping("/transactions/credits")
	public TransactionReportDto getAllCredits(@RequestParam String accountNumber) {
		return externalReportingService.getAllCredits(accountNumber);
	}

	@GetMapping("/transactions/debits")
	public TransactionReportDto getAllDebits(@RequestParam String accountNumber) {
		return externalReportingService.getAllDebits(accountNumber);
	}

	@GetMapping("/transactions/transfers")
	public TransactionReportDto getAllFundTransfers(@RequestParam String accountNumber) {
		return externalReportingService.getAllFundTransfers(accountNumber);
	}

	@GetMapping("/user/current-month-credits")
	public TransactionReportDto getAllCreditsForCurrentMonth(@RequestParam String accountNumber) {
		return externalReportingService.getAllCreditsForCurrentMonth(accountNumber);
	}

	@GetMapping("/user/current-month-debits")
	public TransactionReportDto getAllDebitsForCurrentMonth(@RequestParam String accountNumber) {
		return externalReportingService.getAllDebitsForCurrentMonth(accountNumber);
	}

	// ADMIN

	@GetMapping("/admin/user-all-transactions")
	public TransactionReportDto getAllTransactionsByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllTransactionsByUser(accountNumber);
	}

	@GetMapping("/admin/user-debits")
	public TransactionReportDto getAllDebitsByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllDebitsByUser(accountNumber);
	}

	@GetMapping("/admin/user-credits")
	public TransactionReportDto getAllCreditsByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllCreditsByUser(accountNumber);
	}

	@GetMapping("/admin/user-transfers")
	public TransactionReportDto getAllFundTransfersByUser(@RequestParam String accountNumber) {
		return externalReportingService.getAllFundTransfersByUser(accountNumber);
	}

}
