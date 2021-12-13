package com.learnboot.springbootlearn.service;

import com.learnboot.springbootlearn.entities.User;

/**
 * @author VHBin
 * @date 2021/12/10 - 23:26
 */

public interface UserService {
    public User getByUserNameAndPassword(User user);
}
