package top.webra.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-03 21:18
 * @Description: 数据转换
 */
public class CastUtil {

    /**
     * int to String
     * @param number    int数字
     */
    public static String toString(int number){
        return Integer.toString(number);
    }

    /**
     * long to String
     * @param number    long数字
     */
    public static String toString(long number){
        return Long.toString(number);
    }
    /**
     * double to String
     * @param number    double数字
     */
    public static String toString(double number){
        return Double.toString(number);
    }
    /**
     * String to Double
     * @param str   字符串
     */
    public static Double toDouble(String str){
        return Double.valueOf(str);
    }

    /**
     * String to Long
     * @param str   字符串
     */
    public static Long toLong(String str){
        return Long.valueOf(str);
    }

    /**
     * Obj to String
     * @param obj   对象
     */
    public static Integer toInteger(Object obj){
        String str = String.valueOf(obj);
        return Integer.valueOf(str);
    }

    /**
     * 传入参数必须是：2021-03-01T16:00:00.000Z  的字符串
     * @param str   字符串
     */
    public static String toDateFormat(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat2.format(simpleDateFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
