package utn_frba_mobile.dadm_diario_viajes.models;


import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class Trip  implements Serializable {

    private String id;
    private String userId;
    private String name;
    private Date dateInit;
    private Date dateEnd;
    private String photoUrl;
    private double latitude;
    private double longitude;

    public Trip() {
    }

    public Trip(String id, String userId, String name, Date dateInit, Location location) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.dateInit = dateInit;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateInit() {
        return dateInit;
    }

    public void setDateInit(Date dateInit) {
        this.dateInit = dateInit;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return this.latitude; }

    public double getLongitude() { return this.longitude; }
}
