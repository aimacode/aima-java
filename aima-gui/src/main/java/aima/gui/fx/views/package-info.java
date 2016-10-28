/**
 * This package provides useful controller classes vor the visualization of system states in simulation applications.
 * Each controller class provides a constructor which accepts a root node (either a BorderPane or a Canvas).
 * The constructor adds elements to the node which will be used to visualize the system state. Updates and interactions
 * are handled by the controller. Some controllers know the system and its current state after
 * initialization, others receive the required information in their update methods.
 * 
 * @author Ruediger Lunde
 */
package aima.gui.fx.views;