package zopeefy.com.shk.JwTserviceAuth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import zopeefy.com.shk.entity.UserEntity;
import zopeefy.com.shk.repository.UserRepositoryInterface;
import zopeefy.com.shk.service.JwtTokenService;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenService jwtTokenService;
    private final UserRepositoryInterface userRepositoryInterface;


    public JwtAuthenticationFilter(JwtTokenService jwtTokenService, UserRepositoryInterface userRepositoryInterface) {
        this.jwtTokenService = jwtTokenService;
        this.userRepositoryInterface = userRepositoryInterface;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String Authheader = request.getHeader("Authentication");

        if (Authheader==null || !Authheader.startsWith("bearer")){
            filterChain.doFilter(request , response);
            return;
        }
        String token = Authheader.substring(7);

        try {
            String email = jwtTokenService.ExtractEmailFromToken(token);

                UserEntity user = userRepositoryInterface
                        .findByEmail(email)
                        .orElse(null);

                if (user == null || !Boolean.TRUE.equals(user.getIsActive())
                        || user.getRole() == null) {

                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String role = "ROLE_" + user.getRole().name();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            }
        catch (Exception ex){
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
