package top.webra.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.webra.bean.ResponseBean;
import top.webra.pojo.Post;
import top.webra.service.impl.PostServiceImpl;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:29
 * @Description: 岗位管理接口
 */
@Controller
@ResponseBody
@RequestMapping("/system/department/post")
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    /**
     * 所需参数：岗位名、状态、页码
     * @return 岗位列表
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @GetMapping
    public String getTableDate(String title, Integer state, Integer page){
        return postService.getPost(title,  state, page);
    }

    /**
     * 根据id获取指定岗位信息
     * @param id 岗位id
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @GetMapping("/get")
    public String getPostById(Integer id){
        return postService.getPostById(id);
    }

    /**
     * 获取岗位列表，用于用户选择岗位
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @GetMapping("/tree")
    public String getPostTree(){
        return postService.getPostTree();
    }

    /**
     * 所需参数：岗位对象
     * 该接口同时处理新建、修改岗位，区分点是用户id是否为0
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @PostMapping("/save")
    public String savePost(@RequestHeader("token") String token, Post post){
        return postService.savePost(token, post);
    }

    /**
     * 修改岗位状态
     * @param id 岗位id
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @PostMapping("/state")
    public String updatePostState(@RequestHeader("token") String token, Integer id){
        return postService.updatePostState(token, id);
    }

    /**
     * 删除单个岗位
     * @param id 岗位id
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @DeleteMapping("/delete")
    public String deletePost(@RequestHeader("token") String token, int id){
        return postService.deletePost(token, id);
    }

    /**
     * 批量删除岗位
     * @param ids 岗位ids列表字符串
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @DeleteMapping("/deletes")
    public String deletePosts(@RequestHeader("token") String token, String ids){
        return postService.deletePosts(token, ids);
    }

    /**
     * 导出岗位
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        postService.exportPosts(response);
    }


}
