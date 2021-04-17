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
import top.webra.mapper.DepartmentMapper;
import top.webra.mapper.InformMapper;
import top.webra.mapper.UserMapper;
import top.webra.pojo.Department;
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
    private DepartmentMapper departmentMapper;
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
    @Override
    public String getUserList(String token, Integer departmentId, String username, Integer phone, Integer state, String createDateStart, String createDateEnd, Integer page) {
        // 参数处理
        List<Integer> departmentIds = new ArrayList<>();
        // 时间参数处理
        if (!"".equals(createDateStart) && createDateStart != null){
            createDateStart = CastUtil.toDateFormat(createDateStart);
            createDateEnd = CastUtil.toDateFormat(createDateEnd);
        }

        // 权限判断
        Claims claims = JwtUtil.getClaims(token);
        String stringRoles = claims.get("roles").toString();
        Integer id = Integer.valueOf(claims.get("jti").toString());

        // 管理全部用户
        if (stringRoles.contains("17")) {
            // 部门参数处理
            if (departmentId != null){
                // 需要判该部门是否还有下级部门，将自身及下级部门的id返回成一个列表 交给 departmentIds
                departmentService.getChildrenIds(departmentIds, departmentId);
            }else {
                departmentIds = null;
            }
        }else if (stringRoles.contains("18")){
            // 管理自身部门权限下的用户

            Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("user_id", id).last("limit 1"));
            // 判断部门不为空并且
            if (department != null){
                // 自身权限管理下的所有部门id
                ArrayList<Integer> ids = new ArrayList<>();
                departmentService.getChildrenIds(ids, department.getId());
                if (departmentId != null){
                    if (ids.contains(departmentId)) {
                        departmentService.getChildrenIds(departmentIds, departmentId);
                    }else {
                        // 查询部门id 不在自身权限下，返回空
                        return responseBean.buildOkInitNull("userList");
                    }
                }else {
                    departmentIds = ids;
                }
            }else {
                // 查询自身是18 可管理自身部门下所有用户，但是自身不是部门负责人
                return responseBean.buildOkInitNull("userList");
            }
        }else {
            // 没有权限
            return responseBean.buildOkInitNull("userList");
        }

        // 查询数据
        PageHelper.startPage(page,10);
        List<User> userList = userMapper.getUserList(id, departmentIds, username, phone, state, createDateStart, createDateEnd);
        PageInfo<User> userPageInfo = new PageInfo<>(userList);


        // 整理数据并返回
        HashMap<String, Object> data = new HashMap<>(3);
        data.put("userList", userList);
        data.put("total",userPageInfo.getTotal());
        data.put("page",userPageInfo.getPages());
        return responseBean.buildOk(data);
    }

    /**
     * 根据id查询用户
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public String selectUser(Integer userId){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .select("id", "username", "nickname","department_id", "post_id", "role_id","phone", "email", "state", "remark")
                .eq("id",userId);
        User user = userMapper.selectOne(userQueryWrapper);
        System.out.println(user);
        if (user == null){
            return responseBean.buildNoData();
        }else {
            HashMap<String, Object> data = new HashMap<>(1);
            data.put("user",user);
            return responseBean.buildOk(data);
        }
    }

    /**
     * 用于创建/修改部门时选择负责人
     */
    @Override
    public String selectUserByNickname() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("id", "nickname");
        List<User> users = userMapper.selectList(userQueryWrapper);
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("userList", JSON.toJSONString(users));
        return responseBean.buildOk(data);
    }
    /**
     * 删除用户
     * @param token 权限验证
     * @param id 用户id
     * @return yes/no
     */
    @Override
    public String deleteUser(String token, Integer id) {
        Claims claims = JwtUtil.getClaims(token);
        Integer userId = CastUtil.toInteger(claims.get("jti"));
        String username = claims.get("sub").toString();

        // 删除的用户不可是自身和超管
        if (id.equals(userId) || id == 1){

            logService.createLog("删除用户", username,"违规操作");
            return responseBean.buildError("违规操作");
        }
        int delete = userMapper.deleteById(id);
        if (delete == 1){
            informMapper.update(null, new UpdateWrapper<Inform>().eq("user_id", id).set("user_id", 1));
            logService.createLog("删除用户", username,"删除成功");
            return responseBean.buildOkMsg("删除用户成功");
        }else {
            logService.createLog("删除用户", username,"删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    // 批量删除用户
    @Override
    public String deleteUsers(String token, String ids) {
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(Integer.valueOf(s));
        }

        Claims claims = JwtUtil.getClaims(token);
        Integer userId = CastUtil.toInteger(claims.get("jti"));
        String username = claims.get("sub").toString();

        // 批量删除的用户中不可包含自身和超管
        if (integers.contains(userId) || integers.contains(1)){

            logService.createLog("批量删除用户", username,"违规操作");
            return responseBean.buildError("违规操作");
        }

        int deletes = userMapper.deleteBatchIds(integers);
        if (deletes == 0){
            logService.createLog("删除用户", username,"批量删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }else {
            informMapper.update(null, new UpdateWrapper<Inform>().in("user_id", integers).set("user_id", 1));
            logService.createLog("删除用户", username,"批量删除成功");
            return responseBean.buildOkMsg("批量删除用户成功");
        }
    }

    // 新建/修改用户
    @Override
    public String saveUser(String token, User user) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Claims claims = JwtUtil.getClaims(token);
        String username = claims.get("sub").toString();

        user.setUpdateDate(timestamp);
        if (user.getId().equals(0)){
            user.setPassword("c4b671607e99c43686160e3f31a44e033a1211a818f17534");
            user.setId(null);
            user.setCreateDate(timestamp);
            user.setAvatar("/touxiang.gif");
            return insertUser(username, user);
        }else {
            Integer userId = CastUtil.toInteger(claims.get("jti"));
            if (user.getId().equals(userId) || user.getId() == 1){

                logService.createLog("修改用户信息", username,"违规操作");
                return responseBean.buildError("违规操作");
            }
            return updateUser(username, user);
        }
    }
    // 修改用户状态
    @Override
    public String updateUserState(String token, Integer id) {
        Claims claims = JwtUtil.getClaims(token);
        Integer userId = CastUtil.toInteger(claims.get("jti"));
        String username = claims.get("sub").toString();

        if (id.equals(userId) || id == 1){
            logService.createLog("修改用户状态", username,"违规操作");
            return responseBean.buildError("违规操作");
        }

        User user = userMapper.selectById(id);
        Integer state = user.getState()==1?0:1;
        int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("state", state).last("limit 1"));
        if (update ==1){
            logService.createLog("修改用户状态", username,"修改用户状态:"+ user.getUsername() + ",修改成功");
            return responseBean.buildOkMsg("修改状态成功");
        }else {
            logService.createLog("修改用户状态", username,"修改用户状态:"+ user.getUsername() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    // 修改用户密码（非自己的）
    @Override
    public String updatePassword(String token, Integer id, String rootPassword, String newPassword) {
        Claims claims = JwtUtil.getClaims(token);

        Integer userId = CastUtil.toInteger(claims.get("jti"));
        String username = claims.get("sub").toString();
        if (id.equals(userId) || id == 1){
            logService.createLog("修改用户密码", username,"违规操作");
            return responseBean.buildError("违规操作");
        }
        User user = userMapper.selectById(userId);
        if (MD5Util.getSaltverifyMD5(rootPassword, user.getPassword())) {
            int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("password", MD5Util.getSaltMD5(newPassword)));
            if (update ==1){
                logService.createLog("修改用户密码", username,"修改成功");
                return responseBean.buildOkMsg("修改用户密码成功");
            }else {
                logService.createLog("修改用户密码", username,"修改失败,数据库可能存在异常");
                return responseBean.buildNoDataMsg("数据异常");
            }
        }else {
            logService.createLog("修改用户密码", username,"管理用户自身密码填写错误");
            return responseBean.buildNoDataMsg("您的密码错误");
        }
    }

    /**
     * 注册一个用户
     * @param user 新增用户
     */
    private String insertUser(String username, User user) {
        int insert = userMapper.insert(user);
        if (insert == 1){
            logService.createLog("新建用户", username,"新建用户:"+ user.getUsername() + ",新建成功");
            return responseBean.buildOkMsg("新增用户成功");
        }else {
            logService.createLog("新建用户", username,"新建用户:"+ user.getUsername() + ",新建失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 修改用户信息
     * @param user 更新用户
     */
    private String updateUser(String username, User user) {
        User oldUser = userMapper.selectById(user.getId());
        user.setPassword(oldUser.getPassword());
        int i = userMapper.updateById(user);
        if (i == 1){
            logService.createLog("修改用户", username,"修改用户:"+ user.getUsername() + ",修改成功");
            return responseBean.buildOkMsg("修改用户成功");
        } else {
            logService.createLog("修改用户", username,"修改用户:"+ user.getUsername() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

}
