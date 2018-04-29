package es.uam.eps.dadm.othello_alejandromartin.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import es.uam.eps.dadm.othello_alejandromartin.model.Round;
import es.uam.eps.dadm.othello_alejandromartin.model.TableroOthello;
import es.uam.eps.multij.ExcepcionJuego;

import static es.uam.eps.dadm.othello_alejandromartin.database.RoundDataBaseSchema.UserTable;
import static es.uam.eps.dadm.othello_alejandromartin.database.RoundDataBaseSchema.RoundTable;

/**
 * Created by jalij on 22/03/2017.
 */

public class RoundCursorWrapper extends CursorWrapper {

    private final String DEBUG = "DEBUG";

    public RoundCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Round getRound() throws ExcepcionJuego {

        String playername = getString(getColumnIndex(UserTable.Cols.PLAYERNAME));
        String playerid = getString(getColumnIndex(UserTable.Cols.PLAYERUUID));
        String size = getString(getColumnIndex(RoundTable.Cols.SIZE));
        String roundID = getString(getColumnIndex(RoundTable.Cols.ROUNDUUID));
        String date = getString(getColumnIndex(RoundTable.Cols.DATE));
        String title = getString(getColumnIndex(RoundTable.Cols.TITLE));
        String board = getString(getColumnIndex(RoundTable.Cols.BOARD));

        Round round = new Round(Integer.parseInt(size));

        round.setFirstPlayerName("random");
        round.setSecondPlayerName(playername);
        round.setPlayerUUID(playerid);
        TableroOthello to = new TableroOthello(Integer.parseInt(size));
        to.stringToTablero(board);
        round.setBoard(to);
        round.setSize(Integer.parseInt(size));
        round.setId(roundID);
        round.setDate(date);
        round.setTitle(title);

        try {
            round.getBoard().stringToTablero(board);
        } catch (ExcepcionJuego e) {
            Log.d(DEBUG, "Error turning string into tablero");
        }

        return round;
    }

}
