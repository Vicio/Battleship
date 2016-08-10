

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Entity 
{
	//ID de la entidad
    String id;
	//Vectores de la imagen
    Vector2f position;
	//la escala de la imagen
    float scale;
	//la rotacion de la imagen
    float rotation;
	//las coordenadas
    int[] coordenadas;
	//la vida de la entidad
    int Vida;
	//valida la destruccion de la entidad
    boolean destruido;

	//Se le agrega un renderizador
    RenderComponent renderComponent = null;
	//Se le agregan diversoso componentes
    ArrayList<Component> components = null;
	//Se inicializan las variables
    public Entity(String id)
    {
        this.id = id;
		
        components = new ArrayList<Component>();
		
        position = new Vector2f(0,0);
        scale = 1;
        rotation = 0;
        coordenadas = new int[2];
        Vida = 0;
        destruido = false;
    }

	//metodo para agregar nuevos componentes a la entidad
    public void AddComponent(Component component)
    {
        if(RenderComponent.class.isInstance(component))
            renderComponent = (RenderComponent)component;

        component.setOwnerEntity(this);
	components.add(component);
    }
	//Se obtiene el componente asignado a la entidad
    public Component getComponent(String id)
    {
        for(Component comp : components)
	{
	    if ( comp.getId().equalsIgnoreCase(id) )
	        return comp;
	}
		
	return null;
    }
	//Metodos para obtener o asignar las variables de la entidad
    public Vector2f getPosition()
    {
	return position;
    }
    
    public float getScale()
    {
	return scale;
    }
	
    public float getRotation()
    {
	return rotation;
    }
	
    public String getId()
    {
    	return id;
    }
	public int GetVida()
	{
		return Vida;
	}
	public void SetVida(int vida)
	{
		Vida = vida;
	}
	
	public void ReducirVida()
	{
		Vida--;
		if (Vida <= 0)
			destruido = true;
	}
	
	public int[] GetCoordenadas()
	{
		return coordenadas;
	}
	public void GetCoordenadas(int[] coordenadas)
	{
		this.coordenadas = coordenadas;
	}
	
	public boolean GetDestruido()
	{
		return destruido;
	}
	public void SetDestruido(boolean destruido)
	{
		this.destruido = destruido;
	}

    public void setPosition(Vector2f position) {
	this.position = position;
    }

    public void setRotation(float rotate) {
        rotation = rotate;
    }

    public void setScale(float scale) {
	this.scale = scale;
    }
	//Actualizador del componente de la entidad
    public void update(GameContainer gc, StateBasedGame sb, int delta)
    {
        for(Component component : components)
        {
            component.update(gc, sb, delta);
        }
    }
	//Renderizacion de la entidad
    public void render(GameContainer gc, StateBasedGame sb, Graphics gr)
    {
        if(renderComponent != null)
            renderComponent.render(gc, sb, gr);
    }
}