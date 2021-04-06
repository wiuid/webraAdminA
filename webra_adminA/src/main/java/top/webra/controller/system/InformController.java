package top.webra.controller.system;

import org.springframework.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.bean.ResponseBean;
import top.webra.pojo.Inform;
import top.webra.service.impl.InformServiceImpl;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:29
 * @Description: 公告管理接口
 */
@Slf4j
@Controller
@ResponseBody
@RequestMapping("/system/site/inform")
public class InformController {

    @Autowired
    private ResponseBean responseBean;

    @Autowired
    private InformServiceImpl informService;

    /**
     * 所需参数：公告标题、时间、程度、页码
     * @return 公告列表
     */
    @GetMapping
    public ResponseBean getTableDate(String title, Integer state, Integer page, @Nullable Integer pageSize, String createDateStart, String createDateEnd){
        return informService.selectInformList(title,state,page,pageSize,createDateStart,createDateEnd);
    }

    /**
     * 根据id 获取详细信息
     * @param id 公告id
     */
    @GetMapping("/get")
    public ResponseBean getInform(Integer id){
        log.info("接收的公告id为：" + id);
        if (id == null){
            responseBean.setStatus(204);
            responseBean.setData(null);
            responseBean.setMsg("提供参数为空");
        }else {
            responseBean = informService.selectInform(id);
        }
        return responseBean;
    }

    /**
     * 所需参数：公告对象
     * 同时处理新增、修改公告对象，区别点在公告id
     */
    @PreAuthorize("hasRole('ROLE_inform')")
    @PostMapping("/save")
    public ResponseBean saveInform(Inform inform, @RequestHeader("token") String token){
        return informService.saveInform(inform,token);
    }

    /**
     * 删除单个公告
     * @param id 公告id
     */
    @PreAuthorize("hasRole('ROLE_inform')")
    @DeleteMapping("/delete")
    public ResponseBean deleteInform(@RequestHeader("token") String token, int id){
        return informService.deleteInform(token, id);
    }

    /**
     * 批量删除公告
     * @param ids  公告id列表字符串
     */
    @PreAuthorize("hasRole('ROLE_inform')")
    @DeleteMapping("/deletes")
    public ResponseBean deleteInforms(@RequestHeader("token") String token,String ids){
        return informService.deleteInforms(token, ids);
    }
}