package top.webra.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(rollbackFor = Exception.class)
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

    /** 获取头像 */
    @Override
    public String getAvatar(String token) {
        Claims claims = JwtUtil.parseJWT(token);
        User user = userMapper.selectOne(new QueryWrapper<User>().select("avatar").eq("id", CastUtil.toInteger(claims.get("jti"))));
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("avatar", user.getAvatar());
        return responseBean.buildOk(data);
    }

    /**
     * 获取个人信息页 的 数据
     * @param token 利用token 解析用户信息
     */
    @Override
    public String getInfo(String token) {
        // 存储数据
        HashMap<String, Object> data = new HashMap<>(2);
        // 解析token
        Integer userId = JwtUtil.getUserId(token);
        // 获得用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().select("id", "username", "nickname", "avatar", "phone", "email", "create_date","department_id","role_id").eq("id", userId));
        HashMap map = JSONObject.parseObject(JSON.toJSONString(user), HashMap.class);
        map.remove("department_id");
        map.remove("role_id");
        if (user.getDepartmentId() != null){
            // 获取部门名
            Department department = departmentMapper.selectOne(new QueryWrapper<Department>().select("title").eq("id", user.getDepartmentId()));
            map.put("departmentTitle", department.getTitle());
        }
        // 获取角色名
        Role role = roleMapper.selectOne(new QueryWrapper<Role>().select("title").eq("id", user.getRoleId()));
        map.put("roleTitle", role.getTitle());

        HashMap<String, Object> info = new HashMap<>(3);
        info.put("nickname", user.getNickname());
        info.put("phone", user.getPhone());
        info.put("email", user.getEmail());

        data.put("userInfo", map);
        data.put("info", info);
        return responseBean.buildOk(data);
    }

    /**
     * 修改个人信息
     * @param token     验证
     * @param id        用户id
     * @param nickname  昵称
     * @param phone     手机号
     * @param email     邮箱
     */
    @Override
    public String setInfo(String token, Integer id, String nickname, String phone, String email) {
        if (judge(token, id)){
            int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("nickname", nickname).set("phone", phone).set("email", email).last("limit 1"));
            if (update == 1){
                return responseBean.buildOkMsg("修改信息成功");
            }else {
                return responseBean.buildNoDataMsg("数据异常");
            }
        }else {
            return responseBean.buildUserErr();
        }
    }

    /**
     * 修改个人密码
     * @param token             验证
     * @param id                用户id
     * @param oldPassword       旧密码
     * @param newPassword       新密码
     */
    @Override
    public String setPassword(String token, Integer id, String oldPassword, String newPassword) {
        if (judge(token, id)){
            User user = userMapper.selectOne(new QueryWrapper<User>().select("password").eq("id", id));
            // 原密码对比
            if (MD5Util.getSaltverifyMD5(oldPassword,user.getPassword())){
                // 新密码加密
                newPassword = MD5Util.getSaltMD5(newPassword);
                int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("password", newPassword).last("limit 1"));
                if (update == 1){
                    return responseBean.buildOkMsg("修改密码成功");
                }else {
                    return responseBean.buildNoDataMsg("数据异常");
                }
            }else {
                return responseBean.buildNoDataMsg("原密码错误");
            }
        }else {
            return responseBean.buildUserErr();
        }
    }

    /**
     * @param token 安全判定
     * @param id    用户id
     * @param base64    图像信息
     * @return  yes/no
     */
    @Override
    public String updateAvatar(String token, Integer id, String base64) {
        if (judge(token, id)){
            try {
                Base64.Decoder decoder = Base64.getDecoder();
                // 解码
                byte[] b = decoder.decode(base64);
                for (int i = 0; i < b.length; i++) {
                    // 调整异常数据
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }
                User user = userMapper.selectOne(new QueryWrapper<User>().select("avatar").eq("id", id));
                String imgName;
                // 这样默认头像将存在，自定义头像被覆盖（不太可能同一个时间点两个人新人账号同时更换头像- -造成相互覆盖吧，那样一旦有一个人再更新头像，另一个人头像直接也就改了）
                String avatar = user.getAvatar();
                if ("/touxiang.gif".equals(avatar)){
                    imgName = System.currentTimeMillis()+".jpg";
                }else {
                    imgName = avatar.replaceFirst("/api/image/","");
                }

                String os = System.getProperty("os.name");
                String path = os.toLowerCase().startsWith("win")?winPath:linPath;

                String imgUrl = "/api/image/"+imgName;
                String filepath =path +imgName;
                File file  = new File(filepath);
                if(file.exists()){
                    file.delete();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(b);
                fos.flush();
                fos.close();
                int update = userMapper.update(null, new UpdateWrapper<User>().eq("id", id).set("avatar", imgUrl));
                if (update == 1){
                    return responseBean.buildOkMsg("修改头像成功，刷新即可");
                }else {
                    return responseBean.buildNoDataMsg("数据异常");
                }
            } catch (IOException e) {
                return responseBean.buildNoDataMsg("图片处理异常，请重新提交");
            }
        }else {
            return responseBean.buildUserErr();
        }
    }

    private Boolean judge(String token, Integer id){
        Claims claims = JwtUtil.parseJWT(token);
        return CastUtil.toInteger(claims.get("jti")).equals(id);
    }
}
