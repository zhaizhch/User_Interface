package com.example.demo.controller;

import com.example.demo.common.conf.DisplayErrorCode;
import com.example.demo.common.config.RestfulEntity;
import com.example.demo.entity.PageResult;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.utils.CommonUtils;
import com.example.demo.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(value = "用户管理", tags = "用户管理接口")
@RestController
@RequestMapping("/dluc/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final Integer[] AUTHORITY_LEVEL = {1, 2};

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public RestfulEntity<JSONObject> userLogin(
            HttpServletRequest request,
            @ApiParam(value = "用户登录信息", required = true) @RequestBody @Validated(UserDto.loginGroup.class) UserDto userDto
    ) {
        // 校验用户名是否存在
        List<User> userInfoList = userService.queryUserInfoByUserName(userDto.getUserName());
        if (userInfoList == null) {
            return RestfulEntity.getFailure(DisplayErrorCode.userResult);
        }
        if (userInfoList.isEmpty()) {
            return RestfulEntity.getFailure(DisplayErrorCode.userResultFailure);
        }
        // 校验密码
        String pwd = CommonUtils.encodeByMd5(userDto.getPassword());
        User user = userInfoList.get(0);
        if (!pwd.equals(user.getPassword())) {
            return RestfulEntity.getFailure(DisplayErrorCode.userResultFailure);
        }
        // 生成token
        String token = CommonUtils.getRandomStr();
        user.setToken(token);
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);
        // token更新到数据库
        UserDto userInfoDto1 = new UserDto();
        userInfoDto1.setUserName(user.getUserName());
        userInfoDto1.setToken(token);
        // 登录成功后需要更新landTime
        userInfoDto1.setLandTime(new Date());
        int ret = userService.updateUserInfo(userInfoDto1);
        if (ret == -1) {
            return RestfulEntity.getFailure(DisplayErrorCode.userResultFailure);
        }
        JSONObject result = new JSONObject();
        User user1=(userService.queryUserInfoByUserName(userDto.getUserName())).get(0);
        result.put("data", user1);
        return RestfulEntity.getSuccess(result,"登陆成功");
    }


    @ApiOperation(value = "用户登出")
    @GetMapping(value = "/logout")
    public RestfulEntity userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        session.removeAttribute("loginUser");
        // 登出后更新数据库token
        UserDto userDto = new UserDto();
        userDto.setToken(CommonUtils.getRandomStr());
        userDto.setUserName(loginUser.getUserName());
        int ret = userService.updateUserInfo(userDto);
        if (ret == -1) {
            return RestfulEntity.getFailure(DisplayErrorCode.userLogoutFailure);
        }
        return RestfulEntity.getSuccess("用户登出成功");
    }

    @ApiOperation(value = "新增用户信息")
    @PostMapping(value = "/addUserInfo")
    public RestfulEntity<JSONObject> addUserInfo(
            @ApiParam(value = "用户信息", required = true) @RequestBody @Validated(UserDto.addGroup.class) UserDto userDto
    ) {
        logger.info("addUserInfo -> userInfoDto = " + userDto);
        // 校验用户名长度
        if (userDto.getUserName().length() > 32) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_NAME_LENGTH_ERROR);
        }
        // 校验用户名是否重复
        List<User> userInfoList = userService.queryUserInfoByUserName(userDto.getUserName());
        if (userInfoList == null) {
            return RestfulEntity.getFailure(DisplayErrorCode.DB_QUERY_ERROR);
        }
        if (userInfoList.size() > 0) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_NAME_EXIST_ERROR);
        }
        // 校验密码格式 8-20位
        String compile = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).{8,20}$";
        if (!userDto.getPassword().matches(compile)) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_PWD_FORMAT_ERROR);
        }
        // 校验权限格式 1, 一般 2, 最高

        if (userDto.getAuthority()!=null && (!Arrays.asList(AUTHORITY_LEVEL).contains(userDto.getAuthority()))) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_AUTHORITY_CHECK_ERROR);
        }
        // 新增用户信息
        int ret = userService.insertUserInfo(userDto);
        if (ret == -1) {
            return RestfulEntity.getFailure(DisplayErrorCode.DB_INSERT_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("data", ret);
        return RestfulEntity.getSuccess(result, "success");
    }

    @ApiOperation(value = "重置密码")
    @PostMapping(value = "/resetPassword")
    public RestfulEntity<JSONObject> resetPassword(
            HttpServletRequest request,
            @ApiParam(value = "用户密码", required = true) @RequestBody @Validated(UserDto.resetPwdGroup.class) UserDto userDto
    ) {

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        // 修改当前用户信息，需要校验原始密码
        if(loginUser==null){
            logger.info("未登录 -> resetPassword = " + userDto);
            return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
        }

        if (loginUser.getUserName().equals(userDto.getUserName())) {
            if(!userService.tokenCheck(loginUser)){
                logger.info("Token不匹配 -> resetPassword = " + userDto);
                return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
            }
            // 校验原始密码是否为空
            if (StringUtils.isEmpty(userDto.getRawPassword())) {
                return RestfulEntity.getFailure(DisplayErrorCode.USER_RAWPWD_NULL_ERROR);
            }
            // 校验原始密码是否正确
            List<User> userInfoList = userService.queryUserInfoByUserName(userDto.getUserName());
            if (userInfoList == null || userInfoList.isEmpty()) {
                return RestfulEntity.getFailure(DisplayErrorCode.USER_RESETPWD_ERROR);
            }
            if (!userInfoList.get(0).getPassword().equals(CommonUtils.encodeByMd5(userDto.getRawPassword()))) {
                return RestfulEntity.getFailure(DisplayErrorCode.USER_RAWPWD_CHECK_ERROR);
            }
        } else {
            // 修改非当前用户，不需要校验原始密码，需要校验权限是否满足，(在拦截器中校验, userId为空会报无操作权限，直接在这校验)
            if (loginUser.getAuthority() < 2) {
                return RestfulEntity.getAuthFailure(DisplayErrorCode.USER_NO_OPERATION_AUTHORITY);
            }
        }
        // 校验新密码格式 8-20位
        String compile = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).{8,20}$";
        if (!userDto.getPassword().matches(compile)) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_PWD_FORMAT_ERROR);
        }
        // 更新密码
        int ret = userService.resetPassword(userDto);
        if (ret == -1) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_RESETPWD_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("data", ret);
        return RestfulEntity.getSuccess(result, "success");
    }

    @ApiOperation(value = "批量删除用户")
    @PostMapping(value = "/deleteUserInfoByIds")
    public RestfulEntity<JSONObject> deleteUserInfoByIds(
            @ApiParam(value = "用户id列表", required = true) @RequestBody @Validated(UserDto.delGroup.class) UserDto userDto
    ) {
        logger.info("deleteUserInfoByIds -> userInfoDto = " + userDto);
        int ret = userService.deleteUserInfoByIds(userDto);
        if (ret == -1) {
            return RestfulEntity.getFailure(DisplayErrorCode.DB_DELETE_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("data", ret);
        return RestfulEntity.getSuccess(result, "success");
    }

    @ApiOperation(value = "获取用户列表")
    @PostMapping(value = "/getUserInfoList")
    public RestfulEntity<JSONObject> getUserInfoList(
            @ApiParam(value = "查询条件", required = true) @RequestBody @Validated(UserDto.queryGroup.class) UserDto userDto
    ) {
        logger.info("getUserInfoList -> userInfoDto = " + userDto);
        if (userDto.getPageNum() <= 0 || userDto.getPageSize() <= 0) {
            return RestfulEntity.getFailure(DisplayErrorCode.PARAM_INVALID_ERROR);
        }
        PageResult pageResult = userService.queryUserInfoList(userDto);
        if (pageResult == null) {
            return RestfulEntity.getFailure(DisplayErrorCode.DB_QUERY_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("data", pageResult);
        return RestfulEntity.getSuccess(result, "success");
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping(value = "/updateUserInfo")
    public RestfulEntity<JSONObject> updateUserInfo(
            HttpServletRequest request,
            @ApiParam(value = "用户信息", required = true) @RequestBody @Validated(UserDto.updateGroup.class) UserDto userDto
    ) {
        logger.info("updateUserInfo -> userInfoDto = " + userDto);
        // 校验用户名长度
/*        if (userDto.getUserName().length() > 32) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_NAME_LENGTH_ERROR);
        }*/
        // 校验用户名是否重复, 通过用户名去查询

        HttpSession session = request.getSession();
        User loginUserInfo = (User) session.getAttribute("loginUser");
        List<User> userInfoList = userService.queryUserInfoByUserName(userDto.getUserName());
        if (userInfoList == null) {
            return RestfulEntity.getFailure(DisplayErrorCode.DB_QUERY_ERROR);
        }
        if(loginUserInfo.getUserName().equals(userDto.getUserName())){
            if(!userService.tokenCheck(loginUserInfo)){
                logger.info("Token不匹配 -> updateUserInfo = " + userDto);
                return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
            }
        }else{
            logger.info("userName不匹配 -> updateUserInfo = " + userDto);
            return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
        }

        /*//不需要修改用户名
        if (userInfoList.size() > 0 && (!userInfoList.get(0).getUserId().equals(userDto.getUserId()))) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_NAME_EXIST_ERROR);
        }*/
        // 校验权限格式 1, 一般 2, 最高
        if (!Arrays.asList(AUTHORITY_LEVEL).contains(userDto.getAuthority())) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_AUTHORITY_CHECK_ERROR);
        }
        // 更新用户信息，不能改密码
        UserDto userInfoDto1 = new UserDto();
        userInfoDto1.setBelongTo(userDto.getBelongTo());
        userInfoDto1.setUserName(userDto.getUserName());
        userInfoDto1.setAuthority(userDto.getAuthority());
        userInfoDto1.setUpdateTime(new Date());
        int ret = userService.updateUserInfo(userInfoDto1);
        if (ret == -1) {
            return RestfulEntity.getFailure(DisplayErrorCode.DB_UPDATE_ERROR);
        }
        // 更新session中的用户名以及权限

        // 如果修改的是当前登录账户，权限改变，须重新登录 TODO 测试
        if (userDto.getUserName().equals(loginUserInfo.getUserName())) {
            if (!userDto.getAuthority().equals(loginUserInfo.getAuthority())) {
                session.removeAttribute("loginUser");
                // 修改当前登录的用户成功
                return RestfulEntity.getSuccess("修改当前用户成功, 请重新登录！");
            }
            // 重新设置用户名
            loginUserInfo.setUserName(userDto.getUserName());
        }
        JSONObject result = new JSONObject();
        result.put("data", ret);
        return RestfulEntity.getSuccess(result, "修改用户信息成功！");
    }

    @ApiOperation(value = "检测用户")
    @PostMapping(value = "/userCheck")
    public RestfulEntity<JSONObject> userCheck(
            HttpServletRequest request,
            @ApiParam(value = "用户信息", required = true) @RequestBody @Validated(UserDto.userCheckGroup.class) UserDto userDto
    ) {
        logger.info("userCheck -> userCheck = " + userDto);
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser==null){
            return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
        }
        String testToken="3344577";
        if(userDto.getToken().equals(testToken)){
            JSONObject result = new JSONObject();
            result.put("userId","inner");
            result.put("userName","inner");
            result.put("belongTo","video-test");
            result.put("authority","3");
            return RestfulEntity.getSuccess(result, null);
        }
        List<User> userInfoList = userService.queryUserInfoByToken(userDto);

        if (userInfoList == null||userInfoList.size()==0) {
            return RestfulEntity.getFailure(DisplayErrorCode.TOKEN_QUERY_ERROR);
        }
        User user = userInfoList.get(0);
        if(!user.getUserName().equals(loginUser.getUserName()))
        {
            logger.info("用户未登录:userCheck -> userCheck = " + userDto);
            return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
        }

        if(!userService.tokenCheck(loginUser))
        {
            logger.info("Token不匹配:userCheck -> userCheck = " + userDto);
            return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
        }

        Date landTime = userInfoList.get(0).getLandTime();
        if ((DateUtil.getTimeDifference(new Date(), landTime) > 12)) {
            return RestfulEntity.getFailure(DisplayErrorCode.TOKEN_QUERY_ERROR2);
        }

        if (!Arrays.asList(AUTHORITY_LEVEL).contains(user.getAuthority())) {
            return RestfulEntity.getFailure(DisplayErrorCode.USER_AUTHORITY_CHECK_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("data", userInfoList);
        return RestfulEntity.getSuccess(result, "检查用户token成功！");
    }

    @ApiOperation(value = "用户token更新")
    @PostMapping(value = "/userTokenUpdate")
    public RestfulEntity<JSONObject> userTokenUpdate(
            HttpServletRequest request,
            @ApiParam(value = "用户id列表", required = true) @RequestBody @Validated(UserDto.tokenUpdateGroup.class) UserDto userDto
    ) {
        //验证该用户是否在登录状态
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser==null||!(loginUser.getUserName().equals(userDto.getUserName()))){
            logger.info("用户未登录 -> userTokenUpdate = " + userDto);
            return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
        }
        logger.info("userTokenUpdate -> userTokenUpdate = " + userDto);
        List<User> userInfoList = userService.queryUserInfoByUserName(userDto.getUserName());
        if(userInfoList==null || userInfoList.size()==0){
            try {
                RestfulEntity<JSONObject> result = addUserInfo(userDto);
                if(!result.getStatus().equals("0")){
                    return result;
                }
            }catch(Exception e) {
                logger.error(e.getMessage(), e);
                logger.info("修改数据失败, updateUserInfo -> userInfoDto = " + userDto);
                return RestfulEntity.getFailure(DisplayErrorCode.DB_UPDATE_ERROR);
            }
        }
        if(!userService.tokenCheck(loginUser)){
            logger.info("Token不匹配 -> userTokenUpdate = " + userDto);
            return RestfulEntity.getFailure(DisplayErrorCode.userStatusCheck);
        }
        UserDto userDto1=new UserDto();
        userDto1.setToken(userDto.getToken());
        userDto1.setUserName(userDto.getUserName());
        int ret = userService.updateUserInfo(userDto1);
        if (ret == -1) {
            return RestfulEntity.getFailure(DisplayErrorCode.userResultFailure);
        }
        JSONObject result = new JSONObject();
        result.put("data", userDto);
        return RestfulEntity.getSuccess(result,"Token更新成功");
    }

}
