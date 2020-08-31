package cn.emay.api.pub;

import cn.emay.constant.web.SystemType;
import cn.emay.constant.web.WebAuth;
import cn.emay.constant.web.WebConstant;
import cn.emay.constant.web.WebToken;
import cn.emay.core.client.pojo.Client;
import cn.emay.core.client.service.ClientService;
import cn.emay.core.system.pojo.Resource;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.service.ResourceService;
import cn.emay.core.system.service.UserService;
import cn.emay.redis.RedisClient;
import cn.emay.utils.CaptchaUtils;
import cn.emay.utils.PasswordUtils;
import cn.emay.utils.WebUtils;
import cn.emay.utils.encryption.Base64;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 公共API
 *
 * @author Frank
 */
@RestController
@RequestMapping(value = "/p")
public class PublicApi {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RedisClient redis;

    /**
     * 验证码图片
     */
    @RequestMapping(value = "/loginCaptcha", method = RequestMethod.GET)
    public void loginCaptcha(String uuid) throws Exception {
        CaptchaUtils.writeByRedis(redis, uuid, WebConstant.CAPTCHA_TAG_LOGIN, 1800);
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout() {
        log.info("user : " + WebUtils.getCurrentUser().getUsername() + " logout .");
        WebUtils.logout();
        return Result.rightResult();
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SuperResult<WebToken> login(String username, String password, String captcha, String uuid, String s) {
        if (StringUtils.isEmpty(username)) {
            return SuperResult.badResult("用户名不能为空");
        }
        username = username.toLowerCase();
        if (StringUtils.isEmpty(password)) {
            return SuperResult.badResult("密码不能为空");
        }
        if (StringUtils.isEmpty(captcha)) {
            return SuperResult.badResult("验证码不能为空");
        }
        if (StringUtils.isEmpty(uuid)) {
            return SuperResult.badResult("参数错误");
        }
        if (StringUtils.isEmpty(s)) {
            return SuperResult.badResult("参数错误");
        }
        boolean isOk = CaptchaUtils.checkByRedis(redis, uuid, WebConstant.CAPTCHA_TAG_LOGIN, captcha);
        if (!isOk) {
            return SuperResult.badResult("验证码错误");
        }
        User user = userService.findByUserName(username);
        if (user == null) {
            return SuperResult.badResult("用户名或密码错误");
        }
        if (user.getUserState() == User.STATE_OFF) {
            return SuperResult.badResult("用户已锁定，请联系管理员解锁");
        }
        String pass = PasswordUtils.encrypt(password);
        if (!user.getPassword().equalsIgnoreCase(pass)) {
            return SuperResult.badResult("用户名或密码错误");
        }
        Client client = null;
        List<Resource> userResource = resourceService.getUserResources(user.getId());
        String sdecode = Base64.decodeUTF8(s);
        String system = null;
        WebToken token = null;
        // 运营端
        if (Integer.parseInt(sdecode) % 133 == 0) {
            system = SystemType.OPER.getType();
        }
        // 客户端
        if (Integer.parseInt(sdecode) % 177 == 0) {
            system = SystemType.CLIENT.getType();
            client = clientService.findByUserId(user.getId());
        }
        if (system == null) {
            return SuperResult.badResult("无权访问");
        }
        boolean access = false;
        for (Resource re : userResource) {
            if (re.getResourceType().equalsIgnoreCase(system)) {
                access = true;
                break;
            }
        }
        if (!access) {
            return SuperResult.badResult("无权访问");
        }
        token = WebUtils.login(user, userResource, client);
        log.info("user : " + user.getUsername() + " login .");
        return SuperResult.rightResult(token);
    }

    /**
     * 修改密码
     */
    @WebAuth({})
    @RequestMapping(value = "/changePassword")
    public Result changepass(String password, String newpass) throws IOException {
        if (password == null) {
            return Result.badResult("原密码不能为空");
        }
        if (newpass == null) {
            return Result.badResult("新密码不能为空");
        }
        User user = WebUtils.getCurrentUser();
        String pass = PasswordUtils.encrypt(password);
        if (!user.getPassword().equals(pass)) {
            return Result.badResult("密码错误");
        }
        String newpass1 = PasswordUtils.encrypt(newpass);
        userService.modifyPassword(user.getId(), newpass1);
        log.info("user : " + user.getUsername() + " change password .");
        return Result.rightResult();
    }

}
