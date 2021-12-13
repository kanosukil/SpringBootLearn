package com.learnboot.springbootlearn.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author VHBin
 * @date 2021/12/07 - 17:06
 */

@ToString
@Component
public class User {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String userid;
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String email;
}
