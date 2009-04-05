package aima.learning.knowledge;

import java.util.ArrayList;
import java.util.List;

import aima.learning.framework.DataSetSpecification;
import aima.logic.fol.domain.FOLDomain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLDataSetDomain extends FOLDomain {
	private DataSetSpecification dataSetSpecification;
	private String trueGoalValue = null;
	// Default example prefix, see pg679 of AIMA
	private String examplePrefix = "X"; 
	private List<String> descriptionPredicateNames = new ArrayList<String>();
	
	//
	// PUBLIC METHODS
	//
	public FOLDataSetDomain(DataSetSpecification dataSetSpecification, String trueGoalValue) {
		this.dataSetSpecification = dataSetSpecification;
		this.trueGoalValue = trueGoalValue;
		constructFOLDomain();
	}
	
	public String getGoalPredicateName() {
		return dataSetSpecification.getTarget();
	}
	
	public String getTrueGoalValue() {
		return trueGoalValue;
	}
	
	public List<String> getDescriptionPredicateNames() {
		return descriptionPredicateNames;
	}
	
	public boolean isMultivalued(String descriptivePredicateName) {
		List<String> possibleValues = dataSetSpecification.getPossibleAttributeValues(descriptivePredicateName);
		// If more than two possible values
		// then is multivalued
		if (possibleValues.size() > 2) {
			return true;
		}
		// If one of the possible values for the attribute
		// matches the true goal value then consider
		// it not being multivalued.
		for (String pv : possibleValues) {
			if (trueGoalValue.equals(pv)) {
				return false;
			}
		}
		
		return true;
	}
	
	public String getExampleConstant(int egNo) {
		String egConstant = examplePrefix+egNo;
		addConstant(egConstant);
		return egConstant;
	}
	
	//
	// PRIVATE METHODS
	//
	private void constructFOLDomain() {
		// Ensure the target predicate is included
		addPredicate(dataSetSpecification.getTarget());
		// Create the descriptive predicates
		for (String saName : dataSetSpecification.getNamesOfStringAttributes()) {			
			if (dataSetSpecification.getTarget().equals(saName)) {
				// Don't add the target to the descriptive predicates
				continue;
			}
			// Add a predicate for the attribute
			addPredicate(saName);
			
			descriptionPredicateNames.add(saName);
			
			List<String> attributeValues = dataSetSpecification.getPossibleAttributeValues(saName);
			// If a multivalued attribute need to setup
			// Constants for the different possible values
			if (isMultivalued(saName)) {
				for (String av : attributeValues) {
					addConstant(av);
				}
			}
		}
	}
}
