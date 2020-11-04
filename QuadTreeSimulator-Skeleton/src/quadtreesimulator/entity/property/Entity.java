package quadtreesimulator.entity.property;

public interface Entity{

	void update();

	boolean isDrawable();

	Drawable< ?> getDrawable();
}
