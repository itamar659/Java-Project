package api.consoleApp.consoleMenu.menu;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMenuItem {

    public enum MethodType {
        SUB_MENU, ACTIVE_METHOD;
    }

    private final String label;
    private BaseMenuItem parentMenu;
    private final List<BaseMenuItem> menuItems;

    public BaseMenuItem getParentMenu() {
        return parentMenu;
    }

    public BaseMenuItem(String label) {
        this.label = label;
        this.parentMenu = null;
        menuItems = new ArrayList<>();
    }

    public void addMenuItem(BaseMenuItem menuItem) {
        menuItem.parentMenu = this;
        menuItems.add(menuItem);
    }

    public boolean isValidOption(int choice) {
        return 1 <= choice && choice <= menuItems.size();
    }

    public MethodType click(int choice) {
        return menuItems.get(choice - 1).onClick();
    }

    @Override
    public String toString() {
        return label + System.lineSeparator() +
                textLineSeparator(label.length()) + System.lineSeparator() +
                getMenuOptionsString();
    }

    private String textLineSeparator(int size) {
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < size; i++) {
            separator.append("=");
        }

        return separator.toString();
    }

    private String getMenuOptionsString() {
        StringBuilder menuOptionsStr = new StringBuilder();
        int optionIdx = 1;

        for (BaseMenuItem menuItem : menuItems) {
            menuOptionsStr.append(optionIdx + ". " + menuItem.label + System.lineSeparator());
            optionIdx++;
        }

        return menuOptionsStr.toString();
    }

    protected abstract MethodType onClick();
}
