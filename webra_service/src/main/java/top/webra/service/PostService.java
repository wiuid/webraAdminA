package top.webra.service;

import top.webra.bean.ResponseBean;
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
     */
    String getPost(String title, Integer state, Integer page);

    /**
     * 根据id 查询岗位数据
     * @param id    岗位id
     */
    String getPostById(Integer id);

    /**
     * 用于选择
     */
    String getPostTree();

    /**
     * 修改/新建岗位信息
     * @param post  岗位对象
     */
    String savePost(String token, Post post);

    /**
     * 修改单个岗位状态
     * @param id    岗位id
     */
    String updatePostState(String token, Integer id);

    /**
     * 删除单个岗位
     * @param id    岗位id
     */
    String deletePost(String token, Integer id);

    /**
     * 批量删除岗位
     * @param ids   岗位ids列表字符串
     */
    String deletePosts(String token, String ids);

    void exportPosts(HttpServletResponse response);


}
