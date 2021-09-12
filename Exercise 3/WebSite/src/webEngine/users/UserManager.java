package webEngine.users;

import java.util.Collection;

public class UserManager {

    private final UserList users = new UserList();

    public synchronized void addUser(User user) {
        users.add(user);
    }

    public synchronized void removeUser(User user) {
        users.remove(user);
    }

    public boolean isUsernameExists(String username) {
        return users.findUserByName(username) != null;
    }

    public Collection<String> getNameList() {
        return users.getNames();
    }

    public User getUserByName(String username) {
        return users.findUserByName(username);
    }
}
