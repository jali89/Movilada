/*
 * TableroOthello.java
 *
 * By Alejandro Antonio Martín Almansa
 */

package es.uam.eps.dadm.othello;
 
import java.util.ArrayList;
 
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;
 
/**
 * El TableroOthello es el tablero sobre el cual se desarrolla el juego
 * Othello.
 * El TableroOthello tiene una dimensión de 8 x 8 casillas
 *
 * @author Alejandro Antonio Martín Almansa
 */
public class TableroOthello extends Tablero {
	
	public static final int JUG2 = 0;
	public static final int VACIA = 0;
	public static final int JUG1 = 1;
	public static final int JUG2_FICHA = 2; // Variable auxiliar para pintar tablero
	public static final int TAMANO = 8;
	
    private int[][] casillas; // 0 si libre, 1 si jug1, 2 si jug2
    private boolean enMovsValids;
    private boolean pasaJug1;
    private boolean pasaJug2;
    
    public TableroOthello () {
    	
    	this.turno = JUG1;
    	this.estado = EN_CURSO;
    	this.enMovsValids = false;
    	this.pasaJug1 = false;
    	this.pasaJug2 = false;

    	this.casillas = new int[TAMANO][TAMANO];
    	for (int i = 0; i < TAMANO; i++) {
        	for (int j = 0; j < TAMANO; j++) {
            	this.casillas[i][j] = VACIA;
            }
        }
    	
    	this.casillas[3][3] = JUG2_FICHA;
    	this.casillas[3][4] = JUG1;
    	this.casillas[4][3] = JUG1;
    	this.casillas[4][4] = JUG2_FICHA;
    	
    }
     
    @Override
    protected void mueve(Movimiento m) throws ExcepcionJuego {
         
    	if (m instanceof MovimientoOthello) {
    		
    		if (this.pasaJug1 == true && this.pasaJug2 == true) {
    			// Se comprueba el ganador
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
    		
    		if (m.equals(new MovimientoOthello(-1, -1))) {
    			pasar(this.turno);
    			return;
    		}
    		
    		MovimientoOthello mo = (MovimientoOthello) m;
    		
    		if (esValido(mo)) {
    			this.ultimoMovimiento = mo;
    			this.cambiaTurno();
    			this.pasaJug1 = false;
    	    	this.pasaJug2 = false;
    		} else {
    			ExcepcionJuego e = new ExcepcionJuego("El movimiento no es válido.");
    			throw(e);
    		}
    		
    	} else {
    		ExcepcionJuego e = new ExcepcionJuego("El movimiento no es de tipo Othello.");
			throw(e);
    	}
	   
         
    }
    
    private void comprobarTurnoGanador (int ganador) {
    	
    	if (this.turno == ganador)
    		return;
    	else
    		this.cambiaTurno();
    }
    
    private void pasar (int jugador) {
    	
    	if (jugador == JUG1)
    		this.pasaJug1 = true;
    	else
			this.pasaJug2 = true;
    	
    	this.cambiaTurno();
    	
    }
 
    @Override
    public boolean esValido(Movimiento m) {
         
        if (m == null)
            return false;
         
        if (m instanceof MovimientoOthello) {
             
            MovimientoOthello mo = (MovimientoOthello) m;
            int fila = mo.getFila();
            int columna = mo.getColumna();
            
            boolean valido = false;
            valido = comprobarDireccionAdyacenteValida(fila, columna);	
        	
            return valido;
            
        }
         
        return false;
    }
    
    public boolean comprobarDireccionAdyacenteValida (int fila, int columna) {
        
    	if (this.casillas[fila][columna] == VACIA) {
     	
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
    
    public boolean comprobarVecinosAdyacentesDireccion (int fila, int columna, int ejeX, int ejeY) {
    	
    	int fila_aux = fila  + ejeX, columna_aux = columna  + ejeY;
    	int casillaAdyacente = 0;
    	int vuelta = 0;
    	ArrayList<MovimientoOthello> casillasIntermedias = new ArrayList<MovimientoOthello>();
    	
    	while (!saleDelTablero(fila_aux, columna_aux)) {
        	
        	casillaAdyacente = this.casillas[fila_aux][columna_aux];
        	
        	if (casillaAdyacente == 0)
        		return false;
        	
        	int fichaCasillaAdy = this.turno == JUG2 ? JUG2_FICHA : JUG1;
        	
        	if (casillaAdyacente == fichaCasillaAdy) {
        		if (vuelta == 0)
        			return false;
        		else {
        			if (this.enMovsValids != true) {
        				cambiarCasillasIntermedias(casillasIntermedias);
            			this.casillas[fila][columna] = fichaCasillaAdy;
        			}
        			return true;
        		}
        	}
        	
        	casillasIntermedias.add(new MovimientoOthello(fila_aux, columna_aux));
        		
        	fila_aux  += ejeX;
        	columna_aux  += ejeY;
        	vuelta++;
    		
    	}
    	
    	return false;
    	
    }
    
    public boolean saleDelTablero (int fila, int columna) {
    	
    	if (fila > (TAMANO - 1) || fila < 0 || columna > (TAMANO - 1) || columna < 0)
    		return true;
    	
    	return false;
    }
    
    public void cambiarCasillasIntermedias (ArrayList<MovimientoOthello> casillasIntermedias) {
    	
    	for (MovimientoOthello m : casillasIntermedias) {
    		this.casillas[m.getFila()][m.getColumna()] = (this.turno == JUG2 ? JUG2_FICHA : JUG1);
    	}
    	
    }
 
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
        
        if (validos.size() == 0)
        	validos.add(new MovimientoOthello (-1, -1));
         
        return validos;
    }
 
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
}