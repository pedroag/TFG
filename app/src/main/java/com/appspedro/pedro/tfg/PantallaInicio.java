package com.appspedro.pedro.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

/**
 * Clase PantallaInicio que hereda de Activy y es con la que se inicia la aplicacion
 */
public class PantallaInicio extends Activity {

    /**
     * Metodo llamado cuando la actividad es creada
     * @param savedInstanceState Estado anterior de la actividad guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializo el sdk de Facebook
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //Establece la interfaz de usuario para esta Activity
        setContentView(R.layout.activity_pantallainicio);
        //Salgo de sesión, por si hay alguna abierta
        LoginManager.getInstance().logOut();
        //Se crea una tarea que al ejecutarla muestra la Activity durante 2 segundos
        Thread Espera = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    //Cuando acaban los dos segundos se inicia la Actividad que muestra el menu principal
                    abrirActivityMenu();
                    interrupt();
                }
            }
        };
        //Se inicia la tarea
        Espera.start();
    }

    /**
     * Metodo que inicia la Actividad que muestra el menu
     */
    private void abrirActivityMenu() {
        finish();
        //Transicion entre las actividades
        overridePendingTransition(R.anim.abc_slide_out_bottom, R.anim.abc_slide_out_top);
        startActivity(new Intent(this, PantallaMenuPrincipal.class));
    }

}
