/*
 * Round.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.model;

import java.util.Date;
import java.util.UUID;

import es.uam.eps.dadm.othello_alejandromartin.activities.OthelloPreferenceActivity;

/**
 * La clase Round es la que define cada ronda
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class Round {

    private int size;
    private String id;
    private String title;
    private String date;
    private TableroOthello board;
    private String playerid;
    private String firstPlayerName;
    private String secondPlayerName;

    /**
     * Constructor de la clase Round
     * @param size tamano del tablero de la ronda
     */
    public Round(int size) {
        this.size = size;
        this.id = UUID.randomUUID().toString();
        this.title = "ROUND " + id.toString().substring(19, 23).toUpperCase();
        this.date = new Date().toString();
        this.board = new TableroOthello(size);
    }

    public int getSize() { return size;}

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return this.date;
    }

    public TableroOthello getBoard() {
        return this.board;
    }

    public void setBoard(TableroOthello board) {
        this.board = board;
    }

    public String getPlayerUUID () {
        return this.playerid;
    }

    public void setPlayerUUID (String playerId) {
        this.playerid = playerId;
    }

    public void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }
    public String getFirstPlayerName() {
        return this.firstPlayerName;
    }

    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }

    public String getSecondPlayerName() {
        return this.secondPlayerName;
    }

    public void setSize (int size) {
        this.size = size;
    }

    public void setDate (String date) {
        this.date = date;
    }
}
