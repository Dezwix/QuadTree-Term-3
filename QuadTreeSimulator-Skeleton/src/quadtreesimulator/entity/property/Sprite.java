package quadtreesimulator.entity.property;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

/**
 * <p>
 * Sprite represents what to be drawn during the canvas drawing stage. this class holds information such as
 * {@link Sprite#fill}, {@link Sprite#stroke} and {@link Sprite#strokeWidth}. this class is abstract, meaning 
 * it must be inherited and draw method overridden.
 * fill and stroke values in this class are of type {@link Paint}. this allows  the user to set any color to the sprite
 * or using the {@link ImagePattern} class choose an image asset and set it as filling.
 * </p>
 * 
 * ex: sprite for polyshape<br>
 * <code><pre>
    sprite = new Sprite(){
        {
            setFill( new ImagePattern( new Image( "file:assets/concrete/dsc_1621.png")));
        }

        public void draw( GraphicsContext gc){
            gc.setLineWidth( getWidth());
            if( getStroke() != null){
                gc.setStroke( getStroke());
                gc.strokePolygon( points[0], points[1], pointCount);
            }
            if( getFill() != null){
                gc.setFill( getFill());
                gc.fillPolygon( points[0], points[1], pointCount);
            }
        }
    };
 * </Pre></code>
 * 
 * @author Shahriar (Shawn) Emami
 * @version Mar 11, 2019
 */
public abstract class Sprite implements Drawable< Sprite>{

	private Paint fill, stroke;
	private double strokeWidth;

	/**
	 * set the {@link Paint} to be used when filling the shape
	 * @param paint - an object representing fill content like {@link Paint} or {@link ImagePattern} object
	 * @return the instance of current object
	 */
	public Sprite setFill( Paint color){
		fill = color;
		return this;
	}
	
	/**
	 * set the {@link Paint} to be used when stroking the shape
	 * @param paint - an object representing fill content like {@link Paint} or {@link ImagePattern} object
	 * @return the instance of current object
	 */
	public Sprite setStroke( Paint color){
		stroke = color;
		return this;
	}
	
	/**
	 * set the stroke width to be used when stroking the shape
	 * @param width - stroke width
	 * @return the instance of current object
	 */
	public Sprite setWidth( double width){
		this.strokeWidth = width;
		return this;
	}
	
	/**
	 * get the current fill {@link Color}
	 * @return {@link Paint}
	 */
	public Paint getFill(){
		return fill;
	}

	/**
	 * get the current stroke {@link Color}
	 * @return {@link Paint}
	 */
	public Paint getStroke(){
		return stroke;
	}

	/**
	 * get the current stroke width
	 * @return stroke width
	 */
	public double getWidth(){
		return strokeWidth;
	}
	
	/**
	 * draw the shape given the {@link GraphicsContext}
	 * @param gc - {@link GraphicsContext} object
	 */
	public abstract void draw( GraphicsContext gc);
}
