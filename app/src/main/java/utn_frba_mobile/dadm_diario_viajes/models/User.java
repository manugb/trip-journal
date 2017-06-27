package utn_frba_mobile.dadm_diario_viajes.models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by toiacabrera on 14/05/17.
 */
@IgnoreExtraProperties
public class User {

    private String id;
    private String email;
    private String name;
    private Uri photoUrl;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public static User create(FirebaseUser firebaseUser) {
        User user = new User();
        user.id = firebaseUser.getUid();
        user.email = firebaseUser.getEmail();
        user.name = firebaseUser.getDisplayName();
        user.photoUrl = firebaseUser.getPhotoUrl();
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }
}
