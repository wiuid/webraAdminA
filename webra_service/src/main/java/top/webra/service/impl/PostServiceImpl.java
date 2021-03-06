package top.webra.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.webra.bean.ResponseBean;
import top.webra.mapper.PostMapper;
import top.webra.pojo.Post;
import top.webra.service.PostService;
import top.webra.util.JwtUtil;
import top.webra.util.ResponseUtil;
import top.webra.utils.RedisUtil;

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
 * @Create: 2021-03-05 13:09
 * @Description： 岗位逻辑业务类
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private ResponseBean responseBean;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取岗位列表
     */
    @Override
    public String getPost(String title, Integer state, Integer page) {
        PageHelper.startPage(page, 10);
        List<Post> postList = postMapper.getPostList(title, state);
        PageInfo<Post> postPageInfo = new PageInfo<>(postList);

        HashMap<String, Object> data = new HashMap<>(3);
        data.put("postList", postList);
        data.put("total", postPageInfo.getTotal());
        data.put("page", postPageInfo.getPages());
        return responseBean.buildOk(data);
    }

    /**
     * 根据id 获取岗位数据
     * @param id 岗位id
     */
    @Override
    public String getPostById(Integer id) {
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<Post>().select("id", "title", "serial", "state", "remark").eq("id", id).last("limit 1");
        Post post = postMapper.selectOne(postQueryWrapper);
        HashMap<String, Object> data = new HashMap<>(1);
        data.put("post", post);
        return responseBean.buildOk(data);
    }

    /**
     * 获取岗位 用于用户选择
     * @return 岗位列表
     */
    @Override
    public String getPostTree() {
        String key = "postSelect";
        if (redisUtil.hasKey(key)){
            return redisUtil.get(key).toString();
        }else {
            List<Post> posts = postMapper.selectList(new QueryWrapper<Post>().select("id", "title"));
            HashMap<String, Object> data = new HashMap<>(1);
            data.put("postList", posts);
            String res = responseBean.buildOk(data);
            redisUtil.set("postSelect", res, 3600L);
            return res;
        }
    }

    /**
     * 新增修改岗位接口
     * @param post 岗位对象
     */
    @Override
    public String savePost(String token, Post post) {
        String username = JwtUtil.getUsername(token);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (post.getId().equals(0)){
            return insertPost(username, post, timestamp);
        }else {
            return updatePost(username, post, timestamp);
        }
    }

    /**
     * 新增
     * @param post 岗位对象
     */
    private String insertPost(String username, Post post, Timestamp timestamp){
        post.setId(null);
        post.setCreateDate(timestamp);
        post.setUpdateDate(timestamp);
        int insert = postMapper.insert(post);
        if (insert == 1){
            redisUtil.del("postSelect");
            logService.createLog("新建岗位", username,"新建岗位:"+ post.getTitle() + ",新建成功");
            return responseBean.buildOkMsg("新增岗位成功");
        }else {
            logService.createLog("新建岗位", username,"新建岗位:"+ post.getTitle() + ",新建失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 修改
     * @param post 岗位对象
     */
    private String updatePost(String username, Post post, Timestamp timestamp){
        post.setUpdateDate(timestamp);
        int update = postMapper.updateById(post);
        if (update == 1){
            redisUtil.del("postSelect");
            logService.createLog("修改岗位", username,"修改岗位:"+ post.getTitle() + ",修改成功");
            return responseBean.buildOkMsg("修改岗位成功");
        }else {
            logService.createLog("修改岗位", username,"修改岗位:"+ post.getTitle() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }


    /**
     * 更新岗位状态
     * @param id 岗位id
     */
    @Override
    public String updatePostState(String token, Integer id) {
        Post post = postMapper.selectById(id);
        Integer state = post.getState()==1?0:1;
        String username = JwtUtil.getUsername(token);
        int update = postMapper.update(null, new UpdateWrapper<Post>().eq("id", id).set("state", state).last("limit 1"));
        if (update ==1){
            redisUtil.del("postSelect");
            logService.createLog("修改岗位状态", username,"修改岗位状态:"+ post.getTitle() + ",修改成功");
            return responseBean.buildOkMsg("修改状态成功");
        }else {
            logService.createLog("修改岗位状态", username,"修改岗位状态:"+ post.getTitle() + ",修改失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 删除单个岗位
     * @param id 岗位id
     */
    @Override
    public String deletePost(String token, Integer id) {
        int delete = postMapper.deleteById(id);
        String username = JwtUtil.getUsername(token);
        if (delete == 1){
            redisUtil.del("postSelect");
            logService.createLog("删除岗位", username,"删除成功");
            return responseBean.buildOkMsg("删除岗位成功");
        }else {
            logService.createLog("删除岗位", username,"删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }
    }

    /**
     * 批量删除岗位
     * @param ids 岗位ids列表字符串
     */
    @Override
    public String deletePosts(String token, String ids) {
        String[] split = ids.split(",");
        ArrayList<Integer> integers = new ArrayList<>();
        for (String s : split) {
            integers.add(Integer.valueOf(s));
        }
        String username = JwtUtil.getUsername(token);
        int deletes = postMapper.deleteBatchIds(integers);
        if (deletes == 0){
            logService.createLog("删除岗位", username,"批量删除失败,数据库可能存在异常");
            return responseBean.buildNoDataMsg("数据异常");
        }else {
            redisUtil.del("postSelect");
            logService.createLog("删除岗位", username,"批量删除成功");
            return responseBean.buildOkMsg("批量删除岗位成功");
        }
    }

    /**
     * 导出岗位
     * @param response 响应
     */
    @Override
    public void exportPosts(HttpServletResponse response) {
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("classpath:static/excel/templateExportPost.xls");
            List<Post> posts = postMapper.selectList(new QueryWrapper<>());
            // 根据模板创建一个工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(resourceAsStream);
            // 获取该工作簿的第一个工作表
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 设置列宽
            sheet.setColumnWidth(1,4000);
            sheet.setColumnWidth(5,6000);
            sheet.setColumnWidth(6,6000);
            int rowIndex = 1;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 循环往表中添加数据
            for (Post post : posts) {
                HSSFRow row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(post.getId());
                row.createCell(1).setCellValue(post.getTitle());
                row.createCell(2).setCellValue(post.getSerial());
                row.createCell(3).setCellValue(post.getState());
                row.createCell(4).setCellValue(post.getRemark());
                row.createCell(5).setCellValue(simpleDateFormat.format(post.getCreateDate()));
                row.createCell(6).setCellValue(simpleDateFormat.format(post.getUpdateDate()));
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
}
