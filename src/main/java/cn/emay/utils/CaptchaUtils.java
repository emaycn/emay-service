package cn.emay.utils;

import cn.emay.constant.web.WebConstant;
import cn.emay.redis.RedisClient;
import com.google.code.kaptcha.Producer;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

/**
 * 图片验证码工具
 *
 * @author Frank
 */
public class CaptchaUtils {


    /**
     * 从redis中校验
     */
    public static boolean checkByRedis(RedisClient redis, String sessionId, String tag, String captchaText) {
        String key = genKey(tag, sessionId);
        String value = redis.get(key);
        redis.del(key);
        return captchaText != null && captchaText.equalsIgnoreCase(value);
    }

    /**
     * 写入Redis
     */
    public static void writeByRedis(RedisClient redis, String sessionId, String tag, int timeout) throws IOException {
        HttpServletResponse response = HttpResponseUtils.getCurrentHttpResponse();
        String key = genKey(tag, sessionId);
        String value = create(response.getOutputStream());
        redis.set(key, value, timeout);
        response.getOutputStream().close();
    }

    /**
     * 从Session校验
     */
    public static boolean checkBySession(String sessionId, String tag, String captchaText) {
        HttpSession session = HttpSessionUtils.getCurrentHttpSession();
        String key = genKey(tag, sessionId);
        String value = (String) session.getAttribute(key);
        if (captchaText != null && captchaText.equalsIgnoreCase(value)) {
            session.removeAttribute(key);
            return true;
        }
        return false;
    }

    /**
     * 写入Session
     */
    public static void writeBySession(String sessionId, String tag) throws IOException {
        HttpServletResponse response = HttpResponseUtils.getCurrentHttpResponse();
        HttpSession session = HttpSessionUtils.getCurrentHttpSession();
        String key = genKey(tag, sessionId);
        String value = create(response.getOutputStream());
        session.setAttribute(key, value);
        response.getOutputStream().close();
    }

    /**
     * 生成Key
     */
    private static String genKey(String tag, String id) {
        return MessageFormat.format(WebConstant.CAPTCHA, tag, id);
    }

    /**
     * 创建图片验证码，写入输出流，并返回文字
     */
    public static String create(OutputStream writeOutStream) throws IOException {
        CaptchaResult result = create();
        ImageIO.write(result.getBufferedImage(), "jpg", writeOutStream);
        writeOutStream.close();
        return result.getText();
    }

    /**
     * 创建图片验证码
     */
    public static CaptchaResult create() {
        Producer producer = ApplicationContextUtils.getBean(Producer.class);
        String text = producer.createText();
        BufferedImage bufferedImage = producer.createImage(text);
        return new CaptchaResult(text, bufferedImage);
    }

    /**
     * 验证码生成结果
     *
     * @author Frank
     */
    public static class CaptchaResult {

        /**
         * 验证码
         */
        private String text;
        /**
         * 验证码图片
         */
        private BufferedImage bufferedImage;

        public CaptchaResult() {

        }

        public CaptchaResult(String text, BufferedImage bufferedImage) {
            this.text = text;
            this.bufferedImage = bufferedImage;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public void setBufferedImage(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
        }

    }

}
