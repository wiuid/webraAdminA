package top.webra.bean;

import com.alibaba.fastjson.JSON;
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

    public String build(Integer status, String msg, Map<String, Object> map){
        this.status = status;
        this.msg = msg;
        this.data = map;
        return JSON.toJSONString(this);
    }
    /** 响应正常且携带数据 */
    public String buildOk(Map<String, Object> map){
        this.status = 200;
        this.msg = "success";
        this.data = map;
        return JSON.toJSONString(this);
    }
    /** 响应正常携带自定义消息 */
    public String buildOkMsg(String msg){
        this.status = 200;
        this.msg = msg;
        this.data = null;
        return JSON.toJSONString(this);
    }
    /** 响应正常，直接调用(查询数据为0 使用该方法) */
    public String buildOkNoData(){
        this.status = 200;
        this.msg = "success";
        this.data = null;
        return JSON.toJSONString(this);
    }
    /** 响应异常，增删改查为0 */
    public String buildNoData(){
        this.status = 202;
        this.msg = "no data";
        this.data = null;
        return JSON.toJSONString(this);
    }
    /** 警告 */
    public String buildWarring(String msg){
        this.status = 201;
        this.msg = msg;
        this.data = null;
        return JSON.toJSONString(this);
    }
    /** 错误 */
    public String buildError(String msg){
        this.status = 203;
        this.msg = msg;
        this.data = null;
        return JSON.toJSONString(this);
    }
    /** 响应异常，携带自定义消息 */
    public String buildNoDataMsg(String msg){
        this.status = 202;
        this.msg = msg;
        this.data = null;
        return JSON.toJSONString(this);
    }
    /** 账户异常  敏感操作判断用户token和个人信息不对应 */
    public String buildUserErr(){
        this.status = 444;
        this.msg = null;
        this.data = null;
        return JSON.toJSONString(this);
    }
    /** token过期 */
    public String buildNotLogin(String msg){
        this.status = 403;
        this.msg = msg;
        this.data = null;
        return JSON.toJSONString(this);
    }

    public String buildOkInitNull(String dataName){
        HashMap<String, Object> data = new HashMap<>(3);
        data.put(dataName, new ArrayList<>());
        data.put("total",0);
        data.put("page",1);
        buildOk(data);
        return JSON.toJSONString(this);
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
