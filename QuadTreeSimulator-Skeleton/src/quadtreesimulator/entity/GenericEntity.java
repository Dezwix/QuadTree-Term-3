package quadtreesimulator.entity;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import quadtreesimulator.entity.property.Entity;
import quadtreesimulator.entity.property.Sprite;

/**
 * 
 * @author Shariar (Shawn) Emami
 * @version Sep 22, 2020
 */
public class GenericEntity implements Entity {

	protected Sprite sprite;
	private BooleanProperty drawable;

	public GenericEntity() {
		this( null);
	}

	public GenericEntity( Sprite sprite) {
		this.sprite = sprite;
		drawable = new SimpleBooleanProperty( sprite != null);
	}

	public void setSprite( Sprite sprite) {
		this.sprite = sprite;
		drawable.set( sprite != null);
	}

	public BooleanProperty drawableProperty() {
		return drawable;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub	
	}

	@Override
	public Sprite getDrawable() {
		return sprite;
	}

	@Override
	public boolean isDrawable() {
		return sprite != null && drawable.get();
	}
}
