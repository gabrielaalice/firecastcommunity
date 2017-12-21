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
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.fragment.OccurenceFragment;
import com.example.gabriela.firecastcommunity.helper.MetodsHelpers;

import static br.com.zbra.androidlinq.Linq.stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmNotify {
    static final List<Integer> idsOccurrenceEnabled = new ArrayList<>();
    List<Occurrence> listOccurrenceEnabled;

    public AlarmNotify(Context context) {
        User user = MainActivity.getUser();
        if(user.isNotify()) {
            if (user.isTimeSilence()) {
                Date now = new Date();

                Date startSilence = ReturnDate(user.getTimeStartSilence());
                Date finishSilence = ReturnDate(user.getTimeFinishSilence());

                if (now.after(startSilence) || now.before(finishSilence)) {


                    for (Occurrence o : VerifyNotification()) {
                        Intent intent = new Intent(context, MainActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

                        b.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(GetIconOccurrence(o))
                                .setContentTitle(o.type.name)
                                .setContentText(ReturnDistance(o.distance) + " | " + o.city.name + " no bairro " + o.addressNeighborhood)
                                .setContentIntent(contentIntent)
                                .setPriority(NotificationManager.IMPORTANCE_HIGH);


                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1, b.build());
                    }
                }
            }
        }
    }

    private Date ReturnDate(Date dateParam) {
        Date date = new Date();
        date.setHours(dateParam.getHours());
        date.setMinutes(dateParam.getMinutes());
        date.setSeconds(0);

        return date;
    }

    private static int GetIconOccurrence(Occurrence occurrence) {

        switch (occurrence.type.id){
            case 1:
                return R.drawable.fire_icon;
            case 2:
                return R.drawable.support_icon;
            case 3:
                return R.drawable.dangerous_product_icon;
            case 4:
                return R.drawable.search_rescue_icon;
            case 5:
                return R.drawable.paramedics_icon;
            case 6:
                return R.drawable.firecast_orange; // NOT_SERVICE
            case 7:
                return R.drawable.other_icon;
            case 8:
                return R.drawable.car_accident_icon;
            case 9:
                return R.drawable.firecast_community_orange; // PREVENTIVE
            case 10:
                return R.drawable.tree_cutting_pin;
            case 11:
                return R.drawable.insect_control_pin;
        }
        return R.drawable.other_pin;

    }

    private String ReturnDistance(Double distance) {
        if(distance!=null) {
            return MetodsHelpers.convertNumberInText("pt","BR", distance) + " km";
        }else{
            return "Não foi possível calcular a distância (faltam informações)";
        }
    }

    private List<Occurrence> VerifyNotification() {
        List<Occurrence> occurrenceNotNotify = new ArrayList<>();
        listOccurrenceEnabled = OccurenceFragment.getListOccurrence();

        //if(idsOccurrenceEnabled.size() > 0){
            occurrenceNotNotify = stream(listOccurrenceEnabled)
                    .where(x -> !idsOccurrenceEnabled.contains(x.id))
                    .toList();
        //}

        idsOccurrenceEnabled.clear();
        idsOccurrenceEnabled.addAll(stream(listOccurrenceEnabled).select(x -> x.id).toList());

        return occurrenceNotNotify;
    }
}