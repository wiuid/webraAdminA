package top.webra.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.mapper.InformMapper;
import top.webra.pojo.Inform;
import top.webra.service.InformService;
import top.webra.util.CastUtil;
import top.webra.util.JwtUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:08
 * @Description: 公告逻辑业务类
 */
@Slf4j
@Service
public class InformServiceImpl implements InformService {

    @Autowired
    private InformMapper informMapper;
    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private LogServiceImpl logService;

    /**
     * 第一版，默认十条数据，之后更新添加自定义
     * @param title     标题检索
     * @param state     状态检索
     * @param page      页码检索
     * @param pageSize   每页的数量
     * @param createDateStart   初始创建日期
     * @param createDateEnd     最后创建日期
     */
    public ResponseBean selectInformList(String title, Integer state, Integer page, @Nullable Integer pageSize, String createDateStart, String createDateEnd){
        // 将时间从String转为Timestamp 如果String转换出错，就返回Null

        if (!"".equals(createDateStart) && createDateStart != null){
            createDateStart = CastUtil.toDateFormat(createDateStart);
            createDateEnd = CastUtil.toDateFormat(createDateEnd);
        }else {
            createDateStart = null;
        }
        pageSize = pageSize == null ? 10 : pageSize;
        page = page == null ? 1 : page;
        PageHelper.startPage(page,pageSize);
        List<Inform> informsOrderAsc = informMapper.getInformsOrderAsc(title,state,createDateStart,createDateEnd);
        PageInfo<Inform> informsOrderAscInfo = new PageInfo<>(informsOrderAsc);

        HashMap<String, Object> data = new HashMap<>();
        data.put("informList", informsOrderAsc);
        data.put("total",informsOrderAscInfo.getTotal());
        data.put("page",informsOrderAscInfo.getPages());
        responseBean.buildOk(data);

        return responseBean;
    }

    /**
     * 根据id获取内容
     * @param informId 公告id
     */
    public ResponseBean selectInform(Integer informId) {
        Inform inform = informMapper.getInform(informId);

        if (inform == null){
            responseBean.buildNoData();
        }else {
            HashMap<String, Object> data = new HashMap<>();
            data.put("inform", inform);
            responseBean.buildOk(data);
        }
        return responseBean;
    }

    /**
     * 新建及修改公告处理
     * @param inform    公告id
     * @param token     token
     */
    public ResponseBean saveInform(Inform inform, String token) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // 首先根据公告id决定是修改还是添加数据，null代表新建/其他代表修改
        inform.setUpdateDate(timestamp);

        Claims claims = JwtUtil.getClaims(token);
        String username = claims.get("sub").toString();

        if (inform.getId() == 0){
            // 新增公告
            // 从token中获取用户id赋值给inform 公告
            inform.setUserId(CastUtil.toInteger(claims.get("jti")));
            // 插入数据库
            inform.setCreateDate(timestamp);
            int insert = informMapper.insert(inform);
            // 返回信息
            if (insert == 1){
                logService.createLog("新建公告", username,"新建公告:"+ inform.getTitle() + ",新建成功");
                responseBean.buildOkMsg("新增公告成功");
            }else {
                logService.createLog("新建公告", username,"新建公告:"+ inform.getTitle() + ",新建失败,数据库可能存在异常");
                responseBean.buildNoDataMsg("数据异常");
            }
        } else {
            // 修改某公告
            // 修改数据
            int update = informMapper.updateById(inform);
            // 返回信息
            if (update == 1){
                logService.createLog("修改公告", username, "修改公告:"+ inform.getTitle() + ",修改成功");
                responseBean.buildOkMsg("修改公告成功");
            }else {
                logService.createLog("修改公告", username,"修改公告:"+ inform.getTitle() + ",修改失败,数据库可能存在异常");
                responseBean.buildNoDataMsg("数据异常");
            }
        }
        return responseBean;
    }

    /**
     * 删除单个公告
     * @param id 公告id
     */
    public ResponseBean deleteInform(String token, Integer id) {
        int delete = informMapper.deleteById(id);
        String username = JwtUtil.getUsername(token);
        if (delete == 1){
            logService.createLog("删除公告", username,"删除成功");
            responseBean.buildOkMsg("删除公告成功");
        }else {
            logService.createLog("删除公告", username,"删除失败,数据库可能存在异常");
            responseBean.buildNoDataMsg("数据异常");
        }
        return responseBean;
    }

    /**
     * 批量删除公告
     * @param ids 公告ids列表字符串
     */
    public ResponseBean deleteInforms(String token, String ids) {
        // 将字符串的id列表转换为Integer List
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(CastUtil.toInteger(s));
        }
//        QueryWrapper<Inform> informQueryWrapper = new QueryWrapper<Inform>();
//        informQueryWrapper.in("id", integers);
        String username = JwtUtil.getUsername(token);
        int i = informMapper.deleteBatchIds(integers);
        if (i != 0){
            logService.createLog("删除公告", username,"批量删除成功");
            responseBean.buildOkMsg("批量删除公告成功");
        }else{
            logService.createLog("删除公告", username,"批量删除失败,数据库可能存在异常");
            responseBean.buildNoDataMsg("数据异常");
        }
        return responseBean;
    }


}