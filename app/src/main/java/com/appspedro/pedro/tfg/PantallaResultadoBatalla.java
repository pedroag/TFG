package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Clase PantallaResultadoBatalla que hereda de Activity e implementa View.OnclickListener que muestra resultados de Batalla
 * @author Pedro Alcalá Galiano
 */
public class PantallaResultadoBatalla extends Activity implements OnClickListener{

    /**
     * Variable que guarda un objeto de la clase PartidaBatalla con toda la informacion referente
     */
    private PartidaBatalla partidaBatalla;

    /**
     * Variables TextView que muestran los distintos textos en pantalla
     */
    private TextView jugador1,jugador2,resultado1,resultado2,ganador;

    /**
     * Variables Button referente al boton salir
     */
    private Button salir;

    /**
     * Variable Button rerente al boton repetir
     */
    private Button repetir;

    /**
     * Variable que indica si los sonidos estan activados
     */
    private Boolean sonidosActivados;

    /**
     * Variable que indica si la vibracion esta activada
     */
    private Boolean vibracionActivada;

    /**
     * Variable Vibrator para manejar la vibracion de la activity
     */
    private Vibrator vibrador;

    /**
     * Variable SoundPool para gestionar el sonido al pulsar los botones
     */
    private SoundPool sonidoMenu;

    /**
     * Variable int utilizada tambien para gestionar el sonido de los botones
     */
    private int flujoMenu;


    /**
     * Metodo llamado cuando la actividad es creada
     * @param savedInstanceState Estado anterior de la actividad guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Establece la interfaz de usuario para esta Activity
        setContentView(R.layout.activity_resultado_batalla);

        //Descargamos las preferencias guardadas en la clase Ajustes y las asignamos a variables
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);

        //Creamos el typeface para asignar la fuente a los textviews de la actividad
        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/Square.ttf");

        //Creamos el objecto con el que manejamos el sonido al pulsar los botones
        sonidoMenu = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoMenu = sonidoMenu.load(this, R.raw.botonesmenus, 1);

        //Creamos el objecto con el que reproducimos musica al crear esta actividad
        MediaPlayer mp = MediaPlayer.create(this, R.raw.victory);
        mp.start();

        //Inicializamos el objeto encargado de la vibracion
        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Recuperamos la variable partida de la actividad anterior y la guardamos
        partidaBatalla = getIntent().getParcelableExtra("partida");

        //Asignamos las distintas variables a los elementos de la interfaz de usuario
        jugador1 = (TextView)findViewById(R.id.jugador1);
        jugador2 = (TextView)findViewById(R.id.jugador2);
        ganador = (TextView)findViewById(R.id.ganador);
        resultado1 = (TextView)findViewById(R.id.resultado1);
        resultado2 = (TextView)findViewById(R.id.resultado2);
        salir = (Button)findViewById(R.id.salir);
        repetir = (Button)findViewById(R.id.repetir);

        //Cambiamos la fuente de los elementos de la interfaz de usuario
        jugador2.setTypeface(fuente);jugador1.setTypeface(fuente);ganador.setTypeface(fuente);
        resultado2.setTypeface(fuente);resultado1.setTypeface(fuente);salir.setTypeface(fuente);repetir.setTypeface(fuente);

        //Asignamos el texto de los nombres de los jugadores a los elementos correspondientes para que se vean en pantalla
        jugador1.setText(partidaBatalla.getJugador1()+":");
        jugador2.setText(partidaBatalla.getJugador2()+":");

        //Llamamos al metodo para comprobar si alguno de los jugadores ha perdido
        gestionarDerrotas(partidaBatalla);
        //Llamamos al metodo para mostrar el ganador
        comprobarResultados();

        //Añadimos los botones al Listener para que se registre cuando son pulsados
        salir.setOnClickListener(this);
        repetir.setOnClickListener(this);


    }

    /**
     * Clase que muestra el texto "Derrota" cuando ha perdido algun jugador
     * @param pb PartidaBatalla con el resultado de los jugadores
     */
    private void gestionarDerrotas(PartidaBatalla pb){
        if (!pb.getTiempo1().equals("derrota")) {
            resultado1.setText(pb.getNumjugadas1() + " jugadas en: " + pb.getTiempo1());
        }else{
            resultado1.setText("Derrota");
        }
        if (!pb.getTiempo2().equals("derrota")) {
            resultado2.setText(pb.getNumjugadas2() + " jugadas en: " + pb.getTiempo2());
        }else{
            resultado2.setText("Derrota");
        }
    }

