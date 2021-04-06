package top.webra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.webra.pojo.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 21:27
 * @Description: --
 */
@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {

    List<User> getUserList(@Param("departmentIds") List<Integer> departmentIds,
                       @Param("username") String username,
                       @Param("phone") Integer phone,
                       @Param("state") Integer state,
                       @Param("createDateStart") String createDateStart,
                       @Param("createDateEnd") String createDateEnd);
}
