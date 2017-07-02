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
        Notification notification  = new Notification.Builder(context)
                .setSmallIcon(R.drawable.logoapp)
                .setContentTitle("TripJournal")
                .setContentText("Recuerda subir una nota del dia de hoy en tu viaje!")
                .setAutoCancel(true).build();

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(0, notification);
    }
}