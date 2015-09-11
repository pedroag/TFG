package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.SystemClock;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.Random;

/**
 * Clase PantallaVictoria que hereda de Activity e implementa OnclickListener y que muestra el tablero del juego y permite jugar la partida
 * @author Pedro Alcalá Galiano
 */
public class Juego extends Activity implements OnClickListener {

    /**
     * Variable que gestiona el cronometro en la partida
     */
    private Chronometer tiempo;

    /**
     * Variable que gestiona la musica durante la partida
     */
    private MediaPlayer musica;

    /**
     * Variable que gestiona el sonido al pulsar las fichas del tablero
     */
    private SoundPool sonidoColor;

    /**
     * Variable que gestiona el sonido de error
     */
    private SoundPool error;

    /**
     * Variable que gestiona el sonido al pulsar los botones
     */
    private SoundPool sonidoMenu;

    /**
     * Variable utilizada tambien para gestionar el sonido de las fichas del tablero
     */
    private int flujoColor;

    /**
     * Variable utilizada tambien para gestionar el sonido de error
     */
    private int flujoError;

    /**
     * Variable utilizada tambien para gestionar el sonido de las botones
     */
    private int flujoMenu;

    /**
     * Variable que guarda el nombre del jugador de la partida
     */
    private String usuario;

    /**
     * Variable que guarda el tipo de modo de juego de la partida
     */
    private String modoJuego;

    /**
     * Variable utilizada para mostrar el nombre de usuario
     */
    private TextView idusuario;

    /**
     * Variable utilizada para mostrar numero de aciertos de la mano indicada
     */
    private TextView acie1,acie2,acie3,acie4,acie5,acie6,acie7,acie8,acie9,acie10,acie11,acie12;

    /**
     * Variable utilizada para mostrar numero de aproximaciones de la mano indicada
     */
    private TextView aprox1,aprox2,aprox3,aprox4,aprox5,aprox6,aprox7,aprox8,aprox9,aprox10,aprox11,aprox12;

    /**
     * Variable que guarda los datos referente al modo Batalla
     */
    private PartidaBatalla partidaBatalla;

    /**
     * Variable que guarda el color activo en la seleccion de color
     */
    private String coloractivo = "rojo";

    /**
     * Variable referente a la selección del coloro indicado
     */
    private RadioButton azul,amarillo,rojo,verde,rosa,naranja,morado,celeste;

    /**
     * Variable referente al boton INICIO
     */
    private Button BotonInicio;

    /**
     * Variable referente al boton RONDA
     */
    private Button BotonRonda;

    /**
     * Variable referente al boton SALIR
     */
    private Button BotonSalir;

    /**
     * Variable que maneja el Scrollview del tablero de fichas
     */
    private ScrollView sv;

    /**
     * Variable que guarda el Drawable referente al color seleccionado
     */
    private Drawable drw;

    /**
     * Variable referente al boton de la mano y posicion indicados
     */
    private Button Boton1_1,Boton1_2,Boton1_3,Boton1_4,Boton2_1,Boton2_2,Boton2_3,Boton2_4,
            Boton3_1,Boton3_2,Boton3_3,Boton3_4,Boton4_1,Boton4_2,Boton4_3,Boton4_4,
            Boton5_1,Boton5_2,Boton5_3,Boton5_4,Boton6_1,Boton6_2,Boton6_3,Boton6_4,
            Boton7_1,Boton7_2,Boton7_3,Boton7_4,Boton8_1,Boton8_2,Boton8_3,Boton8_4,
            Boton9_1,Boton9_2,Boton9_3,Boton9_4,Boton10_1,Boton10_2,Boton10_3,Boton10_4,
            Boton11_1,Boton11_2,Boton11_3,Boton11_4,Boton12_1,Boton12_2,Boton12_3,Boton12_4;

    /**
     * Variable referente al ToggleButton para silencia la música
     */
    private ToggleButton toggleMusica;

    /**
     * Variable que gestiona la vibración
     */
    private Vibrator vibracion;

    /**
     * Variable que indica si los sonidos estan activadados
     */
    private Boolean sonidosActivados;

    /**
     * Variable que indica si la vibracion esta activada
     */
    private Boolean vibracionActivada;

    /**
     * Variable que guarda el valor del color de la solucion el numero de ficha indicado
     */
    private String sol1,sol2,sol3,sol4;

    /**
     * Variable que gestiona el Layout referente al numero de mano indicada
     */
    private RelativeLayout mano1,mano2,mano3,mano4,mano5,mano6,mano7,mano8,mano9,mano10,mano11,mano12;

    /**
     * Variable que gestiona el Layout referente al numero de aciertos y aproximaciones en la mano indicada
     */
    private RelativeLayout pmano1,pmano2,pmano3,pmano4,pmano5,pmano6,pmano7,pmano8,pmano9,pmano10,pmano11,pmano12;

    /**
     * Variable que guarda los botones de la jugada activa
     */
    private ArrayList<Button> posible;

    /**
     * Variable que guarda los colores de la solucion
     */
    private ArrayList<String> solucion;

    /**
     * Variable que guarda el numero de mano por el que va
     */
    private int jugadas=1;

