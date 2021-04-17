package top.webra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.webra.pojo.Post;

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
public interface PostMapper  extends BaseMapper<Post> {
    /**
     * 岗位列表
     * @param title     岗位名称
     * @param state     岗位状态
     * @return          岗位列表
     */
    List<Post> getPostList(@Param("title") String title,
                           @Param("state") Integer state);

}
