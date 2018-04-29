package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.model.MovimientoOthello;
import es.uam.eps.dadm.othello_alejandromartin.views.TableroOthelloView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

/**
 * Created by jalij on 15/02/2017.
 */

public class JugadorAndroid implements Jugador, TableroOthelloView.OnPlayListener /*PartidaListener*/ {


    private final int ids[][] = {
            {R.id.o1, R.id.o2, R.id.o3},
            {R.id.o4, R.id.o5, R.id.o6},
            {R.id.o7, R.id.o8, R.id.o9}};

    private int SIZE = 3;

    private Partida partida;
    private String nombre;

    public JugadorAndroid () {
        this.nombre = "Jugador Android";
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

    }

    private int fromViewToI(View view) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (view.getId() == ids[i][j])
                    return i;
        return -1;
    }
    private int fromViewToJ(View view) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (view.getId() == ids[i][j])
                    return j;
        return -1;
    }

    @Override
    public void onPlay(int f, int c) {
        try {
            if (partida.getTablero().getEstado() != Tablero.EN_CURSO) {
                return;
            }
            MovimientoOthello m;
            m = new MovimientoOthello(f, c);
            partida.realizaAccion(new AccionMover(this, m));
        } catch (Exception e) {

        }
    }

}
