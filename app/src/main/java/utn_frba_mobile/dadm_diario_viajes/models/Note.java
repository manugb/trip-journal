package utn_frba_mobile.dadm_diario_viajes.models;

/**
 * Created by manu on 07/05/17.
 */

public class Note {
    private String name;

    // private Trip trip;

    public Note() {}

    public Note(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
}
