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
 * @Create: 2021-03-04 19:00
 * @Description: 公告实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wra_inform")
public class Inform implements Serializable {
    /**唯一标识*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**公告标题*/
    @TableField("title")
    private String title;
    /**发布人/用户id*/
    @TableField("user_id")
    private Integer userId;

    @TableField("state")
    private Integer state;
    /**公告内容*/
    @TableField("text")
    private String text;
    /**创建时间，修改时间*/
    @TableField("create_date")
    private Timestamp createDate;
    @TableField("update_date")
    private Timestamp updateDate;

    @TableField(exist = false)
    private String userNickname;
}
