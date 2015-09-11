package com.appspedro.pedro.tfg;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase PartidaBatalla que hereda de Parcelable y guarda los datos referentes al modo de juego Batalla utilizada
 * para pasar los datos entre las diferentes Activity
 * @author Pedro Alcalá Galiano
 */

public class PartidaBatalla implements Parcelable {
    /**
     * Variable con el nombre del primer jugador
     */
    private String jugador1;
    /**
     * Variable con el nombre del segundo jugador
     */
    private String jugador2;
    /**
     * Variable con el numero de jugadas del primer jugador
     */
    private int numjugadas1;
    /**
     * Variable con el numero de jugadas del segundo jugador
     */
    private int numjugadas2;
    /**
     * Variable con el tiempo del primer jugador
     */
    private String tiempo1;
    /**
     * Variable con el tiempo del segundo jugador
     */
    private String tiempo2;
    /**
     * Variable que contiene el numero del turno
     */
    private int turno;

    /**
     * Constructor vacio de la clase PartidaBatalla
     */
    public PartidaBatalla(){
        super();
        turno=1;
    }

    /**
     * Constructor de la clase PartidaBatalla que descompone los atributos de la clase e un parcel
     * @param in Parcel que recibe los atributos de la clase PartidaBatalla
     */
    public PartidaBatalla(Parcel in){
        //Añadimos todos los atributos de la clase al Parcel
        jugador1=in.readString();
        jugador2=in.readString();
        tiempo1=in.readString();
        tiempo2=in.readString();
        numjugadas1=in.readInt();
        numjugadas2=in.readInt();
        turno=in.readInt();

    }

    /**
     * Constructor a partir de los nombres de jugadores
     * @param j1 Nombre del primer jugador
     * @param j2 Nombre del segundo jugador
     */
    public PartidaBatalla(String j1, String j2){
        super();
        jugador1=j1;
        jugador2=j2;
        turno=1;
    }

    /**
     * Metodo para modificar el nombre del primer jugador
     * @param j1 Nombre del primer jugador
     */
    public void setJugador1(String j1){
        jugador1=j1;
    }

    /**
     * Metodo para modificar el nombre del segundo jugador
     * @param j2 Nombre del segundo jugador
     */
    public void setJugador2(String j2){
        jugador2=j2;
    }

    /**
     * Metodo para modificar el numero de jugadas del jugador 1
     * @param jugadas1 Numero de jugadas del jugador 1
     */
    public void setNumjugadas1(int jugadas1){
        numjugadas1=jugadas1;
    }

    /**
     * Metodo para modificar el numero de jugadas del jugador 2
     * @param jugadas2 Numero de jugadas del jugador 2
     */
    public void setNumjugadas2(int jugadas2){
        numjugadas2=jugadas2;
    }

    /**
     * Metodo para modificar el tiempo del jugador 1
     * @param t1 Tiempo del jugador 1
     */
    public void setTiempo1(String t1){
        tiempo1=t1;
    }

    /**
     * Metodo para modificar el tiempo del jugador 2
     * @param t2 Tiempo del jugador 2
     */
    public void setTiempo2(String t2){
        tiempo2=t2;
    }

    /**
     * Metodo para modificar el turno
     * @param t Numero del turno
     */
    public void setTurno(int t){
        turno=t;
    }

    /**
     * Metodo para obtener el nombre del jugador 1
     * @return Nombre del jugador 1
     */
    public String getJugador1(){
        return jugador1;
    }

    /**
     * Metodo para obtener el nombre del jugador 2
     * @return Nombre del jugador 2
     */
    public String getJugador2(){
        return jugador2;
    }

    /**
     * Metodo para obtener el tiempo del jugador 1
     * @return Tiempo del jugador 1
     */
    public String getTiempo1(){
        return tiempo1;
    }

    /**
     * Metodo para obtener el tiempo del jugador 2
     * @return Tiempo del jugador 2
     */
    public String getTiempo2(){
        return tiempo2;
    }

    /**
     * Metod para obtener el numero de jugadas del jugador 1
     * @return Numero de jugadas del jugador 1
     */
    public int getNumjugadas1(){
        return numjugadas1;
    }

    /**
     * Metod para obtener el numero de jugadas del jugador 2
     * @return Numero de jugadas del jugador 2
     */
    public int getNumjugadas2(){
        return numjugadas2;
    }

    /**
     * Metod para obtener el turno
     * @return Turno
     */
    public int getTurno(){
        return turno;
    }

    /**
     * Metodo obligatorio al heredar de Parcelable pero que no vamos a utilizar
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Metodo para asignar todos los atributos a un Parcel de destino
     * @param dest Parcel destino donde se copian todos los atributos
     * @param flags Flags sobre como deben ser copiados los atributos en el parcel
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Copiamos todos los atributos al Parcel dest
        dest.writeString(jugador1);
        dest.writeString(jugador2);
        dest.writeString(tiempo1);
        dest.writeString(tiempo2);
        dest.writeInt(numjugadas1);
        dest.writeInt(numjugadas2);
        dest.writeInt(turno);
    }

    /**
     * Objeto que genera el objeto de la clase original a partir del Parcel creado anteriormente
     */
    public static final Parcelable.Creator<PartidaBatalla> CREATOR
            = new Parcelable.Creator<PartidaBatalla>() {
        public PartidaBatalla createFromParcel(Parcel in) {
            return new PartidaBatalla(in);
        }
        public PartidaBatalla[] newArray(int tam) {
            return new PartidaBatalla[tam];
        }
    };
}
