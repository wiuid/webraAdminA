package top.webra.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.webra.bean.Jwt;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author webra
 */
@Data
@Component
public class JwtUtil {
    @Autowired
    private Jwt jwt;
    private static Jwt customJwt;

    @PostConstruct
    public void init(){
        customJwt = jwt;
    }

    /**
     * 生成JWT
     * JWT分成3部分：1.头部（header),2.载荷（payload, 类似于飞机上承载的物品)，3.签证（signature)
     *
     * 加密后这3部分密文的字符位数为：
     *  1.头部（header)：36位，Base64编码
     *  2.载荷（payload)：没准，BASE64编码
     *  3.签证（signature)：43位，将header和payload拼接生成一个字符串，
     *                          使用HS256算法和我们提供的密钥（secret,服务器自己提供的一个字符串），
     *                          对str进行加密生成最终的JWT
     * @param id        用户id
     * @param subject   用户名
     * @param roles     用户权限
     */
    public static String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 组合header
        Map<String, Object> map = new HashMap<>(2);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        JwtBuilder builder = Jwts.builder()
                .setHeaderParams(map)
//                如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
//                .setClaims(claims)
                //设置id
                .setId(id)
                //设置用户名
                .setSubject(subject)
                //设置jwt的签发时间
                .setIssuedAt(now)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS256, customJwt.getKey())
                .claim("roles", roles);
        if (customJwt.getTtl() > 0) {
            builder.setExpiration( new Date( nowMillis + customJwt.getTtl()));
        }
        return builder.compact();
    }

    /**
     * 解析JWT
     * Claims的几个属性   jti：id    sub：username  roles：auth
     * {jti=1, sub=admin, iat=1615637863, roles=1,2,3,4,5,6,7,8,9,10,11,12,13,14,, exp=1615641463}
     * @param jwtStr    token
     */
    public static Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(customJwt.getKey())
                .parseClaimsJws(jwtStr)
                .getBody();
    }

    public static Claims getClaims(String token){
        return JwtUtil.parseJWT(token);
    }

    /** 解析token 获取 用户id */
    public static Integer getUserId(String token){
        Claims claims = getClaims(token);
        return Integer.valueOf(claims.get("jti").toString());
    }
    /** 解析token 获取 账户 */
    public static String getUsername(String token){
        Claims claims = getClaims(token);
        return claims.get("sub").toString();
    }
    /** 解析token 获取 用户权限集合 */
    public static List<Integer> getRoles(String token){
        String stringRoles = getStringRoles(token);
        List<String> authIdsList= Arrays.asList(stringRoles .split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
        List<Integer> authIds = new ArrayList<>();
        for (String s : authIdsList) {
            authIds.add(Integer.valueOf(s));
        }
        return authIds;
    }
    /** 解析token 获取 用户权限集合 */
    public static String getStringRoles(String token){
        Claims claims = getClaims(token);
        return claims.get("roles").toString();
    }

}
