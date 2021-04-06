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
 * @Create: 2021-03-04 19:02
 * @Description: 更新日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wra_update_log")
public class UpdateLog implements Serializable {
    /**唯一标识*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**版本号*/
    @TableField("version")
    private String version;
    /**更新内容*/
    @TableField("text")
    private String text;
    /**创建时间*/
    @TableField("create_date")
    private Timestamp createDate;
}
