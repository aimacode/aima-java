/**
 * This package contains classes, which are useful to create map agents. 
 * The contained <code>Map</code> interface provides
 * a simple but sufficient set of read methods for designing
 * environments, agents and viewers. <code>ExtendableMap</code>
 * implements this interface and adds functionality for
 * creation and modification. It maintains named locations with
 * coordinates and links between them.
 * The <code>MapEnvironment</code> enables a <code>MapAgent</code>
 * to travel through a world whose topology is described by a map.
 * Adaptable heuristic functions let the agent modify the
 * heuristic function with respect to the current goal. Standardized
 * dynamic attribute names in class <code>DynAttributeNames</code>
 * make information exchange between environment
 * and agent a bit safer without introducing direct dependencies.
 */
package aima.core.environment.map;