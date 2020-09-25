package cn.emay.utils;

import cn.emay.excel.common.ExcelVersion;
import cn.emay.excel.write.ExcelWriter;
import cn.emay.excel.write.data.SheetDataGetter;
import cn.emay.excel.write.writer.SheetWriter;
import cn.emay.json.JsonHelper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * 响应工具
 *
 * @author Frank
 */
public class HttpResponseUtils {

    /**
     * 获取当前线程的Response
     *
     * @return 响应
     */
    public static HttpServletResponse getCurrentHttpResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }

    /**
     * 加入Cookie
     */
    public static void addCookie(String name, String value, int maxAge) {
        addCookie(name, value, null, null, null, 0, maxAge, false);
    }

    /**
     * 加入Cookie
     */
    public static void addCookie(String name, String value, String comment, String domain, String path, int version, int maxAge, boolean secure) {
        HttpServletResponse response = getCurrentHttpResponse();
        Cookie cookie = new Cookie(name, value);
        cookie.setComment(comment);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setSecure(secure);
        cookie.setVersion(version);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 删除Cookie
     */
    public static void deleteCookie(String name) {
        HttpServletResponse response = getCurrentHttpResponse();
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 输出字节流
     *
     * @param bytes 数据
     */
    public static void outputBytes(byte[] bytes) throws IOException {
        HttpServletResponse response = getCurrentHttpResponse();
        try (OutputStream out = response.getOutputStream()) {
            out.write(bytes);
            out.flush();
        }
    }

    /**
     * 响应Json串
     *
     * @param obj 数据
     */
    public static void outputJson(Object obj) throws IOException {
        String json = JsonHelper.toJsonString(obj);
        outputJson(json);
    }

    /**
     * 响应Json串
     *
     * @param json 数据
     */
    public static void outputJson(String json) throws IOException {
        HttpServletResponse response = getCurrentHttpResponse();
        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=utf-8");
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }

    /**
     * 以Jsonp方式输出
     *
     * @param obj      数据
     * @param callback jsonp回调函数
     */
    public static void outputJsonp(Object obj, String callback) throws IOException {
        String json = JsonHelper.toJsonString(obj);
        outputJsonp(json, callback);
    }

    /**
     * 以Jsonp方式输出
     *
     * @param json     数据
     * @param callback jsonp回调函数
     */
    public static void outputJsonp(String json, String callback) throws IOException {
        HttpServletResponse response = getCurrentHttpResponse();
        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/javascript;charset=utf-8");
            String responseStr = callback + "(eval(" + json + "))";
            out.write(responseStr.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }

    /**
     * 导出Excel
     *
     * @param response  响应
     * @param excelName Excel文件名(不包含.xls/.xlsx后缀)
     * @param version   Excel版本
     * @param handlers  写Sheet处理器
     */
    public static void outputExcelFirstSheet(HttpServletResponse response, String excelName, ExcelVersion version, SheetWriter... handlers) {
        if (handlers == null || handlers.length == 0) {
            throw new IllegalArgumentException("excel handlers is empty!");
        }
        try (OutputStream out = response.getOutputStream()) {
            checkAndFill(response, excelName, version);
            ExcelWriter.write(out, version, handlers);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 导出Excel
     *
     * @param response  响应
     * @param excelName Excel文件名(不包含.xls/.xlsx后缀)
     * @param version   Excel版本
     * @param datas     写Sheet的数据处理器（数据要实现@ExcelSheet、@ExcelColumn注解）
     */
    public static void outputExcelFirstSheet(HttpServletResponse response, String excelName, ExcelVersion version, SheetDataGetter<?>... datas) {
        if (datas == null) {
            throw new IllegalArgumentException("excel datas is empty!");
        }
        try (OutputStream out = response.getOutputStream()) {
            checkAndFill(response, excelName, version);
            ExcelWriter.write(out, version, datas);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 导出Excel
     *
     * @param response  响应
     * @param excelName Excel文件名(不包含.xls/.xlsx后缀)
     * @param version   Excel版本
     * @param datas     写Sheet的数据（数据要实现@ExcelSheet、@ExcelColumn注解）
     */
    public static void outputExcelFirstSheet(HttpServletResponse response, String excelName, ExcelVersion version, List<?>... datas) {
        if (datas == null) {
            throw new IllegalArgumentException("excel datas is empty!");
        }
        try (OutputStream out = response.getOutputStream()) {
            checkAndFill(response, excelName, version);
            ExcelWriter.write(out, version, datas);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 检测及填充数据
     *
     * @param response  响应
     * @param excelName Excel名字
     * @param version   Excel版本
     */
    public static void checkAndFill(HttpServletResponse response, String excelName, ExcelVersion version) throws UnsupportedEncodingException {
        if (response == null) {
            throw new IllegalArgumentException("response is null!");
        }
        if (excelName == null) {
            excelName = UUID.randomUUID().toString().replace("-", "");
        }
        if (version == null) {
            throw new IllegalArgumentException("excel version is null!");
        }
        // response.setContentType("application/x-download");
        if (ExcelVersion.XLS.equals(version)) {
            response.setContentType("application/vnd.ms-excel");
        } else {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + excelName + version.getSuffix());
    }

}
