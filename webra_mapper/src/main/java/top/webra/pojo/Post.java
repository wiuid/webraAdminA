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
 * @Create: 2021-03-04 19:01
 * @Description: 岗位实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wra_post")
public class Post implements Serializable {
    /**唯一标识*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**岗位名*/
    @TableField("title")
    private String title;
    /**顺序，默认0，值小靠前*/
    @TableField("serial")
    private Integer serial;
    /**状态，默认0,0开启，1关闭*/
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
}
