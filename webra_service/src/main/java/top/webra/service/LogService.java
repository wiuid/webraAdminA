package top.webra.service;

import top.webra.bean.ResponseBean;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:07
 * @Description: --
 */
public interface LogService {

    /**
     * 获取日志列表
     * @param title             日志标题
     * @param createDateStart   创建开始时间
     * @param createDateEnd     创建结束时间
     * @param page              页码
     */
    String getLogList(String title,
                            String createDateStart,
                            String createDateEnd,
                            Integer page);


    /**
     * 删除单条日志
     * @param id    日志id
     */
    String deleteLog(Integer id);

    /**
     * 批量删除日志信息
     * @param ids   字符串类型的id列表
     */
    String deleteLogs(String token, String ids);

    /**
     * 导出日志
     */
    void exportLog(String token, HttpServletResponse response);

    /**
     * 删除所有日志数据
     */
    String emptyLog(String token);

}
