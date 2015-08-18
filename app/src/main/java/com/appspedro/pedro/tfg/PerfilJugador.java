package com.appspedro.pedro.tfg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PerfilJugador extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE IF NOT EXISTS jugadores (id_usuario TEXT primary key, nivel TEXT, fecha_registro DATE, " +
            "fecha_ultima_partida DATE, record_jugadas INTEGER, record_tiempo TEXT, partidas_jugadas INTEGER, " +
            "partidas_ganadas INTEGER, reto_primer INTEGER, reto_diez INTEGER, reto_menosminuto INTEGER, reto_primera_jugada INTEGER," +
            "reto_amateur INTEGER, reto_profesional INTEGER, reto_experto INTEGER)";

    public PerfilJugador(Context contexto, String nombre, CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + sqlCreate);
        onCreate(db);
    }
}
