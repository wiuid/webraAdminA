package top.webra.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 18:58
 * @Description: 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wra_user")
public class User implements Serializable {
    /**唯一标识*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**用户登录账号，唯一*/
    @TableField("username")
    private String username;
    /**昵称*/
    @TableField("nickname")
    private String nickname;
    /**密码*/
    @TableField("password")
    private String password;
    /**头像*/
    @TableField("avatar")
    private String avatar;
    /**部门id*/
    @TableField("department_id")
    private Integer departmentId;
    /**岗位id*/
    @TableField("post_id")
    private Integer postId;
    /**角色id*/
    @TableField("role_id")
    private Integer roleId;
    /**手机号*/
    @TableField("phone")
    private String phone;
    /**邮箱号*/
    @TableField("email")
    private String email;
    /**状态，默认0，0开启，1关闭*/
    @TableField("state")
    private Integer state;
    /**备注*/
    @TableField("remark")
    private String remark;
    /**创建时间，修改时间*/
    @TableField("create_date")
    private Timestamp createDate;
    @TableField("update_date")
    private Timestamp updateDate;

    @TableField(exist = false)
    private String departmentTitle;
}
