package fr.unice.polytech.tinypoly.dao;

import fr.unice.polytech.tinypoly.entities.Account;

public interface AccountDao {

    long createAccount(Account account);

    Account readAccount(long accountId);

}
