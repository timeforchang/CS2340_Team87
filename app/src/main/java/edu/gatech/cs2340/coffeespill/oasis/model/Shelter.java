package edu.gatech.cs2340.coffeespill.oasis.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Shelter implements Parcelable {
    private int id;
    private String name;
    private Integer capacity;
    private String restrictions;
    private double longitude;
    private double latitude;
    private String address;
    private String notes;
    private String phone;

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

    public Shelter() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public double getLongitude() {
        return longitude;
    }

    @SuppressWarnings("unused")
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @SuppressWarnings("unused")
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    @SuppressWarnings("unused")
    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    @SuppressWarnings("unused")
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone() {
        return phone;
    }

    @SuppressWarnings("unused")
    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public Shelter(int _id, String _name, int _capacity, int _longitude, int _latitude, String _address, String _notes, String _phone) {
//        id = _id;
//        name = _name;
//        capacity = _capacity;
//        longitude = _longitude;
//        latitude = _latitude;
//        address = _address;
//        notes = _notes;
//        phone = _phone;
//    }

    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        if (o instanceof Shelter) {
            Shelter ptr = (Shelter) o;
            ret = ptr.getId() == this.id;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", restrictions='" + restrictions + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", address='" + address + '\'' +
                ", notes='" + notes + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.capacity);
        dest.writeString(this.restrictions);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.address);
        dest.writeString(this.notes);
        dest.writeString(this.phone);
    }

    protected Shelter(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.capacity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.restrictions = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.address = in.readString();
        this.notes = in.readString();
        this.phone = in.readString();
    }

    public static final Creator<Shelter> CREATOR = new Creator<Shelter>() {
        @Override
        public Shelter createFromParcel(Parcel source) {
            return new Shelter(source);
        }

        @Override
        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };
}
