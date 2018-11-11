package com.h2r.roomthugon.data;

import com.h2r.roomthugon.data.dao.UserDAO;
import com.h2r.roomthugon.data.model.User;
import com.h2r.roomthugon.data.model.UserDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class UserResponsitory implements UserDataSource {
    private static UserDAO userDAO;
    private static UserResponsitory mInstance;
    public UserResponsitory(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static UserResponsitory getInstance(UserDAO  userDAO){
        if(mInstance==null){
            mInstance = new UserResponsitory(userDAO);
        }
        return mInstance;
    }
    @Override
    public Flowable<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    @Override
    public Flowable<List<User>> getUserByName(String userName) {
        return userDAO.getUserByName(userName);
    }

    @Override
    public Flowable<List<User>> getALlUser() {
        return userDAO.getALlUser();
    }

    @Override
    public void insertUser(User... users) {
        userDAO.insertUser(users);
    }

    @Override
    public void deleteUser(User user) {
        userDAO.deleteUser(user);
    }

    @Override
    public void deleteAllUser() {
        userDAO.deleteAllUser();
    }

    @Override
    public void updateUser(User... users) {
        userDAO.updateUser(users);
    }
}
