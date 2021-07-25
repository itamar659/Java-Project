package logic.evoAlgorithm.base;

public interface Parameterizable {

    void setValue(String parameterName, Object value);

    Object getValue(String parameterName);
}
