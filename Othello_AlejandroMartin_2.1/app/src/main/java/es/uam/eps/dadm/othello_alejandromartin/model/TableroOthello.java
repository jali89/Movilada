/*
 * TableroOthello.java
 *
 * By Alejandro Antonio Mart�n Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.model;
 
import java.util.ArrayList;
 
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;
 
/**
 * El TableroOthello es el tablero sobre el cual se desarrolla el juego
 * Othello.
 * El TableroOthello tiene una dimensi�n de 8 x 8 casillas, 
 * en el cual el jugador 1, al realizar un movimiento, depositar� sobre 
 * �ste un 1 como ficha. El jugador 2 har� lo propio pero con un 2, 
 * mientras que si en una casilla vemos un 0, significa que la casilla est� 
 * vac�a. 
 *
 * @author Alejandro Antonio Mart�n Almansa
 */
public class TableroOthello extends Tablero {
	
	public static final int JUG2 = 0; // Valor del Turno del jugador 2
	public static final int VACIA = 0; // Casilla Vac�a
	public static final int JUG1 = 1; // Valor del Turno y Ficha del jugador 1
	public static final int JUG2_FICHA = 2; // Variable auxiliar para pintar la ficha en el tablero
	public static final int TAMANO = 8; // Tama�o del tablero
	
    private int[][] casillas; // 0 si libre, 1 si jug1, 2 si jug2
    private boolean enMovsValids; // variable auxiliar para saber desde donde se llama a esValido
    private boolean pasaJug1; // variable auxiliar para saber si el jugador 1 ha pasado
    private boolean pasaJug2; // variable auxiliar para saber si el jugador 2 ha pasado
    
    /**
     * Constructor de la clase TableroOthello.
     */
    public TableroOthello () {
    	
    	this.turno = JUG1;
    	this.estado = EN_CURSO;
    	this.enMovsValids = false;
    	this.pasaJug1 = false;
    	this.pasaJug2 = false;

    	// Inicializaci�n de las casillas vac�as
    	this.casillas = new int[TAMANO][TAMANO];
    	for (int i = 0; i < TAMANO; i++) {
        	for (int j = 0; j < TAMANO; j++) {
            	this.casillas[i][j] = VACIA;
            }
        }
    	
    	// Disposici�n inicial de las casillas
    	this.casillas[3][3] = JUG2_FICHA;
    	this.casillas[3][4] = JUG1;
    	this.casillas[4][3] = JUG1;
    	this.casillas[4][4] = JUG2_FICHA;
    	
    }
     
    /**
     * M�todo para poner una ficha en el tablero.
     * @param m Movimiento que realiza el jugador.
     */
    @Override
    protected void mueve(Movimiento m) throws ExcepcionJuego {
         
    	if (m instanceof MovimientoOthello) {
    		
    		// Si han pasado los dos jugadores
    		if (this.pasaJug1 == true && this.pasaJug2 == true) {
    			
    			// Se suman las fichas de cada jugador
    			int ganador1 = 0, ganador2 = 0;
    			for (int i = 0; i < TAMANO; i++) {
    	        	for (int j = 0; j < TAMANO; j++) {
    	            	if (this.casillas[i][j] == JUG1) 
    	            		ganador1 += 1;
    	            	else if (this.casillas[i][j] == JUG2_FICHA)
    	            		ganador2 += 1;
    	            	else
    	            		continue;
    	            }
    	        }
    			
    			// Se comprueba el ganador
				if (ganador1 > ganador2)
					comprobarTurnoGanador(JUG1);
				else if (ganador1 < ganador2)
					comprobarTurnoGanador(JUG2);
				else {
					this.estado = TABLAS;
					return;
				}
    				
    			this.estado = FINALIZADA;
    			return;
    		}
    		
    		// Si llega este movimiento significa que el jugador ha pasado
    		if (m.equals(new MovimientoOthello(-1, -1))) {
    			this.ultimoMovimiento = new MovimientoOthello(-1, -1);
    			pasar(this.turno);
    			return;
    		}
    		
    		MovimientoOthello mo = (MovimientoOthello) m;
    		
    		if (esValido(mo)) {
    			this.cambiaTurno();
    			this.ultimoMovimiento = mo;
    			// Si se mueve se vuelven a reiniciar las variables de pasar
    			this.pasaJug1 = false;
    	    	this.pasaJug2 = false;
    		} else {
    			ExcepcionJuego e = new ExcepcionJuego("El movimiento no es v�lido.");
    			throw(e);
    		}
    		
    	} else {
    		ExcepcionJuego e = new ExcepcionJuego("El movimiento no es de tipo Othello.");
			throw(e);
    	}
	   
         
    }
    
