package aima.core.learning.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import aima.core.learning.framework.DataSetSpecification;
import aima.core.logic.fol.domain.FOLDomain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLDataSetDomain extends FOLDomain {
	//
	private static Pattern allowableCharactersRegEx = Pattern
			.compile("[^a-zA-Z_$0-9]");
	//
	private DataSetSpecification dataSetSpecification;
	private String trueGoalValue = null;
	// Default example prefix, see pg679 of AIMA
	private String examplePrefix = "X";
	private List<String> descriptionPredicateNames = new ArrayList<String>();
	private List<String> descriptionDataSetNames = new ArrayList<String>();
	private Map<String, String> dsToFOLNameMap = new HashMap<String, String>();

	//
	// PUBLIC METHODS
	//
	public FOLDataSetDomain(DataSetSpecification dataSetSpecification,
			String trueGoalValue) {
		this.dataSetSpecification = dataSetSpecification;
		this.trueGoalValue = trueGoalValue;
		constructFOLDomain();
	}

	public String getDataSetTargetName() {
		return dataSetSpecification.getTarget();
	}

	public String getGoalPredicateName() {
		return getFOLName(dataSetSpecification.getTarget());
	}

	public String getTrueGoalValue() {
		return trueGoalValue;
	}

	public List<String> getDescriptionPredicateNames() {
		return descriptionPredicateNames;
	}

	public List<String> getDescriptionDataSetNames() {
		return descriptionDataSetNames;
	}

	public boolean isMultivalued(String descriptiveDataSetName) {
		List<String> possibleValues = dataSetSpecification
				.getPossibleAttributeValues(descriptiveDataSetName);
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
		String egConstant = examplePrefix + egNo;
		addConstant(egConstant);
		return egConstant;
	}

	public String getFOLName(String dsName) {
		String folName = dsToFOLNameMap.get(dsName);
		if (null == folName) {
			folName = dsName;
			if (!Character.isJavaIdentifierStart(dsName.charAt(0))) {
				folName = "_" + dsName;
			}
			folName = allowableCharactersRegEx.matcher(folName).replaceAll("_");
			dsToFOLNameMap.put(dsName, folName);
		}

		return folName;
	}

	//
	// PRIVATE METHODS
	//
	private void constructFOLDomain() {
		// Ensure the target predicate is included
		addPredicate(getFOLName(dataSetSpecification.getTarget()));
		// Create the descriptive predicates
		for (String saName : dataSetSpecification.getNamesOfStringAttributes()) {
			if (dataSetSpecification.getTarget().equals(saName)) {
				// Don't add the target to the descriptive predicates
				continue;
			}
			String folSAName = getFOLName(saName);
			// Add a predicate for the attribute
			addPredicate(folSAName);

			descriptionPredicateNames.add(folSAName);
			descriptionDataSetNames.add(saName);

			List<String> attributeValues = dataSetSpecification
					.getPossibleAttributeValues(saName);
			// If a multivalued attribute need to setup
			// Constants for the different possible values
			if (isMultivalued(saName)) {
				for (String av : attributeValues) {
					addConstant(getFOLName(av));
				}
			}
		}
	}
}
