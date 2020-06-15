package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
            Tvalue = aux.substring(18,20);
            Hvalue = aux.substring(26,28);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        limTemp.setText("\n" + limT + " °C\n");
        limHum.setText("\n" + limH + " %\n");
        actVal.setText("Temperatura: " + Tvalue + " °C" + "\n Humedad: " + Hvalue + " %");
    }

    public void onClick(View view){

        switch (view.getId()){
            case R.id.buttonHup:limH+=5; limHum.setText("\n" + limH + " %\n"); break;
            case R.id.buttonTup: limT+=5; limTemp.setText("\n" + limT + " °C\n"); break;
            case R.id.buttonHdown: limH-=5; limHum.setText("\n" + limH + " %\n"); break;
            case R.id.buttonTdown:limT-=5; limTemp.setText("\n" + limT + " °C\n"); break;
            default: break;
        }

    }

    public void isEvent(View view){

    }
}