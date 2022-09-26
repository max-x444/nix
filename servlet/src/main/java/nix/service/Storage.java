package nix.service;

import lombok.NonNull;
import nix.model.User;

import java.util.ArrayList;
import java.util.List;

public final class Storage {
    private static final List<User> USER_LIST = new ArrayList<>();

    private Storage() {
    }

    public static void addUser(@NonNull final User user) {
        USER_LIST.add(user);
    }

    public static List<User> getUsers() {
        return USER_LIST;
    }
}