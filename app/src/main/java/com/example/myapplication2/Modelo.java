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

    /**  Recibe un string y actualiza las variables asociadas, controlando si los valores se
        encuentran en los limites permitidos.
        Los formatos de mensaje válidos son:
            - temperatura xx humedad xx
            - temperaturaMAX xx
            - humedadMIN xx
        Si los valores de temperatura y humedad están fuera de rango, se notifica al controlador y
        se setea alarma en 1.

     **/
    public void updateModel(String cadena){
        //substring(int beginIndex, int endIndex) temperatura xx humedad xx -> 25 de largo
        if(cadena.length()==25) {
            int Ttemp = Integer.parseInt(cadena.substring(12, 13));
            int Htemp = Integer.parseInt(cadena.substring(23, 24));
            setTemperatura(Ttemp);
            setHumedad(Htemp);
            validarNuevosValores();
        }
        //temperaturaMAX xx
        else if(cadena.startsWith("temperaturaMAX")&&cadena.length()==18){
            int TMAXtemp = Integer.parseInt(cadena.substring(16, 17));
            setTemperaturaMAX(TMAXtemp);
            validarNuevosValores();
        }
        //humedadMIN xx
        else if(cadena.startsWith("humedadMIN")&&cadena.length()==13){
            int HMINtemp = Integer.parseInt(cadena.substring(11, 12));
            setHumedadMIN(HMINtemp);
            validarNuevosValores();
        }
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

    private void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getTemperaturaMAX() {
        return temperaturaMAX;
    }

    private void setTemperaturaMAX(int temperaturaMAX) {
        this.temperaturaMAX = temperaturaMAX;
    }

    public int getHumedad() {
        return humedad;
    }

    private void setHumedad(int humedad) {
        this.humedad = humedad;
    }

    public int getHumedadMIN() {
        return humedadMIN;
    }

    private void setHumedadMIN(int humedadMIN) {
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
