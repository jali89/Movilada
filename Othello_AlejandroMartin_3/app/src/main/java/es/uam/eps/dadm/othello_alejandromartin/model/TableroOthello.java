/*
 * TableroOthello.java
 *
 * By Alejandro Antonio Martin Almansa
 */

package es.uam.eps.dadm.othello_alejandromartin.model;
 
import java.util.ArrayList;
 
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;
 
/**
 * El TableroOthello es el tablero sobre el cual se desarrolla el juego
 * Othello.
 * El TableroOthello tiene una dimension de 8 x 8 casillas,
 * en el cual el jugador 1, al realizar un movimiento, depositara sobre
 * este un 1 como ficha. El jugador 2 hara lo propio pero con un 2,
 * mientras que si en una casilla vemos un 0, significa que la casilla esta
 * vacaa.
 *
 * @author Alejandro Antonio Martin Almansa
 */
public class TableroOthello extends Tablero {
	
	public static final int JUG2 = 0; // Valor del Turno del jugador 2
	public static final int VACIA = 0; // Casilla Vacia
	public static final int JUG1 = 1; // Valor del Turno del jugador 1
	public static final int JUG1_FICHA = 1; // Valor de la Ficha del jugador 1
	public static final int JUG2_FICHA = 2; // Variable auxiliar para pintar la ficha en el tablero
	public static final int TABLAS = 8; // Empate
	
    private int[][] casillas; // 0 si libre, 1 si jug1, 2 si jug2
    private boolean enMovsValids; // variable auxiliar para saber desde donde se llama a esValido
    private boolean pasaJug1; // variable auxiliar para saber si el jugador 1 ha pasado
    private boolean pasaJug2; // variable auxiliar para saber si el jugador 2 ha pasado
	private int tamano;
    
    /**
     * Constructor de la clase TableroOthello.
     */
    public TableroOthello (int tamano) {
    	
    	this.turno = JUG1;
    	this.estado = EN_CURSO;
    	this.enMovsValids = false;
    	this.pasaJug1 = false;
    	this.pasaJug2 = false;
		this.tamano = tamano;

    	// Inicializacion de las casillas vacias
    	this.casillas = new int[this.tamano][this.tamano];
    	for (int i = 0; i < this.tamano; i++) {
        	for (int j = 0; j < this.tamano; j++) {
            	this.casillas[i][j] = VACIA;
            }
        }
    	
    	// Disposicion inicial de las casillas
    	this.casillas[(this.tamano/2)-1][(this.tamano/2)-1] = JUG2_FICHA;
    	this.casillas[(this.tamano/2)-1][(this.tamano/2)] = JUG1_FICHA;
    	this.casillas[(this.tamano/2)][(this.tamano/2)-1] = JUG1_FICHA;
    	this.casillas[(this.tamano/2)][(this.tamano/2)] = JUG2_FICHA;
    	
    }
     
    /**
     * Metodo para poner una ficha en el tablero.
     * @param m Movimiento que realiza el jugador.
     */
    @Override
    protected void mueve(Movimiento m) throws ExcepcionJuego {
         
    	if (m instanceof MovimientoOthello) {
    		
    		// Si han pasado los dos jugadores
    		if (this.pasaJug1 == true && this.pasaJug2 == true) {
    			
    			// Se suman las fichas de cada jugador
    			int ganador1 = this.getPuntosJug1(), ganador2 = this.getPuntosJug2();

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
    			ExcepcionJuego e = new ExcepcionJuego("El movimiento no es valido.");
    			throw(e);
    		}
    		
    	} else {
			ExcepcionJuego e = new ExcepcionJuego("El movimiento no es de tipo Othello.");
			throw (e);
		}
    }
    
    /**
     * Metodo auxiliar para comprobar si el ganador es el jugador que tiene el turno.
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
     * Metodo auxiliar para pasar el turno cuando un jugador no puede mover.
     */
    private void pasar (int jugador) {
    	
    	if (jugador == JUG1)
    		this.pasaJug1 = true;
    	else
			this.pasaJug2 = true;
    	
    	this.cambiaTurno();
    	
    }
 
