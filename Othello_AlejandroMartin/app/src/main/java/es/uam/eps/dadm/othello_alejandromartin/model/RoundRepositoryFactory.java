package es.uam.eps.dadm.othello_alejandromartin.model;

import android.content.Context;

import es.uam.eps.dadm.othello_alejandromartin.database.OthelloDataBase;

/**
 * Created by jalij on 22/03/2017.
 */

public class RoundRepositoryFactory {

    private static final boolean LOCAL = true;

    public static RoundRepository createRepository(Context context) {

        RoundRepository repository;
        repository = LOCAL ? new OthelloDataBase(context) : new OthelloDataBase(context);

        try {
            repository.open();
        }
        catch (Exception e) {
            return null;
        }

        return repository;
    }

}
