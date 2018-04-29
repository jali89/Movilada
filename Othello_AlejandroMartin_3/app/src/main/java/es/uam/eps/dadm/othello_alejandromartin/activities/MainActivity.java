/*
 * MainActivity.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.model.Round;

/**
 * La activivty MainActivity es la encargada de mostrar un fragmento de tipo RoundFramnet
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class MainActivity extends AppCompatActivity implements RoundFragment.Callbacks {

    public static final String EXTRA_ROUND_ID = "es.uam.eps.dadm.othello_alejandromartin.round_id";

    /**
     * Metodo que crea la activity con el fragmento
     * @param savedInstanceState objeto de tipo Builder que permite configurar el fragmento.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            RoundFragment roundFragment = RoundFragment.newInstance(roundId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, roundFragment)
                    .commit();
        }
    }

    /**
     * Metodo que instancia un Intent con una ronda y almacena el identificador de dicha ronda
     * @param packageContext contexto de la activity.
     * @param roundId id de la ronda a crear.
     */
    public static Intent newIntent(Context packageContext, String roundId){

        // Crea la nueva round
        Intent intent = new Intent(packageContext, MainActivity.class);

        // Le asigna el id
        intent.putExtra(EXTRA_ROUND_ID, roundId);

        return intent;
    }

    /**
     * Metodo de la interfaz Callbacks, para actualizar la ronda
     * @param round ronda que se actualiza
     */
    @Override
    public void onRoundUpdated(Round round) {
    }

}
