package utn_frba_mobile.dadm_diario_viajes.models;

import java.util.Calendar;

public class Trip {
    private String name;
    private Calendar dateInit;
    private Calendar dateEnd;

    public Trip() {}

    public Trip(String name,Calendar dateInit, Calendar dateEnd) {
        this.name = name;
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Calendar getDateInit() { return this.dateInit; }
    public void setDateInit(Calendar dateInit) { this.dateInit = dateInit; }

    public Calendar getDateEnd() { return this.dateEnd; }
    public void setDateEnd(Calendar dateEnd) { this.dateInit = dateEnd; }
}
