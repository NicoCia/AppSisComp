package com.example.myapplication2;

public class Modelo {
    private int temperatura;
    private int temperaturaMAX;
    private int humedad;
    private int humedadMIN;
    private int tiempo;
    private boolean alarma;

    public Modelo(){
        temperatura=0;
        temperaturaMAX=60;
        humedad=40;
        humedadMIN=30;
        tiempo=0;
        alarma=false;
    }

    public void updateModel(String cadena){
        //substring(int beginIndex, int endIndex) temperatura xx humedad xx
        int Ttemp = Integer.parseInt(cadena.substring(12,13));
        int Htemp = Integer.parseInt(cadena.substring(23,24));
        setTemperatura(Ttemp);
        setHumedad(Htemp);
        validarNuevosValores();
    }

    private void validarNuevosValores(){
        if(fueraDeLimites()){
            setAlarma(true);
            notificar();
        }
        else setAlarma(false);
    }

   private boolean fueraDeLimites(){
        return temperatura>=temperaturaMAX||humedad<humedadMIN;
   }

    public void notificar(){

    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getTemperaturaMAX() {
        return temperaturaMAX;
    }

    public void setTemperaturaMAX(int temperaturaMAX) {
        this.temperaturaMAX = temperaturaMAX;
    }

    public int getHumedad() {
        return humedad;
    }

    public void setHumedad(int humedad) {
        this.humedad = humedad;
    }

    public int getHumedadMIN() {
        return humedadMIN;
    }

    public void setHumedadMIN(int humedadMIN) {
        this.humedadMIN = humedadMIN;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public boolean isAlarma() {
        return alarma;
    }

    public void setAlarma(boolean alarma) {
        this.alarma = alarma;
    }
}