    /**
     * M�todo auxiliar para comprobar si el ganador es el jugador que tiene el turno.
     * Si el ganador es el jugador que tiene el turno se retorna, en caso contrario, 
     * se cambia el turno.
     */
    private void comprobarTurnoGanador (int ganador) {
    	
    	if (this.turno == ganador)
    		return;
    	else
    		this.cambiaTurno();
    }
    
    /**
     * M�todo auxiliar para pasar el turno cuando un jugador no puede mover.
     */
    private void pasar (int jugador) {
    	
    	if (jugador == JUG1)
    		this.pasaJug1 = true;
    	else
			this.pasaJug2 = true;
    	
    	this.cambiaTurno();
    	
    }
 
    /**
     * M�todo para comprobar si un determinado movimiento es v�lido.
     * @param m Movimiento a comprobar si es v�lido o no.
     * @return true si el movimiento es v�lido, false en caso contrario.
     */
    @Override
    public boolean esValido(Movimiento m) {
         
        if (m == null)
            return false;
         
        if (m instanceof MovimientoOthello) {
             
            MovimientoOthello mo = (MovimientoOthello) m;
            int fila = mo.getFila();
            int columna = mo.getColumna();
            
            if (saleDelTablero(((MovimientoOthello) m).getFila(), ((MovimientoOthello) m).getColumna()))
            	return false;
            
            boolean valido = false;
            valido = comprobarCasillaValida(fila, columna);	
        	
            return valido;
            
        }
         
        return false;
    }
    
    /**
     * M�todo auxiliar para comprobar si la casilla es v�lida o no.
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @return true si la casilla es v�lida, false en caso contrario.
     */
    public boolean comprobarCasillaValida (int fila, int columna) {
        
    	// Se comprueba que la casilla est� vac�a
    	if (this.casillas[fila][columna] == VACIA) {
     	
    		// Se comprueban todas las direcciones
	    	for (int i = -1; i != 2; i++) {
	    		for (int j = -1; j != 2; j++) {
					if (i == 0 && j == 0) {
						continue;
					} else {
						if (comprobarVecinosAdyacentesDireccion(fila, columna, i, j) == true) 
							return true;
					}
	    		}
	    	}
	   
    	}
    	
    	return false;
    }
    
    /**
     * M�todo auxiliar para comprobar si en una direcci�n determinada la casilla es v�lida.
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @param ejeX Direcci�n en el eje X.
     * @param ejeY Direcci�n en el eje Y.
     * @return true si la casilla es v�lida en la direcci�n indicada, false en caso contrario.
     */
    public boolean comprobarVecinosAdyacentesDireccion (int fila, int columna, int ejeX, int ejeY) {
    	
    	int fila_aux = fila  + ejeX, columna_aux = columna  + ejeY;
    	int casillaAdyacente = 0;
    	int vuelta = 0;
    	ArrayList<MovimientoOthello> casillasIntermedias = new ArrayList<MovimientoOthello>();
    	
    	while (!saleDelTablero(fila_aux, columna_aux)) {
        	
    		// Casilla adyacente a la que se est� comprobando, en la direcci�n dada
        	casillaAdyacente = this.casillas[fila_aux][columna_aux];
        	
        	if (casillaAdyacente == 0)
        		return false;
        	
        	// Obtener la ficha del jugador que est� jugando
        	int fichaCasillaTurno = (this.turno == JUG2 ? JUG2_FICHA : JUG1);
        	
        	if (casillaAdyacente == fichaCasillaTurno) {
        		
        		// Si es la primera casilla adyacente, no se puede mover
        		if (vuelta == 0)
        			return false;
        		else {
        			// Si no se llama desde el m�todo movimientosValidos,
        			// se comen las casillas intermedias
        			if (this.enMovsValids != true) {
        				cambiarCasillasIntermedias(casillasIntermedias);
            			this.casillas[fila][columna] = fichaCasillaTurno;
        			}
        			return true;
        		}
        	}
        	
        	// Se a�aden a las casillas intermedias, hasta que se salga del tablero, o
        	// se encuentre una casilla vac�a
        	casillasIntermedias.add(new MovimientoOthello(fila_aux, columna_aux));
        		
        	fila_aux  += ejeX;
        	columna_aux  += ejeY;
        	vuelta++;
    		
    	}
    	
    	return false;
    	
    }
    
    /**
     * M�todo auxiliar para comprobar si la casilla se sale del tablero.
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @return true si la casilla se sale del tablero, false en caso contrario.
     */
    public boolean saleDelTablero (int fila, int columna) {
    	
    	if (fila > (TAMANO - 1) || fila < 0 || columna > (TAMANO - 1) || columna < 0)
    		return true;
    	
    	return false;
    }
    
