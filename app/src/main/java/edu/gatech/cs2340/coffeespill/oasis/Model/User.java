package edu.gatech.cs2340.coffeespill.oasis.Model;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class User {
    private static int nextId = 0;
    private int _id;
    private String _username;
    private String _pWord;
    private boolean _locked;
    private String _contact;

    public int getId() { return _id; }

    public String getUsername() { return _username; }
    public void setUsername(String uName) { _username = uName; }

    public String getPassword() { return _pWord; }
    public void setPassword(String password) { _pWord = password; }

    public boolean getLocked() { return _locked; }
    public void setLocked(boolean lock) { _locked = lock; }

    public String getContact() { return _contact; }
    public void setContact(String cont) { _contact = cont; }

    public User(String uName, String pWord, String cont) {
        _id = User.nextId++;
        _username = cont;
        _pWord = pWord;
        _contact = cont;
        _locked = false;
    }

    @Override
    public String toString() {
        return _username + ", " + _locked + ", " + _contact;
    }
}
