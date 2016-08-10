

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;


public class Principal
{

	protected Principal() throws RemoteException 
	{
		super();
	}

	public static void main(String[] args) throws SlickException
	{
		try
		{
			if(args[0].trim().toString().length() > 0)
			{
				Integer.parseInt(args[0]);
				//Nombre del servidor
				String nombre = "ServerBattleship";
				//nuevo servidor
				Servidor servidor = new Servidor();
				//inicio de la interfaz remota
				Interfaz stub2 = (Interfaz) UnicastRemoteObject.exportObject(servidor, 0);
				//Creacion del rmi
				LocateRegistry.createRegistry(Integer.parseInt(args[0]));
				//Registro del servidor
			    Registry registry = LocateRegistry.getRegistry();
				//union con el registro
			    registry.bind(nombre, stub2);
				//mensaje de exito
				System.out.println("Servidor listo");		    
			}
			else
			{
				System.out.println("Puerto en mala configuración");
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
			//Clase principal que carga el AppContainer
		       AppGameContainer app = new AppGameContainer(new BatallaNaval());
			   //se determina el tamaño de pantalla como 1280x800 (16:10) y se inicia la aplicacion
		       app.setDisplayMode(1280, 720, false);
		       app.start();				
		}
	
	}
}
