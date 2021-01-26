package com.shaan.api.myapp;

import com.shaan.api.myapp.authentication.TokenAuthenticationService;
import com.shaan.api.myapp.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.shaan.api.myapp.constant.AppConstant.NON_SSL_SCHEME;
import static com.shaan.api.myapp.constant.AppConstant.SSL_SCHEME;


public class StatelessAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilter.class);
    @Value("${allowed.origins:defaultValue}")
    private String allowedOrigins;

    static final String ORIGIN = "Origin";
    private static Map<String, Boolean> originsMape = new HashMap<String, Boolean>();
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;
    private Boolean matchOrigins(String[] originArr, String protocol)
    {
        String secondMatch = "";
        if(originArr.length == 4) {
            secondMatch = protocol + "*." + originArr[originArr.length - 3] + "." + originArr[originArr.length - 2] + "." + originArr[originArr.length - 1];
        } else if(originArr.length == 3) {
            secondMatch = protocol + "*." + originArr[originArr.length - 2] + "." + originArr[originArr.length - 1];
        }
        return originsMape.containsKey("http://*:*") || originsMape.containsKey(secondMatch);
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
                                                                                            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getHeader(ORIGIN) != null) {
            if (originsMape.size() == 0) {
                String[] originsData = allowedOrigins.trim().split(",");
                for (int i = 0; i < originsData.length; i++) {
                    originsMape.put(originsData[i], true);
                }
            }
            String origin = request.getHeader(ORIGIN);
            String[] originArr = origin.split(Pattern.quote("."));
            String protocol = origin.contains(SSL_SCHEME) ? SSL_SCHEME + "://" : NON_SSL_SCHEME + "://";

            Boolean isAllowedOrigin = originsMape.containsKey(origin) || matchOrigins(originArr, protocol);
            if (!isAllowedOrigin) {
                logger.info("Access-Control-Allow-Origin: {} forbidden", origin);
                response.setStatus(403);
                response.flushBuffer();
                return;
            }
        }
//        response.addHeader("Access-Control-Allow-Origin", allowedOrigins);
        response.addHeader("Access-Control-Allow-Origin", "*");

        response.addHeader("Access-Control-Allow-Methods", "POST,PUT, GET, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers",
                " Origin, X-Requested-With, Content-Type, Accept, accessToken");

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.addHeader("Access-Control-Max-Age", "6000");
            response.setStatus(200);
            response.flushBuffer();
            return;
        } else {
            Authentication authentication = tokenAuthenticationService.getAuthentication((HttpServletRequest) req);
            if (authentication != null && authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);

                User user = (User) authentication.getDetails();

                if (user != null) {
                    chain.doFilter(req, response);
                } else {
                    response.setStatus(403);
                    response.flushBuffer();
                    return;
                }
            } else {
                chain.doFilter(req, response);
            }
        }
    }
}
