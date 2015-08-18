package com.appspedro.pedro.tfg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;


public class PantallaPerfil extends Activity{

    private ProfilePictureView foto;
    private TextView nombre, mejorJugada,partidas,victorias,fecha_registro,fecha_ultima,nivel;
    private RelativeLayout pantalla;
    private TableRow reto1partida,reto10partidas,reto1jugada,reto1minuto,retoamateur,retoprofesioal,retoexperto;
    private PerfilJugador BD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil);
        ProgressDialog pd = ProgressDialog.show(this, "Loading", "Please Wait", true);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "font/AldotheApache.ttf");

        foto = (ProfilePictureView)findViewById(R.id.fotoperfil);
        nombre = (TextView) findViewById(R.id.nombre);
        nivel = (TextView)findViewById(R.id.nivel);
        pantalla = (RelativeLayout) findViewById(R.id.pantallaPerfil);
        mejorJugada = (TextView)findViewById(R.id.mejorpuntuacion);
        partidas = (TextView)findViewById(R.id.partidas);
        victorias = (TextView)findViewById(R.id.partidas_ganadas);
        fecha_registro = (TextView) findViewById(R.id.fecha_registro);
        fecha_ultima = (TextView)findViewById(R.id.fecha_ultima);
        reto1partida = (TableRow)findViewById(R.id.reto1partida);
        reto10partidas = (TableRow)findViewById(R.id.reto10partidas);
        reto1jugada = (TableRow)findViewById(R.id.reto1jugada);
        reto1minuto = (TableRow)findViewById(R.id.reto1minuto);
        retoamateur = (TableRow)findViewById(R.id.retoamateur);
        retoprofesioal = (TableRow)findViewById(R.id.retoprofesional);
        retoexperto = (TableRow)findViewById(R.id.retoexperto);

        nivel.setTypeface(fuente);
        mejorJugada.setTypeface(fuente);
        partidas.setTypeface(fuente);
        victorias.setTypeface(fuente);
        fecha_registro.setTypeface(fuente);
        fecha_ultima.setTypeface(fuente);

        obtenerDatosPerfil();

        ObtenerDatosFacebook(AccessToken.getCurrentAccessToken());
        pantalla.setVisibility(View.VISIBLE);
        pd.dismiss();

    }

    private void obtenerDatosPerfil(){
        BD = new PerfilJugador(this, "BDPerfiles", null, 1);
        SQLiteDatabase bd = BD.getReadableDatabase();

        String[] arg = new String[] {AccessToken.getCurrentAccessToken().getUserId()};
        Cursor c = bd.rawQuery(" SELECT * FROM jugadores WHERE id_usuario=?", arg);

        if (c.moveToFirst()){
            nivel.setText("Nivel: "+c.getString(1));
            partidas.setText("Partidas: "+ c.getString(6));
            victorias.setText("Victorias: "+ c.getString(7));

            String fechar[]=c.getString(2).split("-");
            fecha_registro.setText("Registrado desde: "+ fechar[2]+"-"+fechar[1]+"-"+fechar[0]);

            if(c.getString(3)!=null) {
                String fechau[] = c.getString(3).split("-");
                fecha_ultima.setText("Ultima partida: " + fechau[2]+"-"+fechau[1]+"-"+fechau[0]);
            }else {
                fecha_ultima.setText("-");
            }

            if(c.getString(4)!=null)
                mejorJugada.setText("Record: " + c.getString(4) + " jugadas en "+ c.getString(5));
            else
                mejorJugada.setText("-");

            if(c.getInt(8)!=0)
                reto1partida.setVisibility(View.VISIBLE);
            if(c.getInt(9)!=0)
                reto10partidas.setVisibility(View.VISIBLE);
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

    }

    private void ObtenerDatosFacebook(final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(final JSONObject object, GraphResponse response){
                try {
                    Profile profileDefault = Profile.getCurrentProfile();
                    foto.setProfileId(profileDefault.getId());
                    nombre.setText(profileDefault.getFirstName() + " " + profileDefault.getLastName());

                } catch (Exception e){}
            }
        });
        request.executeAsync();
    }

}
