import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class BatallaNaval extends StateBasedGame
{
	//Enteros usados para cargar los estados del juego
	public static final int PANTALLAINICIO = 0;
	public static final int CONECTARJUEGO = 1;

	public BatallaNaval() 
	{
		super("BattleShip");
		//Se agrega el estado de la pantalla de inicio
		this.addState(new EstadoPantallaInicio(PANTALLAINICIO));
		//Se agrega el estado de la pantalla de inicio del juego
		this.addState(new EstadoConectarJuego(CONECTARJUEGO));
		//Se inicia el estado de la pantalla de inicio
		this.enterState(PANTALLAINICIO);
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException 
	{
	}

}
