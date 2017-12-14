package com.example.gabriela.firecastcommunity.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverAux extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
//		Financa financa = (Financa) intent.getExtras().getSerializable(
//				Constante.FINANCA);
//
//		Intent it = new Intent(context, FinancaPesquisaActivity.class);
//		it.putExtra(Constante.CODIGO_FINANCA, financa.getCodigoFinanca());
//
//		gerarNotificacao(context, it, financa);
	}

//	public void gerarNotificacao(Context context, Intent intent, Financa financa) {
//		NotificationManager nm = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);
//
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(
//				context);
//		builder.setTicker("Alerta de Finan�a");
//		builder.setContentTitle("Alerta de Finan�a");
//		builder.setContentText(financa.toString());
//		builder.setSmallIcon(R.drawable.logo_tcc);
//		builder.setLargeIcon(BitmapFactory.decodeResource(
//				context.getResources(), R.drawable.logo_tcc));
//		builder.setContentIntent(p);
//
//		Notification n = builder.build();
//		n.vibrate = new long[] { 150, 300, 150, 600 };
//		n.flags = Notification.FLAG_AUTO_CANCEL;
//		nm.notify(R.drawable.logo_tcc, n);
//
//		try {
//			Uri som = RingtoneManager
//					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//			Ringtone toque = RingtoneManager.getRingtone(context, som);
//			toque.play();
//		} catch (Exception e) {
//		}
//	}
}