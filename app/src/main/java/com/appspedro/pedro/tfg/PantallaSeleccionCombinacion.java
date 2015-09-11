package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Clase PantallaSeleccionCombinacion que hereda de Activity e implementa OnclickListener y muestra la pantalla de seleccion de
 * codigo de colores de la Batalla
 */
public class PantallaSeleccionCombinacion extends Activity implements OnClickListener{

    /**
     * Variable que guarda el color activo
     */
    private String coloractivo = "rojo";

    /**
     * Variable que muestro el jugador que tiene que elegir combinacion
     */
    private TextView nombreTurno;

    /**
     * Variable referente al boton del mismo color
     */
    private RadioButton azul,amarillo,rojo,verde,rosa,naranja,morado,celeste;

    /**
     * Variable referente al boton de enviar
     */
    private Button enviar;

    /**
     * Variable referente al boton de la conbinacion
     */
    private Button boton1,boton2,boton3,boton4;

    /**
     * Variable que guarda el Drawable segun el coloractivo
     */
    private Drawable drw;

    /**
     * Variable que guarda los datos de la Batalla
     */
    private PartidaBatalla partidaBatalla;

    /**
     * Variable que maneja la vibracion
     */
    private Vibrator vibrador;

    /**
     * Variable que indica si los sonidos estan activados
     */
    private Boolean sonidosActivados;

    /**
     * Variable que indica si la vibracion esta activada
     */
    private Boolean vibracionActivada;

    /**
     * Variable para gestionar el sonido de los botones
     */
    private SoundPool sonidoTecla,sonidoBoton;

    /**
     * Variable utilizada tambien para gestionar el sonido de los botones
     */
    private int flujoTecla,flujoBoton;

    /**
     * Variable que guarda el numero de colores seguna la dificultad
     */
    private String numColores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Establece la interfaz de usuario para esta Activity
        setContentView(R.layout.activity_seleccion_combinacion);

        //Recuperamos las preferencias guardadas en la aplicacion y las asignamos a las variables
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);
        numColores = preferencias.getString("difB", "6");

        //Se inicializar el Drawable a la ficha roja ya que es la que esta seleccionada por defecto
        drw = ContextCompat.getDrawable(this, R.drawable.ficha_roja);

        //Inicializamos la vibracion
        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Inicializamos el sonido de los botones
        sonidoTecla = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoTecla = sonidoTecla.load(this, R.raw.pulsacolor, 1);
        sonidoBoton = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        flujoBoton = sonidoBoton.load(this, R.raw.botonesmenus, 1);

        //Recuperamos el objeto PartidaBatalla de la Activity anterior
        partidaBatalla = getIntent().getParcelableExtra("partida");

        //Asignamos todos los elementos de la interfaz a sus respectivas variables
        nombreTurno = (TextView)findViewById(R.id.nombreTurno);
        enviar = (Button)findViewById(R.id.botonEnviar);
        azul = (RadioButton)findViewById(R.id.radioAzul);amarillo = (RadioButton)findViewById(R.id.radioAmarillo);
        verde = (RadioButton)findViewById(R.id.radioVerde);naranja = (RadioButton)findViewById(R.id.radioNaranja);
        rosa = (RadioButton)findViewById(R.id.radioRosa);rojo = (RadioButton)findViewById(R.id.radioRojo);
        celeste =(RadioButton)findViewById(R.id.radioCeleste);morado = (RadioButton)findViewById(R.id.radioMorado);
        boton1 = (Button)findViewById(R.id.boton1);boton2 = (Button)findViewById(R.id.boton2);
        boton3 = (Button)findViewById(R.id.boton3);boton4 = (Button)findViewById(R.id.boton4);

        //Mostramos el nombre del jugador segun sea su turno de elegir o no
        if(partidaBatalla.getTurno()==1) {
            nombreTurno.setText(partidaBatalla.getJugador2());
        }else{
            nombreTurno.setText(partidaBatalla.getJugador1());
        }

        //Si hemos elegido 8 colores en las preferencias mostramos dos colores mas
        if(numColores.equals("8")){
            celeste.setVisibility(View.VISIBLE);
            morado.setVisibility(View.VISIBLE);
        }

        //Añadimos los botones al Listener para que al ser clickados se ejecute el metodo Onclick
        boton1.setOnClickListener(this);boton2.setOnClickListener(this);
        boton3.setOnClickListener(this);boton4.setOnClickListener(this);
        enviar.setOnClickListener(this);

        azul.setOnClickListener(this);amarillo.setOnClickListener(this);verde.setOnClickListener(this);
        naranja.setOnClickListener(this);rosa.setOnClickListener(this);rojo.setOnClickListener(this);
        celeste.setOnClickListener(this);morado.setOnClickListener(this);

    }

    /**
     * Metodo que gestiona el click en cada uno de los elementos de la interfaz
     * @param v View referente a la Activity
     */
    public void onClick(View v) {
        switch (v.getId()) {
            //Si pinchamos en cual quiera de los botones de la combinacion
            case R.id.boton1:case R.id.boton2:case R.id.boton3:case R.id.boton4:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Pintamos el color pinchado con el color activo
                v.setBackground(drw);
                //Guardamos el nombre del color en el tag
                v.setTag(coloractivo);
                break;
            case R.id.radioAmarillo:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_amarilla);
                //Actualizamos el color activo
                coloractivo="amarillo";
                break;
            case R.id.radioRojo:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_roja);
                //Actualizamos el color activo
                coloractivo="rojo";
                break;
            case R.id.radioVerde:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_verde);
                //Actualizamos el color activo
                coloractivo="verde";
                break;
            case R.id.radioAzul:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_azul);
                coloractivo="azul";
                break;
            case R.id.radioNaranja:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_naranja);
                //Actualizamos el color activo
                coloractivo="naranja";
                break;
            case R.id.radioRosa:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_rosa);
                //Actualizamos el color activo
                coloractivo="rosa";
                break;
            case R.id.radioMorado:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_morada);
                //Actualizamos el color activo
                coloractivo="morado";
                break;
            case R.id.radioCeleste:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoTecla.play(flujoTecla,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(150);
                //Actualizamos el drawable con el color de la ficha seleccionada
                drw=getResources().getDrawable(R.drawable.ficha_celeste);
                //Actualizamos el color activo
                coloractivo="celeste";
                break;
            case R.id.botonEnviar:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if(sonidosActivados)
                    sonidoBoton.play(flujoBoton,1,1,0,0,1);
                if(vibracionActivada)
                    vibrador.vibrate(350);
                //Añadimos al intent el tag de los colores seleccionados para crear la combinacion en el juego
                Intent intent = new Intent(this, Juego.class);
                intent.putExtra("color1",boton1.getTag().toString());
                intent.putExtra("color2",boton2.getTag().toString());
                intent.putExtra("color3",boton3.getTag().toString());
                intent.putExtra("color4",boton4.getTag().toString());
                //Añadimos el modo de juego y el objeto PartidaBatalla con los datos de la partida
                intent.putExtra("modoJuego","batalla");
                intent.putExtra("partida",partidaBatalla);
                finish();
                //Iniciamos el juego
                startActivity(intent);
                break;
        }

    }

    /**
     * Metodo que se ejecuta cuando se pusla el boton fisico del movil "volver"
     */
     public void onBackPressed() {
        finish();
         //Se inicia el menu principal
        startActivity(new Intent(this, PantallaMenuPrincipal.class));
    }
}
