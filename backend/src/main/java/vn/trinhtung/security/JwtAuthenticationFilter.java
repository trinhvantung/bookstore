package vn.trinhtung.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String token = getToken(request);
		System.out.println(token);
		if (StringUtils.hasText(token) && jwtProvider.validateJwtToken(token)) {
			String username = jwtProvider.getUsernameFromToken(token);

			AppUserDetails user = (AppUserDetails) userDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					user, null, user.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		}
		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String token = null;
		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			token = header.substring(7);
		}
		return token;
	}

}
