/*
 * MovimientoOthello.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.model;

import es.uam.eps.multij.Movimiento;

/**
 * Un MovimientoOthello permite a los distintos Jugadores crear movimientos
 * para poder jugar.
 * Los Movimientos de Othello tienen una fila y una columna, que determinan
 * la posicion en la que se colocara la ficha del movimiento.
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class MovimientoOthello extends Movimiento {
	
	private int fila;
	private int columna;
	
	/**
     * Constructor de la clase MovimientoOthello.
     * @param fila La fila seleccionada para el movimiento.
     * @param columna La columna seleccionada para el movimiento.
     */
	public MovimientoOthello (int fila, int columna) {
		
		this.fila = fila;
		this.columna = columna;
		
	}

	/**
     * toString de la clase MovimientoOthello.
     * @return devuelve el String del objeto MovimientoOthello.
     */
	@Override
	public String toString() {
		
		return "Fila: " + this.fila + ", Columna: " + this.columna + ".";
	}

	/**
     * Compara dos objetos de la clase MovimientoOthello.
     * @param o Movimiento a comparar.
     * @return true si los movimientos son iguales, false en caso contrario.
     */
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof MovimientoOthello) {
			
			MovimientoOthello mo = (MovimientoOthello) o;
			
			if ((this.fila == mo.fila) && 
					(this.columna == mo.columna)) {
				return true;
			}
		}
		
		return false;
	}

	/**
     * Getter para obtener la fila del MovimientoOthello.
     * @return Coordenada fila del MovimientoOthello.
     */
	public int getFila() {
		return fila;
	}

	/**
     * Getter para obtener la columna del MovimientoOthello.
     * @return Coordenada columna del MovimientoOthello.
     */
	public int getColumna() {
		return columna;
	}

}