    /**
     * M�todo auxiliar para cambiar las casillas comidas en un movimiento.
     * @param casillasIntermedias Casillas que se han comido.
     */
    public void cambiarCasillasIntermedias (ArrayList<MovimientoOthello> casillasIntermedias) {
    	
    	for (MovimientoOthello m : casillasIntermedias) {
    		this.casillas[m.getFila()][m.getColumna()] = (this.turno == JUG2 ? JUG2_FICHA : JUG1);
    	}
    	
    }
 
    /**
     * M�todo para obtener todos los movimientos v�lidos en un instante de juego determinado.
     * @return array de los movimientos v�lidos.
     */
    @Override
    public ArrayList<Movimiento> movimientosValidos() {
         
        ArrayList<Movimiento> validos = new ArrayList<Movimiento>();
        int i, j;
        
        this.enMovsValids = true;
         
        for (i = 0; i < TAMANO; i++) {
        	for (j = 0; j < TAMANO; j++) {
        		MovimientoOthello m = new MovimientoOthello (i, j);
            	if (esValido(m) == true) {
            		validos.add(m);
            	}
            }
        }
        
        this.enMovsValids = false;
        
        // Si no hay movimientos v�lidos se devuelve el movimiento Pasar
        if (validos.size() == 0)
        	validos.add(new MovimientoOthello (-1, -1));
         
        return validos;
    }
 
    // Los m�todos tableroToString y stringToTablero son los mismos 
    // que los de Victor Mart�n Hern�ndez
    
    /**
     * M�todo para codificar de un objeto Tablero a un String.
     * @return String codificado del Tablero.
     */
    @Override
    public String tableroToString() {
    	
        String tableroString = "";
        
        tableroString += this.turno + " " + this.estado + " " + this.numJugadas + " ";
        
        for (int i = 0; i < TAMANO; i++) {
        	for (int j = 0; j < TAMANO; j++) {
        		tableroString += this.casillas[i][j];
        		if (j % (TAMANO - 1) == 0 && j != 0)
        			tableroString += " ";
            }
        }
        		
        
        return tableroString;
    	
    }
 
    /**
     * M�todo para codificar de un String a un objeto Tablero.
     * @param cadena String codificado para pasar a Tablero.
     */
    @Override
    public void stringToTablero(String cadena) throws ExcepcionJuego {
    		
    	this.numJugadores = 2;
    	this.enMovsValids = false;
    	this.ultimoMovimiento = null;
    	this.pasaJug1 = false;
    	this.pasaJug2 = false;
    	
    	String[] elementosTablero = cadena.split(" ");
    	this.turno = Integer.parseInt(elementosTablero[0]);
    	this.estado = Integer.parseInt(elementosTablero[1]);
    	this.numJugadas = Integer.parseInt(elementosTablero[2]);
    	
    	this.casillas = new int[TAMANO][TAMANO];
    	for (int i = 0, fila = 3; i < TAMANO; i++, fila ++) {
        	for (int j = 0; j < TAMANO; j++) {
            	this.casillas[i][j] = Character.getNumericValue((elementosTablero[fila].charAt(j)));
            }
        }
    }
 
    /**
     * toString de la clase TableroOthello.
     * @return String devuelve el String del objeto TableroOthello.
     */
    @Override
    public String toString() {
        
    	int i, j;
    	
    	String tableroCadena;
    	
    	tableroCadena = "\n\n-------------------------------------------";
    	tableroCadena += "\nEstado: " + this.estado + "\n";
    	tableroCadena += "Numero de jugadores: " + this.numJugadores + "\n";
    	tableroCadena += "Numero de jugadas: " + this.numJugadas + "\n";
    	tableroCadena += "Turno jugador: " + (this.turno == 0 ? 2 : JUG1) + "\n\n";
    	tableroCadena += "     0   1   2   3   4   5   6   7\n";
    	tableroCadena += "    ___ ___ ___ ___ ___ ___ ___ ___\n 0 ";
    	
    	for (i = 0; i < TAMANO; i++) {
        	for (j = 0; j < TAMANO; j++) {
        		
        		tableroCadena += "| " + this.casillas[i][j] + " ";
        		
        		if (j % (TAMANO - 1) == 0 && j != 0) {
        			
        			tableroCadena += "|\n";
        			if (i != (TAMANO - 1))
            			tableroCadena += " " + (i+1) + " ";
        		}
            }
        }
    	
    	return tableroCadena;
    }

	public int getTablero (int fila, int columna) {

		return this.casillas[fila][columna];

	}
}