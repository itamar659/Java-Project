package api.consoleApp.consoleMenu.menu;

public class MenuItem extends BaseMenuItem {

    private Runnable method;
    private static final MethodType THE_ACTION_TYPE = MethodType.ACTIVE_METHOD;

    public void setMethod(Runnable method) {
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

        method.run();
        return THE_ACTION_TYPE;
    }
}
