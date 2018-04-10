package edu.gatech.cs2340.coffeespill.oasis.model;

import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.data.ShelterManager;
import edu.gatech.cs2340.coffeespill.oasis.data.UserManager;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class Model {
    private static Model _instance;
    private ShelterManager sm;
    private UserManager um;

    private Model() {
        sm = new ShelterManager();
        um = new UserManager();
    }

    public static synchronized Model getInstance() {
        if (_instance == null) {
            _instance = new Model();
        }
        return _instance;
    }

    public List<Shelter> getShelters() {
        return sm.getAll();
    }

    public User getUser() {
        return um.getData();
    }

    public void checkin(Shelter s, int checkNum) {
        sm.check(s, checkNum);
        um.check(s, checkNum);
    }

    public void checkout(Shelter s, int checkNum) {
        sm.out(s, checkNum);
        um.out(s, checkNum);
    }

    public void refresh() {
        sm.refresh();
        um.refresh();
    }
}
