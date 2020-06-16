package com.example.myapplication2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileDescriptor;

public class Controlador extends Service{
    private BroadcastReceiver mMessageReceiver;
    private Modelo modelo;
    private static final long updateTime = 900000;
    private static final String filename = "my_data.txt";
    private static final String TAG = "my_tag";
    private static long timeStamp;
    private static int temp;
    private static int hum;
    private static String meds;
    private Cliente cliente;
    private Thread workerThread = null;

    private boolean event;
    @Override
    public void onCreate(){
        Log.d(TAG, "Servicio creado...");
        this.modelo = new Modelo(new File(this.getApplicationContext().getFilesDir(), filename).getAbsolutePath());
        //this.cliente = cliente;
        this.timeStamp = System.currentTimeMillis();


        this.mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkEvent(true);
                temp = intent.getIntExtra("Temp",0);
                hum = intent.getIntExtra("Hum",0);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("is-event"));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "Servicio iniciado...");
        if(workerThread == null || !workerThread.isAlive()) {
            workerThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //TODO implementar cada vez que muestreo pregunto si hay alarma, solo soy cliente,
                    /**
                     *
                     */
                    //updateModel("temperatura 85 humedad 93");
                        /*try {
                            Log.d("myTag", "llegue");
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    //updateView();
                    //Aquí se realiza el trabajo del hilo secundario
                    while(true) {

                        if (checkEvent(false)) {
                            updateModel("temperaturaMAX "+temp);
                            updateModel("humedadMIN "+hum);
                        }
                        if (timeStamp - System.currentTimeMillis() >= updateTime) {
                            timeStamp = System.currentTimeMillis();
                            //String aux = cliente.socketReceive();
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

    synchronized private boolean checkEvent(boolean setFlag){
        if(setFlag) {
            this.event=true;
            return event;
        }
        else if(event){
            this.event=false;
            return true;
        }
        else return false;
    }
}
