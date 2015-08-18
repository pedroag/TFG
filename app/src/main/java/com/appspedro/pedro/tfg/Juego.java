package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


public class Juego extends Activity implements View.OnClickListener {

    private Chronometer tiempo;

    private MediaPlayer musica;
    private SoundPool sonidoColor,error,sonidoMenu;
    private int flujoColor,flujoError,flujoMenu;
    private String usuario;
    private String modoJuego;
    private TextView idusuario,acie1,aprox1,acie2,aprox2,acie3,aprox3,acie4,aprox4,acie5,aprox5,acie6,aprox6,acie7,aprox7,acie8,aprox8;
    private TextView acie9,aprox9,acie10,aprox10,acie11,aprox11,acie12,aprox12,acie13,aprox13,acie14,aprox14,acie15,aprox15;
    private PartidaBatalla partidaBatalla;

    private String coloractivo = "rojo";
    private RadioButton azul,amarillo,rojo,verde,rosa,naranja;
    private Button BotonInicio,BotonRonda,BotonSalir;
    private ScrollView sv;
    private Drawable drw;

    private Button Boton1_1,Boton1_2,Boton1_3,Boton1_4,Boton2_1,Boton2_2,Boton2_3,Boton2_4;
    private Button Boton3_1,Boton3_2,Boton3_3,Boton3_4,Boton4_1,Boton4_2,Boton4_3,Boton4_4;
    private Button Boton5_1,Boton5_2,Boton5_3,Boton5_4,Boton6_1,Boton6_2,Boton6_3,Boton6_4;
    private Button Boton7_1,Boton7_2,Boton7_3,Boton7_4,Boton8_1,Boton8_2,Boton8_3,Boton8_4;
    private Button Boton9_1,Boton9_2,Boton9_3,Boton9_4,Boton10_1,Boton10_2,Boton10_3,Boton10_4;
    private Button Boton11_1,Boton11_2,Boton11_3,Boton11_4,Boton12_1,Boton12_2,Boton12_3,Boton12_4;
    private Button Boton13_1,Boton13_2,Boton13_3,Boton13_4,Boton14_1,Boton14_2,Boton14_3,Boton14_4;
    private Button Boton15_1,Boton15_2,Boton15_3,Boton15_4;
    private ToggleButton toggleMusica;

    private Vibrator vibracion;
    private Boolean sonidosActivados,vibracionActivada;

    private String sol1,sol2,sol3,sol4;
    private RelativeLayout mano1,mano2,mano3,mano4,mano5,mano6,mano7,mano8,mano9,mano10,mano11,mano12,mano13,mano14,mano15;
    private RelativeLayout pmano1,pmano2,pmano3,pmano4,pmano5,pmano6,pmano7,pmano8,pmano9,pmano10,pmano11,pmano12,pmano13,pmano14,pmano15;
    private ArrayList<Button> posible;
    private ArrayList<String> solucion;
    private int jugadas=1;
    private String dificultad;

