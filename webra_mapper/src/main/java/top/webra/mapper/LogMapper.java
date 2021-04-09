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

    List<Log> getLogList(@Param("title") String username,
                           @Param("createDateStart") String createDateStart,
                           @Param("createDateEnd") String createDateEnd);

    @Update("truncate table wra_log")
    Integer emptyLog();
}
