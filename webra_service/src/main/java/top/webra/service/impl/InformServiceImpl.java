package top.webra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.webra.bean.ResponseBean;
import top.webra.mapper.InformMapper;
import top.webra.pojo.Inform;
import top.webra.service.InformService;
import top.webra.util.CastUtil;
import top.webra.util.JwtUtil;
import top.webra.utils.RedisUtil;

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
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 第一版，默认十条数据，之后更新添加自定义
     * @param title     标题检索
     * @param state     状态检索
     * @param page      页码检索
     * @param pageSize   每页的数量
     * @param createDateStart   初始创建日期
     * @param createDateEnd     最后创建日期
     */
    @Override
    public String selectInformList(String title, Integer state, Integer page, @Nullable Integer pageSize, String createDateStart, String createDateEnd){
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

        HashMap<String, Object> data = new HashMap<>(3);
        data.put("informList", informsOrderAsc);
        data.put("total",informsOrderAscInfo.getTotal());
        data.put("page",informsOrderAscInfo.getPages());
        return responseBean.buildOk(data);
    }

    /**
     * 根据id获取内容
     * @param informId 公告id
     */
    @Override
    public String selectInform(Integer informId) {

        Inform inform = informMapper.selectOne(new QueryWrapper<Inform>().select("id", "title", "text", "state").eq("id", informId));
        return informJudge(inform);


    }

    /**
     * 首页查询公告
     * @param informId 公告id
     * @return  公告信息
     */
    @Override
    public String getInform(Integer informId) {

        String key = "newInform";
        if (redisUtil.get(key).toString().equals(informId.toString())) {
            return redisUtil.get("inform").toString();
        }else {
            Inform inform = informMapper.getInform(informId);
            return informJudge(inform);
        }
    }

    private String informJudge(Inform inform){
        if (inform == null){
            return responseBean.buildNoData();
        }else {
            HashMap<String, Object> data = new HashMap<>(1);
            data.put("inform", inform);
            return responseBean.buildOk(data);
        }
    }

    /**
     * 新建及修改公告处理
     * @param inform    公告id
     * @param token     token
     */
    @Override
    public String saveInform(Inform inform, String token) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // 首先根据公告id决定是修改还是添加数据，null代表新建/其他代表修改
        inform.setUpdateDate(timestamp);

        Claims claims = JwtUtil.getClaims(token);
        String username = claims.get("sub").toString();

        if (inform.getId() == 0){
            return insertInform(inform, claims, timestamp, username);
        } else {
            return updateInform(inform, username);
        }
    }

    /**
     * 将新插入/修改的 最新公告添加到redis
     * @param informId 公告id
     */
    private void newInformToRedis(Integer informId){
        // 将新的公告加入到redis中
        Inform inform = informMapper.getInform(informId);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("inform", inform);
        redisUtil.set("inform", responseBean.buildOk(map));
    }

    /**
     * 新建公告
     * @param inform    公告对象
     * @param claims    token对象
     * @param timestamp 创建时间 时间戳
     * @param username  日志记录 操作用户
     * @return  yes/no
     */
    private String insertInform(Inform inform, Claims claims, Timestamp timestamp, String username){
        // 新增公告
        // 从token中获取用户id赋值给inform 公告
        inform.setUserId(CastUtil.toInteger(claims.get("jti")));
        // 插入数据库
        inform.setCreateDate(timestamp);
        int insert = informMapper.insert(inform);
        // 返回信息
        if (insert == 1){
            redisUtil.set("newInform", inform.getId().toString());
            newInformToRedis(inform.getId());
            logService.createLog("新建公告", username,"新建公告:"+ inform.getTitle() + ",新建成功");
            return responseBean.buildOkMsg("新增公告成功");
        }else {
            logService.createLog("新建公告", username,"新建公告:"+ inform.getTitle() + ",新建失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 修改公告
     * @param inform    公告对象
     * @param username  日志记录    操作用户
     * @return  yes/no
     */
    private String updateInform(Inform inform, String username){
        // 修改某公告
        // 修改数据
        int update = informMapper.updateById(inform);
        // 返回信息
        if (update == 1){
            String key = "newInform";
            if (redisUtil.get(key).toString().equals(inform.getId().toString())){
                newInformToRedis(inform.getId());
            }
            logService.createLog("修改公告", username, "修改公告:"+ inform.getTitle() + ",修改成功");
            return responseBean.buildOkMsg("修改公告成功");
        }else {
            logService.createLog("修改公告", username,"修改公告:"+ inform.getTitle() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 删除单个公告
     * @param id 公告id
     */
    @Override
    public String deleteInform(String token, Integer id) {
        int delete = informMapper.deleteById(id);
        String username = JwtUtil.getUsername(token);
        if (delete == 1){
            String key = "newInform";
            if (redisUtil.get(key).toString().equals(id.toString())){
                redisUtil.set(key, "0");
            }
            logService.createLog("删除公告", username,"删除成功");
            return responseBean.buildOkMsg("删除公告成功");
        }else {
            logService.createLog("删除公告", username,"删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 批量删除公告
     * @param ids 公告ids列表字符串
     */
    @Override
    public String deleteInforms(String token, String ids) {
        // 将字符串的id列表转换为Integer List
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(CastUtil.toInteger(s));
        }
        String username = JwtUtil.getUsername(token);
        int i = informMapper.deleteBatchIds(integers);
        if (i != 0){
            redisUtil.set("newInform", "0");
            logService.createLog("删除公告", username,"批量删除成功");
            return responseBean.buildOkMsg("批量删除公告成功");
        }else{
            logService.createLog("删除公告", username,"批量删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }


}
