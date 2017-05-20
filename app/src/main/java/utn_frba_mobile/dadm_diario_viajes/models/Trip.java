package utn_frba_mobile.dadm_diario_viajes.models;

import java.util.Date;

public class Trip {
    private String name;
    private Date dateInit;
    private Date dateEnd;
    private int photo;


    public Trip() {}

    public Trip(String name, Date dateInit, Date dateEnd, int photo) {
        this.name = name;
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        this.photo = photo;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Date getDateInit() { return this.dateInit; }
    public void setDateInit(Date dateInit) { this.dateInit = dateInit; }

    public Date getDateEnd() { return this.dateEnd; }
    public void setDateEnd(Date dateEnd) { this.dateInit = dateEnd; }

    public int getPhoto() { return this.photo; }
    public void setPhoto(int photo) { this.photo = photo; }


}
