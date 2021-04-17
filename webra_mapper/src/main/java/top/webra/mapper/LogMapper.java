package top.webra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import top.webra.pojo.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 21:26
 * @Description: --
 */
@Mapper
@Component
public interface LogMapper extends BaseMapper<Log> {
    /**
     * 日志查询
     * @param username          日志标题
     * @param createDateStart   创建时间开始
     * @param createDateEnd     创建时间结束
     * @return              日志列表
     */
    List<Log> getLogList(@Param("title") String username,
                           @Param("createDateStart") String createDateStart,
                           @Param("createDateEnd") String createDateEnd);

    /**
     * 清空日志
     * @return  yes/no
     */
    @Update("truncate table wra_log")
    Integer emptyLog();
}
