package top.webra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.mapper.DepartmentMapper;
import top.webra.mapper.UserMapper;
import top.webra.pojo.Department;
import top.webra.pojo.User;
import top.webra.service.DepartmentService;
import top.webra.util.JwtUtil;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:08
 * @Description: 部门逻辑业务类
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogServiceImpl logService;

    /**
     * 获取全部部门信息
     */
    @Override
    public String getDepartmentList(String token) {
        Claims claims = JwtUtil.getClaims(token);
        String authIds = claims.get("roles").toString();
        if (authIds.contains("15")) {
            // 管理全部部门
            QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<Department>().orderByAsc("super_id");
            return formatDepartments(departmentQueryWrapper);
        }else if (authIds.contains("16")){
            // 仅管理自己是负责人的部门，用户必须是负责人的情况下！
            Integer userId = Integer.valueOf(claims.get("jti").toString());
            Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("user_id", userId).last("limit 1"));
            if (department != null) {
                if (department.getWhether().equals(1)) {
                    getOneChildren(department);
                    HashMap<String, Object> data = new HashMap<>(1);
                    ArrayList<Department> departments = new ArrayList<>();
                    departments.add(department);
                    data.put("departmentList", departments);
                    return responseBean.buildOk(data);
                }else {
                    // 这里返回空数据
                    return responseBean.buildOkNoData();
                }
            }else{
                // 这里返回也是空数据
                return responseBean.buildOkNoData();
            }
        }
        return responseBean.buildOkInitNull("departmentList");
    }

    /**
     * @param department        已经确保该部门对象存在下级！
     */
    private void getOneChildren(Department department){
        List<Department> childrenList = departmentMapper.selectList(new QueryWrapper<Department>().eq("super_id", department.getId()));
        if (childrenList.size() != 0){
            department.setChildren(childrenList);
            for (Department department1 : childrenList) {
                if (department1.getWhether().equals(1)){
                    getOneChildren(department1);
                }
            }
        }
    }

    /**
     * 获取部门信息，主要包含id、title
     */
    @Override
    public String getDepartmentIdTitle() {
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<Department>()
                .select("id", "title", "super_id", "whether")
                .eq("state", 0)
                .orderByAsc("super_id");
        return formatDepartments(departmentQueryWrapper);
    }

    /**
     * 根据id 查询部门数据
     * @param departmentId 部门id
     */
    @Override
    public String getDepartment(Integer departmentId) {
        Department department = departmentMapper.selectById(departmentId);
        Integer superId = department.getSuperId();
        department.setSuperId(superId==0?null:superId);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("department", department);
        return responseBean.buildOk(map);
    }

    /**
     * 新建/修改部门数据
     * @param department 部门对象
     */
    @Override
    public String saveDepartment(String token, Department department) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String username = JwtUtil.getUsername(token);
        if (department.getId() == 0){
            return insertDepartment(username, department, timestamp);
        }else{
            Department oldDepartment = departmentMapper.selectById(department.getId());
            department.setWhether(oldDepartment.getWhether());
            return updateDepartment(username, token, department, timestamp);
        }
    }


    /**
     * 更新部门数据
     * @param department 部门对象
     * @param timestamp 时间戳
     */
    private String updateDepartment(String username, String token, Department department, Timestamp timestamp){
        // 获取旧部门数据
        Department oldDepartment = departmentMapper.selectOne(new QueryWrapper<Department>().select("id", "state", "super_id").eq("id", department.getId()));
        // 修改部门
        department.setUpdateDate(timestamp);
        int update = departmentMapper.updateById(department);

        // 修改自身及下级部门的状态
        if (!oldDepartment.getState().equals(department.getState())) {
            updateDepartmentState(token, department.getId());
        }

        // 更新部门成功
        if (update == 1){
            // 判断旧数据与新数据 是否更换部门，不相等就是更换了
            if (!oldDepartment.getId().equals(department.getSuperId())){
                // 获取父节点是xx 的数据量
                // 以上已经更新新的数据，所以如果原本父节点只有该节点一个子节点的话，现在应该没有子节点了
                Integer oldChildrenCount = departmentMapper.selectCount(new QueryWrapper<Department>().eq("super_id", oldDepartment.getSuperId()));
                if (oldChildrenCount == 0){
                    System.out.println("将旧父节点改为0");
                    // 将父节点的是否拥有子节点的标识whether 更改为0
                    UpdateWrapper<Department> departmentUpdateWrapper = new UpdateWrapper<Department>()
                            .eq("id", oldDepartment.getSuperId())
                            .set("whether", 0)
                            .last("limit 1");
                    departmentMapper.update(null, departmentUpdateWrapper);
                }
                // 判断当前父节点是否有其他节点
                Integer newChildrenCount = departmentMapper.selectCount(new QueryWrapper<Department>().eq("super_id", department.getSuperId()));
                if (newChildrenCount == 1){
                    System.out.println("将新父节点改为1");
                    // 将父节点的是否拥有子节点标识whether 更改为1
                    UpdateWrapper<Department> departmentUpdateWrapper = new UpdateWrapper<Department>()
                            .eq("id", department.getSuperId())
                            .set("whether", 1)
                            .last("limit 1");
                    departmentMapper.update(null, departmentUpdateWrapper);
                }
            }
            logService.createLog("修改部门", username, "修改部门:"+ department.getTitle() + ",修改成功");
            return responseBean.buildOkMsg("修改部门成功");
        }else {
            logService.createLog("修改部门", username,"修改部门:"+ department.getTitle() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("修改部门失败，请检查数据库");
        }
    }

    /**
     * 修改状态
     * @param token     token
     * @param id        部门id
     */
    @Override
    public String updateDepartmentState(String token, Integer id){
        Department department = departmentMapper.selectById(id);
        Integer state = department.getState().equals(1)?0:1;
        Claims claims = JwtUtil.getClaims(token);
        String username = claims.get("sub").toString();
        // 判断该部门是否存在下级
        if (department.getWhether().equals(1)) {
            // 解析token,获取用户id
            Integer userId = Integer.valueOf(claims.get("jti").toString());
            // 将与该部门及下级部门关联的用户部门id指向为null       并且  自身是该部门负责人
            if (state.equals(1) && department.getUserId().equals(userId)){
                logService.createLog("修改部门状态", username,"修改部门状态:"+ department.getTitle() + ",修改失败,自身是该部门负责人,无法禁用所在部门");
                return responseBean.buildNoDataMsg("请勿禁用自身负责部门");
            }else {
                ArrayList<Integer> childrenIds = new ArrayList<>();
                // 获取全部的子部门id
                getChildrenIds(childrenIds,department.getId());
                // 修改状态
                departmentMapper.update(null, new UpdateWrapper<Department>().in("id", childrenIds).set("state", state).last("limit " + childrenIds.size()));
                // 将该部门与下级部门的关联的用户与负责人 清空
                if (state.equals(1)){
                    // 将关联的部门用户重置
                    userMapper.update(null, new UpdateWrapper<User>().in("department_id", childrenIds).set("department_id", null));
                    // 将部门负责人重置
                    departmentMapper.update(null, new UpdateWrapper<Department>().in("id", childrenIds).set("user_id", null).last("limit " + childrenIds.size()));
                }
                return responseBean.buildOkMsg("状态修改成功");
            }
        }else {
            UpdateWrapper<Department> departmentUpdateWrapper = new UpdateWrapper<>();
            departmentUpdateWrapper.eq("id", id)
                    .set("state", state)
                    .last("limit 1");
            departmentMapper.update(null, departmentUpdateWrapper);
            return responseBean.buildOkMsg("状态修改成功");
        }
    }


    /**
     * 新建部门
     * @param department    部门对象
     * @param timestamp     时间戳
     */
    private String insertDepartment(String username, Department department, Timestamp timestamp){
        department.setCreateDate(timestamp);
        department.setUpdateDate(timestamp);
        int insert = departmentMapper.insert(department);
        if (insert == 1){
            // 判断新建的部门是不是一级部门，不是的话，判断上级部门是否
            if (department.getSuperId() != null){
                Department superDepartment = departmentMapper.selectOne(new QueryWrapper<Department>().eq("id", department.getSuperId()));
                if (superDepartment.getWhether() == 0){
                    superDepartment.setWhether(1);
                    departmentMapper.updateById(superDepartment);
                }
            }
            logService.createLog("新建部门", username,"新建部门:"+ department.getTitle() + ",新建成功");
            return responseBean.buildOkMsg("新增部门成功");
        }else {
            logService.createLog("新建部门", username,"新建部门:"+ department.getTitle() + ",新建失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("新增部门失败，请检查数据库");
        }
    }

    /**
     * 根据id 删除部门
     * @param id    部门id
     */
    @Override
    public String deleteDepartment(String token, Integer id) {
        Department department = departmentMapper.selectById(id);
        if (department.getWhether().equals(1)) {
            ArrayList<Integer> childrenIds = new ArrayList<>();
            getChildrenIds(childrenIds,department.getId());
            departmentMapper.delete(new QueryWrapper<Department>().in("id", childrenIds));
        }else {
            departmentMapper.deleteById(id);
        }
        String username = JwtUtil.getUsername(token);
        logService.createLog("删除部门", username,"删除部门:"+ department.getTitle() + ",删除成功");
        return responseBean.buildOkMsg(department.getTitle() + " 部门及下级部门删除成功");
    }

    public void getChildrenIds(List<Integer> ids, Integer superId){
        ids.add(superId);
        List<Department> departments = departmentMapper.selectList(new QueryWrapper<Department>().select("id", "whether").eq("super_id", superId));
        for (Department department : departments) {
            if (department.getWhether().equals(1)) {
                getChildrenIds(ids,department.getId());
            }else {
                ids.add(department.getId());
            }
        }
    }



    /**
     * 根据条件返回多级部门列表
     * @param departmentQueryWrapper    查询条件
     */
    private String formatDepartments(QueryWrapper<Department> departmentQueryWrapper){
        // 查询的部门数据
        List<Department> departments = departmentMapper.selectList(departmentQueryWrapper);
        // 该mao用来承载数据，返回前端
        HashMap<String, Object> map = new HashMap<>(1);
        // 确保查询有数据
        if (departments.size()>0) {
            // 只有一个数据的话
            if (departments.size() == 1){
                map.put("departmentList",departments);
            }else {
                // 大于等于两条的数据，用下面的list存储
                ArrayList<Department> list = new ArrayList<>();
                // 将一级部门先放进来
                for (Department department : departments) {
                    if (department.getSuperId().equals(0)) {
                        list.add(department);
                    }else {
                        // 这里，由于查询条件中有对superId 进行排序，所以一级部门一定会在前面，不是一级部门开始的后面的数据一定也不是一级部门，所以直接break
                        break;
                    }
                }
                map.put("departmentList", JSON.toJSONString(getChild(list, departments)));
            }
            return responseBean.buildOk(map);
        }else {
            return responseBean.buildNoData();
        }
    }

    /**
     * 循环嵌套，将子节点赋予给父节点的children
     * @param list          带有一级部门的列表
     * @param departments   将数据源中的数据循环提出来
     */
    private List<Department> getChild(List<Department> list, List<Department> departments){
        for (Department department : list) {
            if (department.getWhether().equals(1)){
                ArrayList<Department> arrayList = new ArrayList<>();
                for (Department department1 : departments) {
                    if (department1.getSuperId().equals(department.getId())) {
                        arrayList.add(department1);
                    }
                }
                department.setChildren(arrayList);
                List<Department> children = department.getChildren();
                getChild(children,departments);
            }
        }
        return list;
    }
}
















