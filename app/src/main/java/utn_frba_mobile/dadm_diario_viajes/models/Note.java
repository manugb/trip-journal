package utn_frba_mobile.dadm_diario_viajes.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manu on 07/05/17.
 */
@IgnoreExtraProperties
public class Note {
    private String id;
    private String name;
    private String location;
    private Date date;

    public Note() {
    }

    public Note(String id, String name, String location, Date date) {
        this.id = id;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("location", location);
        result.put("date", date);
        return result;
    }
}
