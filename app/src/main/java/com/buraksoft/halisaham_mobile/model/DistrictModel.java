package com.buraksoft.halisaham_mobile.model;

import java.util.List;

public class DistrictModel {
    private String id;
    private String name;
    private String cityId;
    private List<StreetModel> streets;

    public DistrictModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<StreetModel> getStreets() {
        return streets;
    }

    public void setStreets(List<StreetModel> streets) {
        this.streets = streets;
    }
}
