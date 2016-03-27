package com.example.florian.memomaker.app;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;



/**
 * Created by Florian on 26.03.2016.
 */
public class AlarmReceiver extends BroadcastReceiver{

    public final static String KS_DESCRIPTION="KS_DESCRIPTION";
    public final static String KS_NOTIFYID="KS_NOTIFYID";
    public final static int DEFAULT_VIBRATE=150;


    @Override
    public void onReceive(Context context, Intent inintent) {

        //Konstanten
        String description = inintent.getExtras().getString(KS_DESCRIPTION);
        int notifyID = inintent.getExtras().getInt(KS_NOTIFYID);


        if(description != null && notifyID != 0) {
            //Inhalte der Notification
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(context)
                    .setContentTitle("Deine Memo wird fällig...")
                    .setContentText(description)
                    .setTicker("Alert Memo")
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setDefaults(DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.ic_assignment_late_24dp);

            //Öffnet die App auf dem Memo-Tab
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(PlaceholderFragment.ARG_SECTION_NUMBER, 1);

            /*
            * Der TaskStackBuilder sorgt dafür das nach dem anklicken der Notification der User
            * in der App bleibt und diese nicht direkt geschlossen wird wenn der Zurück-Button
            * betätigt wird.*/
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntent(intent);

            //Der PendingIntent öffnet die App wenn die Notification angeklickt wird.
            PendingIntent pIntent = taskStackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            notificationBuilder.setContentIntent(pIntent);

            //Nach dem anklicken der Notification wird diese beendet und nicht weiter angezeigt
            notificationBuilder.setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notifyID, notificationBuilder.build());

        }
    }
}
