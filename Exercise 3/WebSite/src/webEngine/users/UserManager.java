package webEngine.users;

import java.util.Collection;

public class UserManager {

    private final UserList users = new UserList();

    public synchronized void addUser(String username) {
        users.add(new User(username));
    }

    public synchronized void removeUser(String username) {
        users.remove(users.findUserByName(username));
    }

    public synchronized boolean isUserExists(String username) {
        return users.findUserByName(username) != null;
    }

    public Collection<String> getNameList() {
        return users.getNames();
    }
}
