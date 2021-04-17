package top.webra.service;

import top.webra.pojo.Post;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:07
 * @Description: --
 */
public interface PostService {

    /**
     * 获取全部岗位
     * @param title     岗位名
     * @param state     岗位状态
     * @param page      页码
     * @return 岗位列表
     */
    String getPost(String title, Integer state, Integer page);

    /**
     * 根据id 查询岗位数据
     * @param id    岗位id
     * @return 单岗位信息
     */
    String getPostById(Integer id);

    /**
     * 用于选择
     * @return 岗位列表
     */
    String getPostTree();

    /**
     * 修改/新建岗位信息
     * @param post  岗位对象
     * @param token 权限验证
     * @return yes/no
     */
    String savePost(String token, Post post);

    /**
     * 修改单个岗位状态
     * @param id    岗位id
     * @param token 权限验证
     * @return  yes/no
     */
    String updatePostState(String token, Integer id);

    /**
     * 删除单个岗位
     * @param id    岗位id
     * @param token 权限验证
     * @return  yes/no
     */
    String deletePost(String token, Integer id);

    /**
     * 批量删除岗位
     * @param ids   岗位ids列表字符串
     * @param token 权限验证
     * @return yes/no
     */
    String deletePosts(String token, String ids);

    /**
     * 岗位数据导出
     * @param response 响应
     */
    void exportPosts(HttpServletResponse response);


}
