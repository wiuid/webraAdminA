package top.webra.service;

import org.springframework.lang.Nullable;
import top.webra.pojo.Inform;

import java.text.ParseException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:07
 * @Description: --
 */
public interface InformService {
    /**
     * 返回公告列表，不包含公告正文
     * @param title     标题检索
     * @param state     状态检索
     * @param page      页码检索
     * @param pageSize  每页数据量，当前版本没有，之后会更新
     * @param createDateStart   初始创建日期
     * @param createDateEnd     最后创建日期
     * @return 公告列表
     * @throws ParseException 异常
     */
    String selectInformList(String title, Integer state, Integer page, @Nullable Integer pageSize, String createDateStart, String createDateEnd) throws ParseException;

    /**
     * 查询单条公告信息
     * @param informId      公告id
     * @return 单条公告信息
     */
    String selectInform(Integer informId);

    /**
     * 首页任何人可查
     * @param informId  公告id
     * @return  公告信息
     */
    String getInform(Integer informId);

    /**
     * 新建公告或修改某公告，以token决定公告修改的人
     * @param inform        公告对象
     * @param token         解析
     * @return yes/no
     */
    String saveInform(Inform inform, String token);

    /**
     * 删除单条公告
     * @param id    公告id
     * @param token 权限验证
     * @return yes/no
     */
    String deleteInform(String token, Integer id);
    /**
     * 批量删除公告
     * @param ids   公告id列表字符串
     * @param token 权限验证
     * @return yes/no
     */
    String deleteInforms(String token, String ids);
}
