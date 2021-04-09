package top.webra.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-02 9:36
 * @Description: 自定义响应接口
 */
@Component
@Data
public class ResponseBean {
    private Integer status;
    private String msg;
    private Map<String, Object> data;

    // 响应正常且携带数据
    public void buildOk(Map<String, Object> map){
        this.status = 200;
        this.msg = "success";
        this.data = map;
    }
    // 响应正常携带自定义消息
    public void buildOkMsg(String msg){
        this.status = 200;
        this.msg = msg;
        this.data = null;
    }
    // 响应正常，直接调用(查询数据为0 使用该方法)
    public void buildOkNoData(){
        this.status = 200;
        this.msg = "success";
        this.data = null;
    }
    // 响应异常，增删改查为0
    public void buildNoData(){
        this.status = 202;
        this.msg = "no data";
        this.data = null;
    }
    // 警告
    public void buildWarring(String msg){
        this.status = 201;
        this.msg = msg;
        this.data = null;
    }
    // 错误
    public void buildError(String msg){
        this.status = 203;
        this.msg = msg;
        this.data = null;
    }
    // 响应异常，携带自定义消息
    public void buildNoDataMsg(String msg){
        this.status = 202;
        this.msg = msg;
        this.data = null;
    }
    // 账户异常  敏感操作判断用户token和个人信息不对应
    public void buildUserErr(){
        this.status = 444;
        this.msg = null;
        this.data = null;
    }
    // token过期
    public void buildNotLogin(String msg){
        this.status = 403;
        this.msg = msg;
        this.data = null;
    }

    public void buildOkInitNull(String dataName){
        HashMap<String, Object> data = new HashMap<>();
        data.put(dataName, new ArrayList<Object>());
        data.put("total",0);
        data.put("page",1);
        buildOk(data);
    }

    public static ResponseBean ok(String msg) {
        return new ResponseBean(200, msg, null);
    }

    private ResponseBean() {
    }

    private ResponseBean(Integer status, String msg, Map<String, Object> obj) {
        this.status = status;
        this.msg = msg;
        this.data = obj;
    }
}
