/*
 * RoundListActivity.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.model.Round;

/**
 * La clase RoundListActivity es un gestor que mantiene dos listas,
 * una de fragmentos y otra de transacciones
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks, RoundFragment.Callbacks{

    /**
     * Metodo para crear la actividad gestora de fragmentos y de transacciones
     * @param savedInstanceState objeto de tipo Builder que permite configurar el gestor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new RoundListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    /**
     * Metodo que gestiona cuando se pincha sobre un fragmento de la lista de rondas
     * @param round ronda sobre la que se pincha
     */
    @Override
    public void onRoundSelected(Round round) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = MainActivity.newIntent(this, round.getId());
            startActivity(intent);
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId(),
                    round.getFirstPlayerName(), round.getTitle(), round.getSize(),
                    round.getDate(), round.getBoard().tableroToString());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, roundFragment)
                    .commit();
        }
    }

    /**
     * Metodo que gestiona cuando se actualiza una ronda de la lista
     * @param round ronda que se actualiza
     */
    @Override
    public void onRoundUpdated(Round round) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)
                fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }

    @Override
    public void onPreferencesSelected() {
        Intent intent = new Intent(this, OthelloPreferenceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNewRoundAdded() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)
                fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }
}
