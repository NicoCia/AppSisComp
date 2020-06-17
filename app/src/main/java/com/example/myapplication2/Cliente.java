package com.example.myapplication2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente implements Runnable{

    //public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";
    private boolean flag;
    private String data;
    private Socket socket;                                          //socket
    private BufferedReader in;
    private PrintWriter out;
    private boolean connection;
    private boolean banderaEnvio;
    private boolean finComunicacion;
    private String datoRecibido;
    private boolean datoNuevo;
    private static final int SERVERPORT = 6000;                     //puerto el que les parezca yo puse este en (caso de que no funcione)
    private static final String SERVER_IP = "192.168.100.15";       //IP de la rasp
    private static final String MYTAG="mytag";                      //tag para el mensaje de debugging


    public Cliente() {
        try {

            this.socket = new Socket(InetAddress.getByName(SERVER_IP), SERVERPORT);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream());

            this.flag = false;
            this.connection = true;

            this.banderaEnvio=false;
            this.finComunicacion=false;

        } catch (IOException e1) {
            this.connection = false;
            e1.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(!finComunicacion){
            if(hayMensajeParaEnviar()){
                socketSend(data);
            }
            else if(hayMensajeParaLeer()){
                socketReceive();
            }
        }
        cerrarSocket();
    }

    private void cerrarSocket(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            connection = false;
            e.printStackTrace();
        }
    }

    public void enviar(String mensaje){     //Método que voy a usar para cargar un mensaje. Setea bandera en true
        this.data=mensaje;
        banderaEnvio=true;
    }

    private boolean hayMensajeParaEnviar(){
        return banderaEnvio;
    }

    private boolean hayMensajeParaLeer(){
        boolean aux = false;
        try {
            aux=in.ready();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aux;
    }


    // Método para enviar el mensaje, que se pasa por parámetro.
    private void socketSend(String message){
        try{
           out.write(message); //para
           out.flush();
           Log.d(MYTAG, "socketSending: ");
        }
        catch (Exception e) {
           e.printStackTrace();
        }
        banderaEnvio=false;


    }

    private void socketReceive () {
        try {
            this.datoRecibido = in.readLine();
            datoNuevo=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String leerMensaje(){
        datoNuevo=false;
        return datoRecibido;
    }

    public boolean isDatoNuevo(){
        return datoNuevo;
    }

    public boolean getConnection () {
        return connection;
    }

}