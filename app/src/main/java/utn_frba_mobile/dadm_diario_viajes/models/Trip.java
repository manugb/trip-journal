package utn_frba_mobile.dadm_diario_viajes.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trip  implements Serializable {
    private String name;
    private Date dateInit;
    private Date dateEnd;
    private int photo;
    private ArrayList<Note> notes;


    public Trip() {}

    public Trip(String name, Date dateInit, Date dateEnd, int photo, ArrayList<Note> notes) {
        this.name = name;
        this.dateInit = dateInit;
        this.dateEnd = dateEnd;
        this.photo = photo;
        this.notes = notes;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public Date getDateInit() { return this.dateInit; }
    public void setDateInit(Date dateInit) { this.dateInit = dateInit; }

    public Date getDateEnd() { return this.dateEnd; }
    public void setDateEnd(Date dateEnd) { this.dateInit = dateEnd; }

    public int getPhoto() { return this.photo; }
    public void setPhoto(int photo) { this.photo = photo; }

    public ArrayList<Note> getNotes() { return this.notes; }
    public void setNotes(ArrayList<Note> notes) { this.notes = notes; }

    public void addNote(Note note){ this.notes.add(note);}

}
