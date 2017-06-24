package utn_frba_mobile.dadm_diario_viajes.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Trip {
    private String id;
    private String name;
    private Date dateInit;
    private Date dateEnd;
    private int photo;

    public Trip() {
    }

    public Trip(String id, String name, Date dateInit, Date dateEnd, int photo) {
        this.id = id;
        this.name = name;
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Date getDateInit() { return this.dateInit; }
    public void setDateInit(Date dateInit) { this.dateInit = dateInit; }

    public Date getDateEnd() { return this.dateEnd; }
    public void setDateEnd(Date dateEnd) { this.dateInit = dateEnd; }

    public int getPhoto() { return this.photo; }
    public void setPhoto(int photo) { this.photo = photo; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("dateInit", dateInit);
        result.put("dateEnd", dateEnd);
        result.put("photo", photo);
        return result;
    }

}
