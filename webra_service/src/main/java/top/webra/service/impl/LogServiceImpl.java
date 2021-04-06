package top.webra.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.webra.bean.ResponseBean;
import top.webra.mapper.LogMapper;
import top.webra.pojo.Log;
import top.webra.service.LogService;
import top.webra.util.CastUtil;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:08
 * @Description: 日志逻辑业务类
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private ResponseBean responseBean;
    @Autowired
    private LogMapper logMapper;
    @Override
    public ResponseBean getLogList(String title, String createDateStart, String createDateEnd, Integer page) {
        // 时间参数处理
        if (!"".equals(createDateStart) && createDateStart != null){
            createDateStart = CastUtil.toDateFormat(createDateStart);
            createDateEnd = CastUtil.toDateFormat(createDateEnd);
        }

        // 查询数据
        PageHelper.startPage(page,10);
        List<Log> logList = logMapper.getLogList(title, createDateStart, createDateEnd);
        PageInfo<Log> logPageInfo = new PageInfo<>(logList);

        // 整理数据并返回
        HashMap<String, Object> data = new HashMap<>();
        data.put("logList", logList);
        data.put("total",logPageInfo.getTotal());
        data.put("page",logPageInfo.getPages());
        responseBean.buildOk(data);

        return responseBean;
    }


    public void createLog(String title, String username, String msg){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Log log = new Log(null, title, username, msg, timestamp);
        logMapper.insert(log);
    }



}
