package BANK_MANAGEMENT_SYSTEM;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

class Users {

    Scanner sc = new Scanner(System.in);

    String StartAccountNumber = "05010100001001";
    String CStartAccountNumber = "06010200001001";    
    
    ArrayList<String> incrementAccountNumber(String AccountNumber) throws SQLException {
        ArrayList<String> accountNumbers = new ArrayList<>();
        long AccountNumberAsLong = Long.parseLong(AccountNumber);
        AccountNumberAsLong++;

        // Keep generating new account numbers until a unique one is found
        while (isAccountNumberExists(String.format("%014d", AccountNumberAsLong))) {
            AccountNumberAsLong++;
        }

        accountNumbers.add(String.format("%014d", AccountNumberAsLong));
        return accountNumbers;
    }

    ArrayList<String> incrementCAccountNumber(String cAccountNumber ) throws SQLException {
        ArrayList<String> cAccountNumbers = new ArrayList<>();
        long AccountNumberAsLong = Long.parseLong(cAccountNumber);
        AccountNumberAsLong++;

        // Keep generating new account numbers until a unique one is found
        while (isCAccountNumberExists(String.format("%014d", AccountNumberAsLong))) {
            AccountNumberAsLong++;
        }

        cAccountNumbers.add(String.format("%014d", AccountNumberAsLong));
        return cAccountNumbers;
    }
    
    boolean isAccountNumberExists(String accountNumber) throws SQLException {
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE Account_number = '" + accountNumber + "'");
	        
	        rs.next();
	        
	        return rs.getInt(1) > 0;
	        
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
       return false;
    }
    
    boolean isCAccountNumberExists(String CAccount_number) throws SQLException {
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE Account_number = '" + CAccount_number + "'");
	        
	        rs.next();
	        
	        return rs.getInt(1) > 0;
	        
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
       return false;
    }
    
    boolean isUserExistsForAccountType(String contactNumber, String panCardNumber, String accountType) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root","MYSQL_PASSWORD");
            
            String query = "SELECT COUNT(*) " +
                           "FROM users u " +
                           "JOIN accounts a ON u.user_id = a.user_id " +
                           "WHERE u.Contact_number = ? " +
                           "AND u.PanCard_no = ? " +
                           "AND a.Account_type = ?";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, contactNumber);
            ps.setString(2, panCardNumber);
            ps.setString(3, accountType);
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
//------------------------------------------------------------------------------------------------------------------------------------------------------------
    public int getUserId(Connection conn, String accountNumber) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM users WHERE Account_number = ?");
        stmt.setString(1, accountNumber);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("user_id");
        } else {
            // Handle error if user ID not found
            System.out.println("User ID not found for account number: " + accountNumber);
            return -1; // Or throw an exception
        }
    }
    
    public int getCUserId(Connection conn, String cAccountNumber) throws SQLException {
    	PreparedStatement psu = conn.prepareStatement("SELECT user_id FROM users WHERE CAccount_number = ?");
    	psu.setString(1, cAccountNumber);
    	ResultSet rs = psu.executeQuery();
        if (rs.next()) {
            return rs.getInt("user_id");
        } else {
            // Handle error if user ID not found
            System.out.println("User ID not found for account number: " + cAccountNumber);
            return -1; // Or throw an exception
        }
    }
