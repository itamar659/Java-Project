package logic;

public interface Parameterizable {

    void setValue(String parameterName, Object value);

    Object getValue(String parameterName);
}
