package com.MoneyManager.SpendSense.Security;

import com.MoneyManager.SpendSense.Util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class jwtSecurityFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
final String authheader=request.getHeader("Authorization");
String email=null;
String jwt=null;
if(authheader!=null && authheader.startsWith("Bearer")){
    jwt=authheader.substring(7);
    email=jwtUtils.extractEmail(jwt);
}
if(email!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
    UserDetails userDetails= this.userDetailsService.loadUserByUsername(email);
    if(jwtUtils.validateToken(jwt)){
        UsernamePasswordAuthenticationToken authtoken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
SecurityContextHolder.getContext().setAuthentication(authtoken);

    }
}
filterChain.doFilter(request,response);

    }
}
