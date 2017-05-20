package utn_frba_mobile.dadm_diario_viajes.models;

import java.util.Date;

/**
 * Created by manu on 07/05/17.
 */

public class Note {
    private String name;
    private String location;
    private Date date;

    // private Trip trip;

    public Note() {}

    public Note(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }

}
