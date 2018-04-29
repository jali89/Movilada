/*
 * RoundListFragment.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.database.OthelloDataBase;
import es.uam.eps.dadm.othello_alejandromartin.model.Round;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepository;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepositoryFactory;
import es.uam.eps.multij.ExcepcionJuego;

/**
 * La clase RoundListFragment es la actividad en la cual se muestra la lista de partidas
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class RoundListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private RecyclerView roundRecyclerView;
    private RoundAdapter roundAdapter;
    private Callbacks callbacks;

    /**
     * Interfaz que declara una lista de fragmentos
     */
    public interface Callbacks {
        void onRoundSelected(Round round);
        void onPreferencesSelected();
        void onNewRoundAdded();
    }

    /**
     * Metodo que crea la lista de fragmentos
     * @param savedInstanceState objeto de tipo Builder que permite configurar la lista de
     *                          fragmentos.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Metodo para crear opciones del menu en la barra de tareas
     * @param menu barra de tareas
     * @param inflater inflador de la barra de tareas
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onItemClick(View view, final int position) {
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback()
        {
            @Override
            public void onResponse(List<Round> rounds) {
                callbacks.onRoundSelected(rounds.get(position));
            }
            @Override
            public void onError(String error) {

            }
        };
        String playeruuid = OthelloPreferenceActivity.getPlayerUUID(getActivity());
        try {
            repository.getRounds(playeruuid, null, null, roundsCallback);
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
    }

    /**
     * Metodo que gestiona sobre que icono de la barra de tareas se ha pinchado
     * @param item icono sobre el que se ha pinchado
     * @return true si se ha seleccionado un icono, false en caso contrario
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_round:
                /* Instanciar una partida e inicializarla adecuadamente
                Crear el repositorio
                Instanciar un método callback de tipo BooleanCallback y llamar a
                onNewRoundAdded de callbacks. */
                Round round = new Round(8);
                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                round.setPlayerUUID(OthelloPreferenceActivity.getPlayerUUID(getActivity()));
                repository.addRound(round, new RoundRepository.BooleanCallback() {
                    @Override
                    public void onResponse(boolean ok) {
                        if (ok) {
                            callbacks.onNewRoundAdded();
                        } else
                            Toast.makeText(getActivity(), "Error creando Round", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;
            case R.id.menu_item_logout:
                OthelloPreferenceActivity.setPlayerUUID(getContext(), null);
                OthelloPreferenceActivity.setPlayerName(getContext(), null);
                OthelloPreferenceActivity.setPlayerPassword(getContext(), null);
                startActivity(new Intent(getContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo para arignar los callbacks a la lista de fragmentos
     * @param context contexto de la lista de fragmentos
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    /**
     * Metodo que asigna null al miembro callbacks cuando la lista de fragmentos se destruye
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    /**
     * Metodo llamado cuando la interfaz dela lista de fragmentos se diseña por primera vez
     * @param inflater inflador de la vista de la lista de fragmentos
     * @param container contenedor de la vista de la lista de fragmentos
     * @param savedInstanceState objeto de tipo Builder que permite configurar la lista de
     *                           fragmentos.
     * @return View creada
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_list, container, false);

        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new
                LinearLayoutManager(getActivity());

        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());

        roundRecyclerView.addOnItemTouchListener(new
                RecyclerItemClickListener(getActivity(), new
                RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
                        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback()
                        {
                            @Override
                            public void onResponse(List<Round> rounds) {
                                callbacks.onRoundSelected(rounds.get(position));
                            }
                            @Override
                            public void onError(String error) {
                                Toast.makeText(getContext(), R.string.error_reading_rounds, Toast.LENGTH_SHORT).show();
                            }
                        };
                        String playeruuid = OthelloPreferenceActivity.getPlayerUUID(getActivity());
                        try {
                            repository.getRounds(playeruuid, null, null, roundsCallback);
                        } catch (ExcepcionJuego excepcionJuego) {
                            excepcionJuego.printStackTrace();
                        }
                    }
                }));

        updateUI();

        return view;
    }

    /**
     * Metodo para retomar una lista de fragmentos
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * Metodo para actualizar la interfaz de la lista de fragmentos
     */
    public void updateUI() {
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        try {
            repository.getRounds(OthelloPreferenceActivity.getPlayerUUID(getActivity()), null, null, new RoundRepository.RoundsCallback() {
                @Override
                public void onResponse(List<Round> rounds) {
                    roundAdapter = new RoundAdapter(rounds);
                    roundRecyclerView.setAdapter(roundAdapter);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                }
            });
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }

    }

    /**
     * Clase RoundAdapter para manejar la lista de fragmentos
     */
    public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder> {

        private List<Round> rounds;

        /**
         * Clase RoundHolder que gestionara la clase RoundAdapter para mantener actualizada
         * la lista de fragmentos.
         */
        public class RoundHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

            private TextView idTextView;
            private TextView boardTextView;
            private TextView dateTextView;

            private Round round;

            /**
             * Constructor de la clase RoundHolder
             * @param itemView vista que se quiere gestionar
             */
            public RoundHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                boardTextView = (TextView) itemView.findViewById(R.id.list_item_board);
                dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            }

            /**
             * Metodo que enlaza con una ronda
             * @param round ronda indicada con la cual se va a enlazar
             */
            public void bindRound(Round round){
                this.round = round;
                idTextView.setText(round.getTitle());
                int white_score = round.getBoard().getPuntosJug1();
                int black_score = round.getBoard().getPuntosJug2();
                boardTextView.setText(getView().getResources().getText(R.string.white) + " " +
                        String.valueOf(white_score) + "\n" +
                        getView().getResources().getText(R.string.black) + " " +
                        String.valueOf(black_score));
                dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
            }

            /**
             * Metodo onClick para gestionar cuando se pulsa sobre un elemento de la lista
             * @param v
             */
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = MainActivity.newIntent(context, round.getId());
                context.startActivity(intent);
            }
        }

        /**
         * Constructor de la clase RoundAdapter
         * @param rounds lista de rondas que va a gestionar
         */
        public RoundAdapter(List<Round> rounds){
            this.rounds = rounds;
        }

        /**
         * Metodo para crear la vista de un RoundHolder a partir de un RoundAdapter
         * @param parent clase padre
         * @param viewType tipo de vista
         * @return RoundAdapter.RoundHolder, para gestionar la clase RoundAdapter y
         * mantener actualizada la lista de fragmentos
         */
        @Override
        public RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);
            return new RoundHolder(view);
        }

        /**
         * Metodo que enlaza con una ronda
         * @param holder gestor de la ronda
         * @param position posicion de la ronda seleccionada
         */
        @Override
        public void onBindViewHolder(RoundAdapter.RoundHolder holder, int position) {
            Round round = rounds.get(position);
            holder.bindRound(round);
        }

        /**
         * Metodo para obtener el numero de rondas que contiene la clase
         * @return numero de rondas
         */
        @Override
        public int getItemCount() {
            return rounds.size();
        }

    }

}
