package edu.gatech.cs2340.coffeespill.oasis.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andrew_chang on 2018-02-13.
 */

public class User implements Parcelable {
    private String _id;
    private String _username;
    private String _pWord;
    private boolean _locked;
    private String _contact;
    private String _type;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_pWord() {
        return _pWord;
    }

    public void set_pWord(String _pWord) {
        this._pWord = _pWord;
    }

    public boolean is_locked() {
        return _locked;
    }

    public void set_locked(boolean _locked) {
        this._locked = _locked;
    }

    public String get_contact() {
        return _contact;
    }

    public void set_contact(String _contact) {
        this._contact = _contact;
    }

    public String get_Type() {
        return _type;
    }

    public void set_Type(String type) {
        this._type = type;
    }

    public static List<String> userTypes = Arrays.asList("User", "Employee", "Admin");

    public User(String uName, String pWord, String cont, boolean locked, String type) {
        _username = uName;
        _pWord = pWord;
        _contact = cont;
        _locked = locked;
        _type = type;
    }

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", _username='" + _username + '\'' +
                ", _pWord='" + _pWord + '\'' +
                ", _locked=" + _locked +
                ", _contact='" + _contact + '\'' +
                ", _type='" + _type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        boolean ret = false;
        if (o instanceof User) {
            User ptr = (User) o;
            ret = ptr.get_username() == this._username;
        }
        return ret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this._username);
        dest.writeString(this._pWord);
        dest.writeByte(this._locked ? (byte) 1 : (byte) 0);
        dest.writeString(this._contact);
        dest.writeString(this._type);
    }

    protected User(Parcel in) {
        this._id = in.readString();
        this._username = in.readString();
        this._pWord = in.readString();
        this._locked = in.readByte() != 0;
        this._contact = in.readString();
        this._type = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
