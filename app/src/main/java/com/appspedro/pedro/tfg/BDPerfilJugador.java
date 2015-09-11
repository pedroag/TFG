package com.appspedro.pedro.tfg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;


/**
 * Clase que hereda de SQliteOpenHelper y se encarga de la gestion de la base de datos de jugadores
 * @author Pedro Alcalá Galiano
 */
public class BDPerfilJugador extends SQLiteOpenHelper {

    /**
     * Variable que la sentencia en SQL de la creación de la tabla jugadores
     */
    private static final String sqlCreate = "CREATE TABLE IF NOT EXISTS jugadores (id_usuario TEXT primary key, nivel TEXT, fecha_registro DATE, " +
            "fecha_ultima_partida DATE, record_jugadas INTEGER, record_tiempo TEXT, partidas_jugadas INTEGER, " +
            "partidas_ganadas INTEGER, reto_primer INTEGER, reto_ultima_jugada INTEGER, reto_menosminuto INTEGER, reto_primera_jugada INTEGER," +
            "reto_amateur INTEGER, reto_profesional INTEGER, reto_experto INTEGER)";

    /**
     * Variable con el nombre de la base de datos
     */
    private static final String nombre = "BDjugadores";

    /**
     * Variable con el numero de version
     */
    private static final int version = 1;

    /**
     * Constructor de la clase BDPerfilJugador
     * @param contexto Context en el que se esta creando el objeto
     */
    public BDPerfilJugador(Context contexto) {
        super(contexto, nombre, null, version);
    }

    /**
     * Metodo en el que se crea la base detos guardada en la variable
     * @param db Objeto de SQLiteDatabase sobre el que se ejecutara el execSQL
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    /**
     * Metodo que elimina la base de datos actual y crea una nueva
     * @param db Objeto de SQLiteDatabase sobre el que se ejecutara el execSQL
     * @param oldVersion Version antigua
     * @param newVersion Version nueva
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + sqlCreate);
        onCreate(db);
    }

    /**
     * Metodo para actualizar el nivel del usuario
     * @param id_usuario Identificador del usuario
     * @param nivel Nuevo nivel del usuario
     */
    public void modificarNivel(String id_usuario, String nivel){
        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase db = getWritableDatabase();
        //Introducimos los valores a modificar
        ContentValues valores = new ContentValues();
        valores.put("id_usuario", id_usuario);
        valores.put("nivel",nivel);

        //Dependiendo del nivel actualizamos el estado del reto
        if(nivel.equals("Amateur")){
            valores.put("reto_amateur",1);
        }else if(nivel.equals("Profesional")){
            valores.put("reto_profesional",1);
        }else if(nivel.equals("Experto")){
            valores.put("reto_experto",1);
        }

        //Actualizamos y cerramos la base de datos
        db.update("jugadores", valores,"id_usuario='" + id_usuario+"'", null);
        db.close();
    }

    /**
     * Metodo para actualizar el record del usuario
     * @param id_usuario Identificador del usuario a modificar
     * @param jugadas Nuevo numero de jugadas a actualizar
     * @param tiempo Nuevo tiempo a actualizar
     */
    public void actualizarRecord(String id_usuario, Integer jugadas, String tiempo ){
        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase db = getWritableDatabase();
        //Introducimos los valores a modificar
        ContentValues valores = new ContentValues();
        valores.put("id_usuario", id_usuario);
        valores.put("record_jugadas",jugadas);
        valores.put("record_tiempo",tiempo);
        //Actualizamos y cerramos la base de datos
        db.update("jugadores", valores, "id_usuario='" + id_usuario+"'", null);
        db.close();
    }

