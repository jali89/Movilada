package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import es.uam.eps.dadm.othello_alejandromartin.R;

/**
 * Created by jalij on 22/03/2017.
 */

public class OthelloPreferenceFragment extends PreferenceFragment {

        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }

}