    /**
     * Variable que guarda la dificultad del juego
     */
    private String dificultad;

    /**
     * Variable que guarda el numero de colores posibles para el modo Batalla
     */
    private String numColores;

    /**
     * Variable para gestionar la base de datos de los jugadores
     */
    private BDPerfilJugador BD;

    /**
     * Metodo llamado cuando la actividad es creada
     * @param savedInstanceState Estado anterior de la actividad guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Establece la interfaz de usuario para esta Activity
        setContentView(R.layout.activity_juego);

        //Creamos la tipografia utilizada en esta Activity
        Typeface fuenteTiempo = Typeface.createFromAsset(getAssets(), "font/Calculator.ttf");
        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/Square.ttf");

        //Descargamos la preferencias y las asignamos a sus variables
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        dificultad = preferencias.getString("dif", "facil");
        numColores = preferencias.getString("difB", "6");
        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);

        //Inicialzamos la musica y la ponemos para que se repita
        musica = MediaPlayer.create(this, R.raw.popmetal);
        musica.setLooping(true);

        //Inicialzamos los sonidos de la Activity
        sonidoMenu = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        flujoMenu = sonidoMenu.load(this, R.raw.botonesmenus, 1);
        sonidoColor = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoColor = sonidoColor.load(this, R.raw.pulsacolor, 1);
        error = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoError = error.load(this, R.raw.error, 1);

        //Inicializamos el drawable a color seleccionado por defecto, que el rojo, indicandole su xml
        drw = ContextCompat.getDrawable(this, R.drawable.ficha_roja);

        //Obtenemos el modo de juego y el nombre del jugador que se pasaron desde el menu principal
        modoJuego = getIntent().getStringExtra("modoJuego");
        usuario = getIntent().getStringExtra("usuario");

        //Inicializamos la vibracion
        vibracion = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Asignamos el Scrollview del tablero y desabilitamos la barra lateral
        sv = (ScrollView) this.findViewById(R.id.scrollView);
        sv.setVerticalScrollBarEnabled(false);

        //Inicializamos los ArrayList de la Activity
        solucion = new ArrayList<String>();
        posible = new ArrayList<Button>();

        //Inicializamos todas la variables referentes a la interfaz de la activity y las asgimanos a su respectivo elemento
        idusuario = (TextView) findViewById(R.id.idjugador);
        tiempo = (Chronometer) findViewById(R.id.tiempo);
        TextView texto_tiempo = (TextView)findViewById(R.id.textotiempo);



        mano1 = (RelativeLayout)findViewById(R.id.mano1);mano2 = (RelativeLayout)findViewById(R.id.mano2);
        mano3 = (RelativeLayout)findViewById(R.id.mano3);mano4 = (RelativeLayout)findViewById(R.id.mano4);
        mano5 = (RelativeLayout)findViewById(R.id.mano5);mano6 = (RelativeLayout)findViewById(R.id.mano6);
        mano7 = (RelativeLayout)findViewById(R.id.mano7);mano8 = (RelativeLayout)findViewById(R.id.mano8);
        mano9 = (RelativeLayout)findViewById(R.id.mano9);mano10 = (RelativeLayout)findViewById(R.id.mano10);
        mano11 = (RelativeLayout)findViewById(R.id.mano11);mano12 = (RelativeLayout)findViewById(R.id.mano12);

        toggleMusica = (ToggleButton)findViewById(R.id.musica);

        pmano1 = (RelativeLayout)findViewById(R.id.Pmano1);pmano2 = (RelativeLayout)findViewById(R.id.Pmano2);
        pmano3 = (RelativeLayout)findViewById(R.id.Pmano3);pmano4 = (RelativeLayout)findViewById(R.id.Pmano4);
        pmano5 = (RelativeLayout)findViewById(R.id.Pmano5);pmano6 = (RelativeLayout)findViewById(R.id.Pmano6);
        pmano7 = (RelativeLayout)findViewById(R.id.Pmano7);pmano8 = (RelativeLayout)findViewById(R.id.Pmano8);
        pmano9 = (RelativeLayout)findViewById(R.id.Pmano9);pmano10 = (RelativeLayout)findViewById(R.id.Pmano10);
        pmano11 = (RelativeLayout)findViewById(R.id.Pmano11);pmano12 = (RelativeLayout)findViewById(R.id.Pmano12);

        BotonInicio = (Button)findViewById(R.id.botoninicio);BotonRonda = (Button)findViewById(R.id.botonronda);
        BotonSalir = (Button)findViewById(R.id.salir);

        Boton1_1 = (Button)findViewById(R.id.button9);Boton1_2 = (Button)findViewById(R.id.button11);
        Boton1_3 = (Button)findViewById(R.id.button10);Boton1_4 = (Button)findViewById(R.id.button12);
        Boton2_1 = (Button)findViewById(R.id.button);Boton2_2 = (Button)findViewById(R.id.button7);
        Boton2_3 = (Button)findViewById(R.id.button2);Boton2_4 = (Button)findViewById(R.id.button8);
        Boton3_1 = (Button)findViewById(R.id.button3);Boton3_2 = (Button)findViewById(R.id.button5);
        Boton3_3 = (Button)findViewById(R.id.button4);Boton3_4 = (Button)findViewById(R.id.button6);
        Boton4_1 = (Button)findViewById(R.id.button13);Boton4_2 = (Button)findViewById(R.id.button15);
        Boton4_3 = (Button)findViewById(R.id.button14);Boton4_4 = (Button)findViewById(R.id.button16);
        Boton5_1 = (Button)findViewById(R.id.button17);Boton5_2 = (Button)findViewById(R.id.button19);
        Boton5_3 = (Button)findViewById(R.id.button18);Boton5_4 = (Button)findViewById(R.id.button20);
        Boton6_1 = (Button)findViewById(R.id.button21);Boton6_2 = (Button)findViewById(R.id.button23);
        Boton6_3 = (Button)findViewById(R.id.button22);Boton6_4 = (Button)findViewById(R.id.button24);
        Boton7_1 = (Button)findViewById(R.id.button25);Boton7_2 = (Button)findViewById(R.id.button27);
        Boton7_3 = (Button)findViewById(R.id.button26);Boton7_4 = (Button)findViewById(R.id.button28);
        Boton8_1 = (Button)findViewById(R.id.button29);Boton8_2 = (Button)findViewById(R.id.button31);
        Boton8_3 = (Button)findViewById(R.id.button30);Boton8_4 = (Button)findViewById(R.id.button32);
        Boton9_1 = (Button)findViewById(R.id.button33);Boton9_2 = (Button)findViewById(R.id.button35);
        Boton9_3 = (Button)findViewById(R.id.button34);Boton9_4 = (Button)findViewById(R.id.button36);
        Boton10_1 = (Button)findViewById(R.id.button37);Boton10_2 = (Button)findViewById(R.id.button39);
        Boton10_3 = (Button)findViewById(R.id.button38);Boton10_4 = (Button)findViewById(R.id.button40);
        Boton11_1 = (Button)findViewById(R.id.button41);Boton11_2 = (Button)findViewById(R.id.button43);
        Boton11_3 = (Button)findViewById(R.id.button42);Boton11_4 = (Button)findViewById(R.id.button44);
        Boton12_1 = (Button)findViewById(R.id.button45);Boton12_2 = (Button)findViewById(R.id.button47);
        Boton12_3 = (Button)findViewById(R.id.button46);Boton12_4 = (Button)findViewById(R.id.button48);

        acie1 = (TextView)findViewById(R.id.acie1);aprox1 = (TextView)findViewById(R.id.aprox1);
        acie2 = (TextView)findViewById(R.id.acie2);aprox2 = (TextView)findViewById(R.id.aprox2);
        acie3 = (TextView)findViewById(R.id.acie3);aprox3 = (TextView)findViewById(R.id.aprox3);
        acie4 = (TextView)findViewById(R.id.acie4);aprox4 = (TextView)findViewById(R.id.aprox4);
        acie5 = (TextView)findViewById(R.id.acie5);aprox5 = (TextView)findViewById(R.id.aprox5);
        acie6 = (TextView)findViewById(R.id.acie6);aprox6 = (TextView)findViewById(R.id.aprox6);
        acie7 = (TextView)findViewById(R.id.acie7);aprox7 = (TextView)findViewById(R.id.aprox7);
        acie8 = (TextView)findViewById(R.id.acie8);aprox8 = (TextView)findViewById(R.id.aprox8);
        acie9 = (TextView)findViewById(R.id.acie9);aprox9 = (TextView)findViewById(R.id.aprox9);
        acie10 = (TextView)findViewById(R.id.acie10);aprox10 = (TextView)findViewById(R.id.aprox10);
        acie11 = (TextView)findViewById(R.id.acie11);aprox11 = (TextView)findViewById(R.id.aprox11);
        acie12 = (TextView)findViewById(R.id.acie12);aprox12 = (TextView)findViewById(R.id.aprox12);

        azul = (RadioButton)findViewById(R.id.radioazul);amarillo = (RadioButton)findViewById(R.id.radioamarillo);
        rojo = (RadioButton)findViewById(R.id.radiorojo);verde = (RadioButton)findViewById(R.id.radioverde);
        rosa = (RadioButton)findViewById(R.id.radiorosa);naranja = (RadioButton)findViewById(R.id.radionaranja);
        morado = (RadioButton) findViewById(R.id.radiomorado);celeste = (RadioButton)findViewById(R.id.radioceleste);

        //Cambiamos la tipografia de los elementos
        texto_tiempo.setTypeface(fuente);
        idusuario.setTypeface(fuente);
        tiempo.setTypeface(fuenteTiempo);

        BotonSalir.setTypeface(fuente);BotonInicio.setTypeface(fuente);BotonRonda.setTypeface(fuente);

        //Inicializamos todos los botones de las manos al color negro
        IniciarBotones();

        //Añadimos todos los botones al Listener para ser clickados despues
        azul.setOnClickListener(this);amarillo.setOnClickListener(this);rojo.setOnClickListener(this);
        verde.setOnClickListener(this);rosa.setOnClickListener(this);naranja.setOnClickListener(this);
        morado.setOnClickListener(this);celeste.setOnClickListener(this);

        BotonInicio.setOnClickListener(this);BotonRonda.setOnClickListener(this);BotonSalir.setOnClickListener(this);

        toggleMusica.setOnClickListener(this);

        Boton1_1.setOnClickListener(this);Boton1_2.setOnClickListener(this);
        Boton1_3.setOnClickListener(this);Boton1_4.setOnClickListener(this);
        Boton2_1.setOnClickListener(this);Boton2_2.setOnClickListener(this);
        Boton2_3.setOnClickListener(this);Boton2_4.setOnClickListener(this);
        Boton3_1.setOnClickListener(this);Boton3_2.setOnClickListener(this);
        Boton3_3.setOnClickListener(this);Boton3_4.setOnClickListener(this);
        Boton4_1.setOnClickListener(this);Boton4_2.setOnClickListener(this);
        Boton4_3.setOnClickListener(this);Boton4_4.setOnClickListener(this);
        Boton5_1.setOnClickListener(this);Boton5_2.setOnClickListener(this);
        Boton5_3.setOnClickListener(this);Boton5_4.setOnClickListener(this);
        Boton6_1.setOnClickListener(this);Boton6_2.setOnClickListener(this);
        Boton6_3.setOnClickListener(this);Boton6_4.setOnClickListener(this);
        Boton7_1.setOnClickListener(this);Boton7_2.setOnClickListener(this);
        Boton7_3.setOnClickListener(this);Boton7_4.setOnClickListener(this);
        Boton8_1.setOnClickListener(this);Boton8_2.setOnClickListener(this);
        Boton8_3.setOnClickListener(this);Boton8_4.setOnClickListener(this);
        Boton9_1.setOnClickListener(this);Boton9_2.setOnClickListener(this);
        Boton9_3.setOnClickListener(this);Boton9_4.setOnClickListener(this);
        Boton10_1.setOnClickListener(this);Boton10_2.setOnClickListener(this);
        Boton10_3.setOnClickListener(this);Boton10_4.setOnClickListener(this);
        Boton11_1.setOnClickListener(this);Boton11_2.setOnClickListener(this);
        Boton11_3.setOnClickListener(this);Boton11_4.setOnClickListener(this);
        Boton12_1.setOnClickListener(this);Boton12_2.setOnClickListener(this);
        Boton12_3.setOnClickListener(this);Boton12_4.setOnClickListener(this);

        //Creamos la solucion segun el modo de juego o la dificultad
        gestionarDificultad();
    }

    /**
     * Metodo que gestiona el click en cada uno de los elementos de la interfaz
     * @param v View referente a la Activity
     */
    public void onClick(View v) {
        //Dependiendo del boton clickado hace unas operaciones u otras
        switch (v.getId()) {
            case R.id.botoninicio:
                //Comprobamos las preferencias para la musica y la vibracion
                if(sonidosActivados)
                    musica.start();
                sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);

                //Iniciamos el cronometro
                tiempo.setBase(SystemClock.elapsedRealtime());
                tiempo.start();

                //Deshabiltamos el boton Inicio
                BotonInicio.setEnabled(false);
                //Habilitamos el boton RONDA
                BotonRonda.setEnabled(true);

                //Habilitamos los botones de la primera mano
                Boton1_1.setEnabled(true);
                Boton1_2.setEnabled(true);
                Boton1_3.setEnabled(true);
                Boton1_4.setEnabled(true);
                break;
            case R.id.botonronda:
                //Comprobamos las preferencias para la musica y la vibracion
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);
                //Llamamos al metodo para jugar la mano
                JugarMano();

