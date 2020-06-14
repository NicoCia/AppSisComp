package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Vista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);
        TextView limTemp = findViewById(R.id.textViewTempLim);
        TextView limHum = findViewById(R.id.textViewHumLim);
        TextView actVal = findViewById(R.id.textViewActVal);

        limTemp.setText(50 + " °C");
        limHum.setText(45 + " %");
        actVal.setText("Temperatura: 40" + " °C" + "\n Humedad: 35" + " %");
    }
}