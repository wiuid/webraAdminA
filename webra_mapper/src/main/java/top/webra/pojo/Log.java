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
 * @Description: 日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wra_log")
public class Log implements Serializable {
    /**唯一标识*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**日志标题*/
    @TableField("title")
    private String title;
    /**账户*/
    @TableField("username")
    private String username;
    /**日志内容*/
    @TableField("text")
    private String text;
    /**创建时间*/
    @TableField("create_date")
    private Timestamp createDate;
}
