package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

/**
 * Clase PantallaPerfil que hereda de Activity y que muestra el perfil del jugador
 */
public class PantallaPerfil extends Activity{

    /**
     * Variable que contiene la foto del usuario
     */
    private ProfilePictureView foto;
    /**
     * Variable que muestra el nombre del usuari
     */
    private TextView nombre;
    /**
     * Variable que muestra el record del usuario
     */
    private TextView mejorJugada;
    /**
     * Variable que muestra las partidas jugadas por el usuario
     */
    private TextView partidas;
    /**
     * Variable que muestra el numero de victorias del usuario
     */
    private TextView victorias;
    /**
     * Variable que muestra la fecha de registro del usuario
     */
    private TextView fecha_registro;
    /**
     * Variable que muestra la fecha de la ultima partida
     */
    private TextView fecha_ultima;
    /**
     * Variable que muestra el nivel del usuario
     */
    private TextView nivel;
    /**
     * Variable TableRow que muestra el reto de la primera partida
     */
    private TableRow reto1partida;
    /**
     * Variable TableRow que muestra el reto de las 10 partidas jugadas
     */
    private TableRow retoultimapartida;
    /**
     * Variable TableRow que muestra el reto de la primera jugada
     */
    private TableRow reto1jugada;
    /**
     * Variable TableRow que muestra el reto del primer minuto
     */
    private TableRow reto1minuto;
    /**
     * Variable TableRow que muestra el reto del nivel amateur
     */
    private TableRow retoamateur;
    /**
     * Variable TableRow que muestra el reto del nivel profesional
     */
    private TableRow retoprofesioal;
    /**
     * Variable TableRow que muestra el reto del nivel experto
     */
    private TableRow retoexperto;
    /**
     * Variable PerfilJugado que contiene la base de datos
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
        setContentView(R.layout.activity_pantalla_perfil);

        //Creamos el typeface para asignar la fuente a los textviews de la actividad
        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/Square.ttf");

        //Asignamos las distintas variables a los elementos de la interfaz de usuario
        foto = (ProfilePictureView)findViewById(R.id.fotoperfil);
        nombre = (TextView) findViewById(R.id.nombre);
        nivel = (TextView)findViewById(R.id.nivel);
        mejorJugada = (TextView)findViewById(R.id.mejorpuntuacion);
        partidas = (TextView)findViewById(R.id.partidas);
        victorias = (TextView)findViewById(R.id.partidas_ganadas);
        fecha_registro = (TextView) findViewById(R.id.fecha_registro);
        fecha_ultima = (TextView)findViewById(R.id.fecha_ultima);
        reto1partida = (TableRow)findViewById(R.id.reto1partida);
        retoultimapartida = (TableRow)findViewById(R.id.retoultimajugada);
        reto1jugada = (TableRow)findViewById(R.id.reto1jugada);
        reto1minuto = (TableRow)findViewById(R.id.reto1minuto);
        retoamateur = (TableRow)findViewById(R.id.retoamateur);
        retoprofesioal = (TableRow)findViewById(R.id.retoprofesional);
        retoexperto = (TableRow)findViewById(R.id.retoexperto);

        //Cambiamos la fuente de los elementos de la interfaz de usuario
        nivel.setTypeface(fuente);
        mejorJugada.setTypeface(fuente);
        partidas.setTypeface(fuente);
        victorias.setTypeface(fuente);
        fecha_registro.setTypeface(fuente);
        fecha_ultima.setTypeface(fuente);

        //Inicializamos la base de datos
        BD = new BDPerfilJugador(this);

        //Obtenemos todos los datos del perfil de la base de datos
        obtenerDatosPerfil(BD);

        //Obtenemos los datos referentes a Facebook
        ObtenerDatosFacebook();
    }

    /**
     * Metodo para obtener los datos del usuario almacenados en la base de datos
     * @param perfil Base de datos para obtener los datos
     */
    private void obtenerDatosPerfil(BDPerfilJugador perfil){

        String[] usuario = new String[] {AccessToken.getCurrentAccessToken().getUserId()};
        //Se obtienen los datos del usuario
        Cursor c = perfil.busquedaUsuario(usuario);
        c.moveToFirst();
        //Mostramos en pantalla el nivel, las partidas y las victorias
        nivel.setText("Nivel: "+c.getString(1));
        partidas.setText("Partidas: "+ c.getString(6));
        victorias.setText("Victorias: "+ c.getString(7));

        //Para la fechas, como esta en otro formato las mostramos en el que queremos
        String fechar[]=c.getString(2).split("-");
        fecha_registro.setText("Registrado desde: "+ fechar[2]+"-"+fechar[1]+"-"+fechar[0]);
        //Si no existe ultima partida, mostramos un guion
        if(c.getString(3)!=null) {
            String fechau[] = c.getString(3).split("-");
            fecha_ultima.setText("Ultima partida: " + fechau[2]+"-"+fechau[1]+"-"+fechau[0]);
        }else {
            fecha_ultima.setText("-");
        }
        //Si no existe record mostramos un guion
        if(c.getString(4)!=null)
            mejorJugada.setText("Record: " + c.getString(4) + " jugadas en "+ c.getString(5));
        else
            mejorJugada.setText("-");

        //Comprobamos que retos se han cumplido para mostrarlos en la tabla de retos
        if(c.getInt(8)!=0)
            reto1partida.setVisibility(View.VISIBLE);
        if(c.getInt(9)!=0)
            retoultimapartida.setVisibility(View.VISIBLE);
        if(c.getInt(10)!=0)
            reto1minuto.setVisibility(View.VISIBLE);
        if(c.getInt(11)!=0)
            reto1jugada.setVisibility(View.VISIBLE);
        if(c.getInt(12)!=0)
            retoamateur.setVisibility(View.VISIBLE);
        if(c.getInt(13)!=0)
            retoprofesioal.setVisibility(View.VISIBLE);
        if(c.getInt(14)!=0)
            retoexperto.setVisibility(View.VISIBLE);

    }

    /**
     * Metodo para obtener el nombre de usuario y la fotografia de Facebook
     */
    private void ObtenerDatosFacebook() {
        //Para obtener datos del perfil tenemos que hacer un GraphRequest
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
            //Obtenemos los datos del perfil
            Profile perfil = Profile.getCurrentProfile();
            //Lo mostramos en pantalla y lo guardamos
            foto.setProfileId(perfil.getId());
                nombre.setText(object.optString("name"));
            }
        });
        //Añadimos los parametros que hemos requerido y ejecutamos la peticion
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
