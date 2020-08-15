package hsousa.netcdf.schemagen;

import java.util.Properties;

public abstract class AbstractNode {

    protected AbstractAttributeContainer parent;
    protected final Properties properties;

    public AbstractNode(AbstractAttributeContainer parent, Properties properties) {
        this.parent = parent;
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (parent != null) {
            fullName.append(parent.getFullName()).append('/');
        }
        fullName.append(getName());
        return fullName.toString();
    }

    protected String substitute(String category, String name, String defaultValue) {
        String key = String.format("%s.%s", category, name);
        return properties.getProperty(key, defaultValue);
    }

    public abstract String getName();

    public String camelCase(String str) {
        StringBuilder result = new StringBuilder();
        for (String part : str.split("_")) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
        }
        return result.toString();
    }

    public String dromedaryCase(String str) {
        String camelCase = camelCase(str);
        return Character.toLowerCase(camelCase.charAt(0)) + camelCase.substring(1);
    }

}