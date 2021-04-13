package top.webra.service;

import top.webra.bean.ResponseBean;
import top.webra.pojo.Department;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:07
 * @Description: --
 */
public interface DepartmentService {
    /**
     * 全部
     * @return 全部列表
     */
    String getDepartmentList(String token);

    /**
     * 仅返回正常状态的部门，用于创建部门时的选择上级及用户选择部门
     */
    String getDepartmentIdTitle();

    /**
     * 根据id 获得一个部门的详细数据
     * @param departmentId  部门id
     */
    String getDepartment(Integer departmentId);

    /**
     * 同时处理新建及修改部门数据的请求
     * @param department    部门对象
     */
    String saveDepartment(String token, Department department);

    /**
     * 根据id 修改状态
     * @param id    部门id
     */
    String updateDepartmentState(String token, Integer id);

    /**
     * 根据id 删除部门
     * @param id 部门id
     */
    String deleteDepartment(String token, Integer id);
}
