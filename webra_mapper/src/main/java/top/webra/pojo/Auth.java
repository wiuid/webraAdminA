package top.webra.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 18:59
 * @Description: 权限实体类，该类不需要修改
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wra_auth")
public class Auth implements Serializable {
    /**唯一标识*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**权限名*/
    @TableField("title")
    private String title;
    /**权限标识*/
    @TableField("name")
    private String name;
    /**上级权限id*/
    @TableField("super_id")
    private Integer superId;
    /**路径地址*/
    @TableField("path")
    private String path;
    /**路径地址*/
    @TableField("icon")
    private String icon;
    /**前端页面地址*/
    @TableField("component")
    private String component;
    /**是否存在下级*/
    @TableField("whether")
    private Integer whether;

    @TableField(exist = false)
    private List<Auth> children;
}
