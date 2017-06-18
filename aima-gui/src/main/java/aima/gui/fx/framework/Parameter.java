package aima.gui.fx.framework;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Parameter for simulation experiments. A parameter has a unique name, a finite
 * sequence of values, and a finite sequence of value names. A default value can
 * be specified. Otherwise the first value is the default value. If value names
 * are not specified, a default value description is generated following a
 * name-value equation pattern.
 * 
 * @author Ruediger Lunde
 */
public class Parameter {
	private String name;
	private List<Object> values;
	private List<String> valueNames;
	private int defaultValueIndex;
	private String dependsOnParam;
	private List<String> dependsOnValues;

	public static Parameter find(List<Parameter> params, String paramName) {
		Optional<Parameter> result = params.stream().filter(p -> p.getName().equals(paramName)).findFirst();
		return result.isPresent() ? result.get() : null;
	}

	public static int indexOf(List<Parameter> params, String paramName) {
		for (int i = 0; i < params.size(); i++)
			if (params.get(i).getName().equals(paramName))
				return i;
		return -1;
	}

	public Parameter(String name, Object... values) {
		this.name = name;
		setValues(values);
	}

	public void setValues(Object... values) {
		this.values = Arrays.asList(values);
		valueNames = this.values.stream().map(v -> name + "=" + v).collect(Collectors.toList());

	}

	public void setValueNames(String... valueNames) {
		this.valueNames = Arrays.asList(valueNames);
		if (values != null && values.size() != this.valueNames.size())
			throw new IllegalArgumentException("Different number of values and value names for parameter " + name);
	}

	public void setDefaultValueIndex(int idx) {
		defaultValueIndex = idx;
	}

	/**
	 * Indicates that this parameter is only relevant, if one of the specified
	 * values has been assigned to another parameter.
	 */
	public void setDependency(String paramName, String... values) {
		dependsOnParam = paramName;
		dependsOnValues = Arrays.asList(values);
	}

	public String getName() {
		return name;
	}

	public List<String> getValueNames() {
		return valueNames;
	}

	public List<Object> getValues() {
		return values;
	}

	public int getDefaultValueIndex() {
		return defaultValueIndex;
	}
	
	public String getDependencyParameter() {
		return dependsOnParam;
	}
	
	public List<String> getDependencyValues() {
		return dependsOnValues;
	}
}
