package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class SeleccionCombinacion extends Activity implements View.OnClickListener{

    private String coloractivo = "rojo";
    private TextView nombreTurno;
    private RadioButton azul,amarillo,rojo,verde,rosa,naranja;
    private Button enviar,boton1,boton2,boton3,boton4;
    private Drawable drw;
    private PartidaBatalla partidaBatalla;
    private Vibrator vibrador;
    private Boolean sonidosActivados,vibracionActivada;
    private MediaPlayer sonidoTecla,sonidoBoton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_combinacion);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);

        drw = ContextCompat.getDrawable(this, R.drawable.ficha_roja);

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sonidoTecla = MediaPlayer.create(this,R.raw.pulsacolor);
        sonidoBoton = MediaPlayer.create(this,R.raw.botonesmenus);

        partidaBatalla = getIntent().getParcelableExtra("partida");

        nombreTurno = (TextView)findViewById(R.id.nombreTurno);
        enviar = (Button)findViewById(R.id.botonEnviar);
        azul = (RadioButton)findViewById(R.id.radioAzul);amarillo = (RadioButton)findViewById(R.id.radioAmarillo);
        verde = (RadioButton)findViewById(R.id.radioVerde);naranja = (RadioButton)findViewById(R.id.radioNaranja);
        rosa = (RadioButton)findViewById(R.id.radioRosa);rojo = (RadioButton)findViewById(R.id.radioRojo);
        boton1 = (Button)findViewById(R.id.boton1);boton2 = (Button)findViewById(R.id.boton2);
        boton3 = (Button)findViewById(R.id.boton3);boton4 = (Button)findViewById(R.id.boton4);

        if(partidaBatalla.getTurno()==1) {
            nombreTurno.setText(partidaBatalla.getJugador2());
        }else{
            nombreTurno.setText(partidaBatalla.getJugador1());
        }

        boton1.setOnClickListener(this);boton2.setOnClickListener(this);
        boton3.setOnClickListener(this);boton4.setOnClickListener(this);

        enviar.setOnClickListener(this);

        azul.setOnClickListener(this);amarillo.setOnClickListener(this);verde.setOnClickListener(this);
        naranja.setOnClickListener(this);rosa.setOnClickListener(this);rojo.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boton1:case R.id.boton2:case R.id.boton3:case R.id.boton4:
                if(sonidosActivados)
                    sonidoTecla.start();
                if(vibracionActivada)
                    vibrador.vibrate(150);
                v.setBackground(drw);
                v.setTag(coloractivo);
                break;
            case R.id.radioAmarillo:
                drw=getResources().getDrawable(R.drawable.ficha_amarilla);
                coloractivo="amarillo";
                break;
            case R.id.radioRojo:
                drw=getResources().getDrawable(R.drawable.ficha_roja);
                coloractivo="rojo";
                break;
            case R.id.radioVerde:
                drw=getResources().getDrawable(R.drawable.ficha_verde);
                coloractivo="verde";
                break;
            case R.id.radioAzul:
                drw=getResources().getDrawable(R.drawable.ficha_azul);
                coloractivo="azul";
                break;
            case R.id.radioNaranja:
                drw=getResources().getDrawable(R.drawable.ficha_naranja);
                coloractivo="naranja";
                break;
            case R.id.radioRosa:
                drw=getResources().getDrawable(R.drawable.ficha_rosa);
                coloractivo="rosa";
                break;
            case R.id.botonEnviar:
                if(sonidosActivados)
                    sonidoBoton.start();
                if(vibracionActivada)
                    vibrador.vibrate(350);
                Intent intent = new Intent(this, Juego.class);
                intent.putExtra("color1",boton1.getTag().toString());
                intent.putExtra("color2",boton2.getTag().toString());
                intent.putExtra("color3",boton3.getTag().toString());
                intent.putExtra("color4",boton4.getTag().toString());
                intent.putExtra("modoJuego","batalla");
                intent.putExtra("partida",partidaBatalla);
                startActivity(intent);
                break;
        }

    }
}
