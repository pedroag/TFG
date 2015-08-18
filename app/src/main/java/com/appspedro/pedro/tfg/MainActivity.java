package com.appspedro.pedro.tfg;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private MediaPlayer musicaFondo;
    private SoundPool sonidoBoton;
    private Button unjugador,batalla,ajustes,perfil;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ToggleButton musica;
    private TextView estadoSesión;
    private Boolean sonidosActivados,vibracionActivada;
    private Vibrator vibracion;
    private String usuario;
    private PerfilJugador BD;
    private int flujoSonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/AldotheApache.ttf");

        musicaFondo = MediaPlayer.create(this,R.raw.ninetysecondsoffunk);
        musicaFondo.setLooping(true);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        sonidosActivados = preferencias.getBoolean("son",true);
        vibracionActivada = preferencias.getBoolean("vib",true);

        if(sonidosActivados)
            musicaFondo.start();

        sonidoBoton = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        flujoSonido = sonidoBoton.load(this,R.raw.botonesmenus,1);
        vibracion = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        loginButton = (LoginButton)findViewById(R.id.login_button);

        unjugador = (Button)findViewById(R.id.boton1jugador);
        ajustes = (Button)findViewById(R.id.botonajustes);
        batalla = (Button)findViewById(R.id.batalla);
        musica = (ToggleButton)findViewById(R.id.musica);
        estadoSesión = (TextView)findViewById(R.id.estadoSesion);
        perfil = (Button)findViewById(R.id.botonPerfil);

        unjugador.setTypeface(fuente);
        batalla.setTypeface(fuente);
        ajustes.setTypeface(fuente);
        perfil.setTypeface(fuente);
        estadoSesión.setTypeface(fuente);

        if(AccessToken.getCurrentAccessToken()!=null) {
            perfil.setVisibility(View.VISIBLE);
            ObtenerNombre(AccessToken.getCurrentAccessToken());
        }else {
            estadoSesión.setText("Sesion no iniciada");
        }

        unjugador.setOnClickListener(this);ajustes.setOnClickListener(this);
        batalla.setOnClickListener(this);musica.setOnClickListener(this);
        perfil.setOnClickListener(this);

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    estadoSesión.setText("Sesion no iniciada");
                    perfil.setVisibility(View.INVISIBLE);
                }else {
                    ObtenerNombre(AccessToken.getCurrentAccessToken());
                }
            }
        };

        accessTokenTracker.startTracking();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                perfil.setVisibility(View.VISIBLE);
                ObtenerNombre(loginResult.getAccessToken());
                BD = new PerfilJugador(MainActivity.this, "BDPerfiles", null, 1);
                SQLiteDatabase bd = BD.getReadableDatabase();

                String[] arg = new String[] {loginResult.getAccessToken().getUserId()};
                Cursor c = bd.rawQuery(" SELECT * FROM jugadores WHERE id_usuario=?", arg);

                if (c.getCount()==0){
                    bd.execSQL("INSERT INTO jugadores (id_usuario, nivel, fecha_registro, partidas_jugadas, partidas_ganadas," +
                            " reto_primer, reto_diez, reto_menosminuto, reto_primera_jugada, reto_amateur, reto_profesional, reto_experto) " +
                            "VALUES ('" + loginResult.getAccessToken().getUserId() +"','Novato', date('now'),0,0,0,0,0,0,0,0,0)");
                }
                else{
                    Cursor consultaDias = bd.rawQuery("SELECT julianday('now') - julianday(fecha_ultima_partida) AS dias FROM jugadores WHERE id_usuario=?",arg);
                    consultaDias.moveToFirst();
                    String dias = Integer.toString(consultaDias.getInt(consultaDias.getColumnIndex("dias")));

                    if(Integer.parseInt(dias)>=7) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("¡Ha pasado mas de una semana desde la última partida!")
                                .setCancelable(true)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Se ha cancelado el inicio de sesion",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Se ha producido un error",
                        Toast.LENGTH_SHORT).show();
            }


        });
}
    private void ObtenerNombre(final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(final JSONObject object, GraphResponse response){
                    try {
                        Profile profileDefault = Profile.getCurrentProfile();
                        usuario = profileDefault.getFirstName() + " " + profileDefault.getLastName();
                        estadoSesión.setText(usuario);
                    } catch (Exception e){}
                }
        });
        request.executeAsync();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boton1jugador:
                musicaFondo.stop();
                if(sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);

                if(AccessToken.getCurrentAccessToken()==null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Va a jugar como Invitado. Sus logros y resultados no se guardaran")
                            .setCancelable(true)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent inten = new Intent(MainActivity.this, Juego.class);
                                    inten.putExtra("usuario", "Invitado");
                                    inten.putExtra("modoJuego","individual");
                                    finish();
                                    startActivity(inten);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    Intent inten = new Intent(this,Juego.class);
                    inten.putExtra("usuario", usuario);
                    inten.putExtra("modoJuego","individual");
                    finish();
                    startActivity(inten);
                }
                break;

            case R.id.botonajustes:
                if(sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);
                musicaFondo.stop();
                finish();
                Intent intent = new Intent(this, Ajustes.class);
                startActivity(intent);
                break;

            case R.id.batalla:
                musicaFondo.stop();
                if(sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);

                AlertDialog.Builder jugadores = new AlertDialog.Builder(this);
                jugadores.setTitle("Nombre de los jugadores");

                final TextView j1 = new TextView(this);
                j1.setText("Jugador 1:");
                final TextView j2 = new TextView(this);
                j2.setText("Jugador 2:");
                final EditText jugador1 = new EditText(this);
                jugador1.setInputType(InputType.TYPE_CLASS_TEXT);
                final EditText jugador2 = new EditText(this);
                jugador2.setInputType(InputType.TYPE_CLASS_TEXT);

                LinearLayout ventana=new LinearLayout(this);
                ventana.setOrientation(LinearLayout.VERTICAL);
                ventana.addView(j1);
                ventana.addView(jugador1);
                ventana.addView(j2);
                ventana.addView(jugador2);
                jugadores.setView(ventana);

                jugadores.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                        String jug1 = jugador1.getText().toString();
                        String jug2 = jugador2.getText().toString();

                        PartidaBatalla partidaBatalla = new PartidaBatalla(jug1,jug2);

                        Intent inten2 = new Intent(MainActivity.this, SeleccionCombinacion.class);

                        inten2.putExtra("partida",partidaBatalla);
                        inten2.putExtra("modoJuego","batalla");

                        if(!jug1.equals("") && !jug2.equals("")) {
                            finish();
                            startActivity(inten2);
                        }else {
                            Toast.makeText(getApplicationContext(), "Escribe los dos nombres",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                jugadores.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                jugadores.show();
                break;

            case R.id.botonPerfil:
                if(sonidosActivados)
                    sonidoBoton.play(flujoSonido, 1, 1, 0, 0, 1);
                if(vibracionActivada)
                    vibracion.vibrate(200);
                Intent inten3 = new Intent(this, PantallaPerfil.class);
                startActivity(inten3);
                break;

            case R.id.musica:
                if(musica.isChecked()){
                    musicaFondo.pause();
                }else{
                    musicaFondo.start();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(AccessToken.getCurrentAccessToken()==null)
            estadoSesión.setText("Sesion no iniciada");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.acerca_de) {
            AlertDialog.Builder acerca = new AlertDialog.Builder(this);
            acerca.setTitle("Acerca de");
            acerca.setMessage("TFG realizado por el alumno Pedro Alcala Galiano")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = acerca.create();
            alert.show();

            return true;
        }
        else if(id == R.id.salir){
            if(AccessToken.getCurrentAccessToken()!=null)
                LoginManager.getInstance().logOut();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
