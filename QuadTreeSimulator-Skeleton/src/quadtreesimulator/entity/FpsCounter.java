package quadtreesimulator.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import quadtreesimulator.entity.property.Sprite;

/**
 * 
 * 
 * @author Shahriar (Shawn) Emami
 * @version April 17, 2020
 */
public class FpsCounter extends GenericEntity {

	public static final double ONE_SECOND = 1000000000L;
	public static final double HALF_SECOND = ONE_SECOND / 2F;

	private Font fpsFont;
	private String fpsDisplay;
	private int frameCount;
	private double lastTime;
	private double xPos, yPos;

	public FpsCounter( double x, double y) {
		sprite = new Sprite() {

			@Override
			public void draw( GraphicsContext gc) {
				gc.save();
				Font temp = gc.getFont();
				gc.setFont( fpsFont);
				gc.setFill( getFill());
				gc.fillText( fpsDisplay, xPos, yPos);
				gc.setStroke( getStroke());
				gc.setLineWidth( getWidth());
				gc.strokeText( fpsDisplay, xPos, yPos);
				gc.setFont( temp);
				gc.restore();
			}
		};
		setPos( x, y);
		setFont( Font.font( Font.getDefault().getFamily(), FontWeight.BLACK, 24));
	}

	public void calculateFPS( long now) {
		if ( now - lastTime >= HALF_SECOND) {
			fpsDisplay = Integer.toString( frameCount * 2);
			frameCount = 0;
			lastTime = now;
		}
		frameCount++;
	}

	public FpsCounter setFont( Font font) {
		fpsFont = font;
		return this;
	}

	public FpsCounter setPos( double x, double y) {
		xPos = x;
		yPos = y;
		return this;
	}
}
