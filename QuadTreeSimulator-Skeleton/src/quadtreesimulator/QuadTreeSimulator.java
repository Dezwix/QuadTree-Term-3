package quadtreesimulator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import quadtreesimulator.animator.AbstractAnimator;
import quadtreesimulator.animator.QuadTreeAnimator;
import quadtreesimulator.scene.AbstractScene;
import quadtreesimulator.scene.ColorDetectionScene;

/**
 * This is the start of JavaFX application. This class must extend {@link Application}.
 * 
 * @author Shariar (Shawn) Emami, Aleksandr Anseev
 * @version Nov 1, 2020
 */
public class QuadTreeSimulator extends Application {

	/**
	 * size of the scene
	 */
	private static final double WIDTH = 900, HEIGHT = 600;
	/**
	 * title of application
	 */
	private String title = "Quad Tree Simulator";
	/**
	 * background color of application
	 */
	private Color background = Color.LIGHTPINK;
	/**
	 * {@link BorderPane} is a layout manager that manages all nodes in 5 areas as below:
	 * 
	 * <pre>
	 * -----------------------
	 * |        top          |
	 * -----------------------
	 * |    |          |     |
	 * |left|  center  |right|
	 * |    |          |     |
	 * -----------------------
	 * |       bottom        |
	 * -----------------------
	 * </pre>
	 * 
	 * this object is passed to {@link Scene} object in {@link ParticleLocator#start(Stage)} method.
	 */
	private BorderPane root;
	/**
	 * this object represents the canvas (drawing area)
	 */
	private Canvas canvas;
	private AbstractAnimator animator;
	private AbstractScene scene;
	private Alert alert;

	/**
	 * this method is called at the very beginning of the JavaFX application and can be used to initialize all
	 * components in the application. however, {@link Scene} and {@link Stage} must not be created in this method. this
	 * method does not run JavaFX thread.
	 */
	@Override
	public void init() throws Exception {
		canvas = new Canvas(WIDTH, HEIGHT);
		scene = new ColorDetectionScene();
		scene.setCanvas(canvas);
		scene.createScene();
		animator = new QuadTreeAnimator();
		// TODO: setScene?
		animator.setScene(scene);
		Region statusBar = createStatusBar();
		Region optionsBar = createOptionsBar();
		Region colorBar = createColorBar();
		root = new BorderPane();
		root.setTop(optionsBar);
		root.setBottom(statusBar);
		root.setRight(colorBar);
		root.setCenter(canvas);
		
		canvas.heightProperty().bind(
				root.heightProperty()
				.subtract(statusBar.getHeight())
				.subtract(optionsBar.getHeight())
				);
		canvas.widthProperty().bind(
				root.widthProperty()
				.subtract(colorBar.getWidth())
				.subtract(50)
				);
	}

	/**
	 * <p>
	 * this method is called when JavaFX application is started and it is running on JavaFX thread. this method must at
	 * least create {@link Scene} and finish customizing {@link Stage}. these two objects must be on JavaFX thread when
	 * created.
	 * </p>
	 * <p>
	 * {@link Stage} represents the frame of your application, such as minimize, maximize and close buttons.<br>
	 * {@link Scene} represents the holder of all your JavaFX {@link Node}s.<br>
	 * {@link Node} is the super class of every javaFX class.
	 * </p>
	 * 
	 * @param primaryStage - primary stage of your application that will be rendered
	 */
	@Override
	public void start( Stage primaryStage) throws Exception {
		// Alert is initialized in start because it must be created on JavaFX thread
		alert = new Alert( AlertType.INFORMATION);
		Scene scene = new Scene( root, WIDTH, HEIGHT, background);
		primaryStage.setScene( scene);
		primaryStage.setTitle( title);
		// when escape key is pressed close the application
		primaryStage.addEventHandler( KeyEvent.KEY_RELEASED, ( KeyEvent event) -> {
			if ( KeyCode.ESCAPE == event.getCode()) {
				primaryStage.hide();
			}
		});
		// display the JavaFX application
		primaryStage.show();
	}

	/**
	 * this method is called at the very end when the application is about to exit. this method is used to stop or
	 * release any resources used during the application.
	 */
	@Override
	public void stop() throws Exception {
		animator.stop();
	}

