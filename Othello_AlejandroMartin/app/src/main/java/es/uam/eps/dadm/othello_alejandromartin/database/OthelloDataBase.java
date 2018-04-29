package es.uam.eps.dadm.othello_alejandromartin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.uam.eps.dadm.othello_alejandromartin.model.Round;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepository;
import es.uam.eps.multij.ExcepcionJuego;

import static es.uam.eps.dadm.othello_alejandromartin.database.RoundDataBaseSchema.UserTable;
import static es.uam.eps.dadm.othello_alejandromartin.database.RoundDataBaseSchema.RoundTable;

/**
 * Created by jalij on 22/03/2017.
 */

public class OthelloDataBase implements RoundRepository {

    private final String DEBUG_TAG = "DEBUG";
    private static final String DATABASE_NAME = "othello.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public OthelloDataBase(Context context) {
        helper = new DatabaseHelper(context);
    }

    /**
     * Crear la base de datos y actualizarla cuando sea necesario
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE IF EXISTS " + UserTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RoundTable.NAME);
            createTable(db);
        }

        private void createTable(SQLiteDatabase db) {
            String str1 = "CREATE TABLE " + UserTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + UserTable.Cols.PLAYERUUID + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERNAME + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERPASSWORD + " TEXT);";
            String str2 = "CREATE TABLE " + RoundTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + RoundTable.Cols.ROUNDUUID + " TEXT UNIQUE, "
                    + RoundTable.Cols.PLAYERUUID + " TEXT REFERENCES "+UserTable.Cols.PLAYERUUID + ", "
                    + RoundTable.Cols.DATE + " TEXT, "
                    + RoundTable.Cols.TITLE + " TEXT, "
                    + RoundTable.Cols.SIZE + " TEXT, "
                    + RoundTable.Cols.BOARD + " TEXT);";
            try {
                db.execSQL(str1);
                db.execSQL(str2);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Invoca al metodo getWritableDatabase que devuelve la referencia de tipo SQLiteDatabase,
     * para asi modificar los datos
     * @throws SQLException
     */
    @Override
    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    /**
     * Cierra la base de datos
     */
    public void close() {
        db.close();
    }

    @Override
    public void login(String playername, String playerpassword,
                      LoginRegisterCallback callback) {

        Log.d(DEBUG_TAG, "Login " + playername);
        Cursor cursor = db.query(UserTable.NAME,
                new String[]{UserTable.Cols.PLAYERUUID},
                UserTable.Cols.PLAYERNAME + " = ? AND " + UserTable.Cols.PLAYERPASSWORD + " = ?",
                new String[]{playername, playerpassword}, null, null, null);

        int count = cursor.getCount();
        String uuid = count == 1 && cursor.moveToFirst() ? cursor.getString(0) : "";
        cursor.close();

        if (count == 1)
            callback.onLogin(uuid);
        else
            callback.onError("Username or password incorrect.");

    }

    @Override
    public void register(String playername, String password,
                         LoginRegisterCallback callback) {

        ContentValues values = new ContentValues();
        String uuid = UUID.randomUUID().toString();
        values.put(UserTable.Cols.PLAYERUUID, uuid);
        values.put(UserTable.Cols.PLAYERNAME, playername);
        values.put(UserTable.Cols.PLAYERPASSWORD, password);
        long id = db.insert(UserTable.NAME, null, values);

        if (id < 0)
            callback.onError("Error inserting new player named " + playername);
        else
            callback.onLogin(uuid);
    }

    private ContentValues getContentValues(Round round) {
        ContentValues values = new ContentValues();
        values.put(RoundTable.Cols.PLAYERUUID, round.getPlayerUUID());
        values.put(RoundTable.Cols.ROUNDUUID, round.getId());
        values.put(RoundTable.Cols.DATE, round.getDate());
        values.put(RoundTable.Cols.TITLE, round.getTitle());
        values.put(RoundTable.Cols.SIZE, round.getSize());
        values.put(RoundTable.Cols.BOARD, round.getBoard().tableroToString());

        return values;
    }

    /**
     * 	Anade a la base de datos la partida pasada como primer argumento
     * @param round
     * @param callback
     */
    @Override
    public void addRound(Round round, BooleanCallback callback) {
        ContentValues values = getContentValues(round);
        long id = db.insert(RoundTable.NAME, null, values);

        if (callback != null)
            callback.onResponse(id >= 0);
    }

    @Override
    public void updateRound(Round round, BooleanCallback callback) {
        ContentValues values = getContentValues(round);
        long id = db.update(RoundTable.NAME, values, RoundTable.Cols.ROUNDUUID + " = '" + round.getId() + "'", null);

        if (callback != null)
            callback.onResponse(id >= 0);
    }

    private RoundCursorWrapper queryRounds() {
        String sql = "SELECT " + UserTable.Cols.PLAYERNAME + ", " +
                UserTable.Cols.PLAYERUUID + ", " +
                RoundTable.Cols.ROUNDUUID + ", " +
                RoundTable.Cols.DATE + ", " +
                RoundTable.Cols.TITLE + ", " +
                RoundTable.Cols.SIZE + ", " +
                RoundTable.Cols.BOARD + " " +
                "FROM " + UserTable.NAME + " AS p, " +
                RoundTable.NAME + " AS r " +
                "WHERE " + "p." + UserTable.Cols.PLAYERUUID + "=" +
                "r." + RoundTable.Cols.PLAYERUUID + ";";
        Cursor cursor = db.rawQuery(sql, null);
        return new RoundCursorWrapper(cursor);
    }

    @Override
    public void getRounds(String playeruuid, String orderByField, String group,
                          RoundsCallback callback) throws ExcepcionJuego {
        List<Round> rounds = new ArrayList<>();
        RoundCursorWrapper cursor = queryRounds();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Round round = cursor.getRound();
            if (round.getPlayerUUID().equals(playeruuid))
                rounds.add(round);
            cursor.moveToNext();
        }
        cursor.close();
        if (cursor.getCount() > 0)
            callback.onResponse(rounds);
        else
            callback.onError("No rounds found in database");
    }

    @Override
    public Round getRound(String roundId) {
        RoundCursorWrapper cursor = queryRounds();
        cursor.moveToFirst();
        boolean exists = false;
        Round round = null;
        while (!cursor.isAfterLast()) {
            try {
                round = cursor.getRound();
            } catch (ExcepcionJuego excepcionJuego) {
                excepcionJuego.printStackTrace();
            }
            if (round.getId().equals(roundId)) {
                exists = true;
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        if (exists)
            return round;
        else
            return null;
    }

    /*@Override
    public void getRound(String rounduuid, RoundCallback callback) throws ExcepcionJuego {
        RoundCursorWrapper cursor = queryRounds();
        cursor.moveToFirst();
        boolean exists = false;
        Round round = null;
        while (!cursor.isAfterLast()) {
            round = cursor.getRound();
            if (round.getRoundUUID().equals(rounduuid)) {
                exists = true;
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        if (exists)
            callback.onResponse(round);
        else
            callback.onError("No rounds found in database with id = " + rounduuid);
    }*/

}
