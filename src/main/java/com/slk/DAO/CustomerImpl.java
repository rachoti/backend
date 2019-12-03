package com.slk.DAO;

import static org.assertj.core.api.Assertions.shouldHaveThrown;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.slk.DButil.ConnDB;
import com.slk.model.Customer;
import com.slk.model.Loan;
import com.slk.model.Transaction;
@Repository
public class CustomerImpl implements CustomerInterface{
	static List<Customer> list1=new ArrayList();
	static List<Transaction> listTrans=new ArrayList();
	public CustomerImpl() {
		try{
			Connection conn=ConnDB.getConnection();
			Statement stmt = conn.createStatement(); 
	        ResultSet rs = stmt.executeQuery("select * from customer"); 
	        while (rs.next()) { 
	            Customer cust=new Customer();
	            cust.setAccount_no(rs.getInt("account_no"));
	            cust.setName(rs.getString("name"));
	            cust.setDob(rs.getString("DOB"));
	            cust.setPhone_no(rs.getInt("phone_no"));
	            cust.setUsername(rs.getString("username"));
	            cust.setPassword(rs.getString("password"));
	            cust.setAmount(rs.getDouble("amount"));
	            cust.setBranch_id(rs.getInt("branch_id"));
	            cust.setLoan_id(rs.getString("loan_id"));
	            cust.setType_id(rs.getString("type_id"));
	            list1.add(cust);
	        } 
	        rs.close(); 
	        stmt.close();
			
		}catch(Exception e){
			System.out.println(e);
		}
	}
	@Override
	public Customer viewProfile(int id) {
		// TODO Auto-generated method stub
		for (Customer c:list1) {
			if (c.getAccount_no()==id) {
				System.out.println(c.getAccount_no());
				System.out.println(c.getName());
				System.out.println(c.getPhone_no());				
				return c;
			}
		}
		return null;
		
	}
	
	@Override
	public int loginValidation(String username,String password){
		int flag=0;
		for (Customer c:list1) {
		
			if (c.getUsername().equals(username)  && c.getPassword().equals(password)) {
				flag=1;
							
				return flag;
			}
		}
		return flag;
	}
	
	

	@Override
	public int transfer(int id,double amount,int account_number) {
		// TODO Auto-generated method stub
		int sendAccNo=id;
		int receiveAccNo=account_number;
		double sendTempAmt=0;
		double receiveTempAmt=0;
		double tempAmt=0;
		String accType="";
		double debit=0;
		double credit=0;
		int transId=0;
		int flag=0;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(formatter.format(date));
		CustomerInterface cust=new CustomerImpl();
		try{
			Connection conn=ConnDB.getConnection();
			Statement stmt = conn.createStatement(); 
	        ResultSet rs = stmt.executeQuery("select amount,type_id from customer where account_no="+sendAccNo); 
	        while (rs.next()) { 
	        	sendTempAmt=rs.getDouble("amount");
	        	accType=rs.getString("type_id");
	        }
	       rs = stmt.executeQuery("select amount from customer where account_no="+receiveAccNo); 
	        while (rs.next()) { 
	        	receiveTempAmt=rs.getDouble("amount");
	        }
	        rs = stmt.executeQuery("select max(id) as id from transaction"); 
	        while (rs.next()) { 
	        	transId=rs.getInt("id");
	        }
	        transId++;
	        char c=accType.charAt(0);
	        if(c=='C'){
	        	tempAmt=sendTempAmt-amount;
	        	if(tempAmt>=5000){
	        		debit=amount;
	        		
	        		stmt.executeUpdate("update customer set amount="+tempAmt+"where account_no="+sendAccNo);
	        		stmt.executeUpdate("insert into transaction values("+transId+",'"+formatter.format(date)+"',"+credit+","+debit+","+sendAccNo+","+tempAmt+")");
	        		System.out.println("Sender balance= "+tempAmt);
	        		debit=0;
	        		credit=amount;
	        		
	        		tempAmt=receiveTempAmt+amount;
	        		transId++;
	        		stmt.executeUpdate("update customer set amount="+tempAmt+"where account_no="+receiveAccNo);
	        		stmt.executeUpdate("insert into transaction values("+transId+",'"+formatter.format(date)+"',"+credit+","+debit+","+receiveAccNo+","+tempAmt+")");
	        		System.out.println("receiver balance= "+tempAmt);
	        		flag=1;
	        	}else{
	        		System.out.println("No Minimum Balance");
	        	}
	        }else{
	        	tempAmt=sendTempAmt-amount;
	        	if(tempAmt>=500){
	        		debit=amount;
	        		
	        		stmt.executeUpdate("update customer set amount="+tempAmt+"where account_no="+sendAccNo);
	        		stmt.executeUpdate("insert into transaction values("+transId+",'"+formatter.format(date)+"',"+credit+","+debit+","+sendAccNo+","+tempAmt+")");
	        		System.out.println("Sender balance= "+tempAmt);
	        		debit=0;
	        		credit=amount;
	        		
	        		tempAmt=receiveTempAmt+amount;
	        		transId++;
	        		stmt.executeUpdate("update customer set amount="+tempAmt+"where account_no="+receiveAccNo);
	        		stmt.executeUpdate("insert into transaction values("+transId+",'"+formatter.format(date)+"',"+credit+","+debit+","+receiveAccNo+","+tempAmt+")");
	        		System.out.println("receiver balance= "+tempAmt);
	        		flag=1;
	        	}else{
	        		System.out.println("No Minimum Balance");
	        	}
	        }
	        
	        
	        	        
	        
		}catch(Exception e){
			System.out.println(e);
		}
		
		return flag;
	}

