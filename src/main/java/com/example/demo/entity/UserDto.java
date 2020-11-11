package com.example.demo.entity;




import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserDto {


    // 分组校验
    public static interface queryGroup {
    }

    public static interface userCheckGroup {
    }

    public static interface addGroup {
    }

    public static interface loginGroup {
    }

    public static interface updateGroup {
    }

    public static interface resetPwdGroup {
    }

    public static interface delGroup {
    }

    public static interface tokenUpdateGroup {
    }



    private String userId;

    @NotEmpty(message = "用户名不能为空", groups = {addGroup.class, loginGroup.class, updateGroup.class, resetPwdGroup.class,tokenUpdateGroup.class})
    private String userName;

    @NotEmpty(message = "用户密码不能为空", groups = {addGroup.class, loginGroup.class, resetPwdGroup.class})
    private String password;

    @NotEmpty(message = "用户belongTo不能为空", groups = {addGroup.class, updateGroup.class})
    private String belongTo;

    @NotEmpty(message = "用户原始密码不能为空", groups = {resetPwdGroup.class})
    private String rawPassword;

    @NotNull(message = "用户权限不能为空", groups = {updateGroup.class})
    private Integer authority;

    @NotEmpty(message = "用户id列表不能为空", groups = {delGroup.class})
    private List<String> userIds;

    @NotEmpty(message = "用户id列表不能为空", groups = {userCheckGroup.class,tokenUpdateGroup.class})
    private String token;
    private String tenant;
    private Date createTime;
    private Date updateTime;
    private Date landTime;
    // 已废弃
    private String groupName;
    private Date loseTime;


    // 分页参数 页码，每页条数
    @NotNull(message = "页码不能为空", groups = {queryGroup.class})
    private Integer pageNum;

    @NotNull(message = "每页条数不能为空", groups = {queryGroup.class})
    private Integer pageSize;


    public Date getLoseTime(){return loseTime;}

    public void setLoseTime(Date losetime){this.loseTime=loseTime;}

    public String getUserId() {
        return userId;
    }

    public String getTenant(){
        return tenant;
    }

    public void setTenant(String tenant){ this.tenant=tenant;}

    public String getBelongTo(){
        return belongTo;
    }

    public void setBelongTo(String belongTo){ this.belongTo=belongTo;}

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Date getLandTime() {
        return landTime;
    }

    public void setLandTime(Date landTime) {
        this.landTime = landTime;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    @Override
    public String toString() {
        return "UserInfoDto{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", belongTo='" + belongTo + '\'' +
                ", userName='" + userName + '\'' +
                ", authority=" + authority +
                ", userIds=" + userIds +
                ", token='" + token + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", landTime=" + landTime +
                ", groupName='" + groupName + '\'' +
                ", pageNum=" + pageNum +
                ", loseTime=" + loseTime +
                ", pageSize=" + pageSize +
                '}';
    }
}
