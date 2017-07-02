package utn_frba_mobile.dadm_diario_viajes.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import utn_frba_mobile.dadm_diario_viajes.R;
import utn_frba_mobile.dadm_diario_viajes.activities.MainActivity;

/**
 * Created by toiacabrera on 7/2/17.
 */

public class Utils {

    @SuppressWarnings("static-access")
    public static void generateNotification(Context context) {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context);
        nb.setSmallIcon(R.drawable.minilogoapp);
        nb.setContentTitle("Has escrito algo el dia hoy?");
        nb.setContentText("Contanos que hiciste y recuerdalo para siempre!");
        nb.setTicker("Animate");

        nb.setAutoCancel(true);

        //get the bitmap to show in notification bar
        /*Bitmap bitmap_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.logoapp);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        s.setSummaryText("Contanos que hiciste y recuerdalo para siempre!");
        nb.setStyle(s);*/

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(context);
        TSB.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(11221, nb.build());
    }
}