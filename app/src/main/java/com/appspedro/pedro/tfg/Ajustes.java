package com.appspedro.pedro.tfg;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Clase Ajustes que hereda de PreferenceActivity y muestra en la actividad las distintas preferencias de la aplicacion
 * @author Pedro Alcalá Galiano
 */

public class Ajustes extends PreferenceActivity {

    /**
     * Metodo llamado cuando la actividad es creada
     * @param savedInstanceState Estado anterior de la actividad guardado
     */
    @Override
    @SuppressWarnings("deprecation")
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Carga las listas de preferencias que se encuentran en el archivo indicado
        addPreferencesFromResource(R.xml.preferencias);
    }

    /**
     * Metodo que se ejecuta cuando el pulsado el boton atras
     */
    public void onBackPressed() {
        //Inicia la actividad principal
        finish();
        startActivity(new Intent(this, PantallaMenuPrincipal.class));
    }
}