//------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void newAccountForSavings(String Name, String Contact_number, String EmailId, String Password,
            String Gender, int Age, String Address, String UPI_id, String mpin,
            Timestamp createdAt, String AadharCard_no, String PanCard_no) throws ClassNotFoundException, SQLException {
    	
    	String Pin;
    	String[] BranchName = {"Thane","Dombivli","Jalgaon"};
        String[] IFSCCode = {"ABCD00001", "ABCD00002", "ABCD00003"};
        
    	System.out.println("Select a branch (1. Thane, 2. Dombivli, 3. Jalgaon): ");
        int branchChoice = sc.nextInt();
        System.out.println("ENTER A PIN FOR NET BANKING & BANK PIN TO SET OF 6 DIGIT: ");
        Pin=sc.next();
        if(Pin.length()==6) {
        	System.out.println("CORRECT PIN SET.");
        }
        else {
        	System.out.println("A PIN FOR NET BANKING & BANK PIN TO SET OF 6 DIGIT IS INCORRECT.");
        }

        String selectedBranch = BranchName[branchChoice - 1];
        String selectedIFSCCode = IFSCCode[branchChoice - 1];       
        
    	
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
    	PreparedStatement ps = conn.prepareStatement("INSERT INTO users (Account_number, Name, Contact_number, EmailId, Password, Gender, Age, Address, UPI_id, mpin, Created_at, AadharCard_no, PanCard_no, CAccount_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    
    	ArrayList<String> generatedAccountNumbers = incrementAccountNumber(StartAccountNumber);

    	for (String accountNumber : generatedAccountNumbers) {
    		StartAccountNumber = accountNumber;  // Update the StartAccountNumber for the next user
    		ps.setString(1, accountNumber);
    		ps.setString(2, Name);
    		ps.setString(3, Contact_number);
    		ps.setString(4, EmailId);
    		ps.setString(5, Password);
    		ps.setString(6, Gender);
    		ps.setInt(7, Age);
    		ps.setString(8, Address);
    		ps.setString(9, UPI_id);
    		ps.setString(10, mpin);
    		ps.setTimestamp(11, createdAt);
    		ps.setString(12, AadharCard_no);
    		ps.setString(13, PanCard_no);
    		ps.setString(14, null);  // CAccount_number is not used for savings account
    		
    		ps.executeUpdate();
    		
    		// Insert initial balance for savings account
    		int user_id = getUserId(conn, accountNumber);
            PreparedStatement ps1 = conn.prepareStatement("INSERT INTO accounts (user_id, Account_type, Balance, IFSC_code, Branch_Name, Pin) VALUES (?, ?, ?, ?, ?, ?)");
            ps1.setInt(1, user_id); //
            ps1.setString(2, "saving");
            ps1.setBigDecimal(3, new BigDecimal(1000.00)); // Set initial balance to 1000
            ps1.setString(4, selectedIFSCCode);
            ps1.setString(5, selectedBranch);
            ps1.setString(6, Pin);
            
            ps1.executeUpdate();
    		System.out.println("New savings account created with Account Number: " + accountNumber);
    	}
}

    public void newAccountForCurrent(String Name, String Contact_number, String EmailId, String Password,
            String Gender, int Age, String Address, String UPI_id, String mpin,
            Timestamp createdAt, String AadharCard_no, String PanCard_no) throws ClassNotFoundException, SQLException {
    	
    	String Pin;
    	System.out.println("ENTER A PIN FOR NET BANKING & BANK PIN TO SET OF 6 DIGIT: ");
        Pin=sc.next();
        if(Pin.length()==6) {
        	System.out.println("CORRECT PIN SET.");
        }
        else {
        	System.out.println("A PIN FOR NET BANKING & BANK PIN TO SET OF 6 DIGIT IS INCORRECT.");
        }
        
    	String[] BranchName = {"Thane","Dombivli","Jalgaon"};
        String[] IFSCCode = {"ABCD00001", "ABCD00002", "ABCD00003"};
        
    	System.out.println("Select a branch (1. Thane, 2. Dombivli, 3. Jalgaon): ");
        int branchChoice = sc.nextInt();

        String selectedBranch = BranchName[branchChoice - 1];
        String selectedIFSCCode = IFSCCode[branchChoice - 1];   
    	
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
    
    PreparedStatement ps = conn.prepareStatement("INSERT INTO users (Account_number, Name, Contact_number, EmailId, Password, Gender, Age, Address, UPI_id, mpin, Created_at, AadharCard_no, PanCard_no, CAccount_number) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    // Generate the current account number
    ArrayList<String> generatedAccountNumbers = incrementCAccountNumber(CStartAccountNumber);

    for (String cAccountNumber : generatedAccountNumbers) {
        CStartAccountNumber = cAccountNumber;  // Update the CStartAccountNumber for the next user
          // Set the Account_number for the current account
        ps.setString(1,  null);
        ps.setString(2, Name);
        ps.setString(3, Contact_number);
        ps.setString(4, EmailId);
        ps.setString(5, Password);
        ps.setString(6, Gender);
        ps.setInt(7, Age);
        ps.setString(8, Address);
        ps.setString(9, UPI_id);
        ps.setString(10, mpin);
        ps.setTimestamp(11, createdAt);
        ps.setString(12, AadharCard_no);
        ps.setString(13, PanCard_no);
        ps.setString(14, cAccountNumber);// Savings account number is not used for current account
        
        
        ps.executeUpdate();
        
        int user_id = getCUserId(conn, cAccountNumber);
        PreparedStatement ps1 = conn.prepareStatement("INSERT INTO accounts (user_id, Account_type, Balance, IFSC_code, Branch_Name, Pin) VALUES (?, 'current', ?, ?, ?, ?)");
        ps1.setInt(1, user_id); 
        ps1.setBigDecimal(2, new BigDecimal(1500.00)); // Set initial balance to 1500
        ps1.setString(3, selectedIFSCCode);
        ps1.setString(4, selectedBranch);
        ps1.setString(5, Pin);
        
        ps1.executeUpdate();
        
        System.out.println("New current account created with Account Number: " + cAccountNumber);
    }
}
}

class Accounts{
	
	 Scanner sc =new Scanner(System.in);
	 public boolean deleteAccount(String accountNumber) throws SQLException, ClassNotFoundException {
		    // Query to delete from accounts based on user_id derived from the users table
		    String deleteAccountsQuery = "DELETE FROM accounts WHERE user_id IN (SELECT user_id FROM users WHERE Account_number = ? OR CAccount_number = ?)";
		    
		    // Query to delete from users table
		    String deleteUserQuery = "DELETE FROM users WHERE Account_number = ? OR CAccount_number = ?";

		    Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");

		    try {
		        // Prepare and execute the delete statement for accounts
		        PreparedStatement psDeleteAccounts = conn.prepareStatement(deleteAccountsQuery);
		        psDeleteAccounts.setString(1, accountNumber);
		        psDeleteAccounts.setString(2, accountNumber);
		        psDeleteAccounts.executeUpdate();

		        // Prepare and execute the delete statement for users
		        PreparedStatement psDeleteUser = conn.prepareStatement(deleteUserQuery);
		        psDeleteUser.setString(1, accountNumber);
		        psDeleteUser.setString(2, accountNumber);
		        int rowsDeleted = psDeleteUser.executeUpdate();

		        if (rowsDeleted > 0) {
		            System.out.println("The User Account and associated accounts are deleted.");
		            return true;
		        } else {
		            System.out.println("The User Account is not deleted. ERROR.....");
		            return false;
		        }
		    } catch (SQLException e) {
		        System.out.println("Error while deleting the account: " + e.getMessage());
		        throw e;
		    } finally {
		        conn.close();
		    }
		}


	 
	 public boolean updateUserDetails(String accountNumber, String ifscCode, String newEmail, String newPassword, String newContactNumber, String newAddress, String newBranch) throws SQLException, ClassNotFoundException {
		 	
		 	String updateQuery = "UPDATE users u "
	                           + "JOIN accounts a ON u.user_id = a.user_id "
	                           + "SET u.EmailId = ?, u.Password = ?, u.Contact_number = ?, u.Address = ?, a.Branch_Name = ?, a.IFSC_code = ? "
	                           + "WHERE u.Account_number = ? OR u.CAccount_number = ?";

	        int row;

	        // Database connection setup
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");

	        // Update the account
	        PreparedStatement updatePs = conn.prepareStatement(updateQuery);
	        updatePs.setString(1, newEmail);
	        updatePs.setString(2, newPassword);
	        updatePs.setString(3, newContactNumber);
	        updatePs.setString(4, newAddress);
	        updatePs.setString(5, newBranch);
	        updatePs.setString(6, ifscCode);
	        updatePs.setString(7, accountNumber);
	        updatePs.setString(8, accountNumber);

	        row = updatePs.executeUpdate();
	        return row > 0;
	}

	 public int getUserIdFromAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
	        
		 String query = "SELECT user_id FROM users WHERE Account_number = ? or CAccount_number = ?";
	        
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	        PreparedStatement preparedStatement = conn.prepareStatement(query);
	        preparedStatement.setString(1, accountNumber);
	        preparedStatement.setString(2, accountNumber);
	        
	        ResultSet rs = preparedStatement.executeQuery();
	        if (rs.next()) {
	            
	         	int userid = rs.getInt("user_id");
	         	return userid;
	        }
	        return -1;
	    }
	 
	 public void viewMyAccount(String accountNumber) throws SQLException, ClassNotFoundException {
	        int userId = getUserIdFromAccountNumber(accountNumber);
	        
	        if (userId == -1) {
	            System.out.println("Account not found.");
	            return;
	        }
	        
	        String query = "SELECT a.user_id, a.Account_id, a.Account_type, a.IFSC_code, a.Branch_Name, a.Balance, a.Internet_Banking, a.Pin, a.Created_at, " +
	                "u.Name, u.Contact_number, u.EmailId, u.Gender, u.Age, u.Address, u.AadharCard_no, u.PanCard_no, " +
	                "u.Account_number, u.CAccount_number, " +  
	                "p.Linked_value, p.linked_via, p.Linked_date, " +
	                "c.Card_type, c.Card_Number, c.CVV, c.Expiry_date " +
	                "FROM users u " +
	                "LEFT JOIN accounts a ON u.user_id = a.user_id " +  
	                "LEFT JOIN paytmlink p ON u.user_id = p.user_id " +
	                "LEFT JOIN cards c ON u.user_id = c.user_id " +
	                "WHERE u.user_id = ?"; // Filter by user_id from users table
     
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setInt(1, userId);
	        ResultSet rs = ps.executeQuery();
	        System.out.println("Detailed Account Information:");
	        if (rs.next()) {
	        	//Account Details
	        	System.out.println("User ID: " + rs.getInt("user_id"));
	            System.out.println("Account ID: " + rs.getInt("Account_id"));	            
	            System.out.println("Account Type: " + rs.getString("Account_type"));
	            System.out.println("IFSC Code: " + rs.getString("IFSC_code"));
	            System.out.println("Branch Name: " + rs.getString("Branch_Name"));
	            System.out.println("Balance: " + rs.getBigDecimal("Balance"));
	            System.out.println("Internet Banking: " + rs.getString("Internet_Banking"));
	            System.out.println("Pin: " + rs.getString("Pin"));
	            System.out.println("Account Created At: " + rs.getTimestamp("Created_at"));

	            // User Details
	            System.out.println("User Name: " + rs.getString("Name"));
	            System.out.println("Contact Number: " + rs.getString("Contact_number"));
	            System.out.println("Email ID: " + rs.getString("EmailId"));
	            System.out.println("Gender: " + rs.getString("Gender"));
	            System.out.println("Age: " + rs.getInt("Age"));
	            System.out.println("Address: " + rs.getString("Address"));
	            System.out.println("Aadhar Card No: " + rs.getString("AadharCard_no"));
	            System.out.println("PAN Card No: " + rs.getString("PanCard_no"));

	            // Paytm Link Details (if applicable)
	            String linkedValue = rs.getString("Linked_value");
	            if (linkedValue != null) {
	                System.out.println("Paytm Linked Value: " + linkedValue);
	                System.out.println("Linked Via: " + rs.getString("linked_via"));
	                System.out.println("Linked Date: " + rs.getTimestamp("Linked_date"));
	            } else {
	                System.out.println("Paytm Link: Not Linked");
	            }

	            // Card Details (if applicable)
	            String cardNumber = rs.getString("Card_Number");
	            if (cardNumber != null) {
	                System.out.println("Card Type: " + rs.getString("Card_type"));
	                System.out.println("Card Number: " + cardNumber);
	                System.out.println("CVV: " + rs.getString("CVV"));
	                System.out.println("Expiry Date: " + rs.getDate("Expiry_date"));
	            } else {
	                System.out.println("Card: No Card Linked");
	            }

	            System.out.println("-------------------------------------------------");
	        }
	 }
}