	@Override
	public List<Transaction> transactionhistory(int id) {
		// TODO Auto-generated method stub
		listTrans.clear();
		
		try{
			Connection conn=ConnDB.getConnection();
			Statement stmt = conn.createStatement(); 
	        ResultSet rs = stmt.executeQuery("select * from transaction where account_no="+id); 
	        while (rs.next()) {
	        	Transaction tl=new Transaction();
	        	tl.setId(rs.getInt("id"));
	        	tl.setTrans_date(rs.getString("trans_date"));
	        	tl.setCredit(rs.getDouble("credit"));
	        	tl.setDebit(rs.getDouble("debit"));
	        	tl.setAccount_no(rs.getInt("account_no"));
	        	tl.setAmount(rs.getDouble("balance"));
	        	System.out.println(rs.getDouble("balance"));
	        	listTrans.add(tl);
	        
	        }
		}catch(Exception e){
			System.out.println(e);
		}
			
				return listTrans;
	}

	@Override
	public Customer viewBalance(int id) {
		// TODO Auto-generated method stub
		for (Customer c:list1) {
			System.out.println(c.getAmount());
			if (c.getAccount_no()==id) {				
				return c;
			}
		}
		return null;
	}

	@Override
	public Loan viewLoan(int id) {
		// TODO Auto-generated method stub
		//List<Loan> listLoan=new ArrayList();
		Loan ln=new Loan();
		for (Customer c:list1) {
			System.out.println(c.getAccount_no());
			if (c.getAccount_no()==id) {
		
				String ltype=c.getLoan_id();
				char tempChar=ltype.charAt(0);
				if(tempChar=='C'){
					System.out.println("car loan");
					try{
						Connection conn=ConnDB.getConnection();
						Statement stmt = conn.createStatement(); 
				        ResultSet rs = stmt.executeQuery("select loan.id as id,loan.loan_type as loan_type ,loan.interest_rate as interest_rate from customer,loan where account_no="+id+" and id='"+ltype+"'" );  
				        
				        while (rs.next()) { 
				        	ln.setId(rs.getString("id"));
				        	ln.setLoan_type(rs.getString("loan_type"));
				        	ln.setInterest_rate(rs.getDouble("interest_rate"));
				        	System.out.println(ln.getId());
				        } 
				        rs.close(); 
				        stmt.close();
						
					}catch(Exception e){
						System.out.println(e);
					}
				}else if(tempChar=='H'){
					System.out.println("Home loan");
					
					try{
						Connection conn=ConnDB.getConnection();
						Statement stmt = conn.createStatement(); 
						ResultSet rs = stmt.executeQuery("select loan.id as id,loan.loan_type as loan_type ,loan.interest_rate as interest_rate from customer,loan where account_no="+id+" and id='"+ltype+"'" );
				       
				        while (rs.next()) { 
				        	ln.setId(rs.getString("id"));
				        	ln.setLoan_type(rs.getString("loan_type"));
				        	ln.setInterest_rate(rs.getDouble("interest_rate"));
				        	System.out.println(ln.getId());
				        } 
				        rs.close(); 
				        stmt.close();
						
					}catch(Exception e){
						System.out.println(e);
					}
					
				}else if(tempChar=='S'){
					System.out.println("Student loan");
					
					try{
						Connection conn=ConnDB.getConnection();
						Statement stmt = conn.createStatement(); 
						ResultSet rs = stmt.executeQuery("select loan.id as id,loan.loan_type as loan_type ,loan.interest_rate as interest_rate from customer,loan where account_no="+id+" and id='"+ltype+"'" );
				        
				        while (rs.next()) { 
				        	ln.setId(rs.getString("id"));
				        	ln.setLoan_type(rs.getString("loan_type"));
				        	ln.setInterest_rate(rs.getDouble("interest_rate"));
				        	System.out.println(ln.getId());
				        } 
				        rs.close(); 
				        stmt.close();
						
					}catch(Exception e){
						System.out.println(e);
					}
										
				}else{
					System.out.println("No loan");
				}
				return ln;
			}
		}
		return null;
	}

	@Override
	public List<Customer> viewAllCustomer() {
		// TODO Auto-generated method stub
		return list1;
	}

	public void updateprofile(int id,Customer cust){
		for (Customer c:list1) {
			if (c.getAccount_no()==id) {
				System.out.println("update inside");
				try{
					Connection conn=ConnDB.getConnection();
					Statement stmt = conn.createStatement(); 
			        stmt.executeUpdate("update customer set name='"+cust.getName()+"',DOB='"+cust.getDob()+"',phone_no="+cust.getPhone_no()+",username='"+cust.getUsername()+"',password='"+cust.getPassword()+"' where account_no="+id+""); 
			        System.out.println("successfull updated......!");
				}catch(Exception e){
					System.out.println(e);
				}
				
			}
		}
		
	}
	
}
