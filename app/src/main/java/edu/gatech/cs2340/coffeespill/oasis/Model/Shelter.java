package edu.gatech.cs2340.coffeespill.oasis.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class Shelter implements Parcelable{
    private int id;
    private String name;

    public Shelter(int id, String name, Integer capacity, String restrictions, double longitude, double latitude, String address, String notes, String phone) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.notes = notes;
        this.phone = phone;
    }

    private Integer capacity;
    private String restrictions;
    private double longitude;
    private double latitude;
    private String address;
    private String notes;
    private String phone;

    protected Shelter(Parcel in) {
        id = in.readInt();
        name = in.readString();
        capacity = in.readInt();
        restrictions = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        address = in.readString();
        notes = in.readString();
        phone = in.readString();
    }

    public Shelter() {
    }

    public static final Creator<Shelter> CREATOR = new Creator<Shelter>() {
        @Override
        public Shelter createFromParcel(Parcel in) {
            return new Shelter(in);
        }

        @Override
        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Shelter(int _id, String _name, int _capacity, int _longitude, int _latitude, String _address, String _notes, String _phone) {
        id = _id;
        name = _name;
        capacity = _capacity;
        longitude = _longitude;
        latitude = _latitude;
        address = _address;
        notes = _notes;
        phone = _phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(capacity);
        parcel.writeString(restrictions);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(address);
        parcel.writeString(notes);
        parcel.writeString(phone);
    }
}
