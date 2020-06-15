package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Vista extends AppCompatActivity {

    private TextView limTemp;
    private TextView limHum;
    private TextView actVal;
    private static final String filename = "my_data.txt";
    private static int limT;
    private static int limH;
    private static String Tvalue;
    private static String Hvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);

        this.limTemp = findViewById(R.id.textViewTempLim);
        this.limHum = findViewById(R.id.textViewHumLim);
        this.actVal = findViewById(R.id.textViewActVal);

        try {
            FileInputStream fileInput = openFileInput(filename);
            InputStreamReader file = new InputStreamReader(fileInput);
            BufferedReader br=new BufferedReader(file);
            String aux = br.readLine();
            limT = Integer.parseInt(aux.substring(3,5));
            limH = Integer.parseInt(aux.substring(9,11));
            Tvalue = aux.substring(17,19);
            Hvalue = aux.substring(25,27);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        limTemp.setText("\n" + limT + " °C\n");
        limHum.setText("\n" + limH + " %\n");
        actVal.setText("Temperatura: " + Tvalue + " °C" + "\n Humedad: " + Hvalue + " %");
        //actVal.setText(new File(this.getApplicationContext().getFilesDir(), filename).getAbsolutePath());
    }

    /**
     * Metodo de accion al presionar los botones + y - de la vista. Incrementa o decrementa
     * segun corresponda el limite de temperatura o el limite de humedad
     * @param view  nombre del boton que fue presionado
     */

    public void onClick(View view){

        switch (view.getId()){
            case R.id.buttonHup:if(limH<95){limH+=5; limHum.setText("\n" + limH + " %\n");} break;
            case R.id.buttonTup:if(limT<95){limT+=5; limTemp.setText("\n" + limT + " °C\n");} break;
            case R.id.buttonHdown:if(limH>20){limH-=5; limHum.setText("\n" + limH + " %\n");} break;
            case R.id.buttonTdown:if(limT>20){limT-=5; limTemp.setText("\n" + limT + " °C\n");} break;
            default: break;
        }

    }

    /**
     * Metodo de accion al presionar el boton APLICAR de la vista. Notifica al controlador que el
     * usuario aplico cambios nuevos en la vista.
     * @param view  nombre del boton presionado
     */
    public void isEvent(View view){
        Intent intent = new Intent("is-event");
        intent.putExtra("Temp", "temperaturaMAX " + limT);
        intent.putExtra("Temp", "humedadMIN " + limH);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Imprime los valores actuales de Temperatura y Humedad
     */
    private void printData(){

        try {
            FileInputStream fileInput = openFileInput(filename);
            InputStreamReader file = new InputStreamReader(fileInput);
            BufferedReader br=new BufferedReader(file);
            String aux = br.readLine();
            Tvalue = aux.substring(17,19);
            Hvalue = aux.substring(25,27);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        actVal.setText("Temperatura: " + Tvalue + " °C" + "\n Humedad: " + Hvalue + " %");

    }

    @Override
    public void onResume() {
        super.onResume();

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("update-view"));
    }

    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            printData();
        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}