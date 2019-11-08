package fr.unice.polytech.tinypoly.dao;


import com.google.cloud.datastore.*;
import fr.unice.polytech.tinypoly.entities.Account;

public class DatastoreDao implements AccountDao {

    private Datastore datastore;
    private KeyFactory keyFactory;

    public DatastoreDao() {
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind("Account");
    }

    @Override
    public long createAccount(Account account) {
        IncompleteKey key = keyFactory.newKey();
        FullEntity<IncompleteKey> incAccountEntity = Entity.newBuilder(key)
                .set("email", account.getEmail())
                .set("id", account.getId())
                .build();
        Entity accountEntity = datastore.add(incAccountEntity);
        return accountEntity.getKey().getId();
    }

    @Override
    public Account readAccount(long accountId) {
        return entityToAccount(datastore.get(keyFactory.newKey(accountId)));
    }

    private Account entityToAccount(Entity entity) {
        return new Account(entity.getLong("id"), entity.getString("email"));
    }
}