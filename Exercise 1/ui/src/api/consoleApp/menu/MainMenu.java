package api.consoleApp.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {

    private BaseMenuItem currentMenu;
    private boolean isRunning;
    private final Scanner scanner;
    private boolean waitAfterActiveMethod;

    public void setWaitAfterActiveMethod(boolean waitAfterActiveMethod) {
        this.waitAfterActiveMethod = waitAfterActiveMethod;
    }

    public void setCurrentMenu(BaseMenuItem currentMenu) {
        this.currentMenu = currentMenu;
    }

    public MainMenu() {
        scanner = new Scanner(System.in);
        waitAfterActiveMethod = true;
    }

    public void show() {
        if (currentMenu == null) {
            return;
        }

        isRunning = true;
        while (isRunning) {
            operateCurrentMenu();
        }

        System.out.println("Exit Main Menu");
    }

    private void operateCurrentMenu() {
        System.out.println(currentMenu.toString());
        System.out.println("Enter an option: ");
        BaseMenuItem.MethodType option = BaseMenuItem.MethodType.SUB_MENU;

        try {
            int choice = scanner.nextInt();
            if (currentMenu.isValidOption(choice)) {
                option = currentMenu.click(choice);
            } else {
                System.out.println("There is no such an option. Please enter a valid option number.");
            }
        } catch(InputMismatchException e) {
            System.out.println("The input wasn't a number. Please try again and enter a number.");
        }

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        if (option == BaseMenuItem.MethodType.ACTIVE_METHOD && waitAfterActiveMethod) {
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    public void changeMenu(BaseMenuItem newMenu) {
        currentMenu = newMenu;
    }

    public void backMenu() {
        currentMenu = currentMenu.getParentMenu();
    }

    public void exitMainMenu() {
        isRunning = false;
    }
}
