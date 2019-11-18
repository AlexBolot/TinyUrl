package fr.unice.polytech.tinypoly.dao;

import fr.unice.polytech.tinypoly.entities.Account;

import java.util.List;

public interface AccountDao {

    long createAccount(Account account);

    Account readAccount(long accountId);

    List<Account> listAccounts(String startCursorString);

    boolean hasAccount(long accountId);

    boolean hasAccount(String email);

    long getLastId();
}
