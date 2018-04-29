/*
 * RoundFragment.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.model.MovimientoOthello;
import es.uam.eps.dadm.othello_alejandromartin.model.Round;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepository;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepositoryFactory;
import es.uam.eps.dadm.othello_alejandromartin.model.TableroOthello;
import es.uam.eps.dadm.othello_alejandromartin.views.OthelloView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

/**
 * La clase RoundFragment es la actividad en la cual se muestra la partida o ronda
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class RoundFragment extends Fragment implements PartidaListener {

    public static final String DEBUG = "DEBUG";
    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.othello_alejandromartin.round_id";
    public static final String ARG_FIRST_PLAYER_NAME =
            "es.uam.eps.dadm.othello_alejandromartin.first_player_name";
    public static final String ARG_ROUND_TITLE = "es.uam.eps.dadm.othello_alejandromartin.round_title";
    public static final String ARG_ROUND_DATE = "es.uam.eps.dadm.othello_alejandromartin.round_date";
    public static final String ARG_ROUND_SIZE = "es.uam.eps.dadm.othello_alejandromartin.round_size";
    public static final String ARG_ROUND_BOARD = "es.uam.eps.dadm.othello_alejandromartin.round_board";

    private static final int SIZE = 8;

    private int size;
    private Round round;
    private String roundId;
    private String firstPlayerName;
    private String roundTitle;
    private String roundDate;
    private String roundSize;
    private String boardString;
    private Partida game;
    private Callbacks callbacks;
    private OthelloView boardView;

    /**
     * Interfaz que declara un fragmento
     */
    public interface Callbacks {
        void onRoundUpdated(Round round);
    }

    /**
     * Constructor de la clase RoundFragment
     */
    public RoundFragment() {
    }

    /**
     * Metodo estatico que devuelve una nueva instancia de un fragmento
     * @param roundId ronda del fragmento
     * @param firstPlayerName
     * @param roundTitle
     * @param roundSize
     * @param roundDate
     * @param roundBoard
     * @return RoundFragment creado
     */
    public static RoundFragment newInstance(String roundId, String firstPlayerName,
                                            String roundTitle, int roundSize, String roundDate, String roundBoard) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        args.putString(ARG_FIRST_PLAYER_NAME, firstPlayerName);
        args.putString(ARG_ROUND_TITLE, roundTitle);
        args.putString(ARG_ROUND_SIZE, Integer.toString(roundSize));
        args.putString(ARG_ROUND_DATE, roundDate);
        args.putString(ARG_ROUND_BOARD, roundBoard);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    /**
     * Metodo onCreate que crea el fragmento cuando se llama a esta activity
     * @param savedInstanceState objeto de tipo Builder que permite configurar el fragmento.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ROUND_ID)) {
            roundId = getArguments().getString(ARG_ROUND_ID);
        }
        if (getArguments().containsKey(ARG_FIRST_PLAYER_NAME)) {
            firstPlayerName = getArguments().getString(ARG_FIRST_PLAYER_NAME);
        }
        if (getArguments().containsKey(ARG_ROUND_TITLE)) {
            roundTitle = getArguments().getString(ARG_ROUND_TITLE);
        }
        if (getArguments().containsKey(ARG_ROUND_SIZE)) {
            roundSize = getArguments().getString(ARG_ROUND_SIZE);
        }
        if (getArguments().containsKey(ARG_ROUND_DATE)) {
            roundDate = getArguments().getString(ARG_ROUND_DATE);
        }
        if (getArguments().containsKey(ARG_ROUND_BOARD))
            boardString = getArguments().getString(ARG_ROUND_BOARD);

        round = createRound();
        size = round.getSize();
    }

    private void updateRound() {
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {
            @Override
            public void onResponse(boolean response) {
                if (response == false)
                    Toast.makeText(getContext(), R.string.error_updating_round, Toast.LENGTH_SHORT).show();
            }
        };
        repository.updateRound(round, callback);
    }

    private Round createRound() {
        Round round = new Round(Integer.parseInt(roundSize));
        round.setPlayerUUID(OthelloPreferenceActivity.getPlayerUUID(getActivity()));
        round.setId(roundId);
        round.setFirstPlayerName("random");
        round.setSecondPlayerName(firstPlayerName);
        round.setDate(roundDate);
        round.setTitle(roundTitle);
        TableroOthello to = new TableroOthello(SIZE);
        try {
            to.stringToTablero(boardString);
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
        round.setBoard(to);
        return round;
    }

    /**
     * Metodo llamado cuando la interfaz del fragmento se diseña por primera vez
     * @param inflater inflador de la vista del fragmento
     * @param container contenedor de la vista del fragmento
     * @param savedInstanceState objeto de tipo Builder que permite configurar el fragmento.
     * @return View creada
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_round, container,
                false);
        TextView roundTitleTextView = (TextView)
                rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());
        return rootView;
    }

    /**
     * Metodo para crear la partida o ronda
     */
    @Override
    public void onStart() {
        super.onStart();
        startRound();
    }

    /**
     * Metodo para registrar los listeners del tablero del fragmento
     */
    private void registerListeners() {
        
        FloatingActionButton resetButton = (FloatingActionButton)
                getView().findViewById(R.id.reset_round_fab);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                round.getBoard().reset();
                TableroOthello board = new TableroOthello(SIZE);
                round.setBoard(board);
                startRound();
                callbacks.onRoundUpdated(round);
                boardView.invalidate();
                Toast.makeText(getContext(), R.string.round_restarted, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Metodo para retomar un fragmento
     */
    @Override
    public void onResume() {
        super.onResume();
        boardView.invalidate();
    }

    /**
     * Metodo para arignar los callbacks al fragmento
     * @param context contexto del fragmento
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    /**
     * Metodo que asigna null al miembro callbacks cuando el fragmento se destruye
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    /**
     * Metodo para dar comienzo a una partida
     */
    void startRound() {

        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        JugadorAndroid localPlayer = new JugadorAndroid("Jug1");
        players.add(randomPlayer);
        players.add(localPlayer);

        game = new Partida(round.getBoard(), players);
        game.addObservador(this);

        localPlayer.setPartida(game);

        boardView = (OthelloView) getView().findViewById(R.id.board_othelloview);
        boardView.setBoard(size, round.getBoard());
        boardView.setOnPlayListener(localPlayer);

        registerListeners();

        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }

    /**
     * Metodo para controlar cuando hay un cambio en una partida,
     * tratarlo y actuar según el evento que llegue
     * @param evento evento que se ha producido
     */
    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                boardView.invalidate();

                // Se obtiene el marcador
                TextView sc1 = (TextView) getView().findViewById(R.id.jug1_score);
                TextView sc2 = (TextView) getView().findViewById(R.id.jug2_score);
                TextView t = (TextView) getView().findViewById(R.id.turno);
                int white_score = boardView.getBoard().getPuntosJug1();
                int black_score = boardView.getBoard().getPuntosJug2();


                // Se obtiene el turno
                int turn = boardView.getBoard().getTurno();
                String turnStr = (String) (turn == TableroOthello.JUG2 ? getView().getResources().getText(R.string.black)
                        : getView().getResources().getText(R.string.white));
                sc1.setText(getView().getResources().getText(R.string.white) + " " + String.valueOf(white_score));
                sc2.setText(getView().getResources().getText(R.string.black) + " " + String.valueOf(black_score));
                t.setText(getView().getResources().getText(R.string.turn) + " " + turnStr);

                MovimientoOthello m;

                if (game.getTablero().movimientosValidos().get(0).equals(new MovimientoOthello(-1, -1))) {
                    m = new MovimientoOthello(-1, -1);
                    try {
                        game.realizaAccion(new AccionMover(game.getJugador(boardView.getBoard().getTurno()), m));
                    } catch (ExcepcionJuego excepcionJuego) {
                        excepcionJuego.printStackTrace();
                    }
                }

                callbacks.onRoundUpdated(round);
                updateRound();
                break;

            case Evento.EVENTO_FIN:
                boardView.invalidate();
                updateRound();
                callbacks.onRoundUpdated(round);
                // Cuando termina la partida se crea un AlertDialogFragment,
                // para mostrar quien ha ganado y si se quiere volver a jugar
                new AlertDialogFragment().show(getActivity().getSupportFragmentManager(),
                        "ALERT DIALOG");
                break;
        }
    }

}
