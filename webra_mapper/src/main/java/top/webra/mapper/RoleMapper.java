package top.webra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.webra.pojo.Role;

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
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoleList(@Param("title") String title,
                           @Param("code") String code,
                           @Param("state") Integer state,
                           @Param("createDateStart") String createDateStart,
                           @Param("createDateEnd") String createDateEnd);
}
