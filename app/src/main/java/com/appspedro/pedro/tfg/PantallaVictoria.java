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
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

/**
 * Clase PantallaVictoria que hereda de Activity e implementa OnclickListener y que muestra los resultados de la partida ganada y permite
 * compartirlos en Facebook
 * @author Pedro Alcalá Galiano
 */
public class PantallaVictoria extends Activity implements OnClickListener {

    /**
     * Variable que guarda el nombre del usuario de la partida
     */
    private String usuario;

    /**
     * Variable que guarda el tiempo de la partida ganada
     */
    private String tiempo;

    /**
     * Variable que guarda el numero de jugadas obtendidas en la partida
     */
    private int jugadas;

    /**
     * Variable que guarda el Drawable referente al color
     */
    private Drawable amarillo,azul,naranja,rojo,rosa,verde,morado,celeste;

    /**
     * Variable referente al boton de la solucion
     */
    private Button boton1,boton2,boton3,boton4;

    /**
     * Variable para gestionar la respuesta del share de Facebook
     */
    private CallbackManager callbackManager;

    /**
     * Variable para gestionar la ventana para compartir
     */
    private ShareDialog shareDialog;

    /**
     * Variable referente al boton compartir
     */
    private ShareButton shareButton;

    /**
     * Variable para gestionar la base de datos de los jugadores
     */
    private BDPerfilJugador BD;

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
    private SoundPool sonidoMenu;

    /**
     * Variable utilizada tambien para gestionar el sonido de los botones
     */
    private int flujoMenu;

    /**
     * Variable que maneja la vibracion
     */
    private Vibrator vibrador;

    /**
     * Metodo llamado cuando la actividad es creada
     * @param savedInstanceState Estado anterior de la actividad guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Establece la interfaz de usuario para esta Activity
        setContentView(R.layout.activity_pantalla_victoria);
        //Inicializamos el SDK de Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Creamos el callbackManager para manejar las respuestas del share
        callbackManager = CallbackManager.Factory.create();
        //Inicalizamos la variable
        shareDialog = new ShareDialog(this);

        //Recuperamos las preferencias guardadas en la aplicacion y las asignamos a las variables
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);

        //Inicializamos el sonido de los botones
        sonidoMenu = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoMenu = sonidoMenu.load(this, R.raw.botonesmenus, 1);

        //Inicializamos la vibracion
        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Asignamos el shareButton al boton compartir de la interfaz
        shareButton = (ShareButton)findViewById(R.id.compartir);

        //Inicializamos los drawbles de los diferentes colores posibles, cada uno con su archivo xml
        amarillo = ContextCompat.getDrawable(this, R.drawable.ficha_amarilla);
        azul = ContextCompat.getDrawable(this, R.drawable.ficha_azul);
        naranja = ContextCompat.getDrawable(this, R.drawable.ficha_naranja);
        rojo = ContextCompat.getDrawable(this, R.drawable.ficha_roja);
        rosa = ContextCompat.getDrawable(this, R.drawable.ficha_rosa);
        verde = ContextCompat.getDrawable(this, R.drawable.ficha_verde);
        morado = ContextCompat.getDrawable(this, R.drawable.ficha_morada);
        celeste = ContextCompat.getDrawable(this, R.drawable.ficha_celeste);

        //Inicializamos y reproducimos la musica de victoria al entrar en la Activity
        MediaPlayer mp = MediaPlayer.create(this, R.raw.victory);
        mp.start();

        //Recuperamos el valor del usuario, jugadas y tiempo obtenido en la Activity juego.
        usuario = getIntent().getStringExtra("usuario");
        jugadas = getIntent().getIntExtra("jugadas", 0);
        tiempo = getIntent().getStringExtra("tiempo");

        //Recumperamos el colores de los botones de la solucion del juego
        String color1 = getIntent().getStringExtra("color1");
        String color2 = getIntent().getStringExtra("color3");
        String color3 = getIntent().getStringExtra("color2");
        String color4 = getIntent().getStringExtra("color4");

        //Asignamos cada boton de la solucion con los de la interfaz
        boton1 = (Button) findViewById(R.id.Boton1);
        boton2 = (Button) findViewById(R.id.Boton2);
        boton3 = (Button) findViewById(R.id.Boton3);
        boton4 = (Button) findViewById(R.id.Boton4);

        //Asignamos el botonSalir al de la interfaz
        Button botonSalir = (Button) findViewById(R.id.BotonSalir);

        //Asignamos los elementos de la interfaz
        TextView idusuario = (TextView) findViewById(R.id.idusuario);
        TextView enhorabuena = (TextView) findViewById(R.id.Enhorabuena);
        TextView textResJug = (TextView) findViewById(R.id.textJugadas);
        TextView textResTiempo = (TextView) findViewById(R.id.textTiemo);
        TextView textJugadas = (TextView) findViewById(R.id.textJugadasFinal);
        TextView textTiempo = (TextView) findViewById(R.id.textTiempoFinal);

        //Cambiamos la fuente de los Textview
        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/Square.ttf");
        enhorabuena.setTypeface(fuente);
        textResJug.setTypeface(fuente);
        textResTiempo.setTypeface(fuente);

        //Pintamos los botones segun la solucion obtenida en el jugo
        pintarResultado(color1, color2, color3, color4);

        //Si ha jugado sin iniciar sesion no se muestra el boton compartir
        if(usuario.equals("Invitado"))
            shareButton.setVisibility(View.INVISIBLE);

        //Añadimos los botones al Listener para que al ser clickados se ejecute el metodo Onclick
        botonSalir.setOnClickListener(this);
        shareButton.setOnClickListener(this);

        //Mostramos por pantalla los datos de la partida
        idusuario.setText(usuario);
        textJugadas.setText(Integer.toString(jugadas));
        textTiempo.setText(tiempo);

        //Si se ha jugado conectado se actualiza el perfil
        if(AccessToken.getCurrentAccessToken()!=null)
            actualizarPerfil();

    }

    /**
     * Metodo que gestiona el click en cada uno de los elementos de la interfaz
     * @param v View referente a la Activity
     */
    public void onClick(View v) {
        //Dependiendo del boton clickado hace unas operaciones u otras
        switch (v.getId()) {
            case R.id.BotonSalir:
                //Comprobamos las preferencias para emitir sonidos o vibrar
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                //Se cierra la activity y se crea la del menu principal
                finish();
                startActivity(new Intent(PantallaVictoria.this, PantallaMenuPrincipal.class));
                break;

            case R.id.compartir:
                //Comprobamos las preferencias para emitir sonidos o vibrar
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                //Llamamos al metodo compartir para compartir los datos de la partida en Facebook
                compartir("Puntuacion Mastermind TFG","He ganado la partida en " + jugadas + " jugadas con un tiempo de " + tiempo +
                ", ¡Juega tú para mejorarlo¡ ");
                break;
        }
    }

