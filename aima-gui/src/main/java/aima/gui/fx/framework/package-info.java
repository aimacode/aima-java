/**
 * This package provides useful classes for building graphical demonstration
 * applications based on JavaFX. The framework strongly relies on the builder
 * pattern (GoF). A pane for controlling and visualizing agents in their
 * environment can easily be created by means of a
 * {@link aima.gui.fx.framework.SimulationPaneBuilder}. Using
 * {@link aima.gui.fx.framework.IntegrableApplication} as super
 * class for demonstration applications makes several of them integrable into a
 * common window. Such an integrated application, which allows to switch between
 * different demonstration applications and simple console applications at
 * runtime, can easily be created by means of another builder called
 * {@link aima.gui.fx.framework.IntegratedAppPaneBuilder}.
 * Note that names of controller classes containing GUI logic (often called
 * code-behind files) end with <code>Ctrl</code>.
 * 
 * @author Ruediger Lunde
 */
package aima.gui.fx.framework;