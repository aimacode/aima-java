package aimax.osm.data;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.entities.MapEntity;

/**
 * Classifies map entities with respect to their attributes based
 * on declarative classification rules. A classifier consists of a
 * default classification result and a sequence of classification rules.
 * 
 * <p>Logically, a rule consists of a condition, which constrains name and
 * (optionally) value of one of the entity's attributes, and a sub-classifier.
 * Sub-classifiers should provide a default classification result.
 * Additionally, they can provide further rules which check other
 * attributes. When classifying an entity, the rules are
 * checked in the given order and the classifier of the first match is
 * applied recursively. If no rule match is found, the classifier's default
 * classification result is returned.</p>
 * 
 * <p>For efficiency reasons, rules are grouped by attribute
 * name on implementation level, and within such a group, binary search is
 * used to find a match.</p>
 * @author Ruediger Lunde
 * @param <C> Class of classification results.
 */
public class EntityClassifier<C> {
	List<RuleGroup<C>> rules;
	C defaultEntityClass;
	
	/** Default constructor. */
	public EntityClassifier() {
		rules = new ArrayList<RuleGroup<C>>();
	}
	
	public C getDefaultEntityClass() {
		return defaultEntityClass;
	}
	
	public void setDefaultEntityClass(C defaultEntityClass) {
		this.defaultEntityClass = defaultEntityClass;
	}
	
	/** Clears the default classification result and all rules. */
	public void clear() {
		rules.clear();
		defaultEntityClass = null;
	}
	
	/**
	 * Adds a new classification rule.
	 * @param attName Name of an attribute.
	 * @param attValue Value of an attribute or null.
	 * @param eclass Default classification result, possibly null.
	 * @return The sub-classifier corresponding to the rule.
	 */
	public EntityClassifier<C> addRule(String attName, String attValue, C eclass) {
		EntityClassifier<C> result = new EntityClassifier<C>();
		result.setDefaultEntityClass(eclass);
		RuleGroup<C> rg = null;
		if (!rules.isEmpty()) {
			RuleGroup<C> last = rules.get(rules.size()-1);
			if (last.attName.equals(attName))
				rg = last;
		}
		if (rg == null) {
			rg = new RuleGroup<C>(attName);
			rules.add(rg);
		}
		if (attValue == null)
			rg.defaultSubClassifier = result;
		else {
			int i = 0;
			while (i < rg.attValueRules.size()
					&& attValue.compareTo(rg.attValueRules.get(i).attValue)>0)
				i++;
			rg.attValueRules.add(i, new Rule<C>(attValue, result));
		}
		return result;
	}
	
	/**
	 * Replaces an existing rule and returns the corresponding
	 * sub-classifier if found.
	 */
	public EntityClassifier<C> replaceRule(String attName, String attValue, C eclass) {
		EntityClassifier<C> newClassifier = new EntityClassifier<C>();
		newClassifier.setDefaultEntityClass(eclass);
		for (RuleGroup<C> rg : rules) {
			if (attName.equals(rg.attName)) {
				if (attValue == null) {
					if (rg.defaultSubClassifier != null) {
						rg.defaultSubClassifier = newClassifier;
						return newClassifier;
					}	
				} else {
					for (Rule<C> rule : rg.attValueRules) {
						if (attValue.equals(rule.attValue)) {
							rule.subClassifier = newClassifier;
							return newClassifier;
						}
					}
				}
			}
		}
		return null;
	}
	
	/** Classifies a map entity with respect to the given rules. */
	public C classify(MapEntity entity) {
		C result = null;
		for (RuleGroup<C> rg : rules) {
			String attValue = entity.getAttributeValue(rg.attName);
			if (attValue != null) {
				int min = 0;
				int max = rg.attValueRules.size()-1;
				int curr;
				int cr;
				Rule<C> currRule;
				while (min <= max) {
					curr = (min+max)/2;
					currRule = rg.attValueRules.get(curr);
					cr = attValue.compareTo(currRule.attValue);
					if (cr < 0)
						max = curr-1;
					else if (cr > 0)
						min = curr+1;
					else {
						result = currRule.subClassifier.classify(entity);
						break;
					}
				}
				if (result == null && rg.defaultSubClassifier != null) {
					result =  rg.defaultSubClassifier.classify(entity);
				}
				if (result != null)
					return result;
			}
		}
		return defaultEntityClass;
	}

	
	/////////////////////////////////////////////////////////////////
	// inner classes
	
	/** Maintains a sub-classifier for one attribute value. */
	private static class Rule<C> {
		String attValue;
		EntityClassifier<C> subClassifier;
		Rule(String attValue, EntityClassifier<C> subClassifier) {
			this.attValue = attValue;
			this.subClassifier = subClassifier;
		}
	}
	
	/**
	 * Maintains attribute value rules for one attribute name and
	 * optionally a default sub-classifier which is applied if none
	 * of the rules matches.
	 * @author R. Lunde
	 */
	private static class RuleGroup<C> {
		String attName;
		List<Rule<C>> attValueRules;
		EntityClassifier<C> defaultSubClassifier;
		
		RuleGroup(String attName) {
			this.attName = attName;
			attValueRules = new ArrayList<Rule<C>>();
		}
	}
}