    /**
     * Método para pintar la solucion de la partida jugada y mostrarla en pantalla
     * @param color1 Color del boton1
     * @param color2 Color del boton2
     * @param color3 Color del boton3
     * @param color4 Color del boton4
     */
    void pintarResultado(String color1,String color2,String color3,String color4){
        //Dependiendo del color pintamos los botones
        if(color1.equals("rojo")){
            boton1.setBackground(rojo);
        }else if(color1.equals("verde")){
            boton1.setBackground(verde);
        }else if(color1.equals("naranja")){
            boton1.setBackground(naranja);
        }else if(color1.equals("rosa")){
            boton1.setBackground(rosa);
        }else if(color1.equals("amarillo")){
            boton1.setBackground(amarillo);
        }else if(color1.equals("azul")) {
            boton1.setBackground(azul);
        }else if(color1.equals("morado")){
            boton1.setBackground(morado);
        }else if(color1.equals("celeste")) {
            boton1.setBackground(celeste);
        }

        if(color2.equals("rojo")){
            boton2.setBackground(rojo);
        }else if(color2.equals("verde")){
            boton2.setBackground(verde);
        }else if(color2.equals("naranja")){
            boton2.setBackground(naranja);
        }else if(color2.equals("rosa")){
            boton2.setBackground(rosa);
        }else if(color2.equals("amarillo")){
            boton2.setBackground(amarillo);
        }else if(color2.equals("azul")) {
            boton2.setBackground(azul);
        }else if(color2.equals("morado")){
            boton2.setBackground(morado);
        }else if(color2.equals("celeste")) {
            boton2.setBackground(celeste);
        }

        if(color3.equals("rojo")){
            boton3.setBackground(rojo);
        }else if(color3.equals("verde")){
            boton3.setBackground(verde);
        }else if(color3.equals("naranja")){
            boton3.setBackground(naranja);
        }else if(color3.equals("rosa")){
            boton3.setBackground(rosa);
        }else if(color3.equals("amarillo")){
            boton3.setBackground(amarillo);
        }else if(color3.equals("azul")) {
            boton3.setBackground(azul);
        }else if(color3.equals("morado")){
            boton3.setBackground(morado);
        }else if(color3.equals("celeste")) {
            boton3.setBackground(celeste);
        }

        if(color4.equals("rojo")){
            boton4.setBackground(rojo);
        }else if(color4.equals("verde")){
            boton4.setBackground(verde);
        }else if(color4.equals("naranja")){
            boton4.setBackground(naranja);
        }else if(color4.equals("rosa")){
            boton4.setBackground(rosa);
        }else if(color4.equals("amarillo")){
            boton4.setBackground(amarillo);
        }else if(color4.equals("azul")) {
            boton4.setBackground(azul);
        }else if(color4.equals("morado")){
            boton4.setBackground(morado);
        }else if(color4.equals("celeste")) {
            boton4.setBackground(celeste);
        }

    }

