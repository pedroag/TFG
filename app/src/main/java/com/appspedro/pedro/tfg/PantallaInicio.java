package com.appspedro.pedro.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;


public class PantallaInicio extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantallainicio);
        Thread Espera = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    openApp();
                    interrupt();
                }

            }
        };
        Espera.start();
    }

    private void openApp() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.abc_slide_out_bottom,R.anim.abc_slide_out_top);
    }

}
