package engine;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Listeners implements Serializable {

    private transient Set<Runnable> listeners;

    public void add(Runnable action) {
        if (listeners == null) {
            this.listeners = new HashSet<>();
        }

        listeners.add(action);
    }

    public void remove(Runnable action) {
        if (listeners != null) {
            listeners.remove(action);
            if (listeners.size() == 0) {
                listeners = null;
            }
        }
    }

    public boolean contains(Runnable action) {
        if (listeners != null) {
            return listeners.contains(action);
        }

        return false;
    }

    public void raiseEvent() {
        if (listeners != null) {
            for (Runnable action : listeners) {
                action.run();
            }
        }
    }
}
