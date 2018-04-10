package edu.gatech.cs2340.coffeespill.oasis.model;

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
    private boolean _checked;
    private int _checkedNum;
    private int _checkedSID;

    public boolean is_checked() {
        return _checked;
    }

    public void set_checked(boolean _checked) {
        this._checked = _checked;
    }

    public int get_checkedNum() {
        return _checkedNum;
    }

    public void set_checkedNum(int _checkedNum) {
        this._checkedNum = _checkedNum;
    }

    public int get_checkedSID() {
        return _checkedSID;
    }

    public void set_checkedSID(int _checkedSID) {
        this._checkedSID = _checkedSID;
    }

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

    public User(String _id, String _username, String _pWord, boolean _locked, String _contact, String _type, boolean _checked, int _checkedNum, int _checkedSID) {
        this._id = _id;
        this._username = _username;
        this._pWord = _pWord;
        this._locked = _locked;
        this._contact = _contact;
        this._type = _type;
        this._checked = _checked;
        this._checkedNum = _checkedNum;
        this._checkedSID = _checkedSID;
    }

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", _username='" + _username + '\'' +
                ", _pWord='" + _pWord + '\'' +
                ", _locked=" + _locked +
                ", _contact='" + _contact + '\'' +
                ", _type='" + _type + '\'' +
                ", _checked=" + _checked +
                ", _checkedNum=" + _checkedNum +
                ", _checkedSID=" + _checkedSID +
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
        dest.writeByte(this._checked ? (byte) 1 : (byte) 0);
        dest.writeInt(this._checkedNum);
        dest.writeInt(this._checkedSID);
    }

    protected User(Parcel in) {
        this._id = in.readString();
        this._username = in.readString();
        this._pWord = in.readString();
        this._locked = in.readByte() != 0;
        this._contact = in.readString();
        this._type = in.readString();
        this._checked = in.readByte() != 0;
        this._checkedNum = in.readInt();
        this._checkedSID = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
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
