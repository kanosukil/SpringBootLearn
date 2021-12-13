package com.learnboot.springbootlearn.service.Impl;

import com.learnboot.springbootlearn.service.PersonService;
import com.learnboot.springbootlearn.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author VHBin
 * @date 2021/12/04 - 15:02
 */

public class PersonServiceImpl implements PersonService {

    @Autowired
    private Person person;

    @Override
    public Person getPersonInfo() {
        return person;
    }
}
