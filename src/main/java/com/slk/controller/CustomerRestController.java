package com.slk.controller;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.slk.DAO.CustomerImpl;
import com.slk.DAO.CustomerInterface;
import com.slk.model.Customer;
import com.slk.model.Loan;
import com.slk.model.Transaction;
@CrossOrigin(origins="http://localhost:4200")
@RestController
//@RequestMapping("/customerService")

public class CustomerRestController {
	
	@Autowired
	private CustomerImpl customerImpl;
	CustomerInterface ci=new CustomerImpl();

	@GetMapping("/Customer")
		public List getCustomers() {
		
		return ci.viewAllCustomer();
	}

	@GetMapping("/Customer/{id}")
	public ResponseEntity getCustomer(@PathVariable("id") int id) {
		
		Customer customer=ci.viewProfile(id);
			
		if (customer == null) {
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(customer, HttpStatus.OK);
	}
	//********************  Login   ****************************
	@GetMapping("/Customer/{username}/{password}")
	public ResponseEntity getCustomerLogin(@PathVariable("username") String username,@PathVariable("password") String password) {
		
		int flag=ci.loginValidation(username,password);
			
		if (flag == 0) {
			return new ResponseEntity("No Customer found for ID " + username, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(flag, HttpStatus.OK);
	}
	//
	
	@GetMapping("/Customer/Balance/{id}")
	public ResponseEntity getCustomerBalance(@PathVariable("id") int id) {
		
		Customer customer=ci.viewBalance(id);;
			
		if (customer == null) {
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(customer, HttpStatus.OK);
	}
	
	
	@GetMapping("/Customer/Loan/{id}")
	public ResponseEntity getCustomerLoan(@PathVariable("id") int id) {
		
		Loan loan=ci.viewLoan(id);
			
		if (loan == null) {
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(loan, HttpStatus.OK);
	}
	
	
	@GetMapping("/Customer/Transaction/{id}")
	public ResponseEntity getCustomerTransaction(@PathVariable("id") int id) {
		
		List<Transaction> t=ci.transactionhistory(id);;
			
		if (t == null) {
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(t, HttpStatus.OK);
	}
	
	
	@GetMapping("/Customer/Transfer/{id}/{amount}/{accNo}")
	public ResponseEntity getCustomerTransfer(@PathVariable("id") int id,@PathVariable("amount") double amount,@PathVariable("accNo") int accNo) {
		//double amount=500.00;
		//int accNo=123456;
		int flag=ci.transfer(id,amount,accNo);
			
		if (flag == 0) {
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(flag, HttpStatus.OK);
	}
	
	@PutMapping(value = "/put/Customer/{id}")
	public ResponseEntity createCustomer(@PathVariable("id") int id,@RequestBody Customer cust ) {

		ci.updateprofile(id, cust);
		return new ResponseEntity(cust, HttpStatus.OK);
	}
	    
//	@PutMapping(value = "/put/Customer/{id}")
//	public ResponseEntity createTransfer(@PathVariable("id") int id,@PathVariable("amount") int amount,@PathVariable("account_number") int account_number) {
//
//		Customer cust=ci.transfer(id, amount, account_number);
//		return new ResponseEntity(cust, HttpStatus.OK);
//	}
	
	
//
//	
//	@DeleteMapping("/delete/Customer/{id}")
//	public ResponseEntity deleteCustomer(@PathVariable Long id) {
//
//		if (null == CustomerDAO.delete(id)) {
//			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity(id, HttpStatus.OK);
//
//	}

	
}
