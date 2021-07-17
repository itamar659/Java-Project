package api.consoleApp.menu;

public class Menu extends BaseMenuItem {

    private ParameterizedAction<BaseMenuItem> method;
    private static final MethodType THE_ACTION_TYPE = MethodType.SUB_MENU;

    public void setMethod(ParameterizedAction<BaseMenuItem> method) {
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

        method.execute(this);
        return THE_ACTION_TYPE;
    }
}
