package utn_frba_mobile.dadm_diario_viajes.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utn_frba_mobile.dadm_diario_viajes.R;

public class Trip {
    private String name;
    private Calendar dateInit;
    private Calendar dateEnd;
    private int photo;


    public Trip() {}

    public Trip(String name,Calendar dateInit, Calendar dateEnd, int photo) {
        this.name = name;
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        this.photo = photo;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Calendar getDateInit() { return this.dateInit; }
    public void setDateInit(Calendar dateInit) { this.dateInit = dateInit; }

    public Calendar getDateEnd() { return this.dateEnd; }
    public void setDateEnd(Calendar dateEnd) { this.dateInit = dateEnd; }

    public int getPhoto() { return this.photo; }
    public void setPhoto(int photo) { this.photo = photo; }


}
