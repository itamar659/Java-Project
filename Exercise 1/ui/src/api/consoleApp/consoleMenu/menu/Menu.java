package api.consoleApp.consoleMenu.menu;

import java.util.function.Consumer;

public class Menu extends BaseMenuItem {

    private Consumer<BaseMenuItem> method;
    private static final MethodType THE_ACTION_TYPE = MethodType.SUB_MENU;

    public void setMethod(Consumer<BaseMenuItem> method) {
        this.method = method;
    }

    public Menu(String label) {
        super(label);
    }

    @Override
    protected MethodType onClick() {
        if (method == null) {
            System.out.println("This option doesn't have any functionality.");
            return THE_ACTION_TYPE;
        }

        method.accept(this);
        return THE_ACTION_TYPE;
    }
}
