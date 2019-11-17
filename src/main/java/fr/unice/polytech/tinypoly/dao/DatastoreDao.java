package fr.unice.polytech.tinypoly.dao;


import com.google.cloud.datastore.*;
import fr.unice.polytech.tinypoly.entities.Account;
import fr.unice.polytech.tinypoly.entities.PtitU;

import java.util.ArrayList;
import java.util.List;

public class DatastoreDao implements AccountDao, PtitUDao {

    private Datastore datastore;
    private KeyFactory accountKeyFactory;
    private KeyFactory ptituKeyFactory;

    public DatastoreDao() {
        datastore = DatastoreOptions.getDefaultInstance().getService();
        accountKeyFactory = datastore.newKeyFactory().setKind("Account");
        ptituKeyFactory = datastore.newKeyFactory().setKind("PtitU");
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

    @Override
    public long createPtitU(PtitU ptitU) {
        IncompleteKey key = ptituKeyFactory.newKey();
        FullEntity<IncompleteKey> incPtituEntity = Entity.newBuilder(key)
                .set("hash", ptitU.getHash())
                .set("url", ptitU.getUrl())
                .set("ptitu", ptitU.getPtitu())
                .set("email", ptitU.getEmail())
                .build();
        Entity ptituEntity = datastore.add(incPtituEntity);
        return ptituEntity.getKey().getId();
    }

    @Override
    public PtitU readPtitU(long ptituId) {
        return entityToPtitU(datastore.get(ptituKeyFactory.newKey(ptituId)));
    }

    @Override
    public List<PtitU> listPtitUs(String startCursorString) {
        Cursor startCursor = null;
        if (startCursorString != null && !startCursorString.equals("")) {
            startCursor = Cursor.fromUrlSafe(startCursorString);
        }
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("PtitU")
                .setLimit(10)
                .setStartCursor(startCursor)
                .setOrderBy(StructuredQuery.OrderBy.asc("id"))
                .build();
        QueryResults<Entity> resultList = datastore.run(query);
        return entitiesToPtitU(resultList);
    }

    private List<PtitU> entitiesToPtitU(QueryResults<Entity> entities) {
        List<PtitU> resultPtitUs = new ArrayList<>();
        entities.forEachRemaining(entity -> resultPtitUs.add(entityToPtitU(entity)));
        return resultPtitUs;
    }

    private PtitU entityToPtitU(Entity entity) {
        return new PtitU(entity.getLong("id"),
                entity.getString("url"),
                entity.getString("ptitu"),
                entity.getString("email"));
    }

}