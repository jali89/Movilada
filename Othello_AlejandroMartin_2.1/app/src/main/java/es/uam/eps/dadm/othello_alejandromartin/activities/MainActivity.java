package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.activities.JugadorAndroid;
import es.uam.eps.dadm.othello_alejandromartin.model.TableroOthello;
import es.uam.eps.dadm.othello_alejandromartin.views.TableroOthelloView;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, PartidaListener {

    public static final String BOARDSTRING = "es.uam.eps.dadm.othello_alejandromartin.grid";
    private Partida game;
    private TableroOthello board;
    TableroOthelloView boardView;

    private final int ids[][] = {
            {R.id.o1, R.id.o2, R.id.o3},
            {R.id.o4, R.id.o5, R.id.o6},
            {R.id.o7, R.id.o8, R.id.o9}};

    private int SIZE = 3;

    @Override
    public void onClick(View view) {
        view.setBackgroundResource(R.drawable.green_button_48dp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRound();
    }

    private void registerListeners(JugadorAndroid local) {
        ImageButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) findViewById(ids[i][j]);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                            Toast.makeText(findViewById(R.id.board_othelloview).getContext(), R.string.round_already_finished, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        game.getTablero().reset();
                        startRound();
                        updateUI();
                        Toast.makeText(findViewById(R.id.board_othelloview).getContext(), R.string.round_restarted, Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }

    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        JugadorAndroid localPlayer = new JugadorAndroid();
        players.add(randomPlayer);
        players.add(localPlayer);

        boardView = (TableroOthelloView) findViewById(R.id.board_othelloview);
        boardView.setBoard(8, board);
        boardView.setOnPlayListener(localPlayer);

        game = new Partida(board, players);
        game.addObservador(this);
        localPlayer.setPartida(game);

        registerListeners(localPlayer);

        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }

    private void updateUI() {
        ImageButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) findViewById(ids[i][j]);
                if (board.getTablero(i, j) == TableroOthello.JUG1)
                    button.setBackgroundResource(R.drawable.blue_button_48dp);
                else if (board.getTablero(i, j) == TableroOthello.VACIA)
                    button.setBackgroundResource(R.drawable.void_button_48dp);
                else
                    button.setBackgroundResource(R.drawable.green_button_48dp);
            }
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                boardView.invalidate();
                updateUI();
                break;
            case Evento.EVENTO_FIN:
                boardView.invalidate();
                updateUI();
                Toast.makeText(findViewById(R.id.activity_main).getContext(), R.string.game_over, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            board.stringToTablero(savedInstanceState.getString(BOARDSTRING));
            updateUI();
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        outState.putString(BOARDSTRING, board.tableroToString());
        super.onSaveInstanceState(outState);
    }


    /*
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }*/

}