    /**
     * Clase que decide quien es el ganador dependiendo del numero de jugadas y el tiempo de los jugadores
     */
    private void comprobarResultados(){
        //Compruebo si algunos de los dos tiene menos jugadas
        if(partidaBatalla.getNumjugadas1()<partidaBatalla.getNumjugadas2()){
            ganador.setText(partidaBatalla.getJugador1());
        }else if(partidaBatalla.getNumjugadas1()>partidaBatalla.getNumjugadas2()){
            ganador.setText(partidaBatalla.getJugador2());
        }else if(partidaBatalla.getNumjugadas1()==partidaBatalla.getNumjugadas2() && partidaBatalla.getNumjugadas1()<13){
            //Tiene las mismas jugadas y ninguno de los dos ha perdido, por lo que cogemos los tiempos y los dividimos en un
            //vector con minutos y segundos
            String[] tiempo1 = partidaBatalla.getTiempo1().split(":");
            String[] tiempo2 = partidaBatalla.getTiempo2().split(":");
            //Comprobamos los minutos
            if(Integer.parseInt(tiempo1[0])<Integer.parseInt(tiempo2[0])){
                ganador.setText(partidaBatalla.getJugador1());
            }else if(Integer.parseInt(tiempo1[0])>Integer.parseInt(tiempo2[0])){
                ganador.setText(partidaBatalla.getJugador2());
            }else if(Integer.parseInt(tiempo1[0])==Integer.parseInt(tiempo2[0])){
                //Comprobamos los segundos
                if (Integer.parseInt(tiempo1[1])<Integer.parseInt(tiempo2[1])){
                    ganador.setText(partidaBatalla.getJugador1());
                }else if(Integer.parseInt(tiempo1[1])>Integer.parseInt(tiempo2[1])){
                    ganador.setText(partidaBatalla.getJugador2());
                }else if(Integer.parseInt(tiempo1[1])==Integer.parseInt(tiempo2[1])){
                    ganador.setText("¡EMPATE!");
                }
            }
        }else if(partidaBatalla.getNumjugadas1()==partidaBatalla.getNumjugadas2() && partidaBatalla.getNumjugadas1()==13){
            //Tienen todos los dato iguales
            ganador.setText("¡EMPATE!");
        }
    }

    /**
     * Metodo que gestiona el click en cada uno de los elementos de la interfaz
     * @param v View referente a la Activity
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.salir:
                //Dependiendo de las preferencias activamos el sonido y la vibración
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                //Finalizamos la actividad e iniciamos PantallaMenuPrincipal
                finish();
                startActivity(new Intent(this, PantallaMenuPrincipal.class));
                break;
            case R.id.repetir:
                //Dependiendo de las preferencias activamos el sonido y la vibración
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                //Creamos un objeto PartidaBatlla con los mismos nombre de jugadores
                PartidaBatalla pB = new PartidaBatalla(partidaBatalla.getJugador1(),partidaBatalla.getJugador2());
                Intent i =  new Intent(this, PantallaSeleccionCombinacion.class);
                //Añado al Intent la variable PartidaBatalla para pasar el objeto a la Activity PantallaSeleccionCombinacion
                i.putExtra("partida", pB);
                finish();
                startActivity(i);

        }
    }

}