class Cards{
	
	 public String generateCardNumber() {
	        StringBuilder cardNumber = new StringBuilder();
	        for (int i = 0; i < 15; i++) {
	            cardNumber.append((ThreadLocalRandom.current().nextInt(0, 10)));
	        }
	        return cardNumber.toString();
	    }
	 
	 public String generateCVV() {
	        StringBuilder cvv = new StringBuilder();
	        for (int i = 0; i < 3; i++) {
	            cvv.append(ThreadLocalRandom.current().nextInt(0, 10));
	        }
	        return cvv.toString();
	    }
	 
	 public Date generateExpiryDate() {
	        Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.YEAR, 3);
	        return new Date(cal.getTimeInMillis());
	    }
	 
	 public int getUserIdFromAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
	        String query = "SELECT user_id FROM Users WHERE Account_number = ? OR CAccount_number = ?";
	        
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	        PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setString(2, accountNumber);
            
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                
            	return rs.getInt("user_id");
            	}
			return -1;
	   	}
	 
	 boolean hasExistingCard(int userId, String cardType) throws SQLException, ClassNotFoundException {
	        String query = "SELECT COUNT(*) AS card_count FROM cards WHERE user_id = ? AND card_type = ?";

	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	        PreparedStatement preparedStatement = conn.prepareStatement(query);
	        preparedStatement.setInt(1, userId);
	        preparedStatement.setString(2, cardType);

	        ResultSet rs = preparedStatement.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("card_count") > 0;
	        }
	        return false;
	    }
	 
	 void saveCardToDatabase(String cardType, String cardNumber, String cvv, Date expiryDate, String accountNumber) throws ClassNotFoundException, SQLException {
	        
	            int userId = getUserIdFromAccountNumber(accountNumber);
	            if (userId == -1) {
	                System.out.println("Invalid account number.");
	                return;
	            }
	            
	            if ((cardType.equalsIgnoreCase("debit") || cardType.equalsIgnoreCase("credit")) && hasExistingCard(userId, cardType)) {
	                System.out.println("A debit card already exists for this user. Cannot apply for another.");
	                return;
	            }
	            
	            String sql = "INSERT INTO cards (user_id, card_type, card_number, cvv, expiry_date) VALUES (?, ?, ?, ?, ?)";
	            
	            	Class.forName("com.mysql.cj.jdbc.Driver");
	            	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	                PreparedStatement preparedStatement = conn.prepareStatement(sql);
	                preparedStatement.setInt(1, userId);
	                preparedStatement.setString(2, cardType);
	                preparedStatement.setString(3, cardNumber);
	                preparedStatement.setString(4, cvv);
	                preparedStatement.setDate(5, new java.sql.Date(expiryDate.getTime()));

	                int row = preparedStatement.executeUpdate();
	                if (row > 0) {
	                    System.out.println("Card details successfully saved to the database.");
	                } else {
	                    System.out.println("Failed to save card details to the database.");
	                }
	            
	    }
	 
	 public void applyForDebitCard(String accountNumber) throws ClassNotFoundException, SQLException {
	        String cardType = "debit";
	        String cardNumber = generateCardNumber();
	        String cvv = generateCVV();
	        Date expiryDate = generateExpiryDate();

	        System.out.println("Card Type: " + cardType);
	        System.out.println("Card Number: " + cardNumber);
	        System.out.println("CVV: " + cvv);
	        System.out.println("Expiry Date: " + expiryDate);

	        saveCardToDatabase(cardType, cardNumber, cvv, expiryDate, accountNumber);
	    }
	 
	 public void applyForCreditCard(String accountNumber) throws ClassNotFoundException, SQLException {
	        String cardType = "credit";
	        String cardNumber = generateCardNumber();
	        String cvv = generateCVV();
	        Date expiryDate = generateExpiryDate();

	        System.out.println("Card Type: " + cardType);
	        System.out.println("Card Number: " + cardNumber);
	        System.out.println("CVV: " + cvv);
	        System.out.println("Expiry Date: " + expiryDate);

	        saveCardToDatabase(cardType, cardNumber, cvv, expiryDate, accountNumber);
	    }
}

