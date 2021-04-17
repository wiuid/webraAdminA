package top.webra.adminA;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import top.webra.mapper.*;
import top.webra.pojo.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-04 21:29
 * @Description: Mapper 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private InformMapper informMapper;

    @Test
    public void authTest(){
        QueryWrapper<Inform> informQueryWrapper = new QueryWrapper<>();
        informQueryWrapper.orderByAsc("create_date");
        List<Inform> informs = informMapper.selectList(informQueryWrapper);
        for (Inform inform : informs) {
            System.out.println(inform);
        }
    }

    @Test
    public void userTest(){
    }

    @Autowired
    private DepartmentMapper departmentMapper;
    @Test
    public void departmentTest(){
        UpdateWrapper<Department> departmentUpdateWrapper = new UpdateWrapper<Department>()
                .eq("id", 25)
                .set("whether", 0)
                .last("limit 1");
        departmentMapper.update(null, departmentUpdateWrapper);
    }

    @Test
    public void authUserAsideTest(){

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(6);
        integers.add(7);
        integers.add(8);
        integers.add(9);
        integers.add(10);
        integers.add(11);
        integers.add(12);
        integers.add(13);
        integers.add(14);

        List<Auth> authList = authMapper.selectList(new QueryWrapper<Auth>().in("id", integers));
        ArrayList<Auth> auths = new ArrayList<>();
        for (Auth auth : authList) {
            if (auth.getSuperId().equals(0)){
                auths.add(auth);
            }else{
                for (Auth auth1 : auths) {
                    if (auth1.getId().equals(auth.getSuperId())){
                        List<Auth> children = auth1.getChildren();
                        if (children == null){
                            ArrayList<Auth> auths1 = new ArrayList<>();
                            auths1.add(auth);
                            auth1.setChildren(auths1);
                        }else {
                            children.add(auth);
                        }
                    }
                }
            }
        }
        System.out.println(JSON.toJSONString(auths));
    }

    @Autowired
    private PostMapper postMapper;

    @Test
    public void excelTest(){
        try {
            File excel = ResourceUtils.getFile("classpath:static/excel/templateExportPost.xls");
            FileInputStream fileInputStream = new FileInputStream(excel);
            List<Post> posts = postMapper.selectList(new QueryWrapper<>());
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\webra\\Desktop\\岗位信息表.xls");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();
            System.out.println("数据导出完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
