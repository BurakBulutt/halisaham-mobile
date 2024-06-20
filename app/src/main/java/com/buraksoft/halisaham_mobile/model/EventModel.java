package com.buraksoft.halisaham_mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class EventModel implements Parcelable {
    private String id;
    private Long expirationDate;
    private AreaModel area;
    private String cityId;
    private String districtId;
    private String streetId;
    private Integer maxPeople;
    private String title;
    private String description;
    private UserModel admin;
    private List<UserModel> users;

    public EventModel() {
    }

    protected EventModel(Parcel in) {
        id = in.readString();
        cityId = in.readString();
        districtId = in.readString();
        streetId = in.readString();
        if (in.readByte() == 0) {
            maxPeople = null;
        } else {
            maxPeople = in.readInt();
        }
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
        @Override
        public EventModel createFromParcel(Parcel in) {
            return new EventModel(in);
        }

        @Override
        public EventModel[] newArray(int size) {
            return new EventModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public AreaModel getArea() {
        return area;
    }

    public void setArea(AreaModel area) {
        this.area = area;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserModel getAdmin() {
        return admin;
    }

    public void setAdmin(UserModel admin) {
        this.admin = admin;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cityId);
        dest.writeString(districtId);
        dest.writeString(streetId);
        if (maxPeople == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxPeople);
        }
        dest.writeString(title);
        dest.writeString(description);
    }
}