    private PerfilJugador BD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        dificultad = preferencias.getString("dif", "facil");
        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);


        musica = MediaPlayer.create(this, R.raw.popmetal);
        musica.setLooping(true);

        sonidoMenu = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        flujoMenu = sonidoMenu.load(this,R.raw.botonesmenus,1);
        sonidoColor = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoColor = sonidoColor.load(this, R.raw.pulsacolor, 1);
        error = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoError = error.load(this, R.raw.error, 1);

        drw = ContextCompat.getDrawable(this, R.drawable.ficha_roja);


        modoJuego = getIntent().getStringExtra("modoJuego");
        usuario = getIntent().getStringExtra("usuario");

        vibracion = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sv = (ScrollView) this.findViewById(R.id.scrollView);
        sv.setVerticalScrollBarEnabled(false);

        solucion = new ArrayList<String>();
        posible = new ArrayList<Button>();

        idusuario = (TextView) findViewById(R.id.idjugador);
        tiempo = (Chronometer) findViewById(R.id.tiempo);

        mano1 = (RelativeLayout)findViewById(R.id.mano1);mano2 = (RelativeLayout)findViewById(R.id.mano2);
        mano3 = (RelativeLayout)findViewById(R.id.mano3);mano4 = (RelativeLayout)findViewById(R.id.mano4);
        mano5 = (RelativeLayout)findViewById(R.id.mano5);mano6 = (RelativeLayout)findViewById(R.id.mano6);
        mano7 = (RelativeLayout)findViewById(R.id.mano7);mano8 = (RelativeLayout)findViewById(R.id.mano8);
        mano9 = (RelativeLayout)findViewById(R.id.mano9);mano10 = (RelativeLayout)findViewById(R.id.mano10);
        mano11 = (RelativeLayout)findViewById(R.id.mano11);mano12 = (RelativeLayout)findViewById(R.id.mano12);
        mano13 = (RelativeLayout)findViewById(R.id.mano13);mano14 = (RelativeLayout)findViewById(R.id.mano14);
        mano15 = (RelativeLayout)findViewById(R.id.mano15);

        toggleMusica = (ToggleButton)findViewById(R.id.musica);

        pmano1 = (RelativeLayout)findViewById(R.id.Pmano1);pmano2 = (RelativeLayout)findViewById(R.id.Pmano2);
        pmano3 = (RelativeLayout)findViewById(R.id.Pmano3);pmano4 = (RelativeLayout)findViewById(R.id.Pmano4);
        pmano5 = (RelativeLayout)findViewById(R.id.Pmano5);pmano6 = (RelativeLayout)findViewById(R.id.Pmano6);
        pmano7 = (RelativeLayout)findViewById(R.id.Pmano7);pmano8 = (RelativeLayout)findViewById(R.id.Pmano8);
        pmano9 = (RelativeLayout)findViewById(R.id.Pmano9);pmano10 = (RelativeLayout)findViewById(R.id.Pmano10);
        pmano11 = (RelativeLayout)findViewById(R.id.Pmano11);pmano12 = (RelativeLayout)findViewById(R.id.Pmano12);
        pmano13 = (RelativeLayout)findViewById(R.id.Pmano13);pmano14 = (RelativeLayout)findViewById(R.id.Pmano14);
        pmano15 = (RelativeLayout)findViewById(R.id.Pmano15);

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
        Boton13_1 = (Button)findViewById(R.id.button49);Boton13_2 = (Button)findViewById(R.id.button51);
        Boton13_3 = (Button)findViewById(R.id.button50);Boton13_4 = (Button)findViewById(R.id.button52);
        Boton14_1 = (Button)findViewById(R.id.button53);Boton14_2 = (Button)findViewById(R.id.button55);
        Boton14_3 = (Button)findViewById(R.id.button54);Boton14_4 = (Button)findViewById(R.id.button56);
        Boton15_1 = (Button)findViewById(R.id.button61);Boton15_2 = (Button)findViewById(R.id.button63);
        Boton15_3 = (Button)findViewById(R.id.button62);Boton15_4 = (Button)findViewById(R.id.button64);

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
        acie13 = (TextView)findViewById(R.id.acie13);aprox13 = (TextView)findViewById(R.id.aprox13);
        acie14 = (TextView)findViewById(R.id.acie14);aprox14 = (TextView)findViewById(R.id.aprox14);
        acie15 = (TextView)findViewById(R.id.acie15);aprox15 = (TextView)findViewById(R.id.aprox15);

        azul = (RadioButton)findViewById(R.id.radioazul);amarillo = (RadioButton)findViewById(R.id.radioamarillo);
        rojo = (RadioButton)findViewById(R.id.radiorojo);verde = (RadioButton)findViewById(R.id.radioverde);
        rosa = (RadioButton)findViewById(R.id.radiorosa);naranja = (RadioButton)findViewById(R.id.radionaranja);

        IniciarBotones();

        azul.setOnClickListener(this);amarillo.setOnClickListener(this);rojo.setOnClickListener(this);
        verde.setOnClickListener(this);rosa.setOnClickListener(this);naranja.setOnClickListener(this);

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
        Boton13_1.setOnClickListener(this);Boton13_2.setOnClickListener(this);
        Boton13_3.setOnClickListener(this);Boton13_4.setOnClickListener(this);
        Boton14_1.setOnClickListener(this);Boton14_2.setOnClickListener(this);
        Boton14_3.setOnClickListener(this);Boton14_4.setOnClickListener(this);
        Boton15_1.setOnClickListener(this);Boton15_2.setOnClickListener(this);
        Boton15_3.setOnClickListener(this);Boton15_4.setOnClickListener(this);

        gestionarDificultad();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.botoninicio:

                if(sonidosActivados)
                    musica.start();
                sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);

                tiempo.setBase(SystemClock.elapsedRealtime());
                tiempo.start();

                BotonInicio.setEnabled(false);
                BotonRonda.setEnabled(true);

                Boton1_1.setEnabled(true);
                Boton1_2.setEnabled(true);
                Boton1_3.setEnabled(true);
                Boton1_4.setEnabled(true);
                break;
            case R.id.botonronda:
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);
                JugarMano();

                break;
            case R.id.salir:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.musica:
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
            case R.id.button49:case R.id.button50:case R.id.button51:case R.id.button52:case R.id.button53:case R.id.button54:
            case R.id.button55:case R.id.button56:case R.id.button61:case R.id.button62:case R.id.button63:case R.id.button64:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(100);
                v.setBackground(drw);
                v.setTag(coloractivo);
                break;
            case R.id.radioamarillo:
                if(sonidosActivados)
                    sonidoColor.play(flujoColor, 1, 1, 0, 0, 1);
                drw=getResources().getDrawable(R.drawable.ficha_amarilla);
                coloractivo="amarillo";
                break;
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
        }
    }

    private void gestionarDificultad(){
        if (modoJuego.equals("batalla")) {
            partidaBatalla = getIntent().getParcelableExtra("partida");
            sol1 = getIntent().getStringExtra("color1");
            sol2 = getIntent().getStringExtra("color2");
            sol3 = getIntent().getStringExtra("color3");
            sol4 = getIntent().getStringExtra("color4");
            if(partidaBatalla.getTurno()==1)
                idusuario.setText(partidaBatalla.getJugador2());
            else
                idusuario.setText(partidaBatalla.getJugador1());

        } else if (modoJuego.equals("individual")) {

            idusuario.setText(usuario);

            if(AccessToken.getCurrentAccessToken()!=null){
                BD = new PerfilJugador(this, "BDPerfiles", null, 1);
                SQLiteDatabase bd = BD.getReadableDatabase();

                String[] arg = new String[] {AccessToken.getCurrentAccessToken().getUserId()};
                Cursor c = bd.rawQuery(" SELECT * FROM jugadores WHERE id_usuario=?", arg);
                c.moveToFirst();

                if(c.getString(1).equals("Novato")){
                    dificultad = "facil";
                }else if(c.getString(1).equals("Amateur")){
                    dificultad = "medio";
                }else if(c.getString(1).equals("Profesional")) {
                    dificultad = "dificil";
                }else if(c.getString(1).equals("Experto")) {
                    dificultad = "muydificil";
                }
                GeneraSolucion(dificultad);

            }else{
                GeneraSolucion(dificultad);
            }
        }
        solucion.add(sol1);solucion.add(sol2);solucion.add(sol3);solucion.add(sol4);
    }


    private void JugarMano(){

        switch (jugadas){
            case 1:
                mano1.setEnabled(true);
                posible.clear();
                posible.add(Boton1_1);posible.add(Boton1_2);posible.add(Boton1_3);posible.add(Boton1_4);

                if (!comprobarRepeticiones(posible))
                    break;

                acie1.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox1.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano1.setVisibility(View.VISIBLE);
                mano2.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 2:
                mano2.setEnabled(true);
                posible.clear();
                posible.add(Boton2_1);posible.add(Boton2_2);posible.add(Boton2_3);posible.add(Boton2_4);

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

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

                if (!comprobarRepeticiones(posible))
                    break;

                acie12.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox12.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano12.setVisibility(View.VISIBLE);
                mano13.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 13:
                mano13.setEnabled(true);
                posible.clear();
                posible.add(Boton13_1);posible.add(Boton13_2);posible.add(Boton13_3);posible.add(Boton13_4);

                if (!comprobarRepeticiones(posible))
                    break;

                acie13.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox13.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano13.setVisibility(View.VISIBLE);
                mano14.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 14:
                mano14.setEnabled(true);
                posible.clear();
                posible.add(Boton14_1);posible.add(Boton14_2);posible.add(Boton14_3);posible.add(Boton14_4);

                if (!comprobarRepeticiones(posible))
                    break;

                acie14.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox14.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano14.setVisibility(View.VISIBLE);
                mano15.setVisibility(View.VISIBLE);
                jugadas++;
                break;
            case 15:
                mano15.setEnabled(true);
                posible.clear();
                posible.add(Boton15_1);posible.add(Boton15_2);posible.add(Boton15_3);posible.add(Boton15_4);

                if (!comprobarRepeticiones(posible))
                    break;

                acie15.setText(Integer.toString(ComprobarAciertos(posible)));
                aprox15.setText(Integer.toString(ComprobarAproximaciones(posible)));

                if(Integer.toString(ComprobarAciertos(posible)).equals("4")) {
                    generarVictoria();
                    break;
                }

                deshabilitarMano(posible);

                pmano15.setVisibility(View.VISIBLE);

                generarDerrota();
                jugadas++;
                break;
        }

    }

    private void generarVictoria() {

        Intent intentIndividual = new Intent(this,PantallaVictoria.class);
        intentIndividual.putExtra("color1", sol1);
        intentIndividual.putExtra("color2", sol2);
        intentIndividual.putExtra("color3", sol3);
        intentIndividual.putExtra("color4", sol4);
        intentIndividual.putExtra("usuario", usuario);
        intentIndividual.putExtra("jugadas", jugadas);
        intentIndividual.putExtra("tiempo", tiempo.getText().toString());

        if (modoJuego.equals("individual")) {
            musica.stop();
            tiempo.stop();
            finish();
            startActivity(intentIndividual);
        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 1) {
            partidaBatalla.setNumjugadas1(jugadas);
            partidaBatalla.setTiempo1(tiempo.getText().toString());
            partidaBatalla.setTurno(2);
            Intent intent1jugador = new Intent(this, SeleccionCombinacion.class);
            intent1jugador.putExtra("partida", partidaBatalla);
            finish();
            startActivity(intent1jugador);
        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 2) {
            partidaBatalla.setNumjugadas2(jugadas);
            partidaBatalla.setTiempo2(tiempo.getText().toString());
            Intent intent2jugador = new Intent(this, ResultadoBatalla.class);
            intent2jugador.putExtra("partida", partidaBatalla);
            finish();
            startActivity(intent2jugador);
        }
    }

    private void generarDerrota(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Derrota");
        builder.setMessage("No has conseguido adivinar la combinacion secreta")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (modoJuego.equals("individual")) {
                            PerfilJugador BD = new PerfilJugador(Juego.this, "BDPerfiles", null, 1);
                            SQLiteDatabase bd = BD.getWritableDatabase();

                            if (bd != null && AccessToken.getCurrentAccessToken() != null) {
                                String[] arg = new String[]{AccessToken.getCurrentAccessToken().getUserId()};
                                bd.execSQL("UPDATE jugadores SET partidas_jugadas=partidas_jugadas+1 WHERE  id_usuario=?", arg);
                            }
                            finish();
                            startActivity(new Intent(Juego.this, MainActivity.class));
                        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 1) {
                            partidaBatalla.setTiempo1("derrota");
                            partidaBatalla.setNumjugadas1(jugadas);
                            partidaBatalla.setTurno(2);
                            Intent intent1jugador = new Intent(Juego.this, SeleccionCombinacion.class);
                            intent1jugador.putExtra("partida", partidaBatalla);
                        } else if (modoJuego.equals("batalla") && partidaBatalla.getTurno() == 2) {
                            partidaBatalla.setNumjugadas2(jugadas);
                            partidaBatalla.setTiempo2("derrota");
                            Intent intent2jugador = new Intent(Juego.this, ResultadoBatalla.class);
                            intent2jugador.putExtra("partida", partidaBatalla);
                            startActivity(intent2jugador);
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private int ComprobarAciertos(ArrayList<Button> posible){

        int acertados=0;

        for (int i=0;i<posible.size();i++) {
            if (solucion.get(i).equals(posible.get(i).getTag().toString()))
                acertados++;
        }
        return acertados;
    }

    private int ComprobarAproximaciones(ArrayList<Button> posible){

        int aproximaciones=0;
        Boolean [] bol = {false,false,false,false};

        for (int i=0;i<posible.size();i++)
        {
            if (!solucion.get(i).equals(posible.get(i).getTag().toString()))
            {
                for (int j=0;j<posible.size();j++)
                {
                    if (solucion.get(i).equals(posible.get(j).getTag().toString()) && !bol[j])
                    {
                        aproximaciones++;
                        bol[j]=true;
                        break;
                    }
                }
            }else{
                bol[i]=true;
            }

        }
        return aproximaciones;

    }

    void GeneraSolucion(String dificultad){
        String [] col = {"amarillo","rojo","verde","azul","naranja","rosa"};
        Random  r = new Random();

        if(dificultad.equals("facil")) {

            int x1 = r.nextInt(6);
            sol1 = col[x1];

            int x2 = r.nextInt(6);
            sol2 = col[x2];

            int x3 = r.nextInt(6);
            sol3 = col[x3];

            int x4 = r.nextInt(6);
            sol4 = col[x4];

        }else{

            int p;
            ArrayList<Integer>colores = new ArrayList();
            for (int i = 0; i < 4; i++) {
                p = r.nextInt(6);
                while (colores.contains(p)) {
                    p = r.nextInt(6);
                }
                colores.add(p);
            }
            sol1 = col[colores.get(0)];
            sol2 = col[colores.get(1)];
            sol3 = col[colores.get(2)];
            sol4 = col[colores.get(3)];

        }

    }

    Boolean comprobarRepeticiones(ArrayList<Button> lista){

        if(posible.get(0).getTag().toString().equals("negro") || posible.get(1).getTag().toString().equals("negro") ||
                posible.get(2).getTag().toString().equals("negro") || posible.get(3).getTag().toString().equals("negro")) {
            Toast.makeText(getApplicationContext(), "Rellena todas las fichas",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(dificultad.equals("dificil") && modoJuego.equals("individual")){
            if(posible.get(0).getTag().toString().equals(posible.get(1).getTag().toString())&&posible.get(0).getTag().toString().equals(posible.get(2).getTag().toString())&&
                    posible.get(0).getTag().toString().equals(posible.get(3).getTag().toString())) {
                Toast.makeText(getApplicationContext(), "No puedes utilizar el mismo color para todas",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if(dificultad.equals("muydificil") && modoJuego.equals("individual")){
            int cont;
            for(int i=0;i<4;i++){
                cont=0;
                for(int j=0;j<4;j++) {
                    if (lista.get(i).getTag().toString().equals(lista.get(j).getTag().toString()) && i != j)
                        cont++;
                    if (cont >= 2) {
                        Toast.makeText(getApplicationContext(), "No puedes repetir mas de dos veces el color", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    void deshabilitarMano(ArrayList<Button> lista){
        for(int i=0;i<4;i++){
            lista.get(i).setEnabled(false);
        }
    }

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
        Boton13_1.setTag("negro");Boton13_2.setTag("negro");Boton13_3.setTag("negro");Boton13_4.setTag("negro");
        Boton14_1.setTag("negro");Boton14_2.setTag("negro");Boton14_3.setTag("negro");Boton14_4.setTag("negro");
    }

    @Override
    public void onBackPressed() {
        error.play(flujoError, 1, 1, 0, 0, 1);
    }
}
