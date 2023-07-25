package vn.iodev.iosecurity.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IOUserDetails implements UserDetails {
    TaiKhoan taiKhoan;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return taiKhoan.getMatKhau();
    }

    @Override
    public String getUsername() {
        return taiKhoan.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

    public IOUserDetails(TaiKhoan taiKhoan, Collection<? extends GrantedAuthority> authorities) {
        this.taiKhoan = taiKhoan;
        this.authorities = authorities;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public static IOUserDetails build(TaiKhoan taiKhoan) {
        List<GrantedAuthority> authorities = taiKhoan.getVaiTros().stream()
                                                .map(vaiTro -> new SimpleGrantedAuthority(vaiTro.getTen().name()))
                                                .collect(Collectors.toList());

        return new IOUserDetails(taiKhoan, authorities);
    }

    @Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		IOUserDetails user = (IOUserDetails) o;
		return Objects.equals(taiKhoan.getEmail(), user.getUsername());
	}
}
