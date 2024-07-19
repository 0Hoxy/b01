package org.zerock.b01.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
//User 클래스는 UserDetails 인터페이스를 구현한 클래스로 최대한 간단하게 UserDetails 타입을 생성할 수 있는 방법을 제공한다.
public class MemberSecurityDTO extends User {

    private String mid;

    private String mpw;

    private String email;

    private boolean del;

    private boolean social;

    public MemberSecurityDTO(String username, String password, String email, boolean del, boolean social, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;

    }
}
