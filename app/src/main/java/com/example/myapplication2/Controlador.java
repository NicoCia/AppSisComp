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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.FileDescriptor;

public class Controlador extends Service implements Runnable{
    private BroadcastReceiver mMessageReceiver;
    private Modelo modelo;
    private Cliente cliente;

    private boolean event;

    public Controlador(Modelo modelo, Cliente cliente) {
        this.modelo = modelo;
        this.cliente = cliente;

        this.mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkEvent(true);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("is-event"));

    }

    @Override
    public void run() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateView() {
        Intent intent = new Intent("update-view");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
