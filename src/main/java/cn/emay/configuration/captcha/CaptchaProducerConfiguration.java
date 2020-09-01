package cn.emay.configuration.captcha;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Properties;

/**
 * 图片验证码配置
 *
 * @author Frank
 */
@Configuration
@ConfigurationProperties(prefix = "captcha")
@Order(0)
public class CaptchaProducerConfiguration {

    private String noise;
    private String chars;
    private String length;
    private String width;
    private String height;

    @Bean
    public DefaultKaptcha defaultKaptcha() {
        Properties properties = new Properties();

        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, noise);
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, chars);
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, length);
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, width);
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, height);

        // properties.setProperty(Constants.KAPTCHA_SESSION_KEY, null);
        // properties.setProperty(Constants.KAPTCHA_SESSION_DATE, null);
        // properties.setProperty(Constants.KAPTCHA_SESSION_CONFIG_KEY, null);
        // properties.setProperty(Constants.KAPTCHA_SESSION_CONFIG_DATE, null);
        // properties.setProperty(Constants.KAPTCHA_BORDER, null);
        // properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, null);
        // properties.setProperty(Constants.KAPTCHA_BORDER_THICKNESS, null);
        // properties.setProperty(Constants.KAPTCHA_NOISE_COLOR, null);
        // properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, null);
        // properties.setProperty(Constants.KAPTCHA_PRODUCER_IMPL, null);
        // properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_IMPL, null);
        // properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, null);
        // properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, null);
        // properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, null);
        // properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, null);
        // properties.setProperty(Constants.KAPTCHA_WORDRENDERER_IMPL, null);
        // properties.setProperty(Constants.KAPTCHA_BACKGROUND_IMPL, null);
        // properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM, null);
        // properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO, null);

        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);

        return kaptcha;
    }

    public String getNoise() {
        return noise;
    }

    public void setNoise(String noise) {
        this.noise = noise;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
