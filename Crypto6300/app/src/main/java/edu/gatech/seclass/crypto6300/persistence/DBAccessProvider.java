package edu.gatech.seclass.crypto6300.persistence;

public class DBAccessProvider {

    private static DBAccess dbAccess;


    private DBAccessProvider() {

    }

    public static DBAccess getDBAccess() {
        if (dbAccess == null) {
//            dbAccess = new InMemoryDBAccess();
            dbAccess = new PersistentDBAccess();
        }

        return dbAccess;
    }

}