    /**
     * Metodo para comprobar si un determinado movimiento es valido.
     * @param m Movimiento a comprobar si es valido o no.
     * @return true si el movimiento es valido, false en caso contrario.
     */
    @Override
    public boolean esValido(Movimiento m) {
         
        if (m == null)
            return false;
         
        if (m instanceof MovimientoOthello) {
             
            MovimientoOthello mo = (MovimientoOthello) m;
            int fila = mo.getFila();
            int columna = mo.getColumna();
            
            boolean valido = false;
            valido = comprobarCasillaValida(fila, columna);	
        	
            return valido;
            
        }
         
        return false;
    }
    
    /**
     * Metodo auxiliar para comprobar si la casilla es valida o no.
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @return true si la casilla es valida, false en caso contrario.
     */
    public boolean comprobarCasillaValida (int fila, int columna) {

		if (saleDelTablero(fila, columna))
			return false;
        
    	// Se comprueba que la casilla esta vacia
    	if (this.casillas[fila][columna] == VACIA) {

			boolean flag = false;
     	
    		// Se comprueban todas las direcciones
	    	for (int i = -1; i != 2; i++) {
	    		for (int j = -1; j != 2; j++) {
					if (i == 0 && j == 0) {
						continue;
					} else {
						if (comprobarVecinosAdyacentesDireccion(fila, columna, i, j) == true) 
							flag = flag || true;
						else
							flag = flag || false;
					}
	    		}
	    	}

			return flag;
	   
    	}
    	
    	return false;
    }
    
    /**
     * Metodo auxiliar para comprobar si en una direccion determinada la casilla es valida.
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @param ejeX Direccion en el eje X.
     * @param ejeY Direccion en el eje Y.
     * @return true si la casilla es valida en la direccion indicada, false en caso contrario.
     */
    private boolean comprobarVecinosAdyacentesDireccion (int fila, int columna, int ejeX, int ejeY) {
    	
    	int fila_aux = fila  + ejeX, columna_aux = columna  + ejeY;
    	int casillaAdyacente = 0;
    	int vuelta = 0;
    	ArrayList<MovimientoOthello> casillasIntermedias = new ArrayList<MovimientoOthello>();
    	
    	while (!saleDelTablero(fila_aux, columna_aux)) {
        	
    		// Casilla adyacente a la que se esta comprobando, en la direccion dada
        	casillaAdyacente = this.casillas[fila_aux][columna_aux];
        	
        	if (casillaAdyacente == VACIA)
        		return false;
        	
        	// Obtener la ficha del jugador que esta jugando
        	int fichaCasillaTurno = (this.turno == JUG2 ? JUG2_FICHA : JUG1_FICHA);
        	
        	if (casillaAdyacente == fichaCasillaTurno) {
        		
        		// Si es la primera casilla adyacente, no se puede mover
        		if (vuelta == 0)
        			return false;
        		else {
        			// Si no se llama desde el metodo movimientosValidos,
        			// se comen las casillas intermedias
        			if (this.enMovsValids != true) {
        				cambiarCasillasIntermedias(casillasIntermedias);
            			this.casillas[fila][columna] = fichaCasillaTurno;
        			}
        			return true;
        		}
        	}
        	
        	// Se anaden a las casillas intermedias, hasta que se salga del tablero, o
        	// se encuentre una casilla vacia
        	casillasIntermedias.add(new MovimientoOthello(fila_aux, columna_aux));
        		
        	fila_aux  += ejeX;
        	columna_aux  += ejeY;
        	vuelta++;
    		
    	}
    	
    	return false;
    	
    }
    
    /**
     * Metodo auxiliar para comprobar si la casilla se sale del tablero.
     * @param fila Fila de la casilla.
     * @param columna Columna de la casilla.
     * @return true si la casilla se sale del tablero, false en caso contrario.
     */
    public boolean saleDelTablero (int fila, int columna) {
    	
    	if (fila > (this.tamano - 1) || fila < 0 || columna > (this.tamano - 1) || columna < 0)
    		return true;
    	
    	return false;
    }
    
