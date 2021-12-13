package com.learnboot.springbootlearn.service.Impl;

import com.learnboot.springbootlearn.entities.User;
import com.learnboot.springbootlearn.mapper.UserMapper;
import com.learnboot.springbootlearn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author VHBin
 * @date 2021/12/10 - 23:27
 */

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getByUserNameAndPassword(User user) {
        return userMapper.getByUserNameAndPassword(user);
    }
}
