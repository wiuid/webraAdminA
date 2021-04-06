package top.webra.mapper;

import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import top.webra.pojo.Inform;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 21:26
 * @Description: 公告mapper
 */
@Mapper
@Component
public interface InformMapper extends BaseMapper<Inform> {
    /**
     * 公告列表，检索
     * @param title
     * @param state
     * @param createDateStart
     * @param createDateEnd
     * @return
     */
    List<Inform> getInformsOrderAsc(@Param("title") String title,
                                    @Param("state") Integer state,
                                    @Param("createDateStart") String createDateStart,
                                    @Param("createDateEnd") String createDateEnd);

    /**
     * 通过id获取指定公告
     * @param id
     * @return
     */
    Inform getInform(Integer id);

}
