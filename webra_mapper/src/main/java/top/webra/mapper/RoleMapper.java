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
    /**
     * 查询角色
     * @param title             角色名
     * @param code              角色代码
     * @param state             角色状态
     * @param createDateStart   开始
     * @param createDateEnd     结束
     * @return  角色列表
     */
    List<Role> getRoleList(@Param("title") String title,
                           @Param("code") String code,
                           @Param("state") Integer state,
                           @Param("createDateStart") String createDateStart,
                           @Param("createDateEnd") String createDateEnd);
}
