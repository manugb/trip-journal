package utn_frba_mobile.dadm_diario_viajes.models;

/**
 * Created by toiacabrera on 14/05/17.
 */

public class User {

    private String name;
    private String lastName;

    public static User create(String name, String lastName) {
        User user = new User();
        user.name = name;
        user.lastName = lastName;
        return user;
    }

    public String getName() {
        return this.name;
    }
}
