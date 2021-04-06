package top.webra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.mapper.DepartmentMapper;
import top.webra.mapper.RoleMapper;
import top.webra.mapper.UserMapper;
import top.webra.pojo.Department;
import top.webra.pojo.Role;
import top.webra.pojo.User;
import top.webra.service.InfoService;
import top.webra.util.CastUtil;
import top.webra.util.JwtUtil;
import top.webra.util.MD5Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/3/22 12:07
 * @Description: --
 */
@Service
public class InfoServiceImpl implements InfoService {

    @Value("${file-path.win}")
    private String winPath;
    @Value("${file-path.lin}")
    private String linPath;

    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private RoleMapper roleMapper;

    public ResponseBean getAvatar(String token) {
        JwtUtil jwtUtil = new JwtUtil();
        Claims claims = jwtUtil.parseJWT(token);
        User user = userMapper.selectOne(new QueryWrapper<User>().select("avatar").eq("id", CastUtil.toInteger(claims.get("jti"))));
        HashMap<String, Object> data = new HashMap<>();
        data.put("avatar", user.getAvatar());
        responseBean.buildOk(data);
        return responseBean;
    }

    /**
     * 获取个人信息页 的 数据
     * @param token 利用token 解析用户信息
     */
    public ResponseBean getInfo(String token) {
        // 存储数据
        HashMap<String, Object> data = new HashMap<>();
        // 解析token
        Integer userId = JwtUtil.getUserId(token);
        // 获得用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().select("id", "username", "nickname", "avatar", "phone", "email", "create_date","department_id","role_id").eq("id", userId));
        data.put("user", user);
        if (user.getDepartmentId() != null){
            // 获取部门名
            Department department = departmentMapper.selectOne(new QueryWrapper<Department>().select("title").eq("id", user.getDepartmentId()));
            data.put("departmentTitle", department.getTitle());
        }else {
            data.put("departmentTitle",null);
        }
        // 获取角色名
        Role role = roleMapper.selectOne(new QueryWrapper<Role>().select("title").eq("id", user.getRoleId()));
        data.put("roleTitle", role.getTitle());
        responseBean.buildOk(data);
        return responseBean;
    }

    /**
     * 修改个人信息
     * @param token     验证
     * @param id        用户id
     * @param nickname  昵称
     * @param phone     手机号
     * @param email     邮箱
     */
    public ResponseBean setInfo(String token, Integer id, String nickname, String phone, String email) {
        if (judge(token, id)){
            int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("nickname", nickname).set("phone", phone).set("email", email).last("limit 1"));
            if (update == 1){
                responseBean.buildOkMsg("修改信息成功");
            }else {
                responseBean.buildNoDataMsg("数据异常");
            }
        }else {
            responseBean.buildUserErr();
        }
        return responseBean;
    }

    /**
     * 修改个人密码
     * @param token             验证
     * @param id                用户id
     * @param oldPassword       旧密码
     * @param newPassword       新密码
     */
    public ResponseBean setPassword(String token, Integer id, String oldPassword, String newPassword) {
        if (judge(token, id)){
            User user = userMapper.selectOne(new QueryWrapper<User>().select("password").eq("id", id));
            // 原密码对比
            if (MD5Util.getSaltverifyMD5(oldPassword,user.getPassword())){
                // 新密码加密
                newPassword = MD5Util.getSaltMD5(newPassword);
                int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("password", newPassword).last("limit 1"));
                if (update == 1){
                    responseBean.buildOkMsg("修改密码成功");
                }else {
                    responseBean.buildNoDataMsg("数据异常");
                }
            }else {
                responseBean.buildNoDataMsg("原密码错误");
            }
        }else {
            responseBean.buildUserErr();
        }
        return responseBean;
    }

    // 上传头像
    public ResponseBean updateAvatar(String token, Integer id, String base64) {
        if (judge(token, id)){
            try {
                Base64.Decoder decoder = Base64.getDecoder();
                // 解码
                byte[] b = decoder.decode(base64);
                for (int i = 0; i < b.length; i++) {
                    if (b[i] < 0) {// 调整异常数据
                        b[i] += 256;
                    }
                }
                User user = userMapper.selectOne(new QueryWrapper<User>().select("avatar").eq("id", id));
                String imgName;
                // 这样默认头像将存在，自定义头像被覆盖（不太可能同一个时间点两个人新人账号同时更换头像- -造成相互覆盖吧，那样一旦有一个人再更新头像，另一个人头像直接也就改了）
                String avatar = user.getAvatar();
                if (avatar.equals("/assets/images/touxiang.gif")){
                    imgName = System.currentTimeMillis()+".jpg";
                }else {
                    imgName = avatar.replaceFirst("/image/","");
                }

                String os = System.getProperty("os.name");
                String path = os.toLowerCase().startsWith("win")?winPath:linPath;

                String imgUrl = "/image/"+imgName;
                String filepath =path +imgName;
                File file  = new File(filepath);
                if(file.exists()){
                    file.delete();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(b);
                fos.flush();
                fos.close();
                int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("avatar", "/api" + imgUrl));
                if (update == 1){
                    responseBean.buildOkMsg("修改头像成功，刷新即可");
                }else {
                    responseBean.buildNoDataMsg("数据异常");
                }
            } catch (IOException e) {
                responseBean.buildNoDataMsg("图片处理异常，请重新提交");
            }
        }else {
            responseBean.buildUserErr();
        }
        return responseBean;
    }

    private Boolean judge(String token, Integer id){
        JwtUtil jwtUtil = new JwtUtil();
        Claims claims = jwtUtil.parseJWT(token);
        return CastUtil.toInteger(claims.get("jti")).equals(id);
    }
}