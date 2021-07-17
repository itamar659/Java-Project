package api.consoleApp.menu;

@FunctionalInterface
public interface ParameterizedAction<T> {
    void execute(T val);
}
