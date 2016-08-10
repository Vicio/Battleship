


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class ImageRenderComponent extends RenderComponent {

	Image image;
	
	public ImageRenderComponent(String id, Image image)
	{
		super(id);
		this.image = image;
	}
	//Se asigna la renderizacion de la imagen
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) 
	{
		image.setCenterOfRotation(image.getWidth()/2, image.getHeight()/2);
		Vector2f pos = owner.getPosition();
		float scale = owner.getScale();
		
		image.draw(pos.x, pos.y, scale);
	}
	//se actualiza la rotacion de la imagen
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		image.rotate(owner.getRotation() - image.getRotation());
	}

}