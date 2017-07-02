package utn_frba_mobile.dadm_diario_viajes.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by manu on 07/05/17.
 */
@IgnoreExtraProperties
public class Note {

    private String id;
    private String tripId;
    private String name;
    private String location;
    private Date date;
    private String comments;
    private String imageUrl;

    public Note() {
    }

    public Note(String id, String tripId, String name, String location, Date date, String comments) {
        this.id = id;
        this.tripId = tripId;
        this.name = name;
        this.location = location;
        this.date = date;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }

    public String getComments() { return this.comments; }
    public void setCommets(String comments) { this.comments = comments; }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
