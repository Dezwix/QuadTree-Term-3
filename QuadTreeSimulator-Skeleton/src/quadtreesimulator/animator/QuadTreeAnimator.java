package quadtreesimulator.animator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.WritableBooleanValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import quadtreesimulator.scene.AbstractScene;
import quadtreesimulator.scene.ColorDetectionScene;
import utility.QuadTree;

public class QuadTreeAnimator extends AbstractAnimator {

	private int[] buffer;
	private double x, y;
	private boolean initilized;
	private Canvas drawingCanvas;
	private QuadTree qt;
	
	// my variables
	private boolean isDown;	// true when mouse clicked 
							// [not dragged, more like first frame of dragging]
	private boolean firstClick = true; // sets to false after first click
	
	public void init() {
		if(initilized) return;
		initilized = true;
		ColorDetectionScene scene = (ColorDetectionScene) super.scene;
		qt = scene.getQuadTree();
		ObjectProperty<Color> color = (ObjectProperty<Color>) scene.getOption("color");
		color.addListener((v, o, n) -> qt.clear());
		drawingCanvas = new Canvas(scene.w(), scene.h());
		Canvas canvas = scene.getCanvas();
		canvas.setOnMouseDragged( e -> { x = e.getX(); y = e.getY(); } );
		EventHandler eventHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(firstClick) {
					x = event.getX(); 
					y = event.getY(); 
					firstClick = false;}
				if(isDown) {
					x = event.getX();
					y = event.getY();
				}
				canvas.setOnMouseClicked(e -> { isDown = true; });
				GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
				if(event.isPrimaryButtonDown()) {
					gc.setStroke(color.get());
				}
				gc.setLineWidth(2);
				
				if(isRunning()) {
					gc.strokeLine(x, y, event.getX(), event.getY());
				}
				x = event.getX();
				y = event.getY();
				gc.strokeLine(x, y, event.getX(), event.getY());
				isDown = false;
			}
		};
		canvas.setOnMouseDragged(eventHandler);
		
	}
	
	public void clear() {
		init();
		qt.clear();
		clearAndFill(drawingCanvas.getGraphicsContext2D(), Color.TRANSPARENT);
	}
	
	@Override
	protected void handle(GraphicsContext gc, long now) {
		// TODO Auto-generated method stub
		init();
		clearAndFill(gc, Color.TRANSPARENT);
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		WritableImage image = drawingCanvas.snapshot(sp, null);
		
		if(!image.equals(null)) {
			gc.drawImage(image, 0, 0);
		}
		
		BooleanProperty displayQuadTree = (BooleanProperty) scene.getOption("displayQuadTree");
		if(displayQuadTree.get()) {
			buffer = new int[(int) ( scene.w() * scene.h()) + 1];
			image.getPixelReader().getPixels(0, 0, (int) scene.w(), (int) scene.h(), javafx.scene.image.PixelFormat.getIntArgbInstance(), buffer, 0, (int) scene.w());
			ObjectProperty<Color> color = (ObjectProperty<Color>) scene.getOption("color");
			qt.push(buffer, (int) scene.w(), color.get());
			qt.getDrawable().draw(gc);
		}
	}

}
