package utn_frba_mobile.dadm_diario_viajes.models;

import java.util.Calendar;

/**
 * Created by manu on 07/05/17.
 */

public class Note {
    private String name;
    private String location;
    private Calendar date;

    // private Trip trip;

    public Note() {}

    public Note(String name, String location, Calendar date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }

    public Calendar getDate() { return this.date; }
    public void setDate(Calendar date) { this.date = date; }

}
