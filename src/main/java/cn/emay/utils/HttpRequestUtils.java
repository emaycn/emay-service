package cn.emay.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具
 *
 * @author Frank
 */
public class HttpRequestUtils {

    public final static String X_REQUESTED_WITH = "X-Requested-With";
    public final static String XMLTYPE = "XMLHttpRequest";

    /**
     * 获取当前线程的Request
     *
     * @return 请求
     */
    public static HttpServletRequest getCurrentHttpRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    /**
     * 获取真实IP<br/>
     * 按照X-Forwarded-For、X-Real-IP、getRemoteAddr的顺序找
     *
     * @return 真实ip
     */
    public static String getRemoteRealIp() {
        HttpServletRequest request = getCurrentHttpRequest();
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");
        String ip;
        if (forwarded == null || forwarded.length() == 0 || "unknown".equalsIgnoreCase(forwarded)) {
            if (realIp == null || realIp.length() == 0 || "unknown".equalsIgnoreCase(realIp)) {
                ip = remoteAddr;
            } else {
                ip = realIp;
            }
        } else {
            ip = forwarded.split(",")[0];
        }
        return ip;
    }

    /**
     * 查询Cookie
     */
    public static Cookie findCookie(String name) {
        HttpServletRequest request = getCurrentHttpRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 是否AJAX请求
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getCurrentHttpRequest();
        return XMLTYPE.equalsIgnoreCase(request.getHeader(X_REQUESTED_WITH));
    }

    /**
     * 获取 当前域名
     *
     * @return 当前域名
     */
    public static String getLocalAddress() {
        HttpServletRequest request = getCurrentHttpRequest();
        String uri = request.getRequestURI() == null ? "" : request.getRequestURI();
        String url = request.getRequestURL() == null ? "" : request.getRequestURL().toString();
        String contextPath = request.getContextPath() == null ? "" : request.getContextPath();
        return url.replace(uri, "") + contextPath;
    }

}