class Transactions{
	
	boolean verifyPin(String accountNumber, String pin) throws SQLException, ClassNotFoundException {
		int user_id = getUserIdFromAccountNumber(accountNumber);
		String verifyPinQuery = "SELECT Pin FROM Accounts WHERE user_id = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        PreparedStatement verifyStmt = conn.prepareStatement(verifyPinQuery);
            verifyStmt.setInt(1, user_id);
            ResultSet rs = verifyStmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Pin").equals(pin);
            }
        return false;
    }
	
	public int getUserIdFromAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
		String query = "SELECT user_id FROM Users WHERE Account_number = ? OR CAccount_number = ?";
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, accountNumber);
        preparedStatement.setString(2, accountNumber);
        
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            
        	return rs.getInt("user_id");
        	}
		return -1;
   	}
	
	public void checkBalance(String accountNumber, String pin) throws SQLException, ClassNotFoundException {
	    // First, get user_id from accountNumber
	    int userId = getUserIdFromAccountNumber(accountNumber);
	    if (userId == -1) {
	        System.out.println("Account not found.");
	        return;
	    }
	    
	    String getAccountIdQuery = "SELECT Account_id FROM Accounts WHERE user_id = ?";
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	    PreparedStatement getAccountIdStmt = conn.prepareStatement(getAccountIdQuery);
	    getAccountIdStmt.setInt(1, userId);
	    ResultSet rs = getAccountIdStmt.executeQuery();

	    if (rs.next()) {
	        int accountId = rs.getInt("Account_id");
	        String checkBalanceQuery = "SELECT Balance FROM Accounts WHERE Account_id = ?";
	        PreparedStatement checkStmt = conn.prepareStatement(checkBalanceQuery);
	        checkStmt.setInt(1, accountId);
	        ResultSet balanceRs = checkStmt.executeQuery();
	        if (balanceRs.next()) {
	            double balance = balanceRs.getDouble("Balance");
	            System.out.println("Current balance: " + balance);

	            // Record the transaction for balance check
	            String insertTransactionQuery = "INSERT INTO Transactions (account_id, transaction_type, amount) VALUES (?, 'check_balance', NULL)";
	            PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery);
	            insertStmt.setInt(1, accountId);
	            insertStmt.executeUpdate();
	        } else {
	            System.out.println("Balance could not be retrieved.");
	        }
	    } else {
	        System.out.println("Account not found.");
	    }
	}
	
	public void deposit(String accountNumber, double amount, String pin) throws SQLException, ClassNotFoundException {
		
		int user_id = getUserIdFromAccountNumber(accountNumber);
		if (user_id == -1) {
	        System.out.println("Account not found.");
	        return;
	    }
		String getAccountIdQuery = "SELECT Account_id FROM Accounts WHERE user_id = ?";
		String updateBalanceQuery = "UPDATE Accounts SET Balance = Balance + ? WHERE Account_id = ?";
        String insertTransactionQuery = "INSERT INTO Transactions (account_id, transaction_type, amount) VALUES (?, 'deposite', ?)";
        
        if (!verifyPin(accountNumber, pin)) {
        	System.out.println("Invalid PIN. Transaction aborted.");
        	return;
        }
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root","MYSQL_PASSWORD");
        PreparedStatement acid = conn.prepareStatement(getAccountIdQuery);
        PreparedStatement updateStmt = conn.prepareStatement(updateBalanceQuery);
        PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery);
        
        acid.setInt(1, user_id);
        ResultSet rs = acid.executeQuery();

        int account_id = -1;
        if (rs.next()) {
            account_id = rs.getInt("Account_id");
        }

        if (account_id == -1) {
            System.out.println("Account ID not found.");
            return;
        }
        
        updateStmt.setDouble(1, amount);
        updateStmt.setInt(2, account_id);
        updateStmt.executeUpdate();
        
        insertStmt.setInt(1, account_id);
        insertStmt.setDouble(2, amount);
        insertStmt.executeUpdate();
        
        System.out.println("Deposit successful.");
	}
		
	public void withdraw(String accountNumber, double amount, String pin) throws SQLException, ClassNotFoundException {
	    int user_id = getUserIdFromAccountNumber(accountNumber);
	    if (user_id == -1) {
	        System.out.println("Account not found.");
	        return;
	    }

	    String getAccountIdQuery = "SELECT Account_id FROM Accounts WHERE user_id = ?";
	    String checkBalanceQuery = "SELECT Balance FROM Accounts WHERE Account_id = ?";
	    String updateBalanceQuery = "UPDATE Accounts SET Balance = Balance - ? WHERE Account_id = ?";
	    String insertTransactionQuery = "INSERT INTO Transactions (account_id, transaction_type, amount) VALUES (?, 'withdraw', ?)";

	    if (!verifyPin(accountNumber, pin)) {
	        System.out.println("Invalid PIN. Transaction aborted.");
	        return;
	    }

	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD"); 
	    PreparedStatement acid = conn.prepareStatement(getAccountIdQuery);
	    acid.setInt(1, user_id);
	    ResultSet rs = acid.executeQuery();

	    int account_id = -1;
	    if (rs.next()) {
	        account_id = rs.getInt("Account_id");
	    }

	    if (account_id == -1) {
	        System.out.println("Account ID not found.");
	        return;
	    }

	    // Check if the account has sufficient balance
	    PreparedStatement checkStmt = conn.prepareStatement(checkBalanceQuery);
	    checkStmt.setInt(1, account_id);
	    rs = checkStmt.executeQuery();
	    if (rs.next() && rs.getDouble("Balance") >= amount) {
	        // Update balance
	        PreparedStatement updateStmt = conn.prepareStatement(updateBalanceQuery);
	        updateStmt.setDouble(1, amount);
	        updateStmt.setInt(2, account_id);  
	        updateStmt.executeUpdate();

	        // Record the transaction
	        PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery);
	        insertStmt.setInt(1, account_id);  
	        insertStmt.setDouble(2, amount);
	        insertStmt.executeUpdate();

	        System.out.println("Withdrawal successful.");
	    } else {
	        System.out.println("Insufficient funds. Transaction aborted.");
	    }
	}
	

	
	public void changePin(String accountNumber, String oldPin, String newPin) throws SQLException, ClassNotFoundException {
	    int user_id = getUserIdFromAccountNumber(accountNumber);
	    if (user_id == -1) {
	        System.out.println("Account not found.");
	        return;
	    }
	    
	    String getAccountIdQuery = "SELECT Account_id FROM Accounts WHERE user_id = ?";
	    String updatePinQuery = "UPDATE Accounts SET Pin = ? WHERE Account_id = ? AND Pin = ?";
	    String insertTransactionQuery = "INSERT INTO Transactions (account_id, transaction_type, amount) VALUES ((SELECT Account_id FROM Accounts WHERE user_id = ?), 'change_pin', NULL)";
	    
	    if (!verifyPin(accountNumber, oldPin)) {
	        System.out.println("Invalid PIN. Change aborted.");
	        return;
	    }
	    
	    

	    // Using try-with-resources to ensure resources are closed automatically
	    try {
	    	 Class.forName("com.mysql.cj.jdbc.Driver");
	    	 Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	         PreparedStatement acid = conn.prepareStatement(getAccountIdQuery);
	         PreparedStatement updatePinStmt = conn.prepareStatement(updatePinQuery);
	         PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery);

	        // Retrieve the account ID
	        acid.setInt(1, user_id);
	        ResultSet rs = acid.executeQuery();
	        int account_id = -1;
	        if (rs.next()) {
	            account_id = rs.getInt("Account_id");
	        }

	        if (account_id == -1) {
	            System.out.println("Account ID not found.");
	            return;
	        }

	        // Update the PIN if the old PIN is valid
	        updatePinStmt.setString(1, newPin);
	        updatePinStmt.setInt(2, account_id);
	        updatePinStmt.setString(3, oldPin);
	        int row = updatePinStmt.executeUpdate();
	        
	        if (row > 0) {
	            // Insert the transaction record if the PIN was updated successfully
	            insertStmt.setInt(1, user_id);
	            insertStmt.executeUpdate();
	            System.out.println("PIN changed successfully.");
	        } else {
	            System.out.println("PIN change failed. Invalid old PIN.");
	        }
	        
	    } catch (SQLException e) {
	        System.out.println("Error during PIN change: " + e.getMessage());
	    }
	}
}

