package aima.gui.fx.framework;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Abstract super class for integrable applications. Applications of this kind 
 * consist of a title, a root pane, and methods for initialization and finalization.
 * They inherit a start method which cannot be changed. This makes them integrable
 * into a common main window.
 * 
 * @author Ruediger Lunde
 */
public abstract class IntegrableApplication extends Application {

	protected double sceneWidth = 1200;
	protected double sceneHeight = 800;

	/**
	 * Template method for starting all integrable applications as stand-alone applications.
	 */
	@Override
	public final void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(getTitle());
		primaryStage.setScene(new Scene(createRootPane(), sceneWidth, sceneHeight));
		primaryStage.show();
		initialize();
	}

	/** Primitive operation, returning the title of the application. */
	public abstract String getTitle();
	
	/** Primitive operation, creating the pane to be used as root of the application's scene. */
	public abstract Pane createRootPane();
	
	/** Primitive operation, defining initialization steps (scene graph has been created and made visible on screen before). */
	public abstract void initialize();
	
	/**
	 * Finalization steps to be performed when switching between applications (needed especially in case
	 * several applications shall be integrated into a common window).
	 */
	public abstract void cleanup();
}
