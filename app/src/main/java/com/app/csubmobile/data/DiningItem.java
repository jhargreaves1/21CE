package com.app.csubmobile.data;

/**
 * Created by Jonathan on 4/6/2017.
 */

public class DiningItem {
    public String buildingName;
    public double lng;
    public double lat;
    public String description;
    public int has_menu = 0;

    public DiningItem() {

    }

    public DiningItem(String buildingName, String description, double lng, double lat, int has_menu) {
        super();
        this.buildingName = buildingName;
        this.lng = lng;
        this.lat = lat;
        this.description = description;
        this.has_menu = has_menu;
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
    public String getDescription() {
        return this.description;
    }
    public int getMenubool() {
        return has_menu;
    }
    public void setName(String buildingName) {
        this.buildingName = buildingName;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public void setLat(double lat) {

        this.lat = lat;
    }    public void setDescription(String description) {

        this.description = description;
    }
    public void setMenubool(int has_menu) {
        this.has_menu = has_menu;
    }
}
