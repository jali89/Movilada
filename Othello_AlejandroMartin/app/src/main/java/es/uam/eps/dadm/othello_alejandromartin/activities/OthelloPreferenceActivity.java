package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.othello_alejandromartin.R;

/**
 * Created by jalij on 28/03/2017.
 */

public class OthelloPreferenceActivity  extends AppCompatActivity {

    public final static String BOARDSIZE_KEY = "boardsize";
    public final static String BOARDSIZE_DEFAULT = "0";

    public final static String PLAYERNAME_KEY = "playername";
    public final static String PLAYERNAME_DEFAULT = "Usuario";

    public final static String PLAYERUUID_KEY = "playeruuid";
    public final static String PLAYERUUID_DEFAULT = "0";

    public final static String PASSWORD_KEY = "password";
    public final static String PASSWORD_DEFAULT = "pass";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        OthelloPreferenceFragment fragment = new OthelloPreferenceFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public static String getBoardSize(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(BOARDSIZE_KEY, BOARDSIZE_DEFAULT);
    }

    public static void setBoardsize(Context context, int size) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OthelloPreferenceActivity.BOARDSIZE_KEY, size);
        editor.commit();
    }

    public static String getPlayerName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PLAYERNAME_KEY, PLAYERNAME_DEFAULT);
    }

    public static void setPlayerName(Context context, String name) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OthelloPreferenceActivity.PLAYERNAME_KEY, name);
        editor.commit();
    }

    public static String getPlayerUUID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PLAYERUUID_KEY, PLAYERUUID_DEFAULT);
    }

    public static void setPlayerUUID(Context context, String playerId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OthelloPreferenceActivity.PLAYERUUID_KEY, playerId);
        editor.commit();
    }

    public static String getPlayerPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PASSWORD_KEY, PASSWORD_DEFAULT);
    }

    public static void setPlayerPassword(Context context, String password) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(OthelloPreferenceActivity.PASSWORD_KEY, password);
        editor.commit();
    }

}
