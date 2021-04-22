package top.webra.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.webra.service.UpdateLogService;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:10
 * @Description: 更新日志逻辑业务类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpdateLogServiceImpl implements UpdateLogService {
}
