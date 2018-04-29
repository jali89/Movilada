/*
 * JugadorConsola.java
 *
 * By Alejandro Antonio Martín Almansa
 */

package es.uam.eps.dadm.jugador;

import java.util.Scanner;

import es.uam.eps.dadm.othello.MovimientoOthello;

import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

/**
 * Un JugadorConsola permite a los distintos Jugadores interaccionar con 
 * la consola para poder jugar.
 * Los JugadoresConsola tienen un nombre, que les identificará en el juego.
 *
 * @author Alejandro Antonio Martín Almansa
 */
public class JugadorConsola implements Jugador {

	private String nombre;
	
	/**
     * Constructor de la clase JugadorConsola.
     * @param nombre El nombre que identificará al jugador.
     */
	public JugadorConsola (String nombre) {
		this.nombre = nombre;
	}
	
	/**
     * Trata un evento que ha sucedido en la partida.
     * @param evento Evento que ha sucedido en la partida.
     */
	@Override
	public void onCambioEnPartida(Evento evento) {
		
		Partida p = evento.getPartida();
		Tablero t = p.getTablero();
		
		switch (evento.getTipo()) {
		
			case Evento.EVENTO_CAMBIO:
				
				System.out.print(evento.getDescripcion());
				System.out.print(t.toString());
				break;
				
			case Evento.EVENTO_TURNO:
				
				boolean fin = false;
				
				do {
					
					try {
						
						MovimientoOthello mov = null;
						
						if (t.movimientosValidos().get(0).equals(new MovimientoOthello(-1, -1))) {
							mov = new MovimientoOthello(-1, -1);
						} else {
							Scanner sc = new Scanner(System.in);
				    		
							System.out.print("Introduce la fila en la cual quieres mover:");
							int fila = sc.nextInt();
							System.out.print("Introduce la columna en la cual quieres mover:");
							int columna = sc.nextInt();
							
							sc.close();
							
							mov = new MovimientoOthello(fila, columna);
						}
						
						p.realizaAccion(new AccionMover(this, mov));
						
						fin = true;
				    	
			    	} catch (ExcepcionJuego e) {
			    		Evento ev = new Evento(Evento.EVENTO_ERROR, "Movimiento no válido.\n", evento.getPartida(), null);
			            this.onCambioEnPartida(ev);
			    	}
					
				} while (fin != true);
				
				break;
				
			case Evento.EVENTO_FIN:
				
				System.out.print(evento.getDescripcion());
				break;
				
			case Evento.EVENTO_ERROR:
				
				System.out.print(evento.getDescripcion());
				break;
				
			default:
				
				System.out.print("El tipo de evento no existe.");
				break;
		}
		
	}

	/**
     * Getter para obtener el nombre del JugadorConsola.
     * @return devuelve el nombre del JugadorConsola
     */
	@Override
	public String getNombre() {
		return this.nombre;
	}

	/**
     * Metodo por el cual se obtiene si un JugadorConsola puede jugar.
     * @return true, ya que el JugadorConsola siempre va a poder jugar.
     */
	@Override
	public boolean puedeJugar(Tablero tablero) {
		return true;
	}

}
