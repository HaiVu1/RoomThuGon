package com.h2r.roomthugon.data.model;

import java.util.List;

import io.reactivex.Flowable;

public interface UserDataSource {
    Flowable<User> getUserByUserId(int userId);
    Flowable<List<User>> getUserByName(String userName);
    Flowable<List<User>> getALlUser();
    void insertUser(User... users);
    void deleteUser(User user);
    void deleteAllUser();
    void updateUser(User... users);
}
