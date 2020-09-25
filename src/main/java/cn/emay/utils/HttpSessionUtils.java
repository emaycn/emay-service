package cn.emay.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 请求工具
 *
 * @author Frank
 */
public class HttpSessionUtils {

    /**
     * 获取当前线程的Session
     */
    public static HttpSession getCurrentHttpSession() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession(true);
    }


}
