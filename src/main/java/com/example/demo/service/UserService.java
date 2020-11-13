package com.example.demo.service;

import com.example.demo.entity.PageResult;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.CommonUtils;
import com.example.demo.utils.PageInfoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.demo.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserMapper userMapper;

    //查询所有用户列表，输出分页结果
    public PageResult queryUserInfoList(UserDto userDto) {
        try {
            PageHelper.startPage(userDto.getPageNum(), userDto.getPageSize());
            List<User> userList = userMapper.queryAllUserInfo(userDto);
            PageInfo<User> userInfoPageInfo = new PageInfo<>(userList);
            // 分页信息封装
            PageResult pageResult = PageInfoUtils.getPageResult(userInfoPageInfo, userList);
            logger.info("queryResultDetail -> pageResult = " + pageResult);
            return pageResult;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("查询数据失败, queryUserInfoList -> userInfoDto = " + userDto);
            return null;
        }
    }

    //通过userName查询数据库中的用户信息
    public User queryUserInfoByUserName(String userName) {
        try {
            UserDto userDto = new UserDto();
            userDto.setUserName(userName);
            User user = userMapper.queryUserInfoByName(userDto);
            logger.info("queryUserInfoByUserName -> userInfoList = " + user);
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("查询数据失败, queryUserInfoByUserName -> userName = " + userName);
            return null;
        }
    }

    //通过userId查询用户信息
    public User queryUserInfoByUserId(String userId) {
        try {
            UserDto userDto = new UserDto();
            userDto.setUserId(userId);
            User user = userMapper.queryUserInfoById(userDto);
            logger.info("queryUserInfoByUserId -> userInfoList = " + user);
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("查询数据失败, queryUserInfoByUserId -> userId = " + userId);
            return null;
        }
    }

    //插入用户信息，authority默认为1，tenant默认为0，随机生成字符串userId，对密码进行加密处理
    public int insertUserInfo(UserDto userDto) {
        int ret = -1;
        try {
            if(userDto.getAuthority()==null){
                userDto.setAuthority(1);
            }
            if(userDto.getTenant()==null){
                userDto.setTenant("0");
            }
            userDto.setUserId(CommonUtils.getRandomStr());
            userDto.setPassword(CommonUtils.encodeByMd5(userDto.getPassword()));
            ret = userMapper.insertUserInfo(userDto);
            if (ret == 0) {
                logger.info("新增数据失败, insertUserInfo -> userInfoDto = " + userDto);
                return -1;
            }
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("新增数据失败, insertUserInfo -> userInfoDto = " + userDto);
            return ret;
        }
    }

    //通过token查询用户信息是否存在
    public List<User> queryUserInfoByToken(UserDto userDto) {
        try{
            List<User> userInfoList = userMapper.queryUserInfoByToken(userDto);
            logger.info("queryUserInfoByToken -> userInfoList = " + userInfoList);
            return userInfoList;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("查询数据失败, queryUserInfoByToken -> token = " + userDto.getToken());
            return null;
        }
    }

    //通过userId更新用户信息：用户名、权限、belongTo、登陆时间、token
    public int updateUserInfo(UserDto userInfoDto) {
        int ret = -1;
        try {
            ret = userMapper.updateUserInfo(userInfoDto);
            if (ret == 0) {
                logger.info("修改数据失败, updateUserInfo -> userInfoDto = " + userInfoDto);
                return -1;
            }
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("修改数据失败, updateUserInfo -> userInfoDto = " + userInfoDto);
            return ret;
        }
    }

    //通过userId更新密码
    public int resetPassword(UserDto userDto) {
        int ret = -1;
        // 更新密码
        userDto.setPassword(CommonUtils.encodeByMd5(userDto.getPassword()));
        try {
            ret = userMapper.updateUserInfo(userDto);
            if (ret == 0) {
                logger.info("修改数据失败, resetPassword -> userInfoDto = " + userDto);
                return -1;
            }
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("修改数据失败, resetPassword -> userInfoDto = " + userDto);
            return ret;
        }
    }

    //通过Id批量删除用户列表
    public int deleteUserInfoByIds(UserDto userInfoDto) {
        int ret = -1;
        try {
            ret = userMapper.deleteUserInfoByIds(userInfoDto);
            if (ret == 0) {
                logger.info("删除数据失败, deleteUserInfoByIds -> userInfoDto = " + userInfoDto);
                return -1;
            }
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("删除数据失败, deleteUserInfoByIds -> userInfoDto = " + userInfoDto);
            return ret;
        }
    }

    //检验token是否有效
    public boolean tokenCheck(User user) {
        try {
            UserDto userInfoDto = new UserDto();
            userInfoDto.setToken(user.getToken());
            List<User> userInfoList = userMapper.queryUserInfoByToken(userInfoDto);
            if (userInfoList.size() > 0) {
                Date landTime = userInfoList.get(0).getLandTime();
                // 登录时间超过12小时，自动退出
                if (DateUtil.getTimeDifference(new Date(), landTime) > 12) {
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("查询数据失败, deleteUserInfoByIds -> token = " + user.getToken());
        }
        return false;
    }
}
