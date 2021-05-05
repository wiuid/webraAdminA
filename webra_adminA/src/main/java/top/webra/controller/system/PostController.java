package top.webra.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "岗位管理")
public class PostController {
    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private ResponseBean responseBean;

    /**
     * 所需参数：岗位名、状态、页码
     * @return 岗位列表
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @GetMapping
    @ApiOperation("查询岗位")
    public String getTableDate(String title, Integer state, Integer page){
        return postService.getPost(title,  state, page);
    }

    /**
     * 根据id获取指定岗位信息
     * @param id 岗位id
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @GetMapping("/get")
    @ApiOperation("获取单个岗位")
    public String getPostById(Integer id){
        if (id == null || id < 1){
            return responseBean.buildWarring("请求违法");
        }
        return postService.getPostById(id);
    }

    /**
     * 获取岗位列表，用于用户选择岗位
     */
    @PreAuthorize("hasRole('ROLE_user')")
    @GetMapping("/tree")
    @ApiOperation("获取岗位列表，用于用户选择")
    public String getPostTree(){
        return postService.getPostTree();
    }

    /**
     * 所需参数：岗位对象
     * 该接口同时处理新建、修改岗位，区分点是用户id是否为0
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @PostMapping("/save")
    @ApiOperation("新建/修改岗位")
    public String savePost(@RequestHeader("token") String token, Post post){
        if (post.getId() < 0){
            return responseBean.buildWarring("请求违法");
        }
        return postService.savePost(token, post);
    }

    /**
     * 修改岗位状态
     * @param id 岗位id
     */
    @ApiOperation("修改岗位状态")
    @PreAuthorize("hasRole('ROLE_post')")
    @PostMapping("/state")
    public String updatePostState(@RequestHeader("token") String token, Integer id){
        if (id == null || id < 1){
            return responseBean.buildWarring("请求违法");
        }
        return postService.updatePostState(token, id);
    }

    /**
     * 删除单个岗位
     * @param id 岗位id
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @DeleteMapping("/delete")
    @ApiOperation("删除岗位")
    public String deletePost(@RequestHeader("token") String token, Integer id){
        if (id == null || id < 1){
            return responseBean.buildWarring("请求违法");
        }
        return postService.deletePost(token, id);
    }

    /**
     * 批量删除岗位
     * @param ids 岗位ids列表字符串
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @DeleteMapping("/deletes")
    @ApiOperation("批量删除岗位")
    public String deletePosts(@RequestHeader("token") String token, String ids){
        return postService.deletePosts(token, ids);
    }

    /**
     * 导出岗位
     */
    @PreAuthorize("hasRole('ROLE_post')")
    @GetMapping("/export")
    @ApiOperation("导出岗位")
    public void export(HttpServletResponse response){
        postService.exportPosts(response);
    }


}
