package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


public class PantallaVictoria extends Activity implements View.OnClickListener {

    private String usuario, tiempo;
    private TextView idusuario,textJugadas,textTiempo,enhorabuena,textResJug,textResTiempo;
    private int jugadas;
    private Drawable amarillo,azul,naranja,rojo,rosa,verde;
    private Button boton1,boton2,boton3,boton4;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ShareButton shareButton;
    private PerfilJugador BD;
    private Boolean sonidosActivados,vibracionActivada;
    private SoundPool sonidoMenu;
    private int flujoMenu;
    private Vibrator vibrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_victoria);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        sonidosActivados = preferencias.getBoolean("son", true);
        vibracionActivada = preferencias.getBoolean("vib", true);

        sonidoMenu = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoMenu = sonidoMenu.load(this, R.raw.botonesmenus, 1);

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        shareButton = (ShareButton)findViewById(R.id.compartir);

        amarillo = ContextCompat.getDrawable(this, R.drawable.ficha_amarilla);
        azul = ContextCompat.getDrawable(this, R.drawable.ficha_azul);
        naranja = ContextCompat.getDrawable(this, R.drawable.ficha_naranja);
        rojo = ContextCompat.getDrawable(this, R.drawable.ficha_roja);
        rosa = ContextCompat.getDrawable(this, R.drawable.ficha_rosa);
        verde = ContextCompat.getDrawable(this, R.drawable.ficha_verde);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.victory);
        mp.start();

        usuario = getIntent().getStringExtra("usuario");
        jugadas = getIntent().getIntExtra("jugadas", 0);
        tiempo = getIntent().getStringExtra("tiempo");

        String color1 = getIntent().getStringExtra("color1");
        String color2 = getIntent().getStringExtra("color3");
        String color3 = getIntent().getStringExtra("color2");
        String color4 = getIntent().getStringExtra("color4");

        boton1 = (Button) findViewById(R.id.Boton1);
        boton2 = (Button) findViewById(R.id.Boton2);
        boton3 = (Button) findViewById(R.id.Boton3);
        boton4 = (Button) findViewById(R.id.Boton4);

        Button botonSalir = (Button) findViewById(R.id.BotonSalir);

        idusuario = (TextView) findViewById(R.id.idusuario);
        enhorabuena = (TextView) findViewById(R.id.Enhorabuena);
        textResJug = (TextView) findViewById(R.id.textJugadas);
        textResTiempo = (TextView) findViewById(R.id.textTiemo);
        textJugadas = (TextView) findViewById(R.id.textJugadasFinal);
        textTiempo = (TextView) findViewById(R.id.textTiempoFinal);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/AldotheApache.ttf");
        enhorabuena.setTypeface(fuente);
        textResJug.setTypeface(fuente);
        textResTiempo.setTypeface(fuente);

        pintarResultado(color1, color2, color3, color4);

        if(usuario.equals("invitado"))
            shareButton.setVisibility(View.INVISIBLE);

        botonSalir.setOnClickListener(this);
        shareButton.setOnClickListener(this);

        idusuario.setText(usuario);
        textJugadas.setText(Integer.toString(jugadas));
        textTiempo.setText(tiempo);

        if(AccessToken.getCurrentAccessToken()!=null)
            actualizarPerfil();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BotonSalir:
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                finish();
                startActivity(new Intent(PantallaVictoria.this, MainActivity.class));
                break;

            case R.id.compartir:
                if(sonidosActivados)
                    sonidoMenu.play(flujoMenu, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibrador.vibrate(200);
                compartir("Puntiacion Mastermind TFG","He ganado la partida en " + jugadas + " jugadas con un tiempo de " + tiempo);
                break;
        }

    }
    void pintarResultado(String color1,String color2,String color3,String color4){
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
        }

    }

    private void compartir(String titulo, String contenido){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(titulo)
                    .setImageUrl(Uri.parse("https://www.dropbox.com/s/2xse0qe5kaqq5yl/ic_launcher.png"))
                    .setContentUrl(Uri.parse("https://play.google.com/store"))
                    .setContentDescription(contenido)
                    .build();
            shareDialog.show(linkContent);
        }
    }

    private void actualizarPerfil(){

        String t[] =tiempo.split(":");

        BD = new PerfilJugador(this, "BDPerfiles", null, 1);
        SQLiteDatabase bd = BD.getWritableDatabase();
        String[] usuario = new String[] {AccessToken.getCurrentAccessToken().getUserId()};
        String[] record = new String[] {Integer.toString(jugadas),tiempo,AccessToken.getCurrentAccessToken().getUserId()};


        bd.execSQL("UPDATE jugadores SET partidas_jugadas=partidas_jugadas+1, partidas_ganadas=partidas_ganadas+1" +
                ", fecha_ultima_partida=date('now') WHERE  id_usuario=?", usuario);

        Cursor c = bd.rawQuery(" SELECT * FROM jugadores WHERE id_usuario=?", usuario);
        c.moveToFirst();

        //Actualiza Record
        if(jugadas<c.getInt(4) || c.getString(5)==null){
            bd.execSQL("UPDATE jugadores SET record_jugadas=?, record_tiempo=? WHERE  id_usuario=?", record);
            mostrarRetos("Has establecido un nuevo record");
        }else if(jugadas==c.getInt(4)){
            String t2[]=c.getString(5).split(":");
            if(Integer.parseInt(t[0])<Integer.parseInt(t2[0])){
                bd.execSQL("UPDATE jugadores SET record_jugadas=?, record_tiempo=? WHERE  id_usuario=?", record);
                mostrarRetos("Has establecido un nuevo record");
            }else if(Integer.parseInt(t[0])==Integer.parseInt(t2[0])){
                if(Integer.parseInt(t[1])<Integer.parseInt(t2[1])){
                    bd.execSQL("UPDATE jugadores SET record_jugadas=?, record_tiempo=? WHERE  id_usuario=?", record);
                    mostrarRetos("Has establecido un nuevo record");
                }
            }
        }

        //Actualiza retos
        if(c.getInt(6)!=0 && c.getInt(8)==0) {
            bd.execSQL("UPDATE jugadores SET reto_primer=1, record_jugadas=" +jugadas+ ", record_tiempo='" +tiempo+ "'" +
                    " WHERE  id_usuario=?", usuario);
            mostrarRetos("Has ganado tu primera partida");
        }

        if(c.getInt(6)==10) {
            bd.execSQL("UPDATE jugadores SET reto_diez=1 WHERE  id_usuario=?", usuario);
            mostrarRetos("Has ganado 10 partidas");
        }

        if(Integer.parseInt(t[0])<1 && c.getInt(10)==0){
            bd.execSQL("UPDATE jugadores SET reto_menosminuto=1 WHERE  id_usuario=?", usuario);
            mostrarRetos("Has ganado en menos de 1 minuto");
        }

        if(jugadas==1 && c.getInt(11)==0){
            bd.execSQL("UPDATE jugadores SET reto_primera_jugada=1 WHERE  id_usuario=?", usuario);
            mostrarRetos("Has ganado en la primera jugada");
        }

        //Actualiza nivel
        if(c.getInt(7)==10){
            bd.execSQL("UPDATE jugadores SET nivel='Amateur' reto_amateur=1 WHERE  id_usuario=?", usuario);
            mostrarRetos("Has ascendido a nivel Amateur");
        }else if(c.getInt(7)==25){
            bd.execSQL("UPDATE jugadores SET nivel='Profesional' reto_profesional=1 WHERE  id_usuario=?", usuario);
            mostrarRetos("Has ascendido a nivel Profesional");
        }else if(c.getInt(7)==50){
            bd.execSQL("UPDATE jugadores SET nivel='Experto' reto_experto=1 WHERE  id_usuario=?", usuario);
            mostrarRetos("Has ascendido a nivel Experto");
        }

    }

    private void mostrarRetos(final String reto){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("RETO CONSEGUIDO");
        builder.setMessage(reto);
        builder.setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                compartir("RETO CONSEGUIDO", reto);
            }
        });
        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

        @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}