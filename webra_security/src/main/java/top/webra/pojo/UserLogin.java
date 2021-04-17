package top.webra.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: webra
 * @Create: 2021-03-05 13:22
 * @Description: --
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private Integer state;
    private Collection<? extends GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    /** 账户是否过期 */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /** 账户是否锁定 */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /** 账户凭证是否过期 */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 是否启用 */
    @Override
    public boolean isEnabled() {
        return this.state.equals(0);
    }
}
