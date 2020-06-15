package com.example.myapplication2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente { /**no hace falta que sea runnable**/

    //public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";
    private boolean flag;
    private String data;
    private Socket socket;                                          //socket
    private BufferedReader in;
    private PrintWriter out;
    private static final int SERVERPORT = 6000;                     //puerto el que les parezca yo puse este en (caso de que no funcione)
    private static final String SERVER_IP = "192.168.100.15";       //IP de la rasp
    private static final String MYTAG="mytag";                      //tag para el mensaje de debugging

    public Cliente() {
        try {

            this.socket = new Socket(InetAddress.getByName(SERVER_IP), SERVERPORT);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream());

            this.data="";
            this.flag=false;

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // Método para enviar el mensaje, que se pasa por parámetro.
    public void socketSend(String message){
        /**para leer out.println("mensaje")**/
        try{
            out.write(message); /**para escribir**/
            out.flush();
            out.close();
            socket.close();
            Log.d(MYTAG, "socketSending: ");
            //flag=true;
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String socketReceive(){
        String datoRecibido = "";
        try {
            datoRecibido = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datoRecibido;
    }

    public boolean resultadoEnvio() {
        return flag;
    }
}