package com.appspedro.pedro.tfg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

/**
 * Clase que hereda de ActionBarActivity e implementa OnclickListener y muestra el menu principal con una barra de accion
 * @author Pedro Alcalá Galiano
 */
public class PantallaMenuPrincipal extends ActionBarActivity implements OnClickListener {

    /**
     * Variable para manejar la musica de fondo
     */
    private MediaPlayer musicaFondo;

    /**
     * Variable para gestionar el sonido de los botones
     */
    private SoundPool sonidoBoton;

    /**
     * Variable referente al boton Individual
     */
    private Button unjugador;

    /**
     * Variable referente al boton Batalla
     */
    private Button batalla;

    /**
     * Variable referente al boton Ajustes
     */
    private Button ajustes;

    /**
     * Variable referente al boton Perfil
     */
    private Button perfil;

    /**
     * Variable referente al boton de Facebook
     */
    private LoginButton loginButton;

    /**
     * Variable para gestionar la respuesta del Login de Facebook
     */
    private CallbackManager callbackManager;

    /**
     * Variable para que maneja el ToggleButton de activar y desactivar la musica
     */
    private ToggleButton musica;

    /**
     * Variable que muestra, si la sesion esta iniciada, el nombre del usuario
     */
    private TextView estadoSesion;

    /**
     * Variable que indica si los sonidos estan activados
     */
    private Boolean sonidosActivados;

    /**
     * Variable que indica si la vibracion esta activada
     */
    private Boolean vibracionActivada;

    /**
     * Variable que maneja la vibracion
     */
    private Vibrator vibracion;

    /**
     * Variable que guarda el nombre del usuario activo
     */
    private String usuario = new String();

    /**
     * Variable que gestiona la base de datos del usuario
     */
    private BDPerfilJugador BD;

    /**
     * Variable utilizada tambien para gestionar el sonido de los botones
     */
    private int flujoSonido;

