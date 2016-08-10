


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Component 
{
	//ID de la entidad
    protected String id;
	//Entidad dueña de este componente
    protected Entity owner;
	//ID de la entidad
    public String getId()
    {
        return id;
    }
	//Se asigna el dueño del componente
    public void setOwnerEntity(Entity owner)
    {
    	this.owner = owner;
    }
	//Se le agrega la propiedad actualizable del contenedor del juego
    public abstract void update(GameContainer gc, StateBasedGame sb, int delta);
}