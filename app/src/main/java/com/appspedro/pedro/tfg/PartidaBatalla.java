package com.appspedro.pedro.tfg;


import android.os.Parcel;
import android.os.Parcelable;

public class PartidaBatalla implements Parcelable {
    private String jugador1;
    private String jugador2;
    private int numjugadas1;
    private int numjugadas2;
    private String tiempo1;
    private String tiempo2;
    private int turno;

    public PartidaBatalla(){
        super();
        turno=1;
    }

    public PartidaBatalla(Parcel in){
        jugador1=in.readString();
        jugador2=in.readString();
        tiempo1=in.readString();
        tiempo2=in.readString();
        numjugadas1=in.readInt();
        numjugadas2=in.readInt();
        turno=in.readInt();

    }

    public PartidaBatalla(String j1, String j2){
        super();
        jugador1=j1;
        jugador2=j2;
        turno=1;
    }

    public void setJugador1(String j1){
        jugador1=j1;
    }

    public void setJugador2(String j2){
        jugador2=j2;
    }

    public void setNumjugadas1(int jugadas1){
        numjugadas1=jugadas1;
    }

    public void setNumjugadas2(int jugadas2){
        numjugadas2=jugadas2;
    }

    public void setTiempo1(String t1){
        tiempo1=t1;
    }

    public void setTiempo2(String t2){
        tiempo2=t2;
    }

    public void setTurno(int t){
        turno=t;
    }

    public String getJugador1(){
        return jugador1;
    }

    public String getJugador2(){
        return jugador2;
    }

    public String getTiempo1(){
        return tiempo1;
    }

    public String getTiempo2(){
        return tiempo2;
    }

    public int getNumjugadas1(){
        return numjugadas1;
    }

    public int getNumjugadas2(){
        return numjugadas2;
    }

    public int getTurno(){
        return turno;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jugador1);
        dest.writeString(jugador2);
        dest.writeString(tiempo1);
        dest.writeString(tiempo2);
        dest.writeInt(numjugadas1);
        dest.writeInt(numjugadas2);
        dest.writeInt(turno);
    }

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
