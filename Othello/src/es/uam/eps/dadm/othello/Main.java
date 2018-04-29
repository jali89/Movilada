/*
 * Main.java
 *
 * By Alejandro Antonio Martín Almansa
 */

package es.uam.eps.dadm.othello;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.JugadorAleatorio;

import es.uam.eps.dadm.jugador.JugadorConsola;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws ExcepcionJuego {
		
		int opcion = 0;
		Scanner sc1 = new Scanner(System.in);
		
		System.out.println("-----------------------------------------");
		System.out.println("---------- BIENVENID@ A OTHELLO ---------");
		System.out.println("-----------------------------------------");
		
		do {
			
			System.out.println("\n\n¿A qué modo desea jugar?");
			System.out.println("\t1. Nueva Partida");
			System.out.println("\t2. Cargar Partida");
			System.out.println("\t3. Salir");
			opcion = sc1.nextInt();
			
			if (opcion != 3 && opcion < 3) {
				
				// Si alguno de los jugadores NO son aleatorios comentar linea 50 de este Main.java
				ArrayList<Jugador> jugs = new ArrayList<Jugador>();
				jugs.add(new JugadorAleatorio ("Fichas 2"));
				jugs.add(new JugadorAleatorio ("Fichas 1"));
				
				TableroOthello t = new TableroOthello ();
				
				if (opcion == 2) {
					sc1.nextLine();
					System.out.println("Introduzca una cadena con el siguiente formato:");
					System.out.println("<turno> <estado> <numJugadas> <casillas por filas separadas por espacio>");
					// Ejemplo: Tablas
					// 0 1 60 22222222 22222222 22222222 22222222 11111111 11111111 11111111 11111111
					// Ejemeplo: Gana Fichas 1
					// 0 1 60 22222222 22222222 22222222 11111111 11111111 11111111 11111111 11111111
					// Ejemeplo: Gana Fichas 2
					// 0 1 60 22222222 22222222 22222222 22222222 22222222 11111111 11111111 11111111
					String tablero = sc1.nextLine();
					t.stringToTablero(tablero);
				}
				
				Partida p = new Partida (t, jugs);
				p.addObservador(new JugadorConsola ("Observador"));
				p.comenzar();
				
			} else {
				if (opcion != 3)
					System.out.println("Opcion Incorrecta. Vuleva a intentarlo.");
			}
			
		} while (opcion != 3);	

		sc1.close();
		System.out.println("Le esperamos pronto de vuelta. ¡Adiós!");
		
	}
	
}

