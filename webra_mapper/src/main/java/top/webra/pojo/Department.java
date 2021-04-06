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
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 19:00
 * @Description: 部门实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wra_department")
public class Department implements Serializable {
    /**唯一标识*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**部门名*/
    @TableField("title")
    private String title;
    /**上级部门id*/
    @TableField("super_id")
    private Integer superId;
    /**状态，默认0,0开启，1关闭*/
    @TableField("state")
    private Integer state;
    /**创建时间，修改时间*/
    @TableField("create_date")
    private Timestamp createDate;
    @TableField("update_date")
    private Timestamp updateDate;

    /**负责人/昵称*/
    @TableField("user_id")
    private Integer userId;

    /**是否存在下级*/
    @TableField("whether")
    private Integer whether;

    @TableField(exist = false)
    private List<Department> children;

}
