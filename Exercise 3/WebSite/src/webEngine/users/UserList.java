package webEngine.users;

import java.util.*;
import java.util.stream.Collectors;

public class UserList implements Iterable<User> {

    private final Set<User> users = new HashSet<>();

    public void add(User user) {
        users.add(user);
    }

    public void remove(User user) {
        users.remove(user);
    }

    public User findUserByName(String username) {
        return users.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst()
                .orElse(null);
    }

    public int size() {
        return users.size();
    }

    public Collection<String> getNames() {
        return users.stream().map(User::getName).collect(Collectors.toList());
    }

    @Override
    public Iterator<User> iterator() {
        return this.users.iterator();
    }
}
