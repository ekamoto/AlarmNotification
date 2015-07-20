package br.com.hisamoto.alarmeNotification.activity;

import android.app.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import br.com.hisamoto.alarmeNotification.R;
import br.com.hisamoto.alarmeNotification.broadcast.BroadcastReceiverAux;

import java.util.Calendar;

public class HisamotoActivity extends Activity {

    private CheckBox alerta;
    private CheckBox alerta2;
    private PendingIntent p;
    private static AlarmManager alarme;
    private static Ringtone toque;
    private static Uri som;
    private TimePicker hora;
    private Intent intentAlarme;
    private Button openDialog;
    private EditText versao;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        intentAlarme = new Intent("ALARME_DISPARADO");
        getComponentes();
        anexarEventos();
    }

    public void getComponentes() {

        alerta = (CheckBox) findViewById(R.id.alarme);
        alerta2 = (CheckBox) findViewById(R.id.alarme_2);
        hora = (TimePicker) findViewById(R.id.hora);
        openDialog = (Button) findViewById(R.id.open_dialog);

    }

    private boolean isActiveService() {

        return (AlarmManager) getSystemService(ALARM_SERVICE) != null;
    }

    public void cancelService() {

        Log.i("Ekamoto", "Cancelando Service");
        PendingIntent p = PendingIntent.getBroadcast(this, 0, intentAlarme, 0);

        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarme.cancel(p);
    }

    public void anexarEventos() {

        cancelService();

        Log.i("Ekamoto", "Anexando eventos");

        alerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!alerta.isChecked()) {

                    toque.stop();
                } else {

                    som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    toque = RingtoneManager.getRingtone(getApplicationContext(), som);
                    toque.play();
                }
            }
        });

        alerta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!alerta2.isChecked()) {

                    // Cancela o service
                    alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarme.cancel(p);

                    // Parar música
                    if (BroadcastReceiverAux.toque != null)
                        BroadcastReceiverAux.toque.stop();

                    Toast.makeText(getApplicationContext(), "Alarme Cancelado", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getApplicationContext(), "Alarme Iniciado", Toast.LENGTH_SHORT).show();
                    String horaInput = hora.getCurrentHour() + ":" + hora.getCurrentMinute();

                    String[] parts = horaInput.split(":");
                    String hora = "";
                    hora = parts[0];
                    String minuto = parts[1];

                    if (Integer.parseInt(hora) < 10) {

                        hora = "0" + hora;
                    }

                    horaInput = hora + ":" + minuto;

                    Log.i("Ekamoto", "Hora saida: " + horaInput);

                    BroadcastReceiverAux.hora_x = horaInput;

                    p = PendingIntent.getBroadcast(getApplicationContext(), 0, intentAlarme, 0);

                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(System.currentTimeMillis());
                    c.add(Calendar.SECOND, 3);

                    alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10000, p);

                }
            }
        });

        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(HisamotoActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Dialog teste");

                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.fofinha4);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PendingIntent p = PendingIntent.getBroadcast(this, 0, intentAlarme, 0);

        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarme.cancel(p);
    }

    public void onClickHome(View v) {

        trace("Tela home");
        final Dialog dialog = new Dialog(HisamotoActivity.this);
        dialog.setContentView(R.layout.versao_system);
        dialog.setTitle("Sobre");

        TextView text = (TextView) dialog.findViewById(R.id.versao);
        TextView text2 = (TextView) dialog.findViewById(R.id.versao_code);
        String versionName = "";
        Integer versionCode = 0;

        try {

            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        text.setText("Versão: " + versionName);
        text2.setText("Versão do Código: " + versionCode);

        ImageView image = (ImageView) dialog.findViewById(R.id.image2);
        image.setImageResource(R.drawable.fofinha4);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonSair);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        //versao = (EditText) findViewById(R.id.versao);
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void trace(String msg) {
        toast(msg);
    }
}