class TransactionHistory {

    public int getUserIdFromAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
        String query = "SELECT user_id FROM Users WHERE Account_number = ? OR CAccount_number = ?";
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, accountNumber);
        preparedStatement.setString(2, accountNumber);
        
        ResultSet rs = preparedStatement.executeQuery();
        int userId = -1;
        if (rs.next()) {
            userId = rs.getInt("user_id");
        }
        
        rs.close();
        preparedStatement.close();
        conn.close();
        
        return userId;
    }

    public void transferTransactionToHistory(String accountNumber) throws ClassNotFoundException, SQLException {
        int userId = getUserIdFromAccountNumber(accountNumber);
        if (userId == -1) {
            System.out.println("Account not found.");
            return;
        }

        String getAccountIdQuery = "SELECT Account_id FROM Accounts WHERE user_id = ?";
        String selectSql = "SELECT transaction_id, account_id, transaction_type, amount, transaction_date FROM transactions WHERE account_id = ?";
        String insertSql = "INSERT INTO Transactionhistory (transaction_id, user_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?, ?)";
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        
        
        PreparedStatement acid = conn.prepareStatement(getAccountIdQuery);
        acid.setInt(1, userId);
        ResultSet rs = acid.executeQuery();
        
        int account_id = -1;
        if (rs.next()) {
            account_id = rs.getInt("Account_id");
        }

        if (account_id == -1) {
            System.out.println("Account ID not found.");
            return;
        }
        
        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
        selectStmt.setInt(1, account_id); // Select transactions for this account
        rs = selectStmt.executeQuery();

        PreparedStatement insertStmt = conn.prepareStatement(insertSql);

        // Loop for all transactions and insert to transaction History
        while (rs.next()) {
            int transactionId = rs.getInt("transaction_id");
            String transactionType = rs.getString("transaction_type");
            double amount = rs.getDouble("amount");
            Timestamp transactionDate = rs.getTimestamp("transaction_date");

            // Insert data
            insertStmt.setInt(1, transactionId); 
            insertStmt.setInt(2, userId);         
            insertStmt.setString(3, transactionType); 
            insertStmt.setDouble(4, amount);      
            insertStmt.setTimestamp(5, transactionDate);
            insertStmt.executeUpdate(); 
        }

        rs.close();
        acid.close();
        selectStmt.close();
        insertStmt.close();
        conn.close();

        System.out.println("Transactions have been successfully transferred to the Transactionhistory table.");
    }

    public void showTransactionHistory(String accountNumber) throws ClassNotFoundException, SQLException {
        int userId = getUserIdFromAccountNumber(accountNumber);
        if (userId == -1) {
            System.out.println("Account not found.");
            return;
        }

        String sql = "SELECT history_id, transaction_id, user_id, transaction_type, amount, transaction_date FROM Transactionhistory WHERE user_id = ?";
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            do {
                int historyId = rs.getInt("history_id");
                int transactionId = rs.getInt("transaction_id");
                String transactionType = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                Timestamp transactionDate = rs.getTimestamp("transaction_date");

                System.out.println("History ID: " + historyId + ", Transaction ID: " + transactionId + ", User ID: " + userId + ", Type: " + transactionType + ", Amount: " + amount + ", Date: " + transactionDate);
            } while (rs.next());
        } else {
            System.out.println("No transaction history found for this account.");
        }
    }
}

class Admin{
	public boolean insertAdmin(String adminName, String adminEmail, String Apassword) throws SQLException, ClassNotFoundException {
		
		int row;
		String query = "INSERT INTO Admin (Admin_name, Admin_Email, APassword) VALUES (?, ?, ?)";
		
		Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, adminName);
        ps.setString(2, adminEmail);
        ps.setString(3, Apassword);
        
