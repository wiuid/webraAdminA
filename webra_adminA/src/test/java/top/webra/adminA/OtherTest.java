package top.webra.adminA;

import org.junit.Test;
import top.webra.util.MD5Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 14:49
 * @Description: --
 */
public class OtherTest {
    @Test
    public void stringToList(){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(1);
        ids.add(1);
        ids.add(1);
        String idsString = "";
        for (Integer id : ids) {
            String str = id + ",";
            idsString += str;
        }
        System.out.println(idsString);
        List<String> list= Arrays.asList(idsString .split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
        System.out.println(list);
        /**
         * 1,1,1,1,
         * [1, 1, 1, 1]
         */
    }
    @Test
    public void md5Test(){
        String saltMD5 = MD5Util.getSaltMD5("123456");
        System.out.println(saltMD5);
        boolean saltverifyMD5 = MD5Util.getSaltverifyMD5("123456", "c4b671607e99c43686160e3f31a44e033a1211a818f17534");
        System.out.println(saltverifyMD5);
    }
    @Test
    public void stringToTime(){
        String str = "2021-03-01T16:00:00.000Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        try {
            Date parse = simpleDateFormat.parse(str);
            Timestamp timestamp = new Timestamp(parse.getTime());
            System.out.println(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void avatarTest(){
        String base64Q = "data:image/jpeg;base64,";
        String base64 = "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";
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            // 解码
            byte[] b = decoder.decode(base64);
            for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            String filepath =System.getProperty("java.io.tmpdir") +"测试"+System.currentTimeMillis()+".png";


            File file  = new File(filepath);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(b);
            fos.flush();
            fos.close();
            System.out.println(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