	/**
	 * 
	 * @return
	 */
	private Region createColorBar() {
		ObjectProperty<Color> colorOption = new SimpleObjectProperty<>(Color.BLACK);
		scene.addOption("color", colorOption);
		
		// red color button
		Button red = new Button();
		red.setMinSize(40, 16);
		red.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; -fx-background-color: RED");
		red.setOnAction(e -> colorOption.set(Color.RED));
		
		// green color button
		Button green = new Button();
		green.setMinSize(40, 16);
		green.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; -fx-background-color: GREEN");
		green.setOnAction(e -> colorOption.set(Color.GREEN));
		
		// blue color button
		Button blue = new Button();
		blue.setMinSize(40, 16);
		blue.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; -fx-background-color: BLUE");
		blue.setOnAction(e -> colorOption.set(Color.BLUE));
		
		ToolBar verticalBar = new ToolBar(red, green, blue);
		verticalBar.setOrientation(Orientation.VERTICAL);
		verticalBar.setMinWidth(50);
		return verticalBar;
	}

	/**
	 * create a {@link ToolBar} that represent the menu bar at the top of the application.
	 * 
	 * @return customized {@link ToolBar}
	 */
	public Region createOptionsBar() {

		MenuItem start = new MenuItem( "Start");
		start.setOnAction( ( ActionEvent e) -> animator.start());

		MenuItem stop = new MenuItem( "Stop");
		stop.setOnAction( ( ActionEvent e) -> animator.stop());

		MenuItem clear = new MenuItem( "Clear");
		clear.setOnAction( ( ActionEvent e) -> animator.clear());

		MenuItem exit = new MenuItem( "Exit");
		exit.setOnAction( ( ActionEvent e) -> Platform.exit());

		Slider slider = new Slider( 0, 10, 7);
		DoubleProperty depthOption = (DoubleProperty) scene.getOption( "quadTreeDepth");
		depthOption.bind( slider.valueProperty());

		CustomMenuItem ballLabel = new CustomMenuItem();
		ballLabel.setContent( new Label( "QT Depth:"));
		ballLabel.setHideOnClick( false);

		CustomMenuItem ballCount = new CustomMenuItem();
		ballCount.setContent( slider);
		ballCount.setHideOnClick( false);

		CheckMenuItem fps = new CheckMenuItem( "FPS");
		BooleanProperty fpsOption = (BooleanProperty) scene.getOption( "displayFPS");
		fpsOption.bind( fps.selectedProperty());
		fps.setSelected( true);

		CheckMenuItem displayGrid = new CheckMenuItem( "QuadTree");
		BooleanProperty qtOption = (BooleanProperty) scene.getOption( "displayQuadTree");
		qtOption.bind( displayGrid.selectedProperty());
		displayGrid.setSelected( true);

		RadioMenuItem baseScene = new RadioMenuItem( "Scene");
		baseScene.setSelected( true);

		ToggleGroup sceneToggleGroup = new ToggleGroup();
		baseScene.setToggleGroup( sceneToggleGroup);

		RadioMenuItem baseAnimator = new RadioMenuItem( "Animator");
		baseAnimator.setSelected( true);
		//		baseAnimator.setOnAction( e -> createScene.accept( null));

		ToggleGroup animatorToggleGroup = new ToggleGroup();
		baseAnimator.setToggleGroup( animatorToggleGroup);

		MenuItem info = new MenuItem( "Info");
		info.setOnAction( e -> {
			alert.setTitle( "Information");
			alert.setHeaderText( "Developers");
			alert.setContentText( "Shawn E. \nemamis@algonquincollege.com \n-------------------------------------\nAleksandr Anseev\nanse0012@algonquinlive.com");
			alert.show();
		});

		Menu file = new Menu( "File");
		file.getItems().addAll( start, stop, clear, new SeparatorMenuItem(), exit);

		Menu option = new Menu( "Options");
		option.getItems().addAll( ballLabel, ballCount, new SeparatorMenuItem(), fps, displayGrid);

		Menu help = new Menu( "Help");
		help.getItems().addAll( info);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll( file, option, help);
		return menuBar;
	}

	/**
	 * create a {@link ToolBar} that will represent the status bar of the application.
	 * 
	 * @return customized {@link ToolBar}
	 */
	public Region createStatusBar() {
		Label mouseCoordLabel = new Label( "(0,0)");
		Label dragCoordLabel = new Label( "(0,0)");

		canvas.addEventHandler( MouseEvent.MOUSE_MOVED,
				e -> mouseCoordLabel.setText( "(" + e.getX() + "," + e.getY() + ")"));
		canvas.addEventHandler( MouseEvent.MOUSE_DRAGGED,
				e -> dragCoordLabel.setText( "(" + e.getX() + "," + e.getY() + ")"));

		return new ToolBar( new Label( "Mouse: "), mouseCoordLabel, new Label( "Drag: "), dragCoordLabel);
	}

	/**
	 * main starting point of the application
	 * 
	 * @param args - arguments provided through command line, if any
	 */
	public static void main( String[] args) {
		// this method start the javaFX application.
		// some IDEs are capable of starting JavaFX without this method.
		launch( args);
	}
}
