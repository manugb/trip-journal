package utn_frba_mobile.dadm_diario_viajes.activities;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.fragments.NoteFragment;
import utn_frba_mobile.dadm_diario_viajes.fragments.NotesFragment;
import utn_frba_mobile.dadm_diario_viajes.fragments.TripFragment;
import utn_frba_mobile.dadm_diario_viajes.fragments.TripsFragment;
import utn_frba_mobile.dadm_diario_viajes.models.User;
import utn_frba_mobile.dadm_diario_viajes.notifications.NewNoteNotificationReceiver;

public class MainActivity extends AppCompatActivity {

    private User loggedUser;
    private PendingIntent pendingIntent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = TripsFragment.newInstance();
                    break;
                case R.id.navigation_add_note:
                    selectedFragment = TripFragment.newInstance();
                    Fragment currentFragment = getFragmentManager().findFragmentById(R.id.frame_layout);

                    if (currentFragment instanceof NotesFragment){
                        NoteFragment fragment = NoteFragment.newInstance();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("trip", ((NotesFragment) currentFragment).getTrip());
                        fragment.setArguments(bundle);

                        selectedFragment = fragment;
                    } else {
                        selectedFragment = TripFragment.newInstance();
                    }
                    break;
                case R.id.navigation_profile:
                    selectedFragment = TripsFragment.newInstance();
                    break;
            }

            FragmentManager fm = getFragmentManager();

            int count = fm.getBackStackEntryCount();
            for(int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }

            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();

            return true;
        }

    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureAlarmNotification();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(AuthUiActivity.createIntent(this));
            finish();
            return;
        }

        generateUser(currentUser, new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_main);
                Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(myToolbar);

                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                // Leave fragments code at the end. If we're being restored from a previous state,
                // then we don't need to do anything and should return or else we could end up with overlapping fragments.
                if (savedInstanceState != null) {
                    return;
                }

                //Manually displaying the first fragment - one time only
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, TripsFragment.newInstance());
                transaction.commit();

                //Used to select an item programmatically
                //bottomNavigationView.getMenu().getItem(2).setChecked(true);
            }
        });
    }

    private void configureAlarmNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 30);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MainActivity.this, NewNoteNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void generateUser(final FirebaseUser currentUser, final Runnable onLoaded) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //existe usuario con ese UID
                    loggedUser = dataSnapshot.getValue(User.class);
                    onLoaded.run();
                } else {
                    loggedUser = User.create(currentUser);
                    database.child("users").child(currentUser.getUid()).setValue(loggedUser);
                    onLoaded.run();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                onLogout();
                return true;
            default:
                // If we got here, the user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AuthUiActivity.class);
        startActivity(intent);
    }

    public static Intent createIntent(Context context, IdpResponse idpResponse) {
        Intent in = IdpResponse.getIntent(idpResponse);
        in.setClass(context, MainActivity.class);
        return in;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

}
