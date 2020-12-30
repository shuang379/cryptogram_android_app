package edu.gatech.seclass.crypto6300.core;

import java.util.List;

public class Admin extends User {

    public Admin(String username, String emailAddress) {
        super(username, emailAddress);
    }

    public List<Statistics> viewCryptogramStatistics() {
        return null;
    }

    public void disableCryptogram(String title) {

    }
}
