package com.learnboot.springbootlearn.mapper;

import com.learnboot.springbootlearn.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author VHBin
 * @date 2021/12/10 - 23:00
 */

@Mapper
public interface UserMapper {
    User getByUserNameAndPassword(User user);

    @Delete("delete from user where id=#{id}")
    int deleteByPrimaryKey(Integer id);

    @Insert("insert into user(user_id, user_name, password, email)" +
            "values(#{userid}, #{username}, #{password}, #{email})")
    int insert(User record);

    @Update("update user" +
            "   set user_id=#{userid}, " +
            "   user_name=#{username}" +
            "   password=#{password}" +
            "   email=#{email}" +
            "   where id=#{id}")
    int updateByPrimaryKey(User record);

}
