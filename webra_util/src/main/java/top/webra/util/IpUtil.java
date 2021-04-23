package top.webra.util;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/4/21 20:34
 * @Description: --
 */
public class IpUtil {

    public static String getCity(String ipAddress){
        BufferedReader in = null;
        String result = "";
        try {
            URL url = new URL("http://ip-api.com/json/" + ipAddress + "?lang=zh-CN");
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            urlConnection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            }catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        HashMap<String, String> hashMap = JSON.parseObject(result.toString(), HashMap.class);

        return hashMap.get("regionName") + " " + hashMap.get("city");
    }
}
