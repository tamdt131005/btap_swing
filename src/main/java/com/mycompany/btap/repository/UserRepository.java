package com.mycompany.btap.repository;

import com.mycompany.btap.model.mUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
public class UserRepository {

    private final Path dataDir;
    private final File usersFile;
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<mUser>>(){}.getType();

    public UserRepository() {
        String home = System.getProperty("user.home");
        this.dataDir = Path.of(home, ".btap");
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (IOException ignored) {
            
        }
        this.usersFile = dataDir.resolve("users.json").toFile();
    }

    public synchronized void saveUser(mUser user) {
        List<mUser> users = readAllUsers();
        // replace existing user if id matches, else add
        boolean replaced = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                replaced = true;
                break;
            }
        }
        if (!replaced) users.add(user);
        writeUsers(users);
    }

    public synchronized List<mUser> readAllUsers() {
        if (!usersFile.exists()) return new ArrayList<>();
        try (FileReader fr = new FileReader(usersFile)) {
            List<mUser> users = gson.fromJson(fr, listType);
            return users == null ? new ArrayList<>() : users;
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    private synchronized void writeUsers(List<mUser> users) {
        try (FileWriter fw = new FileWriter(usersFile)) {
            gson.toJson(users, fw);
        } catch (Exception ignored) {
        }
    }
}
