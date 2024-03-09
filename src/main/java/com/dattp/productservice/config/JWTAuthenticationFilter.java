package com.dattp.productservice.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access_token");
        if(accessToken==null){
            try {
                accessToken = Arrays.asList(request.getCookies()).stream()
                .filter(c->c.getName().equals("access_token"))
                .collect(Collectors.toList()).get(0).getValue();
            } catch (Exception e) {}//nếu không có access_token thì sẽ vào catch
        }
        if(accessToken==null){
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // tao giai thuat giai ma
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            // giai ma
            DecodedJWT decodedJWT = jwtVerifier.verify(accessToken);
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            // chuyen ve dang chuan de xu ly
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Arrays.stream(roles).forEach(role->{
                authorities.add(new SimpleGrantedAuthority(role));
            });
            // neu nguoi dung hop le thi set thong tin cho security context
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,null, authorities);
            /*
            {
                "authentication": {
                    "authorities": [
                        {
                            "authority": "ROLE_PRODUCT_ACCESS"
                        }
                    ],
                    "details": null,
                    "authenticated": true,
                    "principal": "anv",
                    "credentials": null,
                    "name": "anv"
                }
            }
            */
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);//lưu lại các thông tin quyền của người dùng hiện tại
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }
    
}
