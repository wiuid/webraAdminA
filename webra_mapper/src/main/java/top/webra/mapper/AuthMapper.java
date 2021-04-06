package top.webra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import top.webra.pojo.Auth;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 21:25
 * @Description: --
 */
@Mapper
@Component
public interface AuthMapper extends BaseMapper<Auth> {
}
