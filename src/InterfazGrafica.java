

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;

public class InterfazGrafica extends BasicGame
{
	private GameContainer contenedor;
	private Entity[] submarino = null;
	private Entity[] destroyer = null;
	private Vector2f[] vectoresNaves;
	private Entity carrier = null;
	private Entity mar = null;
	private Entity mar2 = null;
	private Random rand;
	private String host = "";
	private String jugador = "";
	private String textoX = "x: ";
	private String textoY = "y: ";
	private String textoVictima = "Victima: ";
	private UnicodeFont titulo;
	private UnicodeFont texto;
	private boolean pantallaInicio = true;
	private boolean cargarInterfaz = false;
	private boolean escribirNombre = false;
	private boolean escribirHost = false;
	private boolean escribirX = false;
	private boolean escribirY = false;
	private boolean escribirJugador = false;
	private boolean conexionExitosa = false;
	private boolean turno = false;
	private boolean inicio = false;
	private boolean recibirAtaque = false;
	private int cont = 0;
	private Registry registry;
	private Interfaz interfaz;
	private int tiempoPasado = 0;
	private int[] coordenadas;
	private int seleccion;
	private int[] atk = new int[2];
	private ArrayList<String> jugadores;
	private int naves = 9;
	private String temp = "0";
	
	public InterfazGrafica() 
	{
		super("Battleship");
		rand = new Random();
		crearNaves();
	}

	private void crearNaves() 
	{
		vectoresNaves = new Vector2f[9];
		for(int i = 0; i < 9; i++)
		{
			vectoresNaves[i] = new Vector2f(rand.nextInt(900) + 150, rand.nextInt(500) + 150);
			for(int j = 0; j < i; j++)
			{
				double distancia = vectoresNaves[i].distance(vectoresNaves[j]);
				if(distancia < 150)
				{
					float ancho = 0;
					float alto = 0;
					while(ancho < 200 || ancho > 1000)
					{
						ancho = vectoresNaves[j].x + (-300 + rand.nextInt(13)*50);
					}
					while(alto < 200 || alto > 600)
					{
						alto = vectoresNaves[j].y + (-300 + rand.nextInt(13)*50);
					}
					vectoresNaves[i] = new Vector2f(ancho, alto);
					j = -1;
				}
			}
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		cargarInicio(gc, g);
		interfazJuego(gc, g);
	}

	private void interfazJuego(GameContainer gc, Graphics g) 
	{
		if(cargarInterfaz)
		{
			mar.render(gc, null, g);
			mar2.render(gc, null, g);
			carrier.render(gc, null, g);
			for(int i = 0; i < 5; i++)
			{
				if(i < 3)
					destroyer[i].render(gc, null, g);
				submarino[i].render(gc, null, g);
			}
			
			texto.drawString(400, 10, textoX, org.newdawn.slick.Color.yellow);					
			texto.drawString(500, 10, textoY, org.newdawn.slick.Color.yellow);					
			texto.drawString(600, 10, textoVictima, org.newdawn.slick.Color.yellow);			
		}
	}

	private void cargarInicio(GameContainer gc, Graphics g) 
	{
		if(pantallaInicio)
		{
			titulo.drawString(400, 100, "BattleShip", org.newdawn.slick.Color.yellow);
			texto.drawString(420, 400, "Presione F1 para escribir su nombre y F2 para escribir el host", org.newdawn.slick.Color.yellow);
			texto.drawString(500, 500, "Nombre:", org.newdawn.slick.Color.yellow);
			texto.drawString(500, 600, "Host:", org.newdawn.slick.Color.yellow);
			texto.drawString(500, 700, "Enter para continuar", org.newdawn.slick.Color.yellow);
			texto.drawString(700, 500, jugador, org.newdawn.slick.Color.yellow);
			texto.drawString(700, 600, host, org.newdawn.slick.Color.yellow);			
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException 
	{
		iniciarContenedor(gc);
		cargarFuentes();
		cargarFondo();
		cargarNaves();
	}

	private void cargarNaves() throws SlickException
	{
		submarino = new Entity[5];
		destroyer = new Entity[3];
		carrier = new Entity("carrier");
		
		for(int i = 0; i < 3; i++)
		{
			destroyer[i] = new Entity("destroyer");
			destroyer[i].AddComponent(new ImageRenderComponent("destroyer", new Image("src/Destroyer.png")));
			destroyer[i].setPosition(vectoresNaves[i + 1]);
			destroyer[i].setRotation(rand.nextInt(4)*90);
			destroyer[i].Vida = 4;
		}
		for(int i = 0; i < 5; i++)
		{
			submarino[i] = new Entity("submarino");
			submarino[i].AddComponent(new ImageRenderComponent("submarino", new Image("src/Submarine.png")));
			submarino[i].Vida = 2;
			submarino[i].setPosition(vectoresNaves[i + 4]);
			submarino[i].setRotation(rand.nextInt(4)*90);
		}
		carrier.AddComponent(new ImageRenderComponent("carrier", new Image("src/AircraftCarrier.png")));
		carrier.setPosition(vectoresNaves[0]);
		carrier.setRotation(rand.nextInt(4)*90);
		carrier.Vida = 8;
	}

	private void cargarFondo() throws SlickException
	{
		mar = new Entity("mar");
		mar2 = new Entity("mar");
		mar.AddComponent(new ImageRenderComponent("mar", new Image("src/Sea.png")));
		mar.setPosition(new Vector2f(0,0));
		mar.setPosition(new Vector2f(640,0));
		mar2.AddComponent(new ImageRenderComponent("mar", new Image("src/Sea.png")));
	}

	private void cargarFuentes() throws SlickException
	{
		titulo = new UnicodeFont("src/fuente.ttf", 200, false, false);
		titulo.addAsciiGlyphs();
		titulo.getEffects().add(new ColorEffect());
		titulo.loadGlyphs();

		texto = new UnicodeFont("src/fuente.ttf", 30, false, false);
		texto.addAsciiGlyphs();
		texto.getEffects().add(new ColorEffect());
		texto.loadGlyphs();		
	}

	private void iniciarContenedor(GameContainer gc) 
	{
		contenedor = gc;
		
		contenedor.setShowFPS(false);
		contenedor.setVSync(false);
		contenedor.setVerbose(true);
		
	}
	

	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		if(conexionExitosa)
		{
			mar.update(gc, null, delta);
			carrier.update(gc, null, delta);
			for(int i = 0; i < 5; i++)
			{
				if(i < 3)
					destroyer[i].update(gc, null, delta);
				submarino[i].update(gc, null, delta);
			}
			tiempoPasado += delta;
			if(tiempoPasado >= 2000)
			{
				tiempoPasado = 0;
				if(naves == 0)
				{
					try {
						interfaz.Derrota(jugador);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try 
				{
					turno = interfaz.Turno(jugador);
					inicio = interfaz.InicioPartida();
					coordenadas = interfaz.CoordenadasAtaque();
					recibirAtaque = interfaz.RecibirAtaque(jugador);
					jugadores = interfaz.Jugadores();
				}
				catch (RemoteException e) 
				{
					e.printStackTrace();
				}
				if(recibirAtaque)
				{
					for(int i = 0; i < 5; i++)
					{
						if(i < 3)
							destroyer[i].coordenadas = coordenadas;
						submarino[i].coordenadas = coordenadas;

					}
					carrier.coordenadas = coordenadas;
				}
				
				if(inicio)
				{
					texto.drawString(700, 600, "La batalla ha iniciado", org.newdawn.slick.Color.yellow);					
				}
				
				if(turno)
				{
					texto.drawString(700, 600, "Es tu turno de atacar", org.newdawn.slick.Color.yellow);
				}
			}
			if(tiempoPasado >= 1000)
			{
				try 
				{
					texto.drawString(10, 10, interfaz.mensaje(), org.newdawn.slick.Color.yellow);
				} catch (RemoteException e) 
				{
					e.printStackTrace();
				}
				tiempoPasado = 0;
			}
			texto.drawString(400, 10, textoX, org.newdawn.slick.Color.yellow);					
			texto.drawString(500, 10, textoY, org.newdawn.slick.Color.yellow);					
			texto.drawString(600, 10, textoVictima, org.newdawn.slick.Color.yellow);					
		}
		else
		{
			
		}
	}
	
	public void keyPressed(int key, char c)
	{
		escribirNombre(key, c);
		if(turno)
		{
			if(key == Input.KEY_F3)
			{
				escribirX = true;
				escribirY = false;
				escribirJugador = false;
				cont = 0;
			}
			if(key == Input.KEY_F4)
			{
				escribirX = false;
				escribirY = true;
				escribirJugador = false;
				cont = 0;
			}
			if(key == Input.KEY_F5)
			{
				escribirX = false;
				escribirY = false;
				escribirJugador = true;
				cont = 0;
			}
		}
		if(key == Input.KEY_SPACE)
		{
			try 
			{
				interfaz.EnviarAtaque(atk, jugadores.get(seleccion));
				turno = false;
			} 
			catch (RemoteException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(key == Input.KEY_F2)
		{
			escribirNombre = false;
			escribirHost = true;
			cont = 0;
		}
		if(key == Input.KEY_ENTER)
		{
			try
			{
				registry = LocateRegistry.getRegistry(host);
				interfaz = (Interfaz) registry.lookup("ServerBattleship");
				interfaz.jugadorAgregado(jugador);
				conexionExitosa = true;
			}
			catch(Exception err)
			{
				
			}
		}
		if(escribirNombre && cont > 0)
		{
			jugador += c;
			if(key == Input.KEY_BACK)
			{
				jugador = jugador.substring(0, jugador.length()-2);
			}
		}
		if(escribirHost && cont > 0)
		{
			host += c;
			if(key == Input.KEY_BACK)
			{
				jugador = jugador.substring(0, jugador.length()-2);
			}
		}
		if(escribirX && cont > 0)
		{
			temp += c;
			if(key == Input.KEY_BACK)
			{
				temp = temp.substring(0, temp.length()-2);
			}
			atk[0] = Integer.parseInt(temp);
		}
		if(escribirX && cont > 0)
		{
			temp += c;
			if(key == Input.KEY_BACK)
			{
				temp = temp.substring(0, temp.length()-2);
			}
			atk[1] = Integer.parseInt(temp);
		}
		if(escribirJugador && cont > 0)
		{
			temp += c;
			if(key == Input.KEY_BACK)
			{
				temp = temp.substring(0, temp.length()-2);
			}
			seleccion = Integer.parseInt(temp);
		}
		cont++;
		
	}

	private void escribirNombre(int key, char c) 
	{
		if(key == Input.KEY_F1)
		{
			while(key != Input.KEY_ENTER || key != Input.KEY_F1)
			{
				
				jugador += c;
			}
		}
	}

}
