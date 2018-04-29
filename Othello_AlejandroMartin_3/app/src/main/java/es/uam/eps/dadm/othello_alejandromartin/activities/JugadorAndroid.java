/*
 * JugadorAndroid.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.activities;

import es.uam.eps.dadm.othello_alejandromartin.model.MovimientoOthello;
import es.uam.eps.dadm.othello_alejandromartin.views.OthelloView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * La clase JugadorAndroid es la encargada de gestionar las acciones que realiza el jugador
 * cuando pulsa la pantalla
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class JugadorAndroid implements Jugador, OthelloView.OnPlayListener {

    Partida game;
    private String nombre;

    /**
     * Constructor de la clase JugadorAndroid.
     * @param nombre nombre del jugador.
     */
    public JugadorAndroid(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo para asignarle una partida a un jugador.
     * @param game partida que se le va a asignar al jugador.
     */
    public void setPartida(Partida game) {
        this.game = game;
    }

    /**
     * Getter del atributo nombre del jugador.
     * @return String con el nombre del jugador.
     */
    @Override
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Metodo por el cual se obtiene si un JugadorConsola puede jugar.
     * @return true, ya que el JugadorAndroid siempre va a poder jugar.
     */
    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    /**
     * Trata un evento que ha sucedido en la partida. En esta implementacion no trata eventos,
     * ya que es el onPlay quien lo trata.
     * @param evento Evento que ha sucedido en la partida.
     */
    @Override
    public void onCambioEnPartida(Evento evento) {
    }

    /**
     * Trata la posición que ha pulsado el jugador.
     * @param row fila pulsada por el usuario.
     * @param column columna pulsada por el usuario.
     */
    @Override
    public void onPlay(int row, int column) {

        try {

            // Si la partida esta terminada no hace nada.
            if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                return;
            }

            // Si no, crea una accion y la aplica sobre la partida.
            MovimientoOthello m;
            m = new MovimientoOthello(row, column);
            game.realizaAccion(new AccionMover(this, m));

        } catch (Exception e) {
        }
    }
}
