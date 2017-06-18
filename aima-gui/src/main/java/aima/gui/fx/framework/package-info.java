/**
 * This package provides useful classes for building graphical demo
 * applications based on JavaFX. The framework strongly relies on the builder
 * pattern (GoF). A pane for controlling and visualizing agents in their
 * environment can easily be created by means of a
 * {@link aima.gui.fx.framework.TaskExecutionPaneBuilder}. Using
 * {@link aima.gui.fx.framework.IntegrableApplication} as super
 * class for demo applications makes several of them integrable into a
 * common window. Such an integrated application, which allows to switch between
 * different demo applications and simple command line demos at
 * runtime, can easily be created by means of another builder called
 * {@link aima.gui.fx.framework.IntegratedAppBuilder}.
 * Note that names of controller classes containing GUI logic (often called
 * code-behind files) end with <code>Ctrl</code>.
 * 
 * @author Ruediger Lunde
 */
package aima.gui.fx.framework;