package top.webra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.webra.bean.ResponseBean;
import top.webra.mapper.LogMapper;
import top.webra.pojo.Log;
import top.webra.service.LogService;
import top.webra.util.CastUtil;
import top.webra.util.JwtUtil;
import top.webra.util.ResponseUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    /**
     * @param title             日志标题
     * @param createDateStart   创建开始时间
     * @param createDateEnd     创建结束时间
     * @param page              页码
     * @return  日志列表
     */
    @Override
    public String getLogList(String title, String createDateStart, String createDateEnd, Integer page) {
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
        HashMap<String, Object> data = new HashMap<>(3);
        data.put("logList", logList);
        data.put("total",logPageInfo.getTotal());
        data.put("page",logPageInfo.getPages());
        return responseBean.buildOk(data);
    }

    /**
     * @param id    日志id
     * @return  yes/no
     */
    @Override
    public String deleteLog(Integer id) {
        int i = logMapper.deleteById(id);
        if (i == 1){
            return responseBean.buildOkMsg("删除日志成功");
        }else {
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * @param token  权限验证
     * @param ids   字符串类型的id列表
     * @return yes/no
     */
    @Override
    public String deleteLogs(String token, String ids) {
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(Integer.valueOf(s));
        }
        String username = JwtUtil.getUsername(token);
        int deletes = logMapper.deleteBatchIds(integers);
        if (deletes == 0){
            createLog("删除日志", username,"批量删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }else {
            createLog("删除日志", username,"批量删除成功");
            return responseBean.buildOkMsg("批量删除日志成功");
        }
    }

    /**
     * @param token 权限验证
     * @param response 响应
     */
    @Override
    public void exportLog(String token, HttpServletResponse response) {
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("static/excel/templateExportLog.xls");
            List<Log> logs = logMapper.selectList(new QueryWrapper<>());
            // 根据模板创建一个工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(resourceAsStream);
            // 获取该工作簿的第一个工作表
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 设置列宽
            sheet.setColumnWidth(2,6000);
            sheet.setColumnWidth(3,6000*4);
            int rowIndex = 1;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 循环往表中添加数据
            for (Log log : logs) {
                HSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(log.getId());
                row.createCell(1).setCellValue(log.getTitle());
                row.createCell(2).setCellValue(simpleDateFormat.format(log.getCreateDate()));
                row.createCell(3).setCellValue(log.getText());
            }
            // 设置头信息
            ResponseUtil.setResponse(response);

            // 流的形式传递数据
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param token 权限验证
     * @return yes/no
     */
    @Override
    public String emptyLog(String token) {
        String username = JwtUtil.getUsername(token);
        Integer integer = logMapper.emptyLog();
        System.out.println("清空日志数据库的返回值：" + integer);
        if (integer == 0) {
            createLog("清空日志", username,"清空成功");
            return responseBean.buildOkMsg("清空日志成功");
        }else {
            createLog("清空日志", username,"清空失败,数据库可能存在异常");
            return responseBean.buildWarring("清空日志失败");
        }
    }

    /**
     * @param title     日志标题
     * @param username  操作用户名
     * @param msg       具体消息
     */
    public void createLog(String title, String username, String msg){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Log log = new Log(null, title, username, msg, timestamp);
        logMapper.insert(log);
    }
}
