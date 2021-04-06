package top.webra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.mapper.InformMapper;
import top.webra.mapper.UserMapper;
import top.webra.pojo.Inform;
import top.webra.pojo.User;
import top.webra.service.UserService;
import top.webra.util.CastUtil;
import top.webra.util.JwtUtil;
import top.webra.util.MD5Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:10
 * @Description: 用户逻辑业务类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private InformMapper informMapper;

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private LogServiceImpl logService;


    /**
     *
     * @param departmentId      部门id
     * @param username          用户名
     * @param phone             手机号
     * @param state             状态0/1
     * @param createDateStart   创建起始范围
     * @param createDateEnd     创建结束范围
     * @param page              页码
     */
    public ResponseBean getUserList(Integer departmentId, String username, Integer phone, Integer state, String createDateStart, String createDateEnd, Integer page) {
        List<Integer> departmentIds = new ArrayList<>();
        // 部门参数处理
        if (departmentId != null){
            // 需要判该部门是否还有下级部门，将自身及下级部门的id返回成一个列表 交给 departmentIds
            departmentService.getChildrenIds(departmentIds, departmentId);
        }else {
            departmentIds = null;
        }
        // 时间参数处理
        if (!"".equals(createDateStart) && createDateStart != null){
            createDateStart = CastUtil.toDateFormat(createDateStart);
            createDateEnd = CastUtil.toDateFormat(createDateEnd);
        }
        // 查询数据
        PageHelper.startPage(page,10);
        List<User> userList = userMapper.getUserList(departmentIds, username, phone, state, createDateStart, createDateEnd);
        PageInfo<User> userPageInfo = new PageInfo<>(userList);

        // 整理数据并返回
        HashMap<String, Object> data = new HashMap<>();
        data.put("userList", userList);
        data.put("total",userPageInfo.getTotal());
        data.put("page",userPageInfo.getPages());
        responseBean.buildOk(data);

        return responseBean;
    }

    // 根据id查询用户
    public ResponseBean selectUser(Integer userId){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .select("id", "username", "nickname","department_id", "post_id", "role_id","phone", "email", "state", "remark")
                .eq("id",userId);
        User user = userMapper.selectOne(userQueryWrapper);
        System.out.println(user);
        if (user == null){
            responseBean.buildNoData();
        }else {
            HashMap<String, Object> data = new HashMap<>();
            data.put("user",user);
            responseBean.buildOk(data);
        }
        return responseBean;
    }

    /**
     * 用于创建/修改部门时选择负责人
     */
    public ResponseBean selectUserByNickname() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("id", "nickname");
        List<User> users = userMapper.selectList(userQueryWrapper);
        HashMap<String, Object> data = new HashMap<>();
        data.put("userList", JSON.toJSONString(users));
        responseBean.buildOk(data);
        return responseBean;
    }
    // 删除用户
    public ResponseBean deleteUser(String token, Integer id) {
        int delete = userMapper.deleteById(id);
        String username = JwtUtil.getUsername(token);
        if (delete == 1){
            informMapper.update(null, new UpdateWrapper<Inform>().eq("user_id", id).set("user_id", 1));
            logService.createLog("删除用户", username,"删除成功");
            responseBean.buildOkMsg("删除用户成功");
        }else {
            logService.createLog("删除用户", username,"删除失败,数据库可能存在异常");
            responseBean.buildNoDataMsg("数据异常");
        }
        return responseBean;
    }

    // 批量删除用户
    public ResponseBean deleteUsers(String token, String ids) {
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(Integer.valueOf(s));
        }
        String username = JwtUtil.getUsername(token);
        int deletes = userMapper.deleteBatchIds(integers);
        if (deletes == 0){
            logService.createLog("删除用户", username,"批量删除失败,数据库可能存在异常");
            responseBean.buildNoDataMsg("数据异常");
        }else {
            informMapper.update(null, new UpdateWrapper<Inform>().in("user_id", integers).set("user_id", 1));
            logService.createLog("删除用户", username,"批量删除成功");
            responseBean.buildOkMsg("批量删除用户成功");
        }
        return responseBean;
    }

    // 新建/修改用户
    public ResponseBean saveUser(String token, User user) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String username = JwtUtil.getUsername(token);
        user.setUpdateDate(timestamp);
        if (user.getId().equals(0)){
            user.setPassword("c4b671607e99c43686160e3f31a44e033a1211a818f17534");
            user.setId(null);
            user.setCreateDate(timestamp);
            user.setAvatar("/touxiang.gif");
            insertUser(username, user);
        }else {
            updateUser(username, user);
        }
        return responseBean;
    }
    // 修改用户状态
    public ResponseBean updateUserState(String token, Integer id) {
        User user = userMapper.selectById(id);
        Integer state = user.getState()==1?0:1;
        int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("state", state).last("limit 1"));
        String username = JwtUtil.getUsername(token);
        if (update ==1){
            logService.createLog("修改用户状态", username,"修改用户状态:"+ user.getUsername() + ",修改成功");
            responseBean.buildOkMsg("修改状态成功");
        }else {
            logService.createLog("修改用户状态", username,"修改用户状态:"+ user.getUsername() + ",修改失败,数据库可能存在异常");
            responseBean.buildNoDataMsg("数据异常");
        }
        return responseBean;
    }

    // 修改用户密码（非自己的），这里还需要进一步验证权限啊
    public ResponseBean updatePassword(String token, Integer id, String rootPassword, String newPassword) {
        JwtUtil jwtUtil = new JwtUtil();
        Claims claims = jwtUtil.parseJWT(token);
        Object userId = claims.get("jti");
        User user = userMapper.selectById(CastUtil.toInteger(userId));
        String username = JwtUtil.getUsername(token);
        if (MD5Util.getSaltverifyMD5(rootPassword, user.getPassword())) {
            int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("password", MD5Util.getSaltMD5(newPassword)));
            if (update ==1){
                logService.createLog("修改用户密码", username,"修改成功");
                responseBean.buildOkMsg("修改用户密码成功");
            }else {
                logService.createLog("修改用户密码", username,"修改失败,数据库可能存在异常");
                responseBean.buildNoDataMsg("数据异常");
            }
        }else {
            logService.createLog("修改用户密码", username,"管理用户自身密码填写错误");
            responseBean.buildNoDataMsg("您的密码错误");
        }
        return responseBean;
    }

    /**
     * 注册一个用户
     * @param user 新增用户
     */
    private void insertUser(String username, User user) {
        int insert = userMapper.insert(user);
        if (insert == 1){
            logService.createLog("新建用户", username,"新建用户:"+ user.getUsername() + ",新建成功");
            responseBean.buildOkMsg("新增用户成功");
        }else {
            logService.createLog("新建用户", username,"新建用户:"+ user.getUsername() + ",新建失败,数据库可能存在异常");
            responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 修改用户信息
     * @param user 更新用户
     */
    private void updateUser(String username, User user) {
        User oldUser = userMapper.selectById(user.getId());
        user.setPassword(oldUser.getPassword());
        int i = userMapper.updateById(user);
        if (i == 1){
            logService.createLog("修改用户", username,"修改用户:"+ user.getUsername() + ",修改成功");
            responseBean.buildOkMsg("修改用户成功");
        } else {
            logService.createLog("修改用户", username,"修改用户:"+ user.getUsername() + ",修改失败,数据库可能存在异常");
            responseBean.buildNoDataMsg("数据异常");
        }
    }

}