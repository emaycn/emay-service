package cn.emay.utils;

import cn.emay.configuration.PropertiesConfiguration;
import cn.emay.constant.web.WebConstant;
import cn.emay.constant.web.WebToken;
import cn.emay.core.client.pojo.Client;
import cn.emay.core.client.service.ClientService;
import cn.emay.core.system.pojo.Resource;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.service.UserService;
import cn.emay.excel.common.ExcelVersion;
import cn.emay.excel.write.ExcelWriter;
import cn.emay.redis.RedisClient;
import cn.emay.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

/**
 * Web工具类
 *
 * @author Frank
 */
public class WebUtils {

    /**
     * 登陆
     *
     * @param user      用户
     * @param resources 用户权限资源
     */
    public static WebToken login(User user, List<Resource> resources, Client client) {
        String sessionId = MessageFormat.format(WebConstant.WEB_TOKEN, UUID.randomUUID().toString().replace("-", "").toUpperCase());
        WebToken token = new WebToken(sessionId, user, resources, client);
        RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
        redis.set(sessionId, token, WebConstant.LOGIN_TIMEOUT);
        return token;
    }

    /**
     * 登出
     */
    public static void logout() {
        String sessionId = getSessionId();
        if (sessionId != null) {
            RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
            redis.del(sessionId);
        }
    }

    /**
     * 获取当前登录用户所属客户<br/>
     */
    public static Client setAndGetCurrentClient() {
        Client client = (Client) HttpRequestUtils.getCurrentHttpRequest().getAttribute(WebConstant.CLIENT_CURRENT);
        if (client != null) {
            return client;
        }
        User user = getCurrentUser();
        if (user == null) {
            return null;
        }
        ClientService clientService = ApplicationContextUtils.getBean(ClientService.class);
        client = clientService.findByUserId(user.getId());
        if (client != null) {
            HttpRequestUtils.getCurrentHttpRequest().setAttribute(WebConstant.CLIENT_CURRENT, client);
        }
        return null;
    }

    /**
     * 获取当前登录用户所属客户<br/>
     */
    public static Client getCurrentClient() {
        return (Client) HttpRequestUtils.getCurrentHttpRequest().getAttribute(WebConstant.CLIENT_CURRENT);
    }

    /**
     * 每一次请求都要查询一次数据库来验证用户状态，后续可以把用户状态放到redis管理，状态的改变也随时更新redis
     */
    public static User setAndGetCurrentUser() {
        User user = (User) HttpRequestUtils.getCurrentHttpRequest().getAttribute(WebConstant.REQUEST_USER);
        if (user != null) {
            return user;
        }
        WebToken token = getWebToken();
        if (token == null) {
            return null;
        }
        UserService userService = ApplicationContextUtils.getBean(UserService.class);
        user = userService.findById(token.getUser().getId());
        if (user == null) {
            String sessionId = getSessionId();
            if (sessionId != null) {
                RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
                redis.del(sessionId);
            }
        } else {
            HttpRequestUtils.getCurrentHttpRequest().setAttribute(WebConstant.REQUEST_USER, user);
        }
        return user;
    }

    /**
     * 获取当前登录用户<br/>
     */
    public static User getCurrentUser() {
        return (User) HttpRequestUtils.getCurrentHttpRequest().getAttribute(WebConstant.REQUEST_USER);
    }

    /**
     * 是否有用户登录
     */
    public static boolean isLogin() {
        return getCurrentUser() != null;
    }

    /**
     * 获取Token
     */
    public static WebToken getWebToken() {
        WebToken token = (WebToken) HttpRequestUtils.getCurrentHttpRequest().getAttribute(WebConstant.REQUEST_TOKEN);
        if (token != null) {
            return token;
        }
        String sessionId = getSessionId();
        if (sessionId == null) {
            return null;
        }
        RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
        token = redis.get(sessionId, WebToken.class);
        if (token != null) {
            // 如果token缓存时间小于1小时,刷新
            if (redis.ttl(sessionId) < WebConstant.HOUR_MILLIS) {
                redis.expire(sessionId, WebConstant.LOGIN_TIMEOUT);
            }
            HttpRequestUtils.getCurrentHttpRequest().setAttribute(WebConstant.REQUEST_TOKEN, token);
        }
        return token;
    }

    /**
     * 获取SessionId
     */
    public static String getSessionId() {
        String sessionId = HttpRequestUtils.getCurrentHttpRequest().getHeader(WebConstant.HEAD_AUTH_TOKEN);
        if (null == sessionId) {
            sessionId = HttpRequestUtils.getCurrentHttpRequest().getParameter(WebConstant.HEAD_AUTH_TOKEN);
        }
        return sessionId;
    }

    /**
     * 跳到错误页面或者提示错误信息
     */
    public static void toError(String errorMassage) {
        HttpServletResponse response = HttpResponseUtils.getCurrentHttpResponse();
        PropertiesConfiguration propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfiguration.class);
        if (propertiesConfig.isDev()) {
            String origin = HttpRequestUtils.getCurrentHttpRequest().getHeader("Origin");
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-CSRF-TOKEN");
        }
        try {
            HttpResponseUtils.outputJson(Result.badResult("-1", errorMassage, null));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 跳转到登陆页面或者提示未登陆信息
     */
    public static void toNoLogin() {
        HttpServletResponse response = HttpResponseUtils.getCurrentHttpResponse();
        PropertiesConfiguration propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfiguration.class);
        if (propertiesConfig.isDev()) {
            String origin = HttpRequestUtils.getCurrentHttpRequest().getHeader("Origin");
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-CSRF-TOKEN");
        }
        try {
            HttpResponseUtils.outputJson(Result.badResult("-222", "您还未登陆，请先登录", null));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 跳转到登陆页面或者提示未登陆信息
     */
    public static void toNoChangePass() {
        HttpServletResponse response = HttpResponseUtils.getCurrentHttpResponse();
        PropertiesConfiguration propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfiguration.class);
        if (propertiesConfig.isDev()) {
            String origin = HttpRequestUtils.getCurrentHttpRequest().getHeader("Origin");
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-CSRF-TOKEN");
        }
        try {
            HttpResponseUtils.outputJson(Result.badResult("-111", "首次登陆，未修改密码", null));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 导出数据
     */
    public static void exportExcel(ExcelVersion version, List<?> list) {
        HttpServletResponse response = HttpResponseUtils.getCurrentHttpResponse();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=appdayreport.xlsx");
        try {
            ExcelWriter.write(response.getOutputStream(), version, list);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
