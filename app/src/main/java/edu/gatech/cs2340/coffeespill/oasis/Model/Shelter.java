package edu.gatech.cs2340.coffeespill.oasis.Model;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by andrew_chang on 2018-02-25.
 */

public class Shelter {
    private int _id;
    private String _name;
    private int _capacity;
    private String _restrictions;
    private GeoPoint _coordinates;
    private String _address;
    private String _notes;
    private String _phone;

    public Shelter(int id, String name, int capacity, GeoPoint coordinates, String address, String notes, String phone) {
        _id = id;
        _name = name;
        _capacity = capacity;
        _coordinates = coordinates;
        _address = address;
        _notes = notes;
        _phone = phone;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getCapacity() {
        return _capacity;
    }

    public void setCapacity(int capacity) {
        this._capacity = capacity;
    }

    public String getRestrictions() {
        return _restrictions;
    }

    public void setRestrictions(String restrictions) {
        this._restrictions = restrictions;
    }

    public GeoPoint getCoordinates() {
        return _coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this._coordinates = coordinates;
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        this._address = address;
    }

    public String getNotes() {
        return _notes;
    }

    public void setNotes(String notes) {
        this._notes = notes;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        this._phone = phone;
    }
}