    /**
     * Metodo llamado cuando la actividad es creada
     * @param savedInstanceState Estado anterior de la actividad guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializamos el SDK de Facebook
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //Creamos el callbackManager para manejar las respuestas del login
        callbackManager = CallbackManager.Factory.create();
        //Establece la interfaz de usuario para esta Activity
        setContentView(R.layout.activity_main);
        //Creamos el typeface para asignar la fuente a los textviews de la actividad
        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/Square.ttf");

        //Inicializamos la variable con la musica de fondo, y le ponemos para que se reproduzca en bucle
        musicaFondo = MediaPlayer.create(this,R.raw.ninetysecondsoffunk);
        musicaFondo.setLooping(true);

        //Recuperamos las preferencias guardadas en la aplicacion y las asignamos a las variables
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        sonidosActivados = preferencias.getBoolean("son",true);
        vibracionActivada = preferencias.getBoolean("vib",true);

        //Si el sonido esta activado, reproducimos la musica
        if(sonidosActivados)
            musicaFondo.start();

        //Inicializamos el sonido de los botones
        sonidoBoton = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoSonido = sonidoBoton.load(this, R.raw.botonesmenus, 1);

        //Inicializamos la vibracion
        vibracion = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Asignamos todos los elementos de la interfaz a sus respectivas variables
        loginButton = (LoginButton)findViewById(R.id.login_button);
        unjugador = (Button)findViewById(R.id.boton1jugador);
        ajustes = (Button)findViewById(R.id.botonajustes);
        batalla = (Button)findViewById(R.id.batalla);
        musica = (ToggleButton)findViewById(R.id.musica);
        estadoSesion = (TextView)findViewById(R.id.estadoSesion);
        perfil = (Button)findViewById(R.id.botonPerfil);

        //Cambiamos la fuente de los TextView
        unjugador.setTypeface(fuente);
        batalla.setTypeface(fuente);
        ajustes.setTypeface(fuente);
        perfil.setTypeface(fuente);
        estadoSesion.setTypeface(fuente);

        //Si la sesion esta iniciada mostramos el boton Perfil y el nombre de usuario
        if (AccessToken.getCurrentAccessToken()!=null) {
            perfil.setVisibility(View.VISIBLE);
            ObtenerNombre();
        }else {
            //Si no esta iniciada la sesion mostramos el siguiente texto
            estadoSesion.setText("Sesion no iniciada");
        }

        //Añadimos los botones al Listener para que al ser clickados se ejecute el metodo Onclick
        unjugador.setOnClickListener(this);ajustes.setOnClickListener(this);
        batalla.setOnClickListener(this);musica.setOnClickListener(this);
        perfil.setOnClickListener(this);loginButton.setOnClickListener(this);

        //Creamos esta variable para controlar la desconexion de la sesion
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            /**
             * Metodo llamado cuando cambia el AccesToken
             * @param oldAccessToken El AccesToken antes del cambio
             * @param currentAccessToken El AccesToken actual
             */
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                //Si se ha cerrado la sesion cambia el texto de estado de sesion y desaparece el boton Perfil
                if (currentAccessToken == null){
                    estadoSesion.setText("Sesion no iniciada");
                    perfil.setVisibility(View.INVISIBLE);
                }else {
                    ObtenerNombre();
                }
            }
        };

        //Inicia el rastreo de cambios en el AccessToken
        accessTokenTracker.startTracking();

        /**
         * Registramos la llamada al login a traves del boton de Login
         */
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            /**
             * Metodo que se ejecuta si el login ha sido exitoso
             * @param loginResult Resultado de la operacion del Login
             */
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Se muestra el boton de perfil y se obtiene el nombre de Facebook
                perfil.setVisibility(View.VISIBLE);
                ObtenerNombre();

                //Inicializamos la base de datos
                BD = new BDPerfilJugador(PantallaMenuPrincipal.this);

                String[] usuario = new String[]{loginResult.getAccessToken().getUserId()};
                Boolean nuevo;

                //Guardamos si el jugador que ha iniciado sesion es nuevo
                nuevo = BD.crearJugadorNuevo(usuario);

                //Si no es nuevo comprobamos cuantos dias lleva sin jugar
                if (!nuevo) {
                    //Si lleva mas de una semana mostramos un AlertDialog
                    if (BD.diasSinJugar(usuario) >= 7) {
                        //Inicializamos el AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaMenuPrincipal.this);
                        //Le asignamos un mensaje mostrar
                        builder.setMessage("¡Ha pasado mas de una semana desde la última partida!")
                                .setCancelable(true)
                                        //Añadimos el boton Aceptar que cierra el dialogo al clickarlo
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        //Creamos el AlertDialog y lo mostramos
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                //Si es nuevo registro aparece un cuadro confirmando el registro
                else {
                    //Inicializamos el AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(PantallaMenuPrincipal.this);
                    //Le asignamos un mensaje mostrar
                    builder.setMessage("Te has registrado con éxito")
                            .setCancelable(true)
                                    //Añadimos el boton Aceptar que cierra el dialogo al clickarlo
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creamos el AlertDialog y lo mostramos
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            /**
             * Metodo que se ejecuta cuando se cancela el inicio de sesion
             */
            @Override
            public void onCancel() {
                //Nos muestra un mensaje diciendo que hemos cancelado el inicio de sesion
                Toast.makeText(getApplicationContext(), "Se ha cancelado el inicio de sesion",
                        Toast.LENGTH_SHORT).show();
            }

            /**
             * Metodo que se ejecuta cuando hay un error en el inicio de sesion
             * @param exception Recoge la excepcion ocurrida en el login
             */
            @Override
            public void onError(FacebookException exception) {
                //Nos muestra el mensaje de que se ha producido un error
                Toast.makeText(getApplicationContext(), "Se ha producido un error",
                        Toast.LENGTH_SHORT).show();
            }
        });
}

    /**
     * Metodo para obtener el nombre del usuario que ha iniciado sesion
     */
    private void ObtenerNombre() {

        //Para obtener datos del perfil tenemos que hacer un GraphRequest
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {

                //Obtenemos el nombre del perfil y lo mostramos
                estadoSesion.setText(object.optString("name"));
                usuario = object.optString("name");
            }
        });
        //Añadimos los parametros que hemos requerido y ejecutamos la peticion
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Metodo que gestiona el click en cada uno de los elementos de la interfaz
     * @param v View referente a la Activity
     */
    public void onClick(View v) {
        //Dependiendo del boton pinchando hace unas operaciones u otras
        switch (v.getId()) {
            case R.id.boton1jugador:
                //Paramos la musica de fondo
                musicaFondo.stop();
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if (sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if (vibracionActivada)
                    vibracion.vibrate(200);

                //Comprobamos si hay una sesion iniciada
                if (AccessToken.getCurrentAccessToken() == null) {
                    //Si no hay sesion inicada mostramos una ventana indicando que no se guardan los logros
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Va a jugar como Invitado. Sus logros y resultados no se guardaran")
                            .setCancelable(true)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                //Al pinchar en aceptar iniciamos la actividad juego
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent inten = new Intent(PantallaMenuPrincipal.this, Juego.class);
                                    //A esta actividad le pasamos las variables con el nombre de usuario Invitado y el modo de juego
                                    inten.putExtra("usuario", "Invitado");
                                    inten.putExtra("modoJuego", "individual");
                                    finish();
                                    startActivity(inten);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    //Si hay sesion activa se pasa el nombre del usuario activo y se inicia el juego
                    Intent inten = new Intent(this, Juego.class);
                    inten.putExtra("usuario", usuario);
                    inten.putExtra("modoJuego", "individual");
                    finish();
                    startActivity(inten);
                }
                break;

            case R.id.botonajustes:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if (sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if (vibracionActivada)
                    vibracion.vibrate(200);
                musicaFondo.stop();
                finish();
                //Iniciamos la actividad Ajustes
                Intent intent = new Intent(this, Ajustes.class);
                startActivity(intent);
                break;

            case R.id.batalla:
                musicaFondo.stop();
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if (sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if (vibracionActivada)
                    vibracion.vibrate(200);

                //Creamos un AlertDialog para guardar los nombre de jugadores
                AlertDialog.Builder jugadores = new AlertDialog.Builder(this);
                jugadores.setTitle("Nombre de los jugadores");

                //Creamos los EditText para meter los nombres
                final TextView j1 = new TextView(this);
                j1.setText("Jugador 1:");
                final TextView j2 = new TextView(this);
                j2.setText("Jugador 2:");
                final EditText jugador1 = new EditText(this);
                //Los asignamos a variables para guardarlos
                jugador1.setInputType(InputType.TYPE_CLASS_TEXT);
                final EditText jugador2 = new EditText(this);
                jugador2.setInputType(InputType.TYPE_CLASS_TEXT);

                //Como hemos tenido que meter varios elementos tenemos que crear un Layout personalizado para la ventana
                LinearLayout ventana = new LinearLayout(this);
                ventana.setOrientation(LinearLayout.VERTICAL);
                ventana.addView(j1);
                ventana.addView(jugador1);
                ventana.addView(j2);
                ventana.addView(jugador2);
                //Le añadimos todos los elementos y se lo asignamos al AlertDialog
                jugadores.setView(ventana);

                //Creamos el boton Entrar
                jugadores.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
                    /**
                     * Metodo que se ejecuta cuando pulsamos Entrar
                     * @param dialog Dialogo que recibe el click
                     * @param which Boton que fue pinchado
                     */
                    @Override
                     public void onClick(DialogInterface dialog, int which) {
                        //Sonido al pinchar
                        sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                        //Creamos un objeto de la clase PartidaBatalla con los nombres registrados anteriormente
                        String jug1 = jugador1.getText().toString();
                        String jug2 = jugador2.getText().toString();

                        PartidaBatalla partidaBatalla = new PartidaBatalla(jug1, jug2);

                        Intent inten2 = new Intent(PantallaMenuPrincipal.this, PantallaSeleccionCombinacion.class);

                        //Pasamos el modo de juego y el objeto PartidaBatalla al Intent
                        inten2.putExtra("partida", partidaBatalla);
                        inten2.putExtra("modoJuego", "batalla");

                        //Comprobamos que se han escrito los dos nombre he iniciamos la Activity Juego
                        if (!jug1.equals("") && !jug2.equals("")) {
                            musicaFondo.stop();
                            finish();
                            startActivity(inten2);
                        } else {
                            //Si no hemos puestro los dos nombre mostramos una alerta por pantalla
                            Toast.makeText(getApplicationContext(), "Escribe los dos nombres", Toast.LENGTH_SHORT).show();
                        }
                     }
                });
                //Creamos el boton Cancelar
                jugadores.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    /**
                     * Metodo que se ejecuta cuando pulsamos Entrar
                     * @param dialog Dialogo que recibe el click
                     * @param which Boton que fue pinchado
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se cierra el dialogo
                        dialog.cancel();
                    }
                });

                jugadores.show();
                break;

            case R.id.botonPerfil:
                //Dependiendo de las preferencia activamos el sonido y la vibracion
                if (sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if (vibracionActivada)
                    vibracion.vibrate(200);
                //Abrimos la pantalla Perfil
                Intent inten3 = new Intent(this, PantallaPerfil.class);
                startActivity(inten3);
                break;

            case R.id.musica:
                //Comprobamos si esta activado el ToggleButton para activar o desactivar la musica
                if (musica.isChecked()) {
                    musicaFondo.pause();
                } else {
                    musicaFondo.start();
                }
                break;
        }
    }

    /**
     * Metodo que se ejecuta al volver de la pagina del Login
     * @param requestCode Identificador de donde viene el result
     * @param resultCode Codigo devuelto por la Activity de la que vuelve
     * @param data Intent con los datos devueltos de la Activity de la que vuelve
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Reenvia al callbackManager el resultado del login
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (AccessToken.getCurrentAccessToken() == null)
            estadoSesion.setText("Sesion no iniciada");
    }

    /**
     * Metodo que crea el menua a partir de menu_main.xml
     * @param menu Variable Menu que se le asigna el menu
     * @return True
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Se obtiene los elementos del menu del archivo XML
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Metodo que actua segun el elemento del menu seleccionado
     * @param item Elemento del menu seleccionado
     * @return True si se ha seleccionado un elemento
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Obtenemos el id del elemento de menu seleccionado
        int id = item.getItemId();

        if (id == R.id.acerca_de) {
            //Si el se pulsa Acerca de aparece una Ventana con informacion
            AlertDialog.Builder acerca = new AlertDialog.Builder(this);
            acerca.setTitle("Acerca de");
            acerca.setMessage("TFG realizado por el alumno Pedro Alcala Galiano")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        /**
                         * Metodo que se ejecuta cuando pulsamos Entrar
                         * @param dialog Dialogo que recibe el click
                         * @param id Boton que fue pinchado
                         */
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = acerca.create();
            alert.show();

            return true;
        } else if (id == R.id.salir) {
            //Si se pulsa el boto salir se desconecta la sesion y se cierra la aplicacion
            if (AccessToken.getCurrentAccessToken() != null)
                LoginManager.getInstance().logOut();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
