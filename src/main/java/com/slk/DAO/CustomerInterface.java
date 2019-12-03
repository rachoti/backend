package com.slk.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slk.model.Customer;
import com.slk.model.Loan;
import com.slk.model.Transaction;

@Repository
public interface CustomerInterface {
	Customer viewProfile(int id);
	List<Customer> viewAllCustomer();
	int transfer(int id,double amount,int account_number);
	List<Transaction> transactionhistory(int id);
	Customer viewBalance(int id);
	Loan viewLoan(int id);
	public void updateprofile(int id,Customer cust);
	public int loginValidation(String username,String password);
}
