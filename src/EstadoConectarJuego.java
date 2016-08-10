import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class EstadoConectarJuego extends BasicGameState
{
	//ID del estado
	private int idEstado = 0;
	//Fuentes para el texto del juego
	private UnicodeFont texto;
	private UnicodeFont texto2;
	//Estado del juego
	private StateBasedGame estado;
	//Mensaje para el turno
	private String mensajeTurno = "";
	//Guarda el nombre del jugador
	private String nombre;
	//Guarda el nombre de los otros jugadores
	private String nombres;
	//Guarda el nombre del enemigo atacante
	private String enemigo;
	//Almacena las cordenadas del ataque
	private int[] coordenadas;
	//El tiempo de espera antes de hacer un request al servidor
	private static int TIEMPO_ESPERA = 2000;
	//la ip del host del juego
	private String host;
	//la posicion de la flecha de seleccion
	private int flecha = 200;
	//el valor del enemigo que se selecciona
	private int enemigoSeleccionado = 0;
	//informa el estado de la conexion
	private String mensajeConexion;
	//el registro rmi
	private Registry registro;
	//la interfaz del servicio remoto
	private Interfaz interfaz;
	//confirma la conexion
	private boolean conexionExitosa = false;
	//confirma el inicio del juego
	private boolean iniciarJuego = false;
	//envio del ataque
	private boolean enviarAtaque = false;
	//confirma el momento de seleccionar al enemigo
	private boolean seleccionarEnemigo = false;
	//confirma la recepcion de un ataque
	private boolean recibirAtaque = false;
	//indica la derrota en el juego
	private boolean defeat = false;
	//indica la victoria
	private boolean victoria = false;
	//confirma la animacion de la explosion de un ataque recibido
	private boolean animacionExplosion = false;
	//almacena los jugadores
	private ArrayList<String> jugadores;
	//efectos de sonido
	private Sound enterFX;
	private Sound musicaFondoFX;
	private Sound escribirFX;
	private Sound explosionFX;
	//contenedor del juego
	private GameContainer contenedor;
	//Entidades de los diferentes barcos y buques de guerra
	private Entity[] submarino = null;
	private Entity[] destroyer = null;
	private Vector2f[] vectoresNaves;
	private Entity carrier = null;
	private Entity mar = null;
	private Entity mar2 = null;
	//random para las posiciones de los barcos
	private Random rand;
	//temporizador
	private int tiempo = 0;
	//flecha de la scoordenadas
	private int flechaCoordenadas = 0;
	//Sprite para la explosion
	private SpriteSheet explosion;
	//Animacion de la explosion
	private Animation animExplosion;
	//Enumerable con las diferentes fases del juego
	private enum ESTADOS
	{
		TURNO, SELECCION_ENEMIGO, SELECCION_COORDENADAS, RECEPCION_ATAQUE, DERROTA, VICTORIA, ESPERA
	}
	//se limpia el estado actual
	private ESTADOS estadoActual = null;
	//Se inicializan algunas variables
	public EstadoConectarJuego(int idEstado)
	{
		this.idEstado = idEstado;
		rand = new Random();
		crearNaves();
		estadoActual = ESTADOS.ESPERA;
		coordenadas = new int[2];
		coordenadas[0] = 0;
		coordenadas[1] = 0;
	}
	//Se inicia la creacion de las naves
	private void crearNaves() 
	{
		//Se configuran los vectores
		vectoresNaves = new Vector2f[9];
		//Ciclo para generar las naves
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
	//Inicializacion del resto de las variables
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException 
	{
		//se cargan los estados
		estado = sb;
		nombre = "";
		nombres = "";
		host = "";
		mensajeConexion = "";
		//Se cargan los jugadores
		jugadores = new ArrayList<String>();
		//Se inicia el contenedor
		iniciarContenedor(gc);
		//Se cargan las fuentes
		cargarFuentes();
		//se carga el fondo
		cargarFondo();
		//Se cargan las naves
		cargarNaves();
		//Se cargan los sonidos
		enterFX = new Sound("src/enter.wav");
		explosionFX = new Sound("src/explosion.wav");
		musicaFondoFX = new Sound("musicafondo.wav");
		escribirFX = new Sound("src/typewriter-key-1.wav");
		musicaFondoFX.loop();
		//Se carga la exposion
		explosion = new SpriteSheet("src/explosion.png", 128, 128);
		animExplosion = new Animation(explosion, 50);
	}
	private void cargarNaves() throws SlickException
	{
		//Las entidaddes para las naves
		submarino = new Entity[5];
		destroyer = new Entity[3];
		carrier = new Entity("carrier");
		//Se cargan los componentes y propiedades de las naves asi como la imagen de cada una
		for(int i = 0; i < 3; i++)
		{
			destroyer[i] = new Entity("destroyer" + i);
			destroyer[i].AddComponent(new ImageRenderComponent("destroyerRender" + i, new Image("src/Destroyer.png")));
			destroyer[i].setPosition(vectoresNaves[i + 1]);
			destroyer[i].setRotation(rand.nextInt(4)*90);
			destroyer[i].SetVida(4);
		}
		for(int i = 0; i < 5; i++)
		{
			submarino[i] = new Entity("submarino" + i);
			submarino[i].AddComponent(new ImageRenderComponent("submarinoRender" + i, new Image("src/Submarine.png")));
			submarino[i].setPosition(vectoresNaves[i + 4]);
			submarino[i].setRotation(rand.nextInt(4)*90);
			submarino[i].SetVida(2);
		}
		carrier.AddComponent(new ImageRenderComponent("carrierRender", new Image("src/AircraftCarrier.png")));
		carrier.setPosition(vectoresNaves[0]);
		carrier.setRotation(rand.nextInt(4)*90);
		carrier.SetVida(8);
	}
	
	private void iniciarContenedor(GameContainer gc) 
	{
		//se inicializa el componente fps desactivado, sincronizacion vertical desactivada y debug desactivado
		contenedor = gc;
		
		contenedor.setShowFPS(false);
		contenedor.setVSync(false);
		contenedor.setVerbose(true);
		
	}

	private void cargarFondo() throws SlickException
	{
		//se carga el mar en dos secciones
		mar = new Entity("mar");
		mar2 = new Entity("mar");
		mar.AddComponent(new ImageRenderComponent("marRender", new Image("src/Sea.png")));
		mar.setPosition(new Vector2f(0,0));
		mar2.AddComponent(new ImageRenderComponent("mar2Render", new Image("src/Sea.png")));
		mar2.setPosition(new Vector2f(640,0));
	}

	private void cargarFuentes() throws SlickException
	{
		//Se cargan las fuentes del juego
		texto = new UnicodeFont("src/fuente.ttf", 50, false, false);
		texto.addAsciiGlyphs();
		texto.getEffects().add(new ColorEffect());
		texto.loadGlyphs();

		texto2 = new UnicodeFont("src/fuente.ttf", 30, false, false);
		texto2.addAsciiGlyphs();
		texto2.getEffects().add(new ColorEffect());
		texto2.loadGlyphs();
	}
	//Metodo para el renderizado del juego completo
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException 
	{
		mostrarDatosConexion(gc, sb, g);
		mostrarJugadoresConectados(gc, sb, g);
		iniciarPartida(gc, sb, g);
		explosionAnimacion(gc, sb, g);
	}
	
	
	//aimacion de la explosion
	private void explosionAnimacion(GameContainer gc, StateBasedGame sb,
			Graphics g) 
	{
		if(animacionExplosion)
		{
			animExplosion.draw(coordenadas[0] - 64, coordenadas[1] - 64);
		}
	}
	
	private void mostrarDatosConexion(GameContainer gc, StateBasedGame sb,
			Graphics g) 
	{
		//Se muestra esta interfaz de conexion si el juego no se ha iniciado
		if (!iniciarJuego)
		{
			texto.drawString(300, 200, "Nombre:", org.newdawn.slick.Color.yellow);
			texto.drawString(300, 300, "Host:", org.newdawn.slick.Color.yellow);
			texto.drawString(300, 400, "Conectarse", org.newdawn.slick.Color.yellow);
			texto.drawString(500, 200, nombre, org.newdawn.slick.Color.yellow);
			texto.drawString(500, 300, host, org.newdawn.slick.Color.yellow);
			texto.drawString(275, flecha, ">", org.newdawn.slick.Color.yellow);
			texto.drawString(300, 500, mensajeConexion, org.newdawn.slick.Color.yellow);			
		}
	}

	private void iniciarPartida(GameContainer gc, StateBasedGame sb, Graphics g) 
	{
		//Se verifica si el jugador ya perdio
		if(defeat)
			texto.drawString(500, 400, "Has perdido", org.newdawn.slick.Color.yellow);
		else
		{
			//se verifica si el jugador ganó
			if(victoria)
				texto.drawString(500, 400, "Has ganado", org.newdawn.slick.Color.yellow);
			else
				//se verifica el inicio del juego
				if(iniciarJuego)
				{
					//inicia la renderizacion de los elementos
					int derrota = 0;
					mar.render(gc, null, g);
					mar2.render(gc, null, g);
					//se verifica la destruccion de alguna nave para dejar de dibujarla
					if(!carrier.destruido)
						carrier.render(gc, null, g);
					else
						derrota++;
					for(int i = 0; i < 5; i++)
					{
						if(i < 3)
						{
							if(!destroyer[i].destruido)
								destroyer[i].render(gc, null, g);
							else
								derrota++;
							
						}
						if(!submarino[i].destruido)
							submarino[i].render(gc, null, g);
						else
							derrota++;
					}
					//se envian los mensajes
					texto.drawString(500, 400, mensajeTurno, org.newdawn.slick.Color.yellow);
					try 
					{
						texto2.drawString(10, 10, interfaz.mensaje(), org.newdawn.slick.Color.yellow);
					} 
					catch (RemoteException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(derrota == 9)
					{
						try {
							interfaz.Derrota(nombre);
							defeat = true;
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					interfazEnviarAtaque();
				}
			
		}
	}

	private void interfazEnviarAtaque() 
	{
		//Se verifica el envio del ataque
		if(enviarAtaque)
		{
			//se verifica si es el momento de seleccionar un enemigo
			if(seleccionarEnemigo)
			{
				//se muestra la interfaz de selceccion del enemigo
				texto.drawString(400, 200, "Seleccionar enemigo:", org.newdawn.slick.Color.yellow);			
				texto.drawString(370, 300 + (enemigoSeleccionado*60), ">", org.newdawn.slick.Color.yellow);			
				for(int i = 0; i < jugadores.size(); i++)
				{
					nombres += jugadores.get(i) + "\n";
				}
				texto.drawString(400, 300, nombres, org.newdawn.slick.Color.yellow);			
				
				nombres = "";
			}
			else
			{
				//se muestra la interfaz de seleccion de coordenadas
				texto.drawString(500, 200, "x:", org.newdawn.slick.Color.yellow);			
				texto.drawString(500, 300, "y:", org.newdawn.slick.Color.yellow);							
				texto.drawString(550, 200, "" + coordenadas[0], org.newdawn.slick.Color.yellow);			
				texto.drawString(550, 300, "" + coordenadas[1], org.newdawn.slick.Color.yellow);							
				texto.drawString(470, 200 + (flechaCoordenadas*60), ">", org.newdawn.slick.Color.yellow);			
			}
		}
	}

	private void mostrarJugadoresConectados(GameContainer gc, StateBasedGame sb, Graphics g) 
	{
		//si el juego no ha iniciado se carga una interfaz que muestra los jugadores conectados al servidor
		if(conexionExitosa && !iniciarJuego)
		{
			texto2.drawString(1000, 200, "Jugadores conectados", org.newdawn.slick.Color.yellow);
			try 
			{
				jugadores = interfaz.Jugadores();
				if(interfaz.InicioPartida())
				{
					iniciarJuego = true;
				}
			} 
			catch (RemoteException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0; i < jugadores.size(); i++)
			{
				nombres += jugadores.get(i) + "\n";
			}
			texto2.drawString(1000, 250, nombres, org.newdawn.slick.Color.yellow);
			nombres = "";
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException 
	{
		//se decide a que fase del juego se debe de mover
		if(iniciarJuego)
			switch (estadoActual) 
			{
			case ESPERA:
				espera(delta);
				break;
			case TURNO:
				Turno(gc, sb, delta);
				break;
			case SELECCION_ENEMIGO:
				SeleccionEnemigo();
				break;
			case SELECCION_COORDENADAS:
				SeleccionCoordenadas();
				break;
			case RECEPCION_ATAQUE:
				RecepcionAtaque(gc, sb, delta);
			default:
				break;
			}
	}

	private int calcularDistancia(int x1, int y1, int x2, int y2)
	{
		//se calcula la distancia del punto seleccionado de ataque con relacion a la nave
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	private void RecepcionAtaque(GameContainer gc, StateBasedGame sb, int delta) 
	{
	
		//se incrementa el tiempo
		tiempo += delta;
		if(tiempo > TIEMPO_ESPERA)
		{
			
			try 
			{
				//se cargan las coordenadas en el cliente
				coordenadas = interfaz.CoordenadasAtaque();
				animacionExplosion = true;
				explosionFX.play();
			} 
			catch (RemoteException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Se verifica el ataque sobre las unidades
			for(int i = 0; i < 5; i++)
			{
				if(i < 3)
				{
					int temp = calcularDistancia
					(
							(int) destroyer[i].getPosition().x, 
							(int) destroyer[i].getPosition().y,
							coordenadas[0],
							coordenadas[1]
					);
					
					if(temp < 100)
					{
						destroyer[i].ReducirVida();
						try {
							interfaz.naveDanada("Un destructor de " + nombre + " recibio el impacto en x: " + coordenadas[0] + ", y: " + coordenadas[1]);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				int temp = calcularDistancia
				(
						(int) submarino[i].getPosition().x, 
						(int) submarino[i].getPosition().y,
						coordenadas[0],
						coordenadas[1]
				);
				
				if(temp < 100)
				{
					submarino[i].ReducirVida();
					try {
						interfaz.naveDanada("Un submarino de " + nombre + " recibio el impacto en x: " + coordenadas[0] + ", y: " + coordenadas[1]);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			int temp = calcularDistancia
			(
					(int) carrier.getPosition().x, 
					(int) carrier.getPosition().y,
					coordenadas[0],
					coordenadas[1]
			);
			
			if(temp < 100)
			{
				carrier.ReducirVida();
				try {
					interfaz.naveDanada("El portaaviones de " + nombre + " recibio el impacto en x: " + coordenadas[0] + ", y: " + coordenadas[1]);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			tiempo = 0;
			estadoActual = ESTADOS.ESPERA;
			try {
				interfaz.AtaqueRecibido();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void SeleccionCoordenadas() 
	{
		//se quita la seleccion del enemigo
		seleccionarEnemigo = false;
	}
	//Si es momento de la seleccion del enemigo entonces se vuelven verdaderos los argumentos de ataque
	private void SeleccionEnemigo() 
	{
		enviarAtaque = true;
		seleccionarEnemigo = true;
	}

	private void espera(int delta) 
	{
		tiempo += delta;
		//Si el tiempo de espera es mayor al marcado inicia la seleccion del estado
		if(tiempo > TIEMPO_ESPERA)
		{
			
			carrier.setRotation(90);
			
			try 
			{
				jugadores = interfaz.Jugadores();
				if(jugadores.size() == 1)
					if(jugadores.get(0).equals(nombre))
						victoria = true;					
				if(interfaz.RecibirAtaque(nombre))
					estadoActual = ESTADOS.RECEPCION_ATAQUE;
				else
					if(interfaz.Turno(nombre))
						estadoActual = ESTADOS.TURNO;
				tiempo = 0;
				animacionExplosion = false;
			} 
			catch (RemoteException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//Se verifica si es el turno de atacar
	private void Turno(GameContainer gc, StateBasedGame sb, int delta) 
	{
		tiempo += delta;
		mensajeTurno = "Es tu turno de atacar";
		if(tiempo > TIEMPO_ESPERA)
		{
			estadoActual = ESTADOS.SELECCION_ENEMIGO;
			mensajeTurno = "";
		}
	}

	@Override
	public int getID() 
	{
		return idEstado;
	}
	//Se agregan los caracteres a los campos de ingreso al servidor del juego
	public void keyPressed(int key, char c)
	{
		if(key == Input.KEY_DOWN && flecha < 400 && !iniciarJuego)
			flecha += 100;
		if(key == Input.KEY_UP && flecha > 200 && !iniciarJuego)
			flecha -= 100;
		insertarNombre(key, c);
		insertarHost(key, c);
		conectarse(key, c);
		seleccionarEnemigo(key, c);
	}
	//Se seleccionan las coordenadas usando el mouse
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		if(enviarAtaque && !seleccionarEnemigo)
		{
			coordenadas[0] = newx;
			coordenadas[1] = newy;			
		}
	}
	//se almacenana y se envian las coordenadas al hacer click
	public void mousePressed(int button, int x, int y)
	{
		if(enviarAtaque && !seleccionarEnemigo)
		{
			if(button == Input.MOUSE_LEFT_BUTTON)
			{
				try 
				{
					interfaz.EnviarAtaque(coordenadas, jugadores.get(enemigoSeleccionado));
					enviarAtaque = false;
					estadoActual = ESTADOS.ESPERA;
					tiempo = 0;
				} 
				catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	//Seleccion del enemigo con el teclado
	private void seleccionarEnemigo(int key, char c) 
	{
		if(seleccionarEnemigo)
		{
			if(enemigoSeleccionado > 0 && key == Input.KEY_UP)
				enemigoSeleccionado--;
			if(enemigoSeleccionado < jugadores.size() - 1 && key == Input.KEY_DOWN)
				enemigoSeleccionado++;
			if(key == Input.KEY_ENTER)
				estadoActual = ESTADOS.SELECCION_COORDENADAS;	
		}
	}
	//Intento de conexion
	private void conectarse(int key, char c) 
	{
		if(flecha == 400 && !iniciarJuego)
		{
			if(key == Input.KEY_ENTER)
			{
				mensajeConexion = "Conectando, espere por favor";
				try 
				{
					enterFX.play();
					registro = LocateRegistry.getRegistry(host);
					interfaz = (Interfaz) registro.lookup("ServerBattleship");
					interfaz.jugadorAgregado(nombre);
					conexionExitosa = true;
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	//Se agrega la insercion del nombre con el teclado al estar seleccionado
	private void insertarNombre(int key, char c) 
	{
		if(flecha == 200 && !iniciarJuego)
		{
			if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == ' ' || c == '.')
			{
				escribirFX.play();
				nombre += c;				
			}
			if(key == Input.KEY_BACK)
				if(nombre.length() > 0)
				{
					escribirFX.play();
					nombre = nombre.substring(0, nombre.length()-1);					
				}
		}
	}
	//Se agrega la insercion del host con el teclado al estar seleccionado
	private void insertarHost(int key, char c) 
	{
		if(flecha == 300 && !iniciarJuego)
		{
			if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == ' ' || c == '.')
			{
				escribirFX.play();
				host += c;
			}
			if(key == Input.KEY_BACK)
				if(host.length() > 0)
				{
					escribirFX.play();
					host = host.substring(0, host.length()-1);
				}
		}
	}

}
