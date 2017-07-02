package utn_frba_mobile.dadm_diario_viajes.notifications;

import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import utn_frba_mobile.dadm_diario_viajes.R;

/**
 * Created by toiacabrera on 7/1/17.
 */

public class NewNoteNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Utils.generateNotification(context);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}