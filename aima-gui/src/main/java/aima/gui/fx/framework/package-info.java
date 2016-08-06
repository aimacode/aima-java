/**
 * This package provides useful classes for building graphical agent
 * applications based on JavaFX. The framework strongly relies on the builder
 * pattern (GoF). A pane for controlling and visualizing agents in their
 * environment can easily be created by means of a
 * {@link SimulationPaneBuilder}. Using {@link IntegrableApplication} as super
 * class for demonstration applications makes several of them integrable into a
 * common window. Such an integrated application, which allows to switch between
 * different demonstration applications and simple console applications at
 * runtime, can easily be created by means of another builder called
 * {@link IntegratedAppPaneBuilder}. Note that controller classes containing GUI
 * logic (often called code-behind files) are marked by <code>Ctrl</code>.
 * 
 * @author Ruediger Lunde
 */
package aima.gui.fx.framework;