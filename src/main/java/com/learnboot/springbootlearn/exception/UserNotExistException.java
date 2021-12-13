package com.learnboot.springbootlearn.exception;

/**
 * @author VHBin
 * @date 2021/12/08 - 19:19
 */

public class UserNotExistException extends RuntimeException {
    public UserNotExistException() {
        super("用户不存在!");
    }
}