    /**
     * Metodo para actualizar el estado completado de los retos
     * @param id_usuario Identificador del usuario a modificar
     * @param reto Identificador del reto a modificar
     */
    public void completarReto(String id_usuario, String reto){
        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        //Introducimos los valores a modificar dependiendo del reto
        if(reto.equals("primera")){
            valores.put("reto_primer",1);
        }else if(reto.equals("ultima")){
            valores.put("reto_ultima_jugada",1);
        }else if(reto.equals("minuto")){
            valores.put("reto_menosminuto",1);
        }else if(reto.equals("jugada")){
            valores.put("reto_primera_jugada",1);
        }else if(reto.equals("amateur")){
            valores.put("reto_amateur",1);
        }else if(reto.equals("profesional")){
            valores.put("reto_profesional",1);
        }else if(reto.equals("experto")){
            valores.put("reto_experto",1);
        }

        //Actualizamos y cerramos la base de datos
        db.update("jugadores", valores, "id_usuario='" + id_usuario +"'", null);
        db.close();
    }

    /**
     * Metodo para incrementar el numero de victorias del usuario
     * @param usuario Identificador del usuario a modificar
     */
    public void actualizaVictorias(String[] usuario){
        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase bd = getWritableDatabase();
        //Incremento el valor de las partidas y las victorias y cierro la base de datos y la fecha de la ultima partida
        bd.execSQL("UPDATE jugadores SET partidas_jugadas=partidas_jugadas+1, partidas_ganadas=partidas_ganadas+1" +
                ", fecha_ultima_partida=date('now') WHERE  id_usuario=?", usuario);
        bd.close();
    }

    /**
     * Metodo para incrementar el númnero de partidas jugadas por el usuario
     * @param usuario
     */
    public void actualizaPartidas(String[] usuario){

        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase bd = getWritableDatabase();

        //Incremento el valor de las partidas y la fecha de la ultima partida
        bd.execSQL("UPDATE jugadores SET partidas_jugadas=partidas_jugadas+1, fecha_ultima_partida=date('now') WHERE  id_usuario=?", usuario);
        bd.close();
    }

    /**
     * Metodo para buscar la entrada en la base de datos referente al usuario
     * @param usuario Identificador del usuario
     * @return Resultado de la consulta en forma de cursor
     */
    public Cursor busquedaUsuario(String[] usuario){

        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase bd = getReadableDatabase();

        //Consultamos la base de datos y lo guardamos en un cursor
        Cursor c = bd.rawQuery(" SELECT * FROM jugadores WHERE id_usuario=?", usuario);

        return c;
    }

    /**
     * Metodo para insertar una nueva entrada en la base de datos
     * @param usuario Identificador del usuario a introducir
     * @return True si se ha creado un nuevo usuario, False en otro caso
     */
    public Boolean crearJugadorNuevo(String[] usuario){

        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase bd = getWritableDatabase();

        //Consultamos la base de datos y lo guardamos en un cursor
        Cursor c = bd.rawQuery(" SELECT * FROM jugadores WHERE id_usuario=?", usuario);

        //Comprobamos que no existe una entrado del mismo usuario y lo introducimos
        if (c.getCount()==0) {
            bd.execSQL("INSERT INTO jugadores (id_usuario, nivel, fecha_registro, partidas_jugadas, partidas_ganadas," +
                    " reto_primer, reto_primera_jugada, reto_menosminuto, reto_primera_jugada, reto_amateur, reto_profesional, reto_experto) " +
                    "VALUES ('" + usuario[0] + "','Novato', date('now'),0,0,0,0,0,0,0,0,0)");
            bd.close();
            return true;
        }
        return false;
    }

    /**
     * Metodo que nos dice el numero de dias desde la fecha de la ultima partida hasta la actualidad
     * @param usuario Identificador de usuario
     * @return Numero de dias sin jugar
     */
    public int diasSinJugar(String[] usuario) {

        //Creamos la SQLiteDatabase sobre la que vamos a trabajar
        SQLiteDatabase bd = getReadableDatabase();

        //Consulta en SQL que nos devuelve la diferencia de las dos fechas
        Cursor consultaDias = bd.rawQuery("SELECT julianday('now') - julianday(fecha_ultima_partida) AS dias FROM jugadores " +
                "WHERE id_usuario=?", usuario);
        consultaDias.moveToFirst();
        //Obtenemos el numero de dias y lo pasamos a Integer
        Integer dias = consultaDias.getInt(consultaDias.getColumnIndex("dias"));

        return dias;
    }
}
