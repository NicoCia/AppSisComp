package com.example.myapplication2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileDescriptor;

public class Controlador extends Service{
    private BroadcastReceiver mMessageReceiver;
    private Modelo modelo;
    private static final long updateTime = 900000;
    private static final String filename = "my_data.txt";
    private static final String TAG = "my_tag";
    private static final String CHANNEL_ID = "ALARMAS";
    private static final int notificationId = 13;
    private static long timeStamp;
    private static int tempMAX;
    private static int humMIN;
    private static String meds;
    private Cliente cliente;
    private Thread workerThread = null;

    private boolean event;
    @Override
    public void onCreate(){
        Log.d(TAG, "Servicio creado...");
        this.modelo = new Modelo(new File(this.getApplicationContext().getFilesDir(), filename).getAbsolutePath());
        this.cliente = new Cliente();
        if(cliente.getConnection())new Thread(cliente).start();
        this.timeStamp = System.currentTimeMillis();


        this.mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setEvent();
                tempMAX = intent.getIntExtra("Temp",0);
                humMIN = intent.getIntExtra("Hum",0);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("is-event"));


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "Servicio iniciado...");
        if(workerThread == null || !workerThread.isAlive()) {
            workerThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while(true) {
                        if(cliente.isDatoNuevo()){
                            updateModel(cliente.leerMensaje());
                            if(modelo.isAlarma()){
                                if(modelo.isAlarmaH()) isAlarm("humedad");
                                if(modelo.isAlarmaT()) isAlarm("temperatura");
                            }
                            updateView();
                        }

                        if (checkEvent()) {
                            updateModel("temperaturaMAX " + tempMAX);
                            updateModel("humedadMIN " + humMIN);
                            if(cliente.getConnection()){
                                cliente.enviar("temperatura " + tempMAX);
                                cliente.enviar("humedad " + humMIN);
                            }
                        }
                        if ((timeStamp - System.currentTimeMillis() >= updateTime)&&(cliente.getConnection())) {
                            cliente.enviar("Leer");
                            timeStamp = System.currentTimeMillis();
                        }
                    }
                }
            });
            workerThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Servicio destruido...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Metodo de actualizacion de la vista, lanza intent a traves de un LocalBroadcast
     */
    private void updateView() {
        Intent intent = new Intent("update-view");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Metodo de actualizacion del modelo, carga
     */
    private void updateModel(String cadena){
        modelo.updateModel(cadena);
    }

    private boolean checkEvent(){
        if(event){
            this.event=false;
            return true;
        }
        else return false;
    }

    private void setEvent(){
        this.event=true;
    }

    private void isAlarm(String mensaje){
        String textTitle, textContent;
        if(mensaje.equals("humedad")){
            textTitle="Hora de regar!!";
            textContent = "La Humedad de tu compost está por debajo de la deseada";
        }
        else {
            textTitle="Esto esta que arde!!";
            textContent = "La Temperatura de tu compost ha superado el limite";
        }

        createNotificationChannel();

        Intent intent = new Intent(this, Vista.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(textContent))
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }

    /**
     * Creador de canales de notificacion
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
