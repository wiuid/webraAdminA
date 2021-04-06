package top.webra.util;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021/3/22 12:24
 * @Description: --
 */
public class CompareUtil {
    public Boolean integerNull(Integer number) {
        return number == null;
    }
    public Boolean stringNull(String str) {
        return str == null || str.equals("");
    }
}
