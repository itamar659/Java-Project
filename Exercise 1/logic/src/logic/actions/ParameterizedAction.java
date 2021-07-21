package logic.actions;

@FunctionalInterface
public interface ParameterizedAction<T> {
    void execute(T val);
}
