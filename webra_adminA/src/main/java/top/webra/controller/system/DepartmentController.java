package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.pojo.Department;
import top.webra.service.impl.DepartmentServiceImpl;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:29
 * @Description: 部门管理接口
 */
@Controller
@ResponseBody
@RequestMapping("/system/department")
public class DepartmentController {
    @Autowired
    private DepartmentServiceImpl departmentService;

    /**
     * @return 部门列表
     */
    @PreAuthorize("hasRole('ROLE_department')")
    @GetMapping
    public String getTableDate(@RequestHeader("token") String token){
        return departmentService.getDepartmentList(token);
    }

    /**
     * 根据id获取对应部门详细信息
     * @param id 部门id
     */
    @PreAuthorize("hasRole('ROLE_department')")
    @GetMapping("/get")
    public String getDepartment(Integer id){
        return departmentService.getDepartment(id);
    }

    /**
     * 获取部门树
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @GetMapping("/tree")
    public String getDepartmentIdTitle(){
        return departmentService.getDepartmentIdTitle();
    }


    /**
     * 所需参数：部门对象
     * 该接口同时处理新建、修改部门，区分点是用户id是否为0
     */
    @PreAuthorize("hasRole('ROLE_department')")
    @PostMapping("/save")
    public String saveUser(@RequestHeader("token")String token, Department department){
        return departmentService.saveDepartment(token, department);
    }

    /**
     * 修改部门状态
     */
    @PreAuthorize("hasRole('ROLE_department')")
    @PostMapping("/state")
    public String updateState(@RequestHeader("token")String token, Integer id){
        return departmentService.updateDepartmentState(token, id);
    }

    /**
     * 删除部门及下级部门
     * @param id 部门id
     */
    @PreAuthorize("hasRole('ROLE_department')")
    @DeleteMapping("/delete")
    public String deleteDepartment(@RequestHeader("token")String token, Integer id){
        return departmentService.deleteDepartment(token, id);
    }
}