    /**
     * Metodo auxiliar para cambiar las casillas comidas en un movimiento.
     * @param casillasIntermedias Casillas que se han comido.
     */
    private void cambiarCasillasIntermedias (ArrayList<MovimientoOthello> casillasIntermedias) {
    	
    	for (MovimientoOthello m : casillasIntermedias) {
    		this.casillas[m.getFila()][m.getColumna()] = (this.turno == JUG2 ? JUG2_FICHA : JUG1_FICHA);
    	}
    	
    }
 
    /**
     * Metodo para obtener todos los movimientos validos en un instante de juego determinado.
     * @return array de los movimientos validos.
     */
    @Override
    public ArrayList<Movimiento> movimientosValidos() {
         
        ArrayList<Movimiento> validos = new ArrayList<Movimiento>();
        int i, j;
        
        this.enMovsValids = true;
         
        for (i = 0; i < this.tamano; i++) {
        	for (j = 0; j < this.tamano; j++) {
        		MovimientoOthello m = new MovimientoOthello (i, j);
            	if (esValido(m) == true) {
            		validos.add(m);
            	}
            }
        }
        
        this.enMovsValids = false;
        
        // Si no hay movimientos validos se devuelve el movimiento Pasar
        if (validos.size() == 0)
        	validos.add(new MovimientoOthello (-1, -1));
         
        return validos;
    }
 
    // Los metodos tableroToString y stringToTablero son los mismos
    // que los de Victor Martin Hernandez
    
    /**
     * Metodo para codificar de un objeto Tablero a un String.
     * @return String codificado del Tablero.
     */
    @Override
    public String tableroToString() {
    	
        String tableroString = "";
        
        tableroString += this.turno + " " + this.estado + " " + this.numJugadas + " ";
        
        for (int i = 0; i < this.tamano; i++) {
        	for (int j = 0; j < this.tamano; j++) {
        		tableroString += this.casillas[i][j];
        		if (j % (this.tamano - 1) == 0 && j != 0)
        			tableroString += " ";
            }
        }
        		
        
        return tableroString;
    	
    }
 
    /**
     * Metodo para codificar de un String a un objeto Tablero.
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
    	
    	this.casillas = new int[this.tamano][this.tamano];
    	for (int i = 0, fila = 3; i < this.tamano; i++, fila ++) {
        	for (int j = 0; j < this.tamano; j++) {
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
    	
    	for (i = 0; i < this.tamano; i++) {
        	for (j = 0; j < this.tamano; j++) {
        		
        		tableroCadena += "| " + this.casillas[i][j] + " ";
        		
        		if (j % (this.tamano - 1) == 0 && j != 0) {
        			
        			tableroCadena += "|\n";
        			if (i != (this.tamano - 1))
            			tableroCadena += " " + (i+1) + " ";
        		}
            }
        }
    	
    	return tableroCadena;
    }

	/**
	 * Metodo para devolver una casilla del tablero.
	 * @param fila fila de la casilla
	 * @param columna columna de la casilla
	 * @return int casilla del tablero indicada por fila y columna.
	 */
	public int getTablero (int fila, int columna) {

		return this.casillas[fila][columna];

	}

	/**
	 * Metodo para devolver los puntos del jugador 1 (Blancas).
	 * @return int puntos del jugador 1.
	 */
	public int getPuntosJug1 () {
		int ganador1 = 0;
		for (int i = 0; i < this.tamano; i++) {
			for (int j = 0; j < this.tamano; j++) {
				if (this.casillas[i][j] == JUG1_FICHA)
					ganador1 += 1;
				else
					continue;
			}
		}
		return ganador1;
	}

	/**
	 * Metodo para devolver los puntos del jugador 2 (Negras).
	 * @return int puntos del jugador 2.
	 */
	public int getPuntosJug2 () {
		int ganador2 = 0;
		for (int i = 0; i < this.tamano; i++) {
			for (int j = 0; j < this.tamano; j++) {
				if (this.casillas[i][j] == JUG2_FICHA)
					ganador2 += 1;
				else
					continue;
			}
		}
		return ganador2;
	}
}