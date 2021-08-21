package logic.configurable;

public interface Configurable {

    ReadOnlyConfiguration getConfiguration();

    void setParameter(String parameterName, String value);
}