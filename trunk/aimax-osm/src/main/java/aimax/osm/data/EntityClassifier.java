package aimax.osm.data;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.entities.MapEntity;

/**
 * Classifies map entities with respect to their attributes based
 * on declarative classification rules. Logically, a rule consists of a
 * condition which constrains name and value of one of the entity's attribute
 * and a classification result. When classifying an entity, the rules are
 * checked in the given order and the classification result of the first
 * match is returned. For efficiency reasons, rules are grouped by attribute
 * name on implementation level, and within such a group, binary search is
 * used to find a match.
 * @author R. Lunde
 * @param <C> Class of classification results.
 */
public class EntityClassifier<C> {
	List<RuleGroup<C>> rules = new ArrayList<RuleGroup<C>>();
	C defaultEntityClass;
	
	/** Default constructor. */
	public C getDefaultEntityClass() {
		return defaultEntityClass;
	}
	
	/** Clears all rules. */
	public void clear() {
		rules.clear();
		defaultEntityClass = null;
	}
	
	/**
	 * Adds a new classification rule.
	 * @param attName Name of an attribute or null.
	 * @param attValue Value of an attribute or null.
	 * @param eclass Classification result.
	 */
	public void addRule(String attName, String attValue, C eclass) {
		if (attName == null) {
			defaultEntityClass = eclass;
		} else {
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
				rg.defaultEntityClass = eclass;
			else {
				int i = 0;
				while (i < rg.attValueRules.size()
						&& attValue.compareTo(rg.attValueRules.get(i).attValue)>0)
					i++;
				rg.attValueRules.add(i, new Rule<C>(attValue, eclass));
			}
		}
	}
	
	/** Replaces an existing rule and returns true if found. */
	public boolean replaceRule(String attName, String attValue, C eclass) {
		if (attName == null) {
			defaultEntityClass = eclass;
		} else {
			for (RuleGroup<C> rg : rules) {
				if (attName.equals(rg.attName)) {
					if (attValue == null) {
						if (rg.defaultEntityClass != null) {
							rg.defaultEntityClass = eclass;
							return true;
						}	
					} else {
						for (Rule<C> rule : rg.attValueRules) {
							if (attValue.equals(rule.attValue)) {
								rule.entityClass = eclass;
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/** Classifies a map entity with respect to the given rules. */
	public C classify(MapEntity entity) {
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
					if (cr == 0)
						return currRule.entityClass;
					else if (cr < 0)
						max = curr-1;
					else
						min = curr+1;
				}
				if (rg.defaultEntityClass != null)
					return rg.defaultEntityClass;
			}
		}
		return defaultEntityClass;
	}

	
	/////////////////////////////////////////////////////////////////
	// inner classes
	
	/** Maintains a classification result for one attribute value. */
	private static class Rule<C> {
		String attValue;
		C entityClass;
		Rule(String attValue, C entityClass) {
			this.attValue = attValue;
			this.entityClass = entityClass;
		}
	}
	
	/**
	 * Maintains rules for one attribute name and optionally a default
	 * classification result if none of the rules match.
	 * @author R. Lunde
	 */
	private static class RuleGroup<C> {
		String attName;
		List<Rule<C>> attValueRules;
		C defaultEntityClass;
		
		RuleGroup(String attName) {
			this.attName = attName;
			attValueRules = new ArrayList<Rule<C>>();
		}
	}
}