        row = ps.executeUpdate();
        if (row > 0) {
            
            return true;
        } 
        else {
            return false;
        }
	}
	
	public boolean checkAdminLoginInDB(String adminEmail,String Apassword) throws ClassNotFoundException, SQLException {
		String query ="Select * from Admin where Admin_Email =? and APassword = ?";
		
		Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        PreparedStatement ps = conn.prepareStatement(query);
       
        ps.setString(1, adminEmail);
        ps.setString(2, Apassword);
        
        ResultSet rs = ps.executeQuery();  // Use executeQuery() instead of executeUpdate()

        if (rs.next()) {  
            return true;
        } 
        else {
            return false;
        }
	}
	
	public void viewAllAccountsByAdmin(String adminEmail,String Apassword) throws ClassNotFoundException, SQLException {
		if(checkAdminLoginInDB(adminEmail,Apassword)) {
			String query = "SELECT u.user_id, u.Name, u.Contact_number, u.EmailId, u.Gender, u.Age, u.Address, u.AadharCard_no, u.PanCard_no, u.Account_number, u.CAccount_number, "
	                + " a.Account_id, a.Account_type, a.IFSC_code, a.Branch_Name, a.Balance, a.Internet_Banking, a.Pin, a.Created_at, "
	                + " p.Linked_value, p.linked_via, p.Linked_date, "
	                + " c.Card_type, c.Card_Number, c.CVV, c.Expiry_date "
	                + " FROM users u "
	                + " INNER JOIN accounts a ON u.user_id = a.user_id "
	                + " INNER JOIN paytmlink p ON u.user_id = p.user_id "
	                + " INNER JOIN cards c ON u.user_id = c.user_id";


			Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	        Statement ps = conn.createStatement();
	        ResultSet rs = ps.executeQuery(query);
	        System.out.println("Detailed Account Information:");
	        while (rs.next()) {
	        	// User Details
	            System.out.println("User ID: " + rs.getInt("user_id"));
	            System.out.println("User Name: " + rs.getString("Name"));
	            System.out.println("Account Number: " + rs.getString("Account_number"));
	            System.out.println("CAccount Number: " + rs.getString("CAccount_number")); 
	            System.out.println("Contact Number: " + rs.getString("Contact_number"));
	            System.out.println("Email ID: " + rs.getString("EmailId"));
	            System.out.println("Gender: " + rs.getString("Gender"));
	            System.out.println("Age: " + rs.getInt("Age"));
	            System.out.println("Address: " + rs.getString("Address"));
	            System.out.println("Aadhar Card No: " + rs.getString("AadharCard_no"));
	            System.out.println("PAN Card No: " + rs.getString("PanCard_no"));
	            
	            // Account Details
	            System.out.println("Account Type: " + rs.getString("Account_type"));
	            System.out.println("Account ID: " + rs.getInt("Account_id"));
	            System.out.println("IFSC Code: " + rs.getString("IFSC_code"));
	            System.out.println("Branch Name: " + rs.getString("Branch_Name"));
	            System.out.println("Balance: " + rs.getBigDecimal("Balance"));
	            System.out.println("Internet Banking: " + rs.getString("Internet_Banking"));
	            System.out.println("Pin: " + rs.getString("Pin"));
	            System.out.println("Account Created At: " + rs.getTimestamp("Created_at"));

	            // PayTm Link Details (if applicable)
	            String linkedValue = rs.getString("Linked_value");
	            if (linkedValue != null) {
	                System.out.println("Paytm Linked Value: " + linkedValue);
	                System.out.println("Linked Via: " + rs.getString("linked_via"));
	                System.out.println("Linked Date: " + rs.getTimestamp("Linked_date"));
	            } else {
	                System.out.println("Paytm Link: Not Linked");
	            }

	            // Card Details (if applicable)
	            String cardNumber = rs.getString("Card_Number");
	            if (cardNumber != null) {
	                System.out.println("Card Type: " + rs.getString("Card_type"));
	                System.out.println("Card Number: " + cardNumber);
	                System.out.println("CVV: " + rs.getString("CVV"));
	                System.out.println("Expiry Date: " + rs.getDate("Expiry_date"));
	            } else {
	                System.out.println("Card: No Card Linked");
	            }

	            System.out.println("-------------------------------------------------");
	        }
	    } else {
	        System.out.println("Access Denied: You must be logged in as an admin.");
	    }
	}
}

class Paytmlink {
	
	public int getUserIdFromAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
        String query = "SELECT user_id FROM Users WHERE Account_number = ? OR CAccount_number = ?";
        
       
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, accountNumber);
        preparedStatement.setString(2, accountNumber);
        
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            
        	return rs.getInt("user_id");
        	}
		return -1;
   	}
	
	public void enableInternetBanking(String accountNumber) throws SQLException, ClassNotFoundException {
	    
		int userId = getUserIdFromAccountNumber(accountNumber);
		if (userId == -1) {
            System.out.println("Account not found.");
            return;
        }
		
	    String queryUpdate = "UPDATE Accounts SET Internet_Banking = 'enabled' WHERE user_id = ?";
	    
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM", "root", "MYSQL_PASSWORD");
	    PreparedStatement psUpdate = conn.prepareStatement(queryUpdate);
	    psUpdate.setInt(1, userId);

	    int rows = psUpdate.executeUpdate();

	    if (rows > 0) {
	            System.out.println("Internet banking has been enabled for account number: " + accountNumber);
	        }
	        else {
	            System.out.println("Failed to enable internet banking for account number: " + accountNumber);
	        }
	}
	
	public boolean isInternetBankingEnabled(String accountNumber) throws SQLException, ClassNotFoundException {
		
		
		String query = "SELECT Internet_Banking FROM Accounts WHERE user_id = ?";
		int userId = getUserIdFromAccountNumber(accountNumber);
		if (userId == -1) {
            System.out.println("Account not found.");
            return false;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM","root","MYSQL_PASSWORD");
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
            
        if (rs.next()) {
                boolean flag = "enabled".equalsIgnoreCase(rs.getString("Internet_Banking")); 
            	return flag;
            }
        return false;
    }
	
	public boolean linkPaytmAccountByMobile(String accountNumber, int userId, String linkMethod, String linkValue) throws ClassNotFoundException, SQLException {
		userId = getUserIdFromAccountNumber(accountNumber);
        
		String insertQuery = "INSERT INTO Paytmlink (user_id, linked_via, Linked_value, Linked_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        	
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM","root","MYSQL_PASSWORD");
            PreparedStatement ps = conn.prepareStatement(insertQuery);

            ps.setInt(1, userId);
            ps.setString(2, "mobile");
            ps.setString(3, linkValue);

            int row = ps.executeUpdate();
            if(row>0) {
            System.out.println("Stored sucessfully.");
            return true;    
            }
        
        return false;
    }

	public boolean linkPaytmAccountByAadhar(String accountNumber, int userId, String linkMethod, String linkValue) throws ClassNotFoundException, SQLException {
		userId = getUserIdFromAccountNumber(accountNumber);
        
		String insertQuery = "INSERT INTO Paytmlink (user_id, linked_via, Linked_value, Linked_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        	
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM","root","MYSQL_PASSWORD");
            PreparedStatement ps = conn.prepareStatement(insertQuery);

            ps.setInt(1, userId);
            ps.setString(2, "aadhar");
            ps.setString(3, linkValue);

            int row = ps.executeUpdate();
            if(row>0) {
            	System.out.println("Stored sucessfully.");
            return true; 
            }
        
        return false;
    }
	
	public boolean linkPaytmAccountByDC(String accountNumber, int userId, String linkMethod, String linkValue) throws ClassNotFoundException, SQLException {
		userId = getUserIdFromAccountNumber(accountNumber);
        
		String insertQuery = "INSERT INTO Paytmlink (user_id, linked_via, Linked_value, Linked_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        	
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BANK_MANAGEMENT_SYSTEM","root","MYSQL_PASSWORD");
            PreparedStatement ps = conn.prepareStatement(insertQuery);

            ps.setInt(1, userId);
            ps.setString(2, "debit_card");
            ps.setString(3, linkValue);

            int row = ps.executeUpdate();
            if(row>0) {
            	System.out.println("Stored sucessfully.");
            return true; 
            }
        
        return false;
    }
	
}

class BankManagementSystemMain {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);    	
        Users u = new Users();
        Accounts a = new Accounts();
        Cards cd = new Cards();
        Admin ad = new Admin();
        Transactions t = new Transactions();
        TransactionHistory th = new TransactionHistory();
        Paytmlink p = new Paytmlink();
        int c;
        boolean flag;
        String accountNumber;
        
        do {
        System.out.println("\n 1.Open New Account\n 2.Update Account\n 3.Delete Account\n 4.Apply for Cards(dc, cc)\n 5.Transaction\n 6.Check Transaction History\n 7.Link Account with Paytm(mobile number or debit card or Aadhar no)\n 8.View My Account\n 9.Admin Login\n 10.View All Account\n 11.Transfer Transactions to Transaction History(By Admin)\n 12.Exit");
        System.out.println("ENTER THE CHOICE YOU WANT: ");
        c = sc.nextInt();
        
        switch (c) {
            case 1:
            	//Implement to Open New Account
            	
                int Age,c3,c2, c1;
                String Name, Contact_number, EmailId, Password, cpassword, Gender, Address, UPI_id, AadharCard_no, PanCard_no, mpin;
                Timestamp createdAt = new Timestamp(System.currentTimeMillis());
                
                sc.nextLine();
                System.out.println("Enter your name: ");
                Name = sc.nextLine();

                System.out.println("Enter Your EmailId: ");
                EmailId = sc.next();

                System.out.println("Enter your password: ");
                Password = sc.next();

                System.out.println("Enter your confirm password: ");
                cpassword = sc.next();
                if (Password.equals(cpassword)) {
                    System.out.println("Password is Set successfully.");
                } else {
                    System.out.println("VERIFY UR SELF AGAIN.");
                    return;
                }

                System.out.println("Enter your Address: ");
                Address = sc.next();

                System.out.println("Enter your UPI ID: ");
                UPI_id = sc.next();

                System.out.print("Enter your gender 'Male write m' 'Female write f' : ");
                Gender = sc.next();
                if("m".equals(Gender)) {
        			System.out.println("MALE\n");
        		}
        		else if("f".equals(Gender)){
        			System.out.println("FEMALE\n");
        		}
        		else {
        			System.out.println("ERROR: GIVE VALID INPUT.");
        			return;
        		}

                System.out.print("Enter your contact number (10 digit numbers only): ");
                Contact_number = sc.next();
                if (Contact_number.length() != 10) {
                    System.out.println("Error: Enter a 10-digit contact number.");
                    return;
                }

                System.out.print("Enter your MPIN Number (6 digit numbers MPIN number is allowed): ");
                mpin = sc.next();
                if (mpin.length() != 6) {
                    System.out.println("Error: Enter a 6-digit MPIN.");
                    return;
                }

                System.out.print("Enter your Aadhar Card Number (12 digit numbers Aadhar Card number is allowed): ");
                AadharCard_no = sc.next();
                if (AadharCard_no.length() != 12) {
                    System.out.println("Error: Enter a 12-digit Aadhar Card Number.");
                    return;
                }

                System.out.print("Enter a valid 12-character alphanumeric PAN Card Number: ");
                PanCard_no = sc.next();
                if ((PanCard_no.length() == 12 && PanCard_no.matches("^[a-zA-Z0-9]{12}$"))) {
                    System.out.println("VALID NUMBER.");
                } else {
                    System.out.println("ERROR.");
                    return;
                }
                
                System.out.println("Enter your age: ");
                Age = sc.nextInt();
                if (Age >= 18) {
                    System.out.println("You are eligible to open two types of accounts: ");
                    System.out.println("1. Savings 2. Current");
                    c1 = sc.nextInt();
                    switch (c1) {
                        case 1:
                            u.newAccountForSavings(Name, Contact_number, EmailId, Password, Gender, Age, Address, UPI_id, mpin, createdAt, AadharCard_no, PanCard_no);
                            break;
                        case 2:
                            u.newAccountForCurrent(Name, Contact_number, EmailId, Password, Gender, Age, Address, UPI_id, mpin, createdAt, AadharCard_no, PanCard_no);
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                } else {
                    System.out.println("You are not eligible. First have a PAN card and then come again.");
                }
                break;

            case 2:    	        
    	        //Implement Update Account  
            	String newEmail, newPassword, newContactNumber, newAddress;
            	
            	System.out.println("Enter Account Number:");
                accountNumber = sc.next();                
            	System.out.println("Enter new Email:");
                newEmail = sc.next();
                System.out.println("Enter new Password:");
                newPassword = sc.next();
            	System.out.println("Enter new Address: ");
            	newAddress = sc.next();
            	System.out.println("Enter new Contact Number: ");
            	newContactNumber = sc.next();
            	if (newContactNumber.length() != 10) {
                    System.out.println("Error: Enter a 10-digit contact number.");
                    return;
                }
            	String[] BranchName = {"Thane", "Dombivli", "Jalgaon"};
                String[] IFSCCode = {"ABCD00001", "ABCD00002", "ABCD00003"};
                System.out.println("Select a branch (1. Thane, 2. Dombivli, 3. Jalgaon): ");
                int branchChoice = sc.nextInt();
                sc.nextLine();  // Consume newline

                String selectedBranch = BranchName[branchChoice - 1];
                String selectedIFSCCode = IFSCCode[branchChoice - 1];
                
                try {
                    flag = a.updateUserDetails(accountNumber,selectedIFSCCode, newEmail, newPassword, newContactNumber, newAddress, selectedBranch);
                    if (flag) {
                        System.out.println("Update operation successful.");
                    } else {
                        System.out.println("Update operation failed.");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    sc.close();
                } //finally {}
                break;
                
            case 3:
                // Implement Delete Account
            	System.out.println("Enter Account Number (for savings or current):");
                String AccountNumber = sc.next();
            	a.deleteAccount(AccountNumber);
                break;

            case 4:
                // Implement Apply for Cards
            	System.out.println("Enter Account Number (for savings or current):");
                accountNumber = sc.next();
            	System.out.println("Apply for carrds : 1.Debit Cards 2.Credit Cards");
            	c2 = sc.nextInt();
			
			System.out.println("ENTER USER ID SO THAT ");
			switch(c2)
            		{
            			case 1:
            				cd.applyForDebitCard(accountNumber);
            				break;
            			case 2:
            				cd.applyForCreditCard(accountNumber);
            				break;
            			default:
            				System.out.println("INVALID CHOICE.");
            				break;
            		}
                break;
              
            case 5:
                // Implement Transaction
            	double amount;
            	String pin;
           	
            	System.out.println(" 1.Check Balance\n 2.Deposit Money\n 3.Withdraw Money\n 4.Change Pin\n\nENTER YOUR CHOICE:");
            	c3=sc.nextInt();
            	
            	switch(c3) {
            	
            	case 1:
            		System.out.print("Enter your account number: ");
                    accountNumber = sc.next();
                    
                    System.out.print("Enter your PIN: ");
                    pin = sc.next();
        			
        			t.checkBalance(accountNumber, pin);
             		break;
             		
            	case 2:
          			System.out.println("Enter the Account Number: ");
        			accountNumber = sc.next();
        			
        			System.out.println("Enter the Pin: ");
        			pin = sc.next();
        			
        			System.out.println("Enter the Depositing amount: ");
        			amount = sc.nextDouble();
        			
        			t.deposit(accountNumber, amount, pin);
            		break;
            		
            	case 3:
            		System.out.println("Enter the Account Number: ");
        			accountNumber = sc.next();
        			
        			System.out.println("Enter the Pin: ");
        			pin = sc.next();
        			
        			System.out.println("Enter the Withdrawing amount: ");
        			amount = sc.nextDouble();
        			
        			t.withdraw(accountNumber, amount, pin);
            		break;
            			
            	case 4:
            		System.out.println("Enter the Account Number: ");
        			accountNumber = sc.next();
        			
            		System.out.print("Enter current PIN: ");
                    pin = sc.next();
                    
            		System.out.print("Enter new PIN: ");
                    String newPin = sc.next();
                    
                    t.changePin(accountNumber, pin, newPin);
            		break;
            		            		
            	default:
            		System.out.println("Invalid Choice.");
            		break;
            	}
			break;

            case 6:
                // Implement Check Transaction History
            	System.out.println("Enter the Account Number: ");
    			accountNumber = sc.next();
    			
    			th.showTransactionHistory(accountNumber);
            	break;

            case 7:
                // Implement Link Account with Online Banking (PayTM)
            	String linkMethod, linkValue;
            	System.out.println("::MAKE INTERNET BANKING AVAILABLE::");
            	System.out.print("Enter Account Number: ");
            	accountNumber = sc.next();
            	p.enableInternetBanking(accountNumber);
            	
            	int userId = p.getUserIdFromAccountNumber(accountNumber);
                if (userId == -1) {
                    System.out.println("Invalid account number or user not found.");
                    return;
                }
                
                if (p.isInternetBankingEnabled(accountNumber)) {
                    System.out.println("link method via mobile (press m), aadhar (press a), debit_card (press d):");
                    linkMethod = sc.next();
                    if("m".equals(linkMethod)) {
                    	System.out.print("Enter Linked Value (e.g., mobile number): ");
                        linkValue = sc.next();
                        if (linkValue.length() != 10) {
                            System.out.println("Error: Enter a 10-digit contact number.");
                            return;
                        }
                    	p.linkPaytmAccountByMobile(accountNumber, userId, linkMethod, linkValue);
                    }
                    else if("a".equals(linkMethod)){
                    	System.out.print("Enter Linked Value (e.g., Aadhar number): ");
                        linkValue = sc.next();
                        if (linkValue.length() != 12) {
                            System.out.println("Error: Enter a 10-digit Aadhar number.");
                            return;
                        }
                        p.linkPaytmAccountByAadhar(accountNumber, userId, linkMethod, linkValue);
                    }
                    else if("d".equals(linkMethod)) {
                    	System.out.print("Enter Linked Value (e.g., Debit Card number): ");
                        linkValue = sc.next();
                        if (linkValue.length() != 15) {
                            System.out.println("Error: Enter a 15-digit Debit Card number.");
                            return;
                        }
                        p.linkPaytmAccountByDC(accountNumber, userId, linkMethod, linkValue);
                    }
                    else {
                    	System.out.println("choose the correct option.");
                    }
                    
                } else {
                    System.out.println("Internet banking is not enabled for this account.");
                }
                break;
                
            case 8:
                // Implement View My Account
            	System.out.print("Enter Account Number: ");
            	accountNumber = sc.next();
            	a.viewMyAccount(accountNumber);
                break;
                
            case 9:
                //Implement Admit Login by insert query            	
            	System.out.println("Enter Admin Name:");
                String adminName = sc.next();
                
                System.out.println("Enter Admin Email:");
                String adminEmail = sc.next();
                
                System.out.println("Enter Admin Password:");
                String password = sc.next();
                
                try {
                	flag = ad.insertAdmin(adminName, adminEmail, password);
                	if (flag) {
                		System.out.println("Admin record inserted successfully.");
                    } else {
                    	System.out.println("Failed to insert admin record. ERROR...");
                    }
                }catch(SQLException | ClassNotFoundException e) {
                	e.printStackTrace();
                }
                
                break;
                
            case 10:
            	//Implement View All Account
            	System.out.println("ENTER ADMIN's EMAIL-ID: ");
            	adminEmail=sc.next();
        
            	System.out.println("ENTER THE PASSWORD: ");
            	password = sc.next();
            	
            	ad.checkAdminLoginInDB(adminEmail, password);
      
            	ad.viewAllAccountsByAdmin(adminEmail, password);
            	
            	break;
            	
            case 11:
            	//Implement Transfer Transactions to Transaction History(By Admin)       
            	System.out.println("ENTER ADMIN's EMAIL-ID: ");
            	adminEmail=sc.next();
        
            	System.out.println("ENTER THE PASSWORD: ");
            	password = sc.next();
            	
            	ad.checkAdminLoginInDB(adminEmail, password);
            	
            	System.out.print("Enter Account Number: ");
            	accountNumber = sc.next();
            	th.transferTransactionToHistory(accountNumber);
            	break;
            
            case 12:
            	System.out.println("Exiting...");
                break;
                
            default:
                System.out.println("INVALID CHOICE.");
                break;
        }
        
    }while(c!=12);
    }
}