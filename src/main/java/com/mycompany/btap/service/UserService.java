package com.mycompany.btap.service;

import com.mycompany.btap.model.mUser;

public interface UserService {
    /**
     * Register a new user. Returns a success message on success.
     */
    String registerUser(String username, String password, String email) throws ServiceException;

    /**
     * Login and return the user on success.
     */
    mUser loginUser(String username, String password) throws ServiceException;
}
