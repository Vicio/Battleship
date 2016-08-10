import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;


public class Metadatos implements Serializable
{
	private static final long serialVersionUID = 1L;
	private volatile static Metadatos instance = null;	
	private Interfaz interfaz;
	private Registry registro;
	private String nombre;

	private Metadatos(Interfaz interfaz, Registry registro, String nombre) 
	{
		this.interfaz = interfaz;
		this.registro = registro;
		this.nombre = nombre;
	}
	
	public Interfaz getInterfaz()
	{
		return interfaz;
	}
	public Registry getRegistry()
	{
		return registro;
	}
	public String getNombre()
	{
		return nombre;
	}
	public void setInterfaz(Interfaz interfaz)
	{
		this.interfaz = interfaz;
	}
	public void setRegistry(Registry registro)
	{
		this.registro = registro;
	}
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public static Metadatos getInstance(Interfaz interfaz, Registry registro, String nombre) 
	{
		if (instance == null)
		{
			instance = new Metadatos(interfaz, registro, nombre);
		}
		return instance;
	}
	
	public static Metadatos getInstance()
	{
		return instance;		
	}
}
