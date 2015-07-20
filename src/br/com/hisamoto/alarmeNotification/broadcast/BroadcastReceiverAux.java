package br.com.hisamoto.alarmeNotification.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import br.com.hisamoto.alarmeNotification.ApplicationContextProvider;
import br.com.hisamoto.alarmeNotification.R;
import br.com.hisamoto.alarmeNotification.activity.HisamotoActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BroadcastReceiverAux extends BroadcastReceiver {

    public static Uri som;
    public static Ringtone toque;
    private String hora_input;
    public static String hora_x;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Ekamoto", "-> Recebe Alarme");
        hora_input = intent.getStringExtra("hora");
        Log.i("Ekamoto", "Hora chegada: " + hora_input);

        if(toque != null) {

            toque.stop();
        }
        gerarNotificacao(context, new Intent(context, HisamotoActivity.class), "Mamãe/Papai", "Aviso", "Fome mamãe");
    }


    public void gerarNotificacao(Context context, Intent intent, CharSequence ticker, CharSequence titulo, CharSequence descricao) {

        try {

            if(ativarAlarme()) {

                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setTicker(ticker);
                builder.setContentTitle(titulo);
                builder.setContentText(descricao);
                builder.setSmallIcon(R.drawable.ic_launcher);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
                builder.setContentIntent(p);

                Notification n = builder.build();
                n.vibrate = new long[]{150, 300, 150, 600};
                n.flags = Notification.FLAG_AUTO_CANCEL;
                nm.notify(R.drawable.ic_launcher, n);

                Log.i("Ekamoto", "-> Star Música");
                // TYPE_RINGTONE som do toque do celular
                // TYPE_NOTIFICATION som da notificação
                som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                toque = RingtoneManager.getRingtone(context, som);
                toque.play();
            }
        } catch (Exception e) {

            e.printStackTrace();
            Log.i("Ekamoto", "-> Erro: Star Música");
        }
    }

    public boolean ativarAlarme() {

        if(hora_x == null) {

            Log.i("Ekamoto", "Hora do alarme Null");
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        // OU
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        Date data_atual = cal.getTime();

        String data_completa = dateFormat.format(data_atual);

        String hora_atual = dateFormat_hora.format(data_atual);

        Log.i("Ekamoto","data_completa: " + data_completa);
        Log.i("Ekamoto","data_atual: " + data_atual.toString());
        Log.i("Ekamoto","hora_atual: " + hora_atual + " Hora input: " + hora_x);
        Toast.makeText(ApplicationContextProvider.getContext(), "hora_atual: " + hora_atual + " Hora input: " + hora_x, Toast.LENGTH_SHORT).show();

        return hora_atual.equals(hora_x);
    }
}