package com.example.gabriela.firecastcommunity.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.domain.Occurrence;
import com.example.gabriela.firecastcommunity.fragment.OccurenceFragment;
import static br.com.zbra.androidlinq.Linq.stream;

import java.util.ArrayList;
import java.util.List;

public class AlarmNotify {
    static final List<Integer> idsOccurrenceEnabled = new ArrayList<>();
    List<Occurrence> listOccurrenceEnabled;
    List<Integer> occurrenceNotNotify;

    public AlarmNotify(Context context) {
        occurrenceNotNotify = VerifyNotification();

        if(occurrenceNotNotify.size() > 0) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder b = new NotificationCompat.Builder(context);

            b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.firecast_orange)
                    .setContentTitle(GenerationTitle(occurrenceNotNotify))
                    //.setContentText(GenerationTextListOccurrence(occurrenceNotNotify))
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH);


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, b.build());
        }
    }

    private CharSequence GenerationTextListOccurrence(List<Integer> occurrenceNotNotify) {
        List<Occurrence> occurrenceEnabled = stream(OccurenceFragment.getListOccurrence())
                .where(x->occurrenceNotNotify.contains(x.id)).toList();

        String textNotification = "";
        boolean first=true;
        for (Occurrence o:occurrenceEnabled) {
            if(!first){
                textNotification+="\n";
            }
            textNotification += o.type.name +" | "+o.city.name + (o.distance!=null?" em " + o.distance:"");
            first=false;
        }

        return textNotification;
    }

    private CharSequence GenerationTitle(List<Integer> occurrenceNotNotify) {
        String pluralOrSingular = occurrenceNotNotify.size()>1 ? "s" : "";
        return occurrenceNotNotify.size() + " ocorrência" + pluralOrSingular + " nova"+ pluralOrSingular;
    }

    private List<Integer> VerifyNotification() {
        occurrenceNotNotify = new ArrayList<>();
        listOccurrenceEnabled = OccurenceFragment.getListOccurrence();

        if(idsOccurrenceEnabled.size() > 0){
            occurrenceNotNotify = stream(listOccurrenceEnabled)
                    .where(x -> !idsOccurrenceEnabled.contains(x.id))
                    .select(x -> x.id).toList();
        }

        idsOccurrenceEnabled.clear();
        idsOccurrenceEnabled.addAll(stream(listOccurrenceEnabled).select(x -> x.id).toList());

        return occurrenceNotNotify;
    }
}