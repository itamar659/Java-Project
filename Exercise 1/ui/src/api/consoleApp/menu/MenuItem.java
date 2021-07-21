package api.consoleApp.menu;

import logic.actions.Action;

public class MenuItem extends BaseMenuItem {

    private Action method;
    private static final MethodType THE_ACTION_TYPE = MethodType.ACTIVE_METHOD;

    public void setMethod(Action method) {
        this.method = method;
    }

    public MenuItem(String label) {
        super(label);
    }

    @Override
    protected MethodType onClick() {
        if (method == null) {
            System.out.println("This option doesn't have any functionality.");
            return THE_ACTION_TYPE;
        }

        method.execute();
        return THE_ACTION_TYPE;
    }
}
