package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    JwtUtil jwtUtil;
    /**
     * 在请求之前执行还是之后执行
     * 之前: pre
     * 之后: post
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器之间执行的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否开启
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的内容
     * return 任何值都表示继续执行
     * setsendzuulResponse(false)表示不再执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 获取request域
        HttpServletRequest request = currentContext.getRequest();
        if (request.getMethod().equals("OPTIONS")) {
            return null;
        }
        // 登录不需要判断
        if (request.getRequestURL().toString().indexOf("login") > 0) {
            return null;
        }

        // 得到头信息
        String header = request.getHeader("Authorization");
        if (header!=null) {
            if (header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String)claims.get("roles");
                    if (roles.equals("admin")) {
                        currentContext.addZuulRequestHeader("Authorization",header);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    currentContext.setSendZuulResponse(false);  // 终止运行
                }
            }
        }
        System.out.println("333");
        currentContext.setSendZuulResponse(false);      // 终止运行
        currentContext.setResponseStatusCode(403);      // 权限不足
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
