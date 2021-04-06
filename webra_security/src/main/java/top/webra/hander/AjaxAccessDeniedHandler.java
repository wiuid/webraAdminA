package top.webra.hander;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.webra.bean.ResponseBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足 处理器
 * @author webra
 */
@Component
public class AjaxAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ResponseBean responseBean;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e)
            throws IOException {
        responseBean.setStatus(300);

        responseBean.setMsg("Access Error");
        responseBean.setData(null);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseBean));
    }
}
