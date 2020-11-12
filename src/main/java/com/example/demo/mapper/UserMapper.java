package com.example.demo.mapper;

import com.example.demo.entity.User;
import com.example.demo.entity.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    User queryUserInfoByName(UserDto userDto);

    User queryUserInfoById(UserDto userDto);

    List<User> queryUserInfoByToken(UserDto userDto);

    List<User> queryAllUserInfo(UserDto userDto);

    int insertUserInfo(UserDto userDto);

    int deleteUserInfoByIds(UserDto userDto);

    int updateUserInfo(UserDto userDto);

    int deleteUserInfoByName(UserDto userDto);

    int deleteUserInfoByToken(UserDto userDto);

}
