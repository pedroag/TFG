package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ResultadoBatalla extends Activity implements View.OnClickListener{

    private PartidaBatalla partidaBatalla;
    private TextView jugador1,jugador2,resultado1,resultado2,ganador;
    private Button salir,repetir;
    private Boolean sonidosActivados,vibracionActivada;
    private Vibrator vibrador;
    private SoundPool sonidoMenu;
    private int flujoMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_batalla);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);

        sonidoMenu = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoMenu = sonidoMenu.load(this, R.raw.botonesmenus, 1);

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        partidaBatalla = getIntent().getParcelableExtra("partida");

        jugador1 = (TextView)findViewById(R.id.jugador1);
        jugador2 = (TextView)findViewById(R.id.jugador2);
        ganador = (TextView)findViewById(R.id.ganador);
        resultado1 = (TextView)findViewById(R.id.resultado1);
        resultado2 = (TextView)findViewById(R.id.resultado2);
        salir = (Button)findViewById(R.id.salir);
        repetir = (Button)findViewById(R.id.repetir);


        jugador1.setText(partidaBatalla.getJugador1()+":");
        jugador2.setText(partidaBatalla.getJugador2()+":");

        if (!partidaBatalla.getTiempo1().equals("derrota")) {
            resultado1.setText(partidaBatalla.getNumjugadas1() + " jugadas en: " + partidaBatalla.getTiempo1());
        }else{
            resultado1.setText("Derrota");
        }
        if (!partidaBatalla.getTiempo2().equals("derrota")) {
            resultado2.setText(partidaBatalla.getNumjugadas2() + " jugadas en: " + partidaBatalla.getTiempo2());
        }else{
            resultado2.setText("Derrota");
        }
        comprobarResultados();

        salir.setOnClickListener(this);
        repetir.setOnClickListener(this);


    }

    private void comprobarResultados(){
        if(partidaBatalla.getNumjugadas1()<partidaBatalla.getNumjugadas2()){
            ganador.setText(partidaBatalla.getJugador1());
        }else if(partidaBatalla.getNumjugadas1()>partidaBatalla.getNumjugadas2()){
            ganador.setText(partidaBatalla.getJugador2());
        }else if(partidaBatalla.getNumjugadas1()==partidaBatalla.getNumjugadas2() && partidaBatalla.getNumjugadas1()!=16){
            String[] tiempo1 = partidaBatalla.getTiempo1().split(":");
            String[] tiempo2 = partidaBatalla.getTiempo2().split(":");
            if(Integer.parseInt(tiempo1[0])<Integer.parseInt(tiempo2[0])){
                ganador.setText(partidaBatalla.getJugador1());
            }else if(Integer.parseInt(tiempo1[0])>Integer.parseInt(tiempo2[0])){
                ganador.setText(partidaBatalla.getJugador2());
            }else if(Integer.parseInt(tiempo1[0])==Integer.parseInt(tiempo2[0])){
                if (Integer.parseInt(tiempo1[1])<Integer.parseInt(tiempo2[1])){
                    ganador.setText(partidaBatalla.getJugador1());
                }else if(Integer.parseInt(tiempo1[1])>Integer.parseInt(tiempo2[1])){
                    ganador.setText(partidaBatalla.getJugador2());
                }else if(Integer.parseInt(tiempo1[1])==Integer.parseInt(tiempo2[1])){
                    ganador.setText("¡EMPATE!");
                }
            }
        }else if(partidaBatalla.getNumjugadas1()==partidaBatalla.getNumjugadas2() && partidaBatalla.getNumjugadas1()==16){
            ganador.setText("¡EMPATE!");
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.salir:
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.repetir:
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                PartidaBatalla pB = new PartidaBatalla(partidaBatalla.getJugador1(),partidaBatalla.getJugador2());
                Intent i =  new Intent(this, SeleccionCombinacion.class);
                i.putExtra("partida", pB);
                finish();
                startActivity(i);

        }
    }

}
