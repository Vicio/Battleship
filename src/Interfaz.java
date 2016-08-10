

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Interfaz extends Remote
{
		//regresa los jugadores
		public ArrayList<String> Jugadores ()
			throws RemoteException;
			//agrega un jugador
		public void jugadorAgregado(String jugador)
			throws RemoteException;
		//indica el inicio de la partida
		public boolean InicioPartida()
			throws RemoteException;
		//envia la confirmacion de un ataque
		public boolean RecibirAtaque(String jugador)
			throws RemoteException;
		//envia las coordenadas del ataque y el jugador al que llega
		public void EnviarAtaque(int[] coordenadas, String jugador)
			throws RemoteException;
		//regresa las coordenadas del ataque
		public int[] CoordenadasAtaque()
			throws RemoteException;
		//indica la derrota de un jugador
		public void Derrota(String jugador)
			throws RemoteException;
		//envia mensajes a todos los jugadores
		public String mensaje()
			throws RemoteException;
		//indica el turno de el jugador actual
		public boolean Turno(String jugador) 
			throws RemoteException;
		//envia la confirmacion del ataque recibido
		public void AtaqueRecibido()
				throws RemoteException;
		//envia el daño de un barco a todos los jugadores
		public void naveDanada(String mens)
				throws RemoteException;
}
