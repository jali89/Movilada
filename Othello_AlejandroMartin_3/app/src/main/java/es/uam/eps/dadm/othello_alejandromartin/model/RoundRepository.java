/*
 * RoundRepository.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase RoundRepository es la que almacena toda la lista de rondas
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class RoundRepository {

    private static final int SIZE = 8;
    private static RoundRepository repository;

    private List<Round> rounds;

    /**
     * Metodo para obtener un repositorio de partidas a partir de un contexto
     * @param context contexto del que se obtiene el repositorio
     * @return RoundRepository del contexto
     */
    public static RoundRepository get(Context context) {
        if (repository == null) {
            repository = new RoundRepository(context);
        }
        return repository;
    }

    /**
     * Constructor de la clase RoundRepository
     * @param context contexto
     */
    private RoundRepository(Context context) {
        rounds = new ArrayList<Round>();
    }

    /**
     * Getter de las rondas del repositorio
     * @return lista de rondas del repositorio
     */
    public List<Round> getRounds() {
        return rounds;
    }

    /**
     * Metodo para obtener una ronda del repositorio
     * @param id de la ronda que se desea obtener
     * @return ronda que corresponde con el id dado
     */
    public Round getRound(String id) {
        for (Round round : rounds) {
            if (round.getId().equals(id))
                return round;
        }
        return null;
    }

    /**
     * Metodo para anadir una ronda al repositorio
     * @param round ronda que se desea anadir
     */
    public void addRound(Round round) { rounds.add(round); }

    /**
     * Metodo para obtener el tamano del repositorio de rondas
     * @return tamano del repositorio de rondas
     */
    public static int getSIZE() {
        return SIZE;
    }
}
