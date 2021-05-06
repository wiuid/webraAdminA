package top.webra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.mapper.RoleMapper;
import top.webra.mapper.UserMapper;
import top.webra.pojo.Role;
import top.webra.pojo.User;
import top.webra.service.RoleService;
import top.webra.util.CastUtil;
import top.webra.util.JwtUtil;
import top.webra.util.ResponseUtil;
import top.webra.utils.RedisUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-17 16:03
 * @Description: --
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private LogServiceImpl logService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 检索角色
     * @param title             角色名
     * @param code              角色字符
     * @param state             角色状态
     * @param createDateStart   创建开始时间
     * @param createDateEnd     创建结束时间
     * @param page              页码
     */
    @Override
    public String getRoleList(String title, String code, Integer state, String createDateStart, String createDateEnd, Integer page) {
        // 判断搜索条件是否存在，存在的话 需要进行格式化 才可以存放数据库
        if (!"".equals(createDateStart) && createDateStart != null){
            createDateStart = CastUtil.toDateFormat(createDateStart);
            createDateEnd = CastUtil.toDateFormat(createDateEnd);
        }else {
            createDateStart = null;
        }
        // 分页查询
        PageHelper.startPage(page,10);
        List<Role> roleList = roleMapper.getRoleList(title, code, state, createDateStart, createDateEnd);
        // 查询获得详细信息
        PageInfo<Role> roleOrderAscInfo = new PageInfo<>(roleList);

        HashMap<String, Object> data = new HashMap<>(3);
        data.put("roleList", roleList);
        data.put("total",roleOrderAscInfo.getTotal());
        data.put("page",roleOrderAscInfo.getPages());
        return responseBean.buildOk(data);
    }

    /**
     * 根据id 获取角色信息
     * @param id    角色id
     */
    @Override
    public String getRole(Integer id) {
        Role role = roleMapper.selectById(id);

        if (role == null){
            return responseBean.buildNoData();
        }else {
            HashMap<String, Object> data = new HashMap<>(1);
            data.put("role", role);
            return responseBean.buildOk(data);
        }
    }

    /**
     * 角色新建/修改处理
     * @param role  角色对象
     */
    @Override
    public String saveRole(String token, Role role) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        role.setUpdateDate(timestamp);
        String username = JwtUtil.getUsername(token);
        if (role.getId().equals(0)){
            role.setCreateDate(timestamp);
            return insertRole(username, role);
        }else {
            return updateRole(username, role);
        }
    }

    /**
     * 新建角色
     * @param role 角色对象
     */
    private String insertRole(String username, Role role){
        int insert = roleMapper.insert(role);
        if (insert == 1){
            redisUtil.del("roleSelect");
            logService.createLog("新建角色", username,"新建角色:"+ role.getTitle() + ",新建成功");
            return responseBean.buildOkMsg("新增角色成功");
        }else {
            logService.createLog("新建角色", username,"新建角色:"+ role.getTitle() + ",新建失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 修改角色
     * @param role 角色对象
     */
    private String updateRole(String username, Role role){
        int update = roleMapper.updateById(role);
        if (update == 1){
            redisUtil.del("roleSelect");
            logService.createLog("修改角色", username,"修改角色:"+ role.getTitle() + ",修改成功");
            return responseBean.buildOkMsg("修改角色成功");
        }else {
            logService.createLog("修改角色", username,"修改角色:"+ role.getTitle() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 根据id 修改状态
     * @param id    角色id
     */
    @Override
    public String updateRoleSwitch(String token, Integer id){
        Role role = roleMapper.selectById(id);
        Integer state = role.getState()==1?0:1;
        int update = roleMapper.update(null, new UpdateWrapper<Role>().eq("id", id).set("state", state).last("limit 1"));
        String username = JwtUtil.getUsername(token);
        if (update ==1){
            redisUtil.del("roleSelect");
            logService.createLog("修改角色状态", username,"修改角色状态:"+ role.getTitle() + ",修改成功");
            return responseBean.buildOkMsg("修改状态成功");
        }else {
            logService.createLog("修改角色状态", username,"修改角色状态:"+ role.getTitle() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 删除单个角色
     * @param id    角色id
     */
    @Override
    public String deleteRole(String token, Integer id) {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().select("id").eq("role_id", id));
        // 将拥有该角色的用户修改角色为默认值2 普通用户
        setUserRoleDefault(users);
        int delete = roleMapper.deleteById(id);
        String username = JwtUtil.getUsername(token);
        if (delete == 1){
            redisUtil.del("roleSelect");
            userMapper.update(null, new UpdateWrapper<User>().eq("role_id", id).set("role_id", 2));
            logService.createLog("删除角色", username,"删除成功");
            return responseBean.buildOkMsg("删除角色成功");
        }else {
            logService.createLog("删除角色", username,"删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 批量删除角色
     * @param ids    角色ids列表字符串
     */
    @Override
    public String deleteRoles(String token, String ids) {
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(Integer.valueOf(s));
        }
        // 将拥有该角色的用户修改角色为默认值2 普通用户
        List<User> users = userMapper.selectList(new QueryWrapper<User>().select("id").in("role_id", integers));
        setUserRoleDefault(users);
        int deletes = roleMapper.deleteBatchIds(integers);
        String username = JwtUtil.getUsername(token);
        if (deletes == 0){
            logService.createLog("删除角色", username,"批量删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }else {
            redisUtil.del("roleSelect");
            userMapper.update(null, new UpdateWrapper<User>().in("role_id", integers).set("role_id", 2));
            logService.createLog("删除角色", username,"批量删除成功");
            return responseBean.buildOkMsg("批量删除角色成功");
        }
    }

    /**
     * 将用户的权限id改为普通用户
     * @param users 用户对象列表
     */
    private void setUserRoleDefault(List<User> users){
        if (users.size() != 0){
            ArrayList<Integer> userIds = new ArrayList<>();
            for (User user : users) {
                userIds.add(user.getId());
            }
            userMapper.update(null, new UpdateWrapper<User>().in("id", userIds).set("role_id", 2).last("limit " + users.size()));
        }
    }
    /**
     * 角色列表，用以 选择
     */
    @Override
    public String getRoleTree() {
        String key = "roleSelect";
        if (redisUtil.hasKey(key)){
            return redisUtil.get(key).toString();
        }else {
            List<Role> roles = roleMapper.selectList(new QueryWrapper<Role>().select("id", "title"));
            HashMap<String, Object> data = new HashMap<>(1);
            data.put("roleList", roles);
            String res = responseBean.buildOk(data);
            redisUtil.set(key, res, 3600L);
            return res;
        }
    }

    @Override
    public void exportRoles(HttpServletResponse response) {
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("classpath:static/excel/templateExportRole.xls");
            List<Role> roles = roleMapper.selectList(new QueryWrapper<>());
            // 根据模板创建一个工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(resourceAsStream);
            // 获取该工作簿的第一个工作表
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 设置列宽
            sheet.setColumnWidth(1,4000);
            sheet.setColumnWidth(7,6000);
            sheet.setColumnWidth(6,6000);
            int rowIndex = 1;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 循环往表中添加数据
            for (Role role : roles) {
                HSSFRow row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(role.getId());
                row.createCell(1).setCellValue(role.getTitle());
                row.createCell(2).setCellValue(role.getCode());
                row.createCell(3).setCellValue(role.getSerial());
                row.createCell(4).setCellValue(role.getState());
                row.createCell(5).setCellValue(role.getRemark());
                row.createCell(6).setCellValue(simpleDateFormat.format(role.getCreateDate()));
                row.createCell(7).setCellValue(simpleDateFormat.format(role.getUpdateDate()));
            }
            ResponseUtil.setResponse(response);
            // 流的形式传递数据
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            log.info("导出角色信息异常");
        }
    }
}
