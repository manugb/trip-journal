package utn_frba_mobile.dadm_diario_viajes.models;

import java.util.Date;

/**
 * Created by manu on 07/05/17.
 */

public class Note {
    private String name;
    private String location;
    private Date date;
    private int photo;
    private String comments;

    // private Trip trip;

    public Note() {}

    public Note(String name, String location, Date date, int photo, String comments) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.photo = photo;
        this.comments = comments;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }


    public int getPhoto() { return this.photo; }
    public void setPhoto(int photo) { this.photo = photo; }

    public String getComments() { return this.comments; }
    public void setCommets(String comments) { this.comments = comments; }

}
