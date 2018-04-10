package edu.gatech.cs2340.coffeespill.oasis.Model;

import java.util.List;

import edu.gatech.cs2340.coffeespill.oasis.Data.ShelterManager;
import edu.gatech.cs2340.coffeespill.oasis.Data.UserManager;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class Model {
    private static Model _instance;
    private ShelterManager sm;
    private UserManager um;
    private User user;
    private List<Shelter> shelters;

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
        shelters = sm.getAll();
        return shelters;
    }

    public User getUser() {
        user = um.getData();
        return user;
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
