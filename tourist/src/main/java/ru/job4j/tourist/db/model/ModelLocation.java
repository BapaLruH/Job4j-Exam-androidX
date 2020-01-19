package ru.job4j.tourist.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import ru.job4j.tourist.db.dbhelper.DBClass;
import ru.job4j.tourist.db.dbhelper.NotRequiredForDB;

public class ModelLocation implements DBClass, Parcelable {
    public Integer _id;
    public String title;
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String test = "test";

    public ModelLocation() {
    }

    protected ModelLocation(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readInt();
        }
        title = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    @NotRequiredForDB
    public static final Creator<ModelLocation> CREATOR = new Creator<ModelLocation>() {
        @Override
        public ModelLocation createFromParcel(Parcel in) {
            return new ModelLocation(in);
        }

        @Override
        public ModelLocation[] newArray(int size) {
            return new ModelLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(_id);
        }
        dest.writeString(title);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelLocation that = (ModelLocation) o;

        if (_id != null ? !_id.equals(that._id) : that._id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null)
            return false;
        return longitude != null ? longitude.equals(that.longitude) : that.longitude == null;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
