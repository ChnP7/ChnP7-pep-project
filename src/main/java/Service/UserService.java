package Service;

import java.util.List;

import DAO.UserDAO;
import Model.Account;

/**
 * A class that is the service layer to be able to provide information and updates to
 * Accounts on the platform, including logic to validate inputs.
 */
public class UserService {
    
    /* DAO with access to internal database operations */
    private UserDAO dao;

    public UserService() {
        dao = UserDAO.getInstance();
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
    public Account addAccount(Account account) {

        /* Ensure account does not already exist */
        List<Account> allAccounts = this.getAllAccounts();
        for (Account otherAccount : allAccounts) {
            if (otherAccount.getUsername().equals(account.getUsername())) {
                return null;
            }
        }

        /* Ensure password is at least 4 characters */
        if (account.getPassword().length() < 4) {
            return null;
        }

        /* Ensure username is not empty */
        if (account.getUsername().length() == 0) {
            return null;
        }

        return dao.createAccount(account);
    }

    /**
     * @return an ArrayList of all Accounts on the platform.
     */
    public List<Account> getAllAccounts() {
        List<Account> accounts = dao.getAllAccounts();
        return accounts;
    }
    
}
