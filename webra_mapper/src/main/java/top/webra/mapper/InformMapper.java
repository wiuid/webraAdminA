package top.webra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.webra.pojo.Inform;

import java.util.List;

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
     * @param title             公告标题
     * @param state             公告状态
     * @param createDateStart   创建时间开始
     * @param createDateEnd     创建时间结束
     * @return  公告列表
     */
    List<Inform> getInformsOrderAsc(@Param("title") String title,
                                    @Param("state") Integer state,
                                    @Param("createDateStart") String createDateStart,
                                    @Param("createDateEnd") String createDateEnd);

    /**
     * 通过id获取指定公告
     * @param id    公告id
     * @return      单条公告信息，用于首页公告查看
     */
    Inform getInform(Integer id);

}
