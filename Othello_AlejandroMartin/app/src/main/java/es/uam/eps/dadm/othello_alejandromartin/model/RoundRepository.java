/*
 * RoundRepository.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.multij.ExcepcionJuego;

/**
 * RoundRepository es la interfaz atraves de la cual se conectara con la base de datos
 *
 * @author Alejandro Antonio Martin Almansa
 */
public interface RoundRepository {

    void open() throws Exception;

    void close();

    interface LoginRegisterCallback {
        void onLogin(String playerUuid);
        void onError(String error);
    }

    interface RoundsCallback {
        void onResponse(List<Round> rounds);
        void onError(String error);
    }

    void login(String playername, String password, LoginRegisterCallback callback);

    void register(String playername, String password, LoginRegisterCallback callback);

    interface BooleanCallback {
        void onResponse(boolean ok);
    }

    /**
     * Getter de las rondas del repositorio
     * @param playeruuid
     * @param orderByField
     * @param group
     * @param callback
     */
    void getRounds(String playeruuid, String orderByField, String group,
                   RoundsCallback callback) throws ExcepcionJuego;

    /**
     * Metodo para anadir una ronda al repositorio
     * @param round
     * @param callback
     */
    void addRound(Round round, BooleanCallback callback);

    Round getRound(String roundId);

    void updateRound(Round round, BooleanCallback callback);

}
