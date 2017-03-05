package com.app.csubmobile.data;

/**
 * Created by Jonathan on 2/19/2017.
 */

import java.io.Serializable;

public class BuildingItem implements Serializable {
    public String buildingName;
    public double lng;
    public double lat;
    public String buildingAbbrev;


    public BuildingItem() {
    }

    public BuildingItem(String buildingName, String buildingAbbrev, double lng, double lat) {
        super();
        this.buildingName = buildingName;
        this.lng = lng;
        this.lat = lat;
        this.buildingAbbrev = buildingAbbrev;
    }

    public void setName(String buildingName) {
        this.buildingName = buildingName;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setAbbrev(String buildingAbbrev) {
        this.buildingAbbrev = buildingAbbrev;
    }

    public String getName() {
        return this.buildingName;
    }

    public double getLng() {
        return this.lng;
    }
    public double getLat() {
        return this.lat;
    }
    public String getAbbrev() {
        return this.buildingAbbrev;
    }


}
