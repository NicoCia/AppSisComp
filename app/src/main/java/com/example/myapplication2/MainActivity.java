package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication2.ui.vista.VistaFragment;

public class MainActivity extends AppCompatActivity {

    VistaFragment vistaFragment = new VistaFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, vistaFragment)
                    .commitNow();
        }
    }

    public void onClick(View view){
        TextView textView = (TextView) findViewById(R.id.message);
        textView.setText(R.string.texto_prueba);
    }

}