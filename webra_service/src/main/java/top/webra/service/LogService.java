package top.webra.service;

import top.webra.bean.ResponseBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:07
 * @Description: --
 */
public interface LogService {

    ResponseBean getLogList(String title,
                            String createDateStart,
                            String createDateEnd,
                            Integer page);
}