    /**
     * Metodo para compartir en Facebook
     * @param titulo Titulo de lo que se va a compartir
     * @param contenido Contenido que se comparte
     */
    private void compartir(String titulo, String contenido){
        //Comprobamos si se puede mostrar el ShareDialog
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            //Creamos el contenido
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    //Le asignamos el titulo
                    .setContentTitle(titulo)
                    //Le asignamos una imagen para mostrar que subimos a internete
                    .setImageUrl(Uri.parse("http://s2.postimg.org/m7kf0y8zt/565px_Mastermind.jpg"))
                    //Un enlace al que se accederia clickando en Facebook
                    .setContentUrl(Uri.parse("https://play.google.com/store"))
                    //Le asignamos el contenido que se quiere compartir
                    .setContentDescription(contenido)
                    //Lo construimos
                    .build();
            //Mostramos la pantalla de compartir con lo creado anteriormente
            shareDialog.show(linkContent);
        }
    }

    /**
     * Método para actulizar los datos del perfil en caso de jugar conectado
     */
    private void actualizarPerfil(){

        //Obtenemos los datos de la id de Facebook y el tiempo que vamos a utilizar
        String t[] =tiempo.split(":");
        String[] usuario = new String[] {AccessToken.getCurrentAccessToken().getUserId()};
        String user_id = AccessToken.getCurrentAccessToken().getUserId();

        //Inicializamos la base de datos
        BD = new BDPerfilJugador(this);

        //Incrementamos el numero de victorias del usuario
        BD.actualizaVictorias(usuario);

        //Recuperamos todos los datos del usuario
        Cursor c = BD.busquedaUsuario(usuario);
        c.moveToFirst();

        //Comprobamos si ha obtenido un numero inferior de jugadas al guardado en la base de datos
        if(jugadas<c.getInt(4) || c.getString(5)==null){
            //Actualizamos el record en la base de datos
            BD.actualizarRecord(user_id,jugadas,tiempo);
            //Mostramos una ventana indicando que hemos batido el record y podremos compartirlo
            mostrarRetos("Nuevo Record","Has establecido un nuevo record con "+jugadas+" jugadas en "+tiempo);
            //Si son las mismas, comprobamos el tiempo, primero los minutos y despues los segundos
        }else if(jugadas==c.getInt(4)){
            String t2[]=c.getString(5).split(":");
            if(Integer.parseInt(t[0])<Integer.parseInt(t2[0])){
                BD.actualizarRecord(user_id, jugadas, tiempo);
                mostrarRetos("Nuevo Record","Has establecido un nuevo record con "+jugadas+" jugadas en "+tiempo);
            }else if(Integer.parseInt(t[0])==Integer.parseInt(t2[0])){
                if(Integer.parseInt(t[1])<Integer.parseInt(t2[1])){
                    BD.actualizarRecord(user_id, jugadas, tiempo);
                    mostrarRetos("Nuevo Record","Has establecido un nuevo record con "+jugadas+" jugadas en "+tiempo);
                }
            }
        }

        //Comprobamos si hemos ganado una partida y no hemos conseguido el reto todavia
        if(c.getInt(6)!=0 && c.getInt(8)==0) {
            //Al ser la primera partida actulizamos como mejor record
            BD.actualizarRecord(user_id, jugadas, tiempo);
            //Mostramos una ventana indicando que hemos batido el record y podremos compartirlo
            mostrarRetos("Nuevo Record","Has establecido un nuevo record con "+jugadas+" jugadas en "+tiempo);
            //Indicamos en la base de datos que este Reto está conseguido
            BD.completarReto(user_id,"primera");
            //Mostramos una ventana indicando que hemos conseguido el record y podremos compartirlo
            mostrarRetos("Reto Conseguido","Has ganado tu primera partida");
        }

        //Comprobamos si hemos ganado en la ultima mano y no hemos conseguido el reto todavia
        if(jugadas==12 && c.getInt(9)==0) {
            //Indicamos en la base de datos que este Reto está conseguido
            BD.completarReto(user_id, "ultima");
            //Mostramos una ventana indicando que hemos conseguido el Reto y podremos compartirlo
            mostrarRetos("Reto Conseguido","Has ganado en la útima jugada");
        }

        //Comprobamos si hemos ganado en menos de un minuto y no hemos conseguido el reto todavia
        if(Integer.parseInt(t[0])<1 && c.getInt(10)==0){
            //Indicamos en la base de datos que este Reto está conseguido
            BD.completarReto(user_id, "minuto");
            //Mostramos una ventana indicando que hemos conseguido el Reto y podremos compartirlo
            mostrarRetos("Reto Conseguido","Has ganado en menos de 1 minuto");
        }

        //Comprobamos si hemos ganado en la primera mano y no hemos conseguido el reto todavia
        if(jugadas==1 && c.getInt(11)==0){
            //Indicamos en la base de datos que este Reto está conseguido
            BD.completarReto(user_id,"jugada");
            //Mostramos una ventana indicando que hemos conseguido el Reto y podremos compartirlo
            mostrarRetos("Reto Conseguido","Has ganado en la primera jugada");
        }

        //Actualiza nivel según el numero de victorias

        //Si hemos ganada 10 veces subimos a nivel Amateur
        if(c.getInt(7)==10){
            //Indicamos en la base de datos el nuevo nivel
            BD.modificarNivel(user_id,"Amateur");
            //Mostramos una ventana indicando que hemos subido de nivel y podremos compartirlo
            mostrarRetos("Reto Conseguido","Has ascendido a nivel Amateur");
            //Si hemos ganada 25 veces subimos a nivel Profesional
        }else if(c.getInt(7)==25){
            //Indicamos en la base de datos el nuevo nivel
            BD.modificarNivel(user_id, "Profesional");
            //Mostramos una ventana indicando que hemos subido de nivel y podremos compartirlo
            mostrarRetos("Reto Conseguido","Has ascendido a nivel Profesional");
            //Si hemos ganada 50 veces subimos a nivel Experto
        }else if(c.getInt(7)==50){
            //Indicamos en la base de datos el nuevo nivel
            BD.modificarNivel(user_id, "Experto");
            //Mostramos una ventana indicando que hemos subido de nivel y podremos compartirlo
            mostrarRetos("Reto Conseguido","Has ascendido a nivel Experto");
        }

    }

    /**
     * Método para mostra la ventana del Reto conseguido
     * @param titulo Título de la ventana
     * @param reto Contenido de la ventana
     */
    private void mostrarRetos(final String titulo,final String reto){
        //Creamos el AlertDialog a mostrar
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Le asignamos el titulo
        builder.setTitle(titulo);
        //Le asignamos el contendio
        builder.setMessage(reto);
        //Le asignamos el boton compartir que al pulsarlo llama al metodo compartir
        builder.setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                compartir(titulo, reto);
            }
        });
        //Le asignamos el boton "Aceptar" que cierra la ventas
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        //Lo creamos y lo mostramos
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Metodo que se ejecuta al volver de la pagina del Share
     * @param requestCode Identificador de donde viene el result
     * @param resultCode Codigo devuelto por la Activity de la que vuelve
     * @param data Intent con los datos devueltos de la Activity de la que vuelve
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Reenvia al callbackManager el resultado del shareDialog
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}