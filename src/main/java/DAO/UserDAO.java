package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

/*
 * Represents a DAO for a User in the Social media app.
 * Singleton
 */
public class UserDAO {

    private static UserDAO dao = null;

    /* Column name for account id */
    private String id = "account_id";

    /* Column name for username */
    private String username = "username";

    /* Column name for password */
    private String password = "password";

    private UserDAO() {
        
    }

    public static UserDAO getInstance() {
        if (dao == null) {
            dao = new UserDAO();
        }
        return dao;
    }

    /**
     * Retrieves all Accounts on the Social Media Platform.
     * @return An ArrayList of Account objects, each one representing an account.
     */
    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Account;";
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Account account = new Account(results.getInt(id), results.getString(username),
                    results.getString(password));
                accountList.add(account);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return accountList;
    }

    /**
     * Creates (adds) a new account to the Social Media Platform. 
     * Returns an Account object representing the newly-added account having
     * a newly-generated id. 
     * 
     * This operation will be unsuccessful if:
     * (1) Account username exists
     * (2) Account password is less than 4 characters long
     * (3) Account username is empty
     * (4) An error occurs while adding the account internally
     * @param account account object representing the account to be added
     * @return the account object on success, null on failure
     */
    public Account createAccount(Account account) {

        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?,?);";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.executeUpdate();

            ResultSet results = statement.getGeneratedKeys();
            if (results.next()) {
                int acc_id = (int) results.getLong(id);
                return new Account(acc_id, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }
    
}
