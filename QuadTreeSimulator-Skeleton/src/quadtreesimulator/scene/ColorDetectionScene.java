package quadtreesimulator.scene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import utility.QuadTree;

public class ColorDetectionScene extends AbstractScene {

	private QuadTree qt;
	
	@Override
	public AbstractScene createScene() {
		SimpleDoubleProperty quadTreeDepth = new SimpleDoubleProperty(0);
		quadTreeDepth.addListener( (v, o, n) -> qt.setMaxDepth( n.intValue()));
		qt = new QuadTree((int) quadTreeDepth.get(), canvasNode.getWidth(), canvasNode.getHeight());
		SimpleBooleanProperty displayQuadTree = new SimpleBooleanProperty(true);
		BooleanProperty bp = qt.drawableProperty();
		bp.bind(displayQuadTree);
		addOption("displayQuadTree", displayQuadTree);
		addOption("quadTreeDepth", quadTreeDepth);
		return null;
	}
	
	public QuadTree getQuadTree() {
		return qt;
	}

}
