package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String filename = "my_data.txt";
    private static final String defaultValues = "T: 60 H: 30 tAct 00 hAct 00\0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!existsFile(filename)){
            new File(this.getApplicationContext().getFilesDir(), filename);
            try {
                FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(defaultValues.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Intent intent = new Intent(this,Controlador.class);
        startService(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Vista.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 2000);

    }

    /**
     * Metodo que controla la existencia en memoria interna del fichero de datos
     * @param fileName Nombre del archivo de datos
     * @return boolean true si el archivo existe, false en caso contrario
     */
    public boolean existsFile(String fileName) {
        for (String tmp : fileList()) {
            if (tmp.equals(fileName))
                return true;
        }
        return false;
    }
}