                break;
            case R.id.salir:
                //Paramos la musica y salimos al menu principal
                musica.stop();
                finish();
                startActivity(new Intent(this, PantallaMenuPrincipal.class));
                break;
            case R.id.musica:
                //Comprobamos si esta pulsado o no el ToggleButton de la musica para pararla o no
                if(toggleMusica.isChecked()){
                    musica.pause();
                }else{
                    musica.start();
                }
                break;

            case R.id.button:case R.id.button2:case R.id.button3:case R.id.button4:case R.id.button5:case R.id.button6:
            case R.id.button7:case R.id.button8:case R.id.button9:case R.id.button10:case R.id.button11:case R.id.button12:
            case R.id.button13:case R.id.button14:case R.id.button15:case R.id.button16:case R.id.button17:case R.id.button18:
            case R.id.button19:case R.id.button20:case R.id.button21:case R.id.button22:case R.id.button23:case R.id.button24:
            case R.id.button25:case R.id.button26:case R.id.button27:case R.id.button28:case R.id.button29:case R.id.button30:
            case R.id.button31:case R.id.button32:case R.id.button33:case R.id.button34:case R.id.button35:case R.id.button36:
            case R.id.button37:case R.id.button38:case R.id.button39:case R.id.button40:case R.id.button41:case R.id.button42:
            case R.id.button43:case R.id.button44:case R.id.button45:case R.id.button46:case R.id.button47:case R.id.button48:

                //Comprobamos las preferencias para la musica y la vibracion
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(100);
                //Pintamos del color activo
                v.setBackground(drw);
                //Asignamos el tag con el color activo
                v.setTag(coloractivo);
                break;
            case R.id.radioamarillo:
                //Comprobamos las preferencias para la musica
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                //Creamos del drawable de este color con archivo xml
                drw=getResources().getDrawable(R.drawable.ficha_amarilla);
                //Cambiamos el color activo a este
                coloractivo="amarillo";
                break;
            //Hacemos lo mismo dependiendo del color seleccionado
            case R.id.radiorojo:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_roja);
                coloractivo="rojo";
                break;
            case R.id.radioverde:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_verde);
                coloractivo="verde";
                break;
            case R.id.radioazul:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_azul);
                coloractivo="azul";
                break;
            case R.id.radionaranja:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_naranja);
                coloractivo="naranja";
                break;
            case R.id.radiorosa:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_rosa);
                coloractivo="rosa";
                break;
            case R.id.radiomorado:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_morada);
                coloractivo="morado";
                break;
            case R.id.radioceleste:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_celeste);
                coloractivo="celeste";
                break;
        }
    }

    /**
     * Método que crea la dificultad segun el modo de juego o las preferencias
     */
    private void gestionarDificultad(){
        //Si el modo de juego es Batalla
        if (modoJuego.equals("batalla")) {
            //Se comprueba cuantos colores se han seleccionado en las preferencias
            if(numColores.equals("8")) {
                //Si son ocho, se muestra en la tabla de colores
                morado.setVisibility(View.VISIBLE);
                celeste.setVisibility(View.VISIBLE);
            }
            //Se obtiene los datos del objeto PartidaBatalla enviado desde PantallaSeleccionCombinacion
            partidaBatalla = getIntent().getParcelableExtra("partida");
            //Creamos la solucion con los colores que se ha especificado manualmente por el rival
            sol1 = getIntent().getStringExtra("color1");
            sol2 = getIntent().getStringExtra("color2");
            sol3 = getIntent().getStringExtra("color3");
            sol4 = getIntent().getStringExtra("color4");
            //Dependiendo del turno mostramos un nombre u otro
            if(partidaBatalla.getTurno()==1)
                idusuario.setText(partidaBatalla.getJugador1());
            else
                idusuario.setText(partidaBatalla.getJugador2());

            //Si el modo de juego es individual
        } else if (modoJuego.equals("individual")) {

            //Se muestra el nombre del jugador
            idusuario.setText(usuario);

            //Si se ha iniciado sesión
            if(AccessToken.getCurrentAccessToken()!=null){
                BD = new BDPerfilJugador(this);
                String[] usuario = new String[] {AccessToken.getCurrentAccessToken().getUserId()};

                //Se obtiene los datos referentes al jugador en la base de datos
                Cursor c = BD.busquedaUsuario(usuario);
                c.moveToFirst();

                //Se ponde una dificultad, segun el nivel del jugador
                if(c.getString(1).equals("Novato")){
                    dificultad = "facil";
                }else if(c.getString(1).equals("Amateur")){
                    dificultad = "medio";
                }else if(c.getString(1).equals("Profesional")) {
                    dificultad = "dificil";
                    morado.setVisibility(View.VISIBLE);
                    celeste.setVisibility(View.VISIBLE);
                }else if(c.getString(1).equals("Experto")) {
                    dificultad = "muydificil";
                    morado.setVisibility(View.VISIBLE);
                    celeste.setVisibility(View.VISIBLE);
                }
                //Se genera la solucion segúin la dificultad
                GeneraSolucion(dificultad);

                //Si no se ha iniciado sesion
            }else{
                //Se muestran los colores posibles segun la dificultad
                if(dificultad.equals("dificil") || dificultad.equals("muydificil")){
                    morado.setVisibility(View.VISIBLE);
                    celeste.setVisibility(View.VISIBLE);
                }
                //Se genera la solucin
                GeneraSolucion(dificultad);
            }
        }
        //Se añaden al ArrayList todos los elementos de la solucion
        solucion.add(sol1);solucion.add(sol2);solucion.add(sol3);solucion.add(sol4);
    }


    /**
     * Método para gestiona la mano que se está jugando y la comprobacion de resutados
     */
    private void JugarMano(){
        //Según la mano activa
        switch (jugadas){
            case 1:
                //Se habilita la mano que toca
                mano1.setEnabled(true);
                //Se quitan todos los botones de la mano anterior
                posible.clear();
                //Se añande todos los botones de esta mano
                posible.add(Boton1_1);posible.add(Boton1_2);posible.add(Boton1_3);posible.add(Boton1_4);

                //Se comprueba si se han rellenado todos los botones
                if (!comprobarRepeticiones(posible)) {
                    //Si no se han rellenado no se pasará a la siguiente mano
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                //Se guarda el numero de aproximacion y aciertos obtenidos llamando a los métodos correspondientes
                acie1.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox1.setText(Integer.toString(ComprobarAproximaciones(posible)));

                //Se comprueba si hay 4 aciertos
                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    //Se genera la victoria dependiendo del modo de juego
                    generarVictoria();
                    break;
                }

                //Se deshabilitan los botones de esta mano para que no puedan ser pulsados
                deshabilitarMano(posible);

                //Se muestra el resultado de la mano
                pmano1.setVisibility(View.VISIBLE);

                //Se muestra la siguiente mano
                mano2.setVisibility(View.VISIBLE);

                //Se incrementa el numero de jugadas
                jugadas++;
                break;

            //Para los demas casos de jugada se hace igual excepto para la ultima mano
            case 2:
                mano2.setEnabled(true);
                posible.clear();
                posible.add(Boton2_1);posible.add(Boton2_2);posible.add(Boton2_3);posible.add(Boton2_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie2.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox2.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano2.setVisibility(View.VISIBLE);
                mano3.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 3:
                mano3.setEnabled(true);
                posible.clear();
                posible.add(Boton3_1);posible.add(Boton3_2);posible.add(Boton3_3);posible.add(Boton3_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie3.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox3.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano3.setVisibility(View.VISIBLE);
                mano4.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 4:
                mano4.setEnabled(true);
                posible.clear();
                posible.add(Boton4_1);posible.add(Boton4_2);posible.add(Boton4_3);posible.add(Boton4_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie4.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox4.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano4.setVisibility(View.VISIBLE);
                mano5.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 5:
                mano5.setEnabled(true);
                posible.clear();
                posible.add(Boton5_1);posible.add(Boton5_2);posible.add(Boton5_3);posible.add(Boton5_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie5.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox5.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano5.setVisibility(View.VISIBLE);
                mano6.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 6:
                mano6.setEnabled(true);
                posible.clear();
                posible.add(Boton6_1);posible.add(Boton6_2);posible.add(Boton6_3);posible.add(Boton6_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie6.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox6.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano6.setVisibility(View.VISIBLE);
                mano7.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 7:
                mano7.setEnabled(true);
                posible.clear();
                posible.add(Boton7_1);posible.add(Boton7_2);posible.add(Boton7_3);posible.add(Boton7_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie7.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox7.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano7.setVisibility(View.VISIBLE);
                mano8.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 8:
                sv.setVerticalScrollBarEnabled(true);
                mano8.setEnabled(true);
                posible.clear();
                posible.add(Boton8_1);posible.add(Boton8_2);posible.add(Boton8_3);posible.add(Boton8_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie8.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox8.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano8.setVisibility(View.VISIBLE);
                mano9.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 9:
                mano9.setEnabled(true);
                posible.clear();
                posible.add(Boton9_1);posible.add(Boton9_2);posible.add(Boton9_3);posible.add(Boton9_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie9.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox9.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano9.setVisibility(View.VISIBLE);
                mano10.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 10:
                mano10.setEnabled(true);
                posible.clear();
                posible.add(Boton10_1);posible.add(Boton10_2);posible.add(Boton10_3);posible.add(Boton10_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie10.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox10.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano10.setVisibility(View.VISIBLE);
                mano11.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 11:
                mano11.setEnabled(true);
                posible.clear();
                posible.add(Boton11_1);posible.add(Boton11_2);posible.add(Boton11_3);posible.add(Boton11_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie11.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox11.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano11.setVisibility(View.VISIBLE);
                mano12.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 12:
                mano12.setEnabled(true);
                posible.clear();
                posible.add(Boton12_1);posible.add(Boton12_2);posible.add(Boton12_3);posible.add(Boton12_4);

                if (!comprobarRepeticiones(posible)) {
                    error.play(flujoError, 1, 1, 0, 0, 1);
                    break;
                }

                acie12.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox12.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano12.setVisibility(View.VISIBLE);
                generarDerrota();
                jugadas++;
                break;

        }

    }

    /**
     * Método para gestionar la victoria de la partida
     */
    private void generarVictoria() {

        //Se para la musica
        musica.stop();

        //Se crea un intent con la informacion que pasariamos a la PantallaVictoria en caso de estar en modo individual
        Intent intentIndividual = new Intent(this,PantallaVictoria.class);
        intentIndividual.putExtra("color1", sol1);
        intentIndividual.putExtra("color2", sol2);
        intentIndividual.putExtra("color3", sol3);
        intentIndividual.putExtra("color4", sol4);
        intentIndividual.putExtra("usuario", usuario);
        intentIndividual.putExtra("jugadas", jugadas);
        intentIndividual.putExtra("tiempo", tiempo.getText().toString());

        //Si el modo es individual se pasa lo anterior
        if (modoJuego.equals("individual")) {
            tiempo.stop();
            finish();
            startActivity(intentIndividual);

            //En caso del modo batalla se comprueba el turno
        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 1) {
            //Si es el primer turno se rellena el objeto PartidaBatalla y se vuelve a PantallaSeleccionCombinacion
            partidaBatalla.setNumjugadas1(jugadas);
            partidaBatalla.setTiempo1(tiempo.getText().toString());
            //Se indica el cambio de turno
            partidaBatalla.setTurno(2);
            Intent intent1jugador = new Intent(this, PantallaSeleccionCombinacion.class);
            intent1jugador.putExtra("partida", partidaBatalla);
            finish();
            startActivity(intent1jugador);
        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 2) {
            //En caso de ser el segundo turno se rellena el objeto PartidaBatalla y se pasa a la PantallaVictoria
            partidaBatalla.setNumjugadas2(jugadas);
            partidaBatalla.setTiempo2(tiempo.getText().toString());
            Intent intent2jugador = new Intent(this, PantallaResultadoBatalla.class);
            intent2jugador.putExtra("partida", partidaBatalla);
            finish();
            startActivity(intent2jugador);
        }
    }

    /**
     * Método para gestionar la derrota en el juego
     */
    private void generarDerrota(){
        //Se para la musica
        musica.stop();

        //Se muestra un alertDialog diciendo que has perdidos
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Derrota");
        builder.setMessage("No has conseguido adivinar la combinacion secreta")
                .setCancelable(false)
                //Se crea un solo botón Aceptar
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Se comprueba el modo de juego
                        if (modoJuego.equals("individual")) {
                            //Si es individual y estas conectado se te actualizan el numero de partidas y vuelves al menu
                            BD = new BDPerfilJugador(Juego.this);

                            if (AccessToken.getCurrentAccessToken() != null) {
                                String[] arg = new String[]{AccessToken.getCurrentAccessToken().getUserId()};
                                BD.actualizaPartidas(arg);
                            }

                            finish();
                            startActivity(new Intent(Juego.this, PantallaMenuPrincipal.class));
                            //Si es en modo Batalla se actualizan los datos del jugador y dependiendo del turno se vuelve a
                            //PantallaSeleccionCombinacion o PantallaResultadoBatalla
                        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 1) {
                            partidaBatalla.setTiempo1("derrota");
                            partidaBatalla.setNumjugadas1(jugadas);
                            //Se indica el cambio de turno
                            partidaBatalla.setTurno(2);
                            finish();
                            Intent intent1jugador = new Intent(Juego.this, PantallaSeleccionCombinacion.class);
                            intent1jugador.putExtra("partida", partidaBatalla);
                        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 2) {
                            partidaBatalla.setNumjugadas2(jugadas);
                            partidaBatalla.setTiempo2("derrota");
                            finish();
                            Intent intent2jugador = new Intent(Juego.this, PantallaResultadoBatalla.class);
                            intent2jugador.putExtra("partida", partidaBatalla);
                            startActivity(intent2jugador);
                        }
                    }
                });
        //Se crea el AlertDialgo y se muestra
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Método para comprobar el número de acierto en la mano
     * @param posible ArrayList con los elementos de la mano para comprobar
     * @return Numero de aciertos
     */
    private int ComprobarAciertos(ArrayList<Button> posible){

        int acertados=0;

        //Recorremos el ArrayList de la posible solucion y comprobamos su tag con el valor de la solcion en el mismo lugar
        for (int i=0;i<posible.size();i++) {
            if (solucion.get(i).equals(posible.get(i).getTag().toString()))
                //se incremena si es igual
                acertados++;
        }
        return acertados;
    }

    /**
     * Método para comprobar el número de aproximacioes en la mano
     * @param posible ArrayList con los elementos de la mano para comprobar
     * @return Numero de aproximaciones
     */
    private int ComprobarAproximaciones(ArrayList<Button> posible){

        int aproximaciones=0;
        //Vector inicializado a false para indicar que elemntos de la solucion ya no tenemos que comprobar
        Boolean [] posicionesAcertadas = {false,false,false,false};
        //Vector inicializado a false para indicar que elementos de la psoible solucion ya no tenemos que comprobar
        Boolean [] aproximacionesEscogidas = {false,false,false,false};

        //Descartamos todos los elementos que ha sido acertados
        for (int i=0;i<posible.size();i++) {
            if (solucion.get(i).equals(posible.get(i).getTag().toString())) {
                posicionesAcertadas[i] = true;
            }
        }
        //Recorremos el ArrayList de la solucion, pero solo los elementos que no hemos acertado
        for (int i=0;i<posible.size();i++) {
            if(!posicionesAcertadas[i]) {
                //Recorremos el vector posible
                for (int j = 0; j < posible.size(); j++) {
                    //Comprobamos que son iguales y que ya no se ha utilizado como aproximacion
                    if (solucion.get(i).equals(posible.get(j).getTag().toString()) && !aproximacionesEscogidas[j]) {
                        //Incrementamos las aproximaciones y descartamos el elemento de la solucion para futuras busquedas
                        aproximaciones++;
                        aproximacionesEscogidas[j] = true;
                        //Salimos de ester bucle para seguir con otro elemento
                        break;
                    }
                }
            }
        }
        return aproximaciones;
    }

    /**
     * Método para crear el ArrayList con los colores de la solución
     * @param dificultad Dificultad utilizada
     */
    void GeneraSolucion(String dificultad){
        //Vector con los posibles 6 colores
        String [] col = {"amarillo","rojo","verde","azul","naranja","rosa"};

        //Vector con los posibles 8 colores
        String [] col2 = {"amarillo","rojo","verde","azul","naranja","rosa","morado","celeste"};
        Random  r = new Random();

        if(dificultad.equals("facil")) {

            int p;
            ArrayList<Integer>colores = new ArrayList();
            for (int i = 0; i < 4; i++) {
                //Escogemos un color aleatorio entre los 6 posibles
                p = r.nextInt(6);
                //Si ya esta incluido buscamos otro
                while (colores.contains(p)) {
                    p = r.nextInt(6);
                }
                colores.add(p);
            }
            //Asignamos los colores a la solución
            sol1 = col[colores.get(0)];
            sol2 = col[colores.get(1)];
            sol3 = col[colores.get(2)];
            sol4 = col[colores.get(3)];

        }else if(dificultad.equals("medio")){

            //Asigmanos a cada elemento un color del vector obtenido aleatoriamente sin importar repeticiones
            int x1 = r.nextInt(6);
            sol1 = col[x1];

            int x2 = r.nextInt(6);
            sol2 = col[x2];

            int x3 = r.nextInt(6);
            sol3 = col[x3];

            int x4 = r.nextInt(6);
            sol4 = col[x4];

        }else if(dificultad.equals("dificil")){

            //Igual que la dificultad facil pero con el vector de 8 elementos
            int p;
            ArrayList<Integer>colores = new ArrayList();
            for (int i = 0; i < 4; i++) {
                p = r.nextInt(8);
                while (colores.contains(p)) {
                    p = r.nextInt(8);
                }
                colores.add(p);
            }
            sol1 = col2[colores.get(0)];
            sol2 = col2[colores.get(1)];
            sol3 = col2[colores.get(2)];
            sol4 = col2[colores.get(3)];
        }else if(dificultad.equals("muydificil")){

            //Igual que la dificultad medio pero con el vector de 8 elementos
            int x1 = r.nextInt(8);
            sol1 = col2[x1];

            int x2 = r.nextInt(8);
            sol2 = col2[x2];

            int x3 = r.nextInt(8);
            sol3 = col2[x3];

            int x4 = r.nextInt(8);
            sol4 = col2[x4];
        }
    }

    /**
     * Método para comprobar si se han pintado todos los botones de la mano
     * @param lista ArrayList con los botones de la mano activa
     * @return True si estan todos rellenos, false en caso contrario
     */
    Boolean comprobarRepeticiones(ArrayList<Button> lista){

        //Se comprueba si hay algun elemento con el tag "negro"
        if(lista.get(0).getTag().toString().equals("negro") || lista.get(1).getTag().toString().equals("negro") ||
                lista.get(2).getTag().toString().equals("negro") || lista.get(3).getTag().toString().equals("negro")) {
            //Se muetra un mensaje advirtiendo para que los relleno todos
            Toast.makeText(getApplicationContext(), "Rellena todas las fichas",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Mñetodo para deshabilitar los botones de la mano
     * @param lista
     */
    void deshabilitarMano(ArrayList<Button> lista){
        //Se recorren todos los elementos y se van deshabilitando
        for(int i=0;i<4;i++){
            lista.get(i).setEnabled(false);
        }
    }

    /**
     * Método para iniciar el tag de todos los botones de todas las manos a "negro"
     */
    void IniciarBotones(){
        Boton1_1.setTag("negro");Boton1_2.setTag("negro");Boton1_3.setTag("negro");Boton1_4.setTag("negro");
        Boton2_1.setTag("negro");Boton2_2.setTag("negro");Boton2_3.setTag("negro");Boton2_4.setTag("negro");
        Boton3_1.setTag("negro");Boton3_2.setTag("negro");Boton3_3.setTag("negro");Boton3_4.setTag("negro");
        Boton4_1.setTag("negro");Boton4_2.setTag("negro");Boton4_3.setTag("negro");Boton4_4.setTag("negro");
        Boton5_1.setTag("negro");Boton5_2.setTag("negro");Boton5_3.setTag("negro");Boton5_4.setTag("negro");
        Boton6_1.setTag("negro");Boton6_2.setTag("negro");Boton6_3.setTag("negro");Boton6_4.setTag("negro");
        Boton7_1.setTag("negro");Boton7_2.setTag("negro");Boton7_3.setTag("negro");Boton7_4.setTag("negro");
        Boton8_1.setTag("negro");Boton8_2.setTag("negro");Boton8_3.setTag("negro");Boton8_4.setTag("negro");
        Boton9_1.setTag("negro");Boton9_2.setTag("negro");Boton9_3.setTag("negro");Boton9_4.setTag("negro");
        Boton10_1.setTag("negro");Boton10_2.setTag("negro");Boton10_3.setTag("negro");Boton10_4.setTag("negro");
        Boton11_1.setTag("negro");Boton11_2.setTag("negro");Boton11_3.setTag("negro");Boton11_4.setTag("negro");
        Boton12_1.setTag("negro");Boton12_2.setTag("negro");Boton12_3.setTag("negro");Boton12_4.setTag("negro");
    }

    /**
     * Método utilizado al pulsar el boton fisico de volver atras
     */
    @Override
    public void onBackPressed() {
        //Suena un mensaje de error
        error.play(flujoError, 1, 1, 0, 0, 1);
    }
}
