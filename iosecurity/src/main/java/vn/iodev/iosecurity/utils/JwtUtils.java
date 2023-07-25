package vn.iodev.iosecurity.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import vn.iodev.iosecurity.model.IOUserDetails;
import vn.iodev.iosecurity.model.VaiTro;

import org.springframework.security.core.Authentication;

@Component
@Slf4j
public class JwtUtils {
    @Value("${io.app.jwtSecret}")
	private String jwtSecret;

	@Value("${io.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {
		StringBuilder vaiTroStrs = new StringBuilder();
		log.info("In jwt utils");
		IOUserDetails userPrincipal = (IOUserDetails) authentication.getPrincipal();
		for (VaiTro vt : userPrincipal.getTaiKhoan().getVaiTros()) {
			if (!vaiTroStrs.toString().equals("")) {
				vaiTroStrs.append(",");
			}
			vaiTroStrs.append(vt.getTen().toString());
		}
		log.info("In jwt utils vaitros: " + vaiTroStrs.toString());

		return Jwts.builder()
				.setSubject(String.format("%s,%s,%s", (userPrincipal.getUsername()), userPrincipal.getTaiKhoan().getId(), userPrincipal.getTaiKhoan().getLoaiTaiKhoan()))
				.setIssuedAt(new Date())
				.claim("vaiTros", userPrincipal.getTaiKhoan().getVaiTros())
				.claim("vaiTrosStr", vaiTroStrs.toString())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		String subject = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
		return subject.split(",")[0];
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
