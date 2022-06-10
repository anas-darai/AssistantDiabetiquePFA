package com.anasdarai.assistant_diabtique.notifications;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.anasdarai.assistant_diabtique.db_manager;
import com.anasdarai.assistant_diabtique.objs.Tache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlanifierTachesService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getBundleExtra("bundle");

        if(bundle==null) {
            final db_manager dbManager = new db_manager(context);
            final ArrayList<Tache> tachesPerma = dbManager.getTachesPerForNotifcation();

            for (Tache t:tachesPerma ) {
                planifierTache(t,context);
            }
        }else{
            Tache tache= (Tache) bundle.getSerializable("tache");
            NotificationHelper notificationHelper = new NotificationHelper(context,tache);
            notificationHelper.createNotification();
        }
    }


    public static void planifierTache(Tache tache,Context context) {

        Calendar calendar = Calendar.getInstance();

        if (tache.permanent==1){
            if (tache.time.isEmpty() ) {
                calendar.set(Calendar.HOUR_OF_DAY, 7);
                calendar.set(Calendar.MINUTE, 0);
            }else{
                float f_tm = Float.parseFloat(tache.time);
                if (1 > f_tm) {
                    int hour= (int) (f_tm * 24);
                    int min= (int) ((f_tm*24f-hour)*60);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, 7);
                    calendar.set(Calendar.MINUTE, 0);
                }
            }
        }else{
            Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.FRENCH);
                try {
                    cal.setTime(sdf.parse(tache.time));// all done
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));

        }
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(context, PlanifierTachesService.class);


        Bundle bundle = new Bundle();

        bundle.putSerializable("tache",tache);
        intent.putExtra("bundle",bundle);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, tache.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }
}
