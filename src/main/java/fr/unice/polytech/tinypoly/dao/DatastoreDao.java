package fr.unice.polytech.tinypoly.dao;


import com.google.cloud.datastore.*;
import fr.unice.polytech.tinypoly.entities.Account;

import java.util.ArrayList;
import java.util.List;

public class DatastoreDao implements AccountDao {

    private Datastore datastore;
    private KeyFactory accountKeyFactory;

    public DatastoreDao() {
        datastore = DatastoreOptions.getDefaultInstance().getService();
        accountKeyFactory = datastore.newKeyFactory().setKind("Account");
    }

    @Override
    public long createAccount(Account account) {
        IncompleteKey key = accountKeyFactory.newKey();
        FullEntity<IncompleteKey> incAccountEntity = Entity.newBuilder(key)
                .set("email", account.getEmail())
                .set("id", account.getId())
                .build();
        Entity accountEntity = datastore.add(incAccountEntity);
        return accountEntity.getKey().getId();
    }

    @Override
    public Account readAccount(long accountId) {
        return entityToAccount(datastore.get(accountKeyFactory.newKey(accountId)));
    }

    @Override
    public List<Account> listAccounts(String startCursorString) {
        Cursor startCursor = null;
        if (startCursorString != null && !startCursorString.equals("")) {
            startCursor = Cursor.fromUrlSafe(startCursorString);
        }
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("Account")
                .setLimit(1000)
                .setStartCursor(startCursor)
                .setOrderBy(StructuredQuery.OrderBy.asc("id"))
                .build();
        QueryResults<Entity> resultList = datastore.run(query);
        return entitiesToAccount(resultList);
    }

    @Override
    public boolean hasAccount(long accountId) {
        return listAccounts(null)
                .stream()
                .anyMatch(account -> account.getId() == accountId);
    }

    private List<Account> entitiesToAccount(QueryResults<Entity> entities) {
        List<Account> resultAccounts = new ArrayList<>();
        entities.forEachRemaining(entity -> resultAccounts.add(entityToAccount(entity)));
        return resultAccounts;
    }

    private Account entityToAccount(Entity entity) {
        return new Account(entity.getLong("id"), entity.getString("email"));
    }

}