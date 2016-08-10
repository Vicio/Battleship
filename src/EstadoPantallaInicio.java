import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class EstadoPantallaInicio extends BasicGameState
{
	//ID del estado del juego
	private int idEstado = 0;
	//Fuente para el titulo
	private UnicodeFont titulo;
	//Fuente para el resto del texto
	private UnicodeFont texto;
	//Estado del juego
	private StateBasedGame estado;
	//Efectos de sonido
	private Sound musicaFondoFX;
	//Temporizador
	private int tiempo = 0;

	//Instancia de la aplicacion
	public EstadoPantallaInicio(int idEstado)
	{
		this.idEstado = idEstado;
	}
	
	//Inicializacion de los parametros
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException 
	{
		estado = sb;
		//Se carga la musica y se pone en reproduccion
		musicaFondoFX = new Sound("src/musicafondo.wav");
		musicaFondoFX.loop();
		//Se llama el metodo para cargar fuentes
		cargarFuentes();
	}
	
	//Metodo para cargar fuentes
	private void cargarFuentes() throws SlickException
	{
		//Se carga la fuente y se le configuran el tamaño y color 
		titulo = new UnicodeFont("src/fuente.ttf", 200, false, false);
		titulo.addAsciiGlyphs();
		titulo.getEffects().add(new ColorEffect());
		titulo.loadGlyphs();
		//Se carga la fuente y se le configuran el tamaño y color 
		texto = new UnicodeFont("src/fuente.ttf", 50, false, false);
		texto.addAsciiGlyphs();
		texto.getEffects().add(new ColorEffect());
		texto.loadGlyphs();
	}
	//Renderiza los gráficos del titulo
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException 
	{
		//Se carga el titulo
		titulo.drawString(400, 100, "BattleShip", org.newdawn.slick.Color.yellow);
		//Se carga el texto
		texto.drawString(400, 500, "Presione cualquier tecla para continuar", org.newdawn.slick.Color.yellow);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException 
	{
		//Se le suma el tiempo transcurrido en milisegundos
		tiempo += delta;
	}

	//Metodo que retorna el ID del estado
	@Override
	public int getID() 
	{
		return idEstado;
	}
	
	public void keyPressed(int key, char c)
	{
		//Sonido de inicio del juego
		Sound fx;
		try {
			fx = new Sound("src/enter.wav");
			fx.play();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//se detiene la musica de fondo cuando se entra a el siguiente estado.
		musicaFondoFX.stop();
		//Se mueve al estado de conectar juego
		estado.enterState(BatallaNaval.CONECTARJUEGO);
	}

}
