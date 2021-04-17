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
    /**
     * 用户列表查询
     * @param id                id
     * @param departmentIds     部门id列表
     * @param username          用户名
     * @param phone             手机
     * @param state             状态
     * @param createDateStart   开始
     * @param createDateEnd     结束
     * @return  用户列表
     */
    List<User> getUserList(@Param("id") Integer id,
                       @Param("departmentIds") List<Integer> departmentIds,
                       @Param("username") String username,
                       @Param("phone") Integer phone,
                       @Param("state") Integer state,
                       @Param("createDateStart") String createDateStart,
                       @Param("createDateEnd") String createDateEnd);
}
