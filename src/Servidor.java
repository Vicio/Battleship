import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Servidor implements Interfaz
{
	//lista de los jugadores
	private ArrayList<String> jugadores;
	//las coordenadas de un ataque
	private int[] coordenadas;
	//el receptor del ataque
	private String atacado;
	//el nombre del atacante
	private String atacante;
	//el mensaje global
	private String mensaje;
	//el contador del jugador actual
	private int contador = 0;
	
	//inicializacion de la lista y las variables
	protected Servidor() throws RemoteException 
	{
		super();
		jugadores = new ArrayList<String>();
		mensaje = "";
		atacante = "~`133}}{{";
	}


	//Regreso de los jugadores
	@Override
	public ArrayList<String> Jugadores() throws RemoteException 
	{
		return jugadores;
	}

	@Override
	public boolean InicioPartida() throws RemoteException 
	{
		//Si los jugadores son 2 entonces se inicia la partida
		if (jugadores.size() == 2)
		{
			System.out.println("Partida Iniciada");
			atacante = jugadores.get(contador);
			return true;			
		}
		else
			return false;
	}

	@Override
	public boolean Turno(String jugador) throws RemoteException 
	{
		//si el nombre del jugador es igual al atacante entonces es su turno de atacar
		if(atacante.equals(jugador))
		{
			System.out.println("Turno de " + jugadores.get(contador));
			atacante = "~`133}}{{";
			return true;			
		}
		else
			return false;
	}

	@Override
	public boolean RecibirAtaque(String jugador) throws RemoteException 
	{
		//se valida si el jugador va a recibir el ataque
		if(jugador.equals(atacado))
		{
			System.out.println(jugador + " esta listo para ser atacado");
			return true;			
		}
		else
			return false;
	}

	@Override
	public void EnviarAtaque(int[] coordenadas, String jugador) throws RemoteException 
	{
		//se guardan las coordenadas del ataque
		this.coordenadas = coordenadas;
		atacado = jugador;
		System.out.println("Ataque enviado para: " + jugador + " x: " + coordenadas[0] + " y: " + coordenadas[1]);
	}

	@Override
	public int[] CoordenadasAtaque() throws RemoteException 
	{
		//seenvian la coordenadas del ataque al cliente
		System.out.println(atacado + " recibio las coordenadas");
		return coordenadas;
	}

	@Override
	public void Derrota(String jugador) throws RemoteException 
	{
		//se remueve el jugador una vez que pierde
		jugadores.remove(jugador);
		mensaje = "El jugador " + jugador + " ha perdido\n" + mensaje;
	}

	@Override
	public void jugadorAgregado(String jugador) throws RemoteException 
	{
		//se agrega un jugador
		jugadores.add(jugador);
		System.out.println("jugador " + jugador + " agregado");
	}

	@Override
	public String mensaje() throws RemoteException 
	{	
		//se envia un mensaje a todos los clientes
		return mensaje;
	}
	
	public void naveDanada(String mens)
	{
		//se envia un mensaje de una nave dañada por un ataque
		mensaje = mens + "\n" + mensaje;
	}

	@Override
	public void AtaqueRecibido() throws RemoteException 
	{
		//se selecciona el jugador activo para el ataque
		if(contador < jugadores.size() - 1)
			contador++;
		else
			contador = 0;
		atacado = "";
		atacante = jugadores.get(contador);
	}


}
