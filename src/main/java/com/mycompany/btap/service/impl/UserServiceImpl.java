package com.mycompany.btap.service.impl;

import com.mycompany.btap.model.mUser;
import com.mycompany.btap.repository.UserRepository;
import com.mycompany.btap.service.ServiceException;
import com.mycompany.btap.service.UserService;
import com.mycompany.btap.util.PasswordUtil;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepo = new UserRepository();

    @Override
    public String registerUser(String username, String password, String email) throws ServiceException {
        // Simple validations
        if (username == null || username.isBlank()) throw new ServiceException("Username is required");
        if (password == null || password.length() < 6) throw new ServiceException("Password must be at least 6 characters");
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) throw new ServiceException("Email is invalid");

        List<mUser> users = userRepo.readAllUsers();
        // Check username/email uniqueness
        for (mUser u : users) {
            if (u.getUsername() != null && u.getUsername().equalsIgnoreCase(username)) {
                throw new ServiceException("Username already exists");
            }
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(email)) {
                throw new ServiceException("Email already registered");
            }
        }

        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(password.toCharArray(), salt);

        int nextId = users.stream().mapToInt(mUser::getId).max().orElse(0) + 1;
        mUser newUser = new mUser(nextId, username, email, hash, salt);
        userRepo.saveUser(newUser);
        return "Tạo tài khoản thành công";
    }

    @Override
    public mUser loginUser(String username, String password) throws ServiceException {
        List<mUser> users = userRepo.readAllUsers();
        for (mUser u : users) {
            if (u.getUsername() != null && u.getUsername().equalsIgnoreCase(username)) {
                String salt = u.getSalt();
                String storedHash = u.getPasswordHash();
                if (salt == null || storedHash == null) break;
                boolean ok = PasswordUtil.verifyPassword(password.toCharArray(), salt, storedHash);
                if (ok) {
                    // return sanitized user (without password fields) or original
                    return new mUser(u.getId(), u.getUsername(), u.getEmail());
                } else {
                    throw new ServiceException("Sai mật khẩu");
                }
            }
        }
        throw new ServiceException("Người dùng không tồn tại");
    }
}
