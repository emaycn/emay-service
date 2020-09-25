package cn.emay.api.oper.base;

import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.base.dto.PortableMobileImport;
import cn.emay.core.base.pojo.PortableMobile;
import cn.emay.core.base.service.PortableMobileService;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.excel.read.ExcelReader;
import cn.emay.utils.CheckUtils;
import cn.emay.utils.FileUploadUtils;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import cn.emay.utils.string.StringUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 携号转网API
 *
 * @author chang
 */
@RestController
@Api(tags = {"携号转网"})
@RequestMapping(value = "/o/portable", method = RequestMethod.POST)
public class PortableMobileApi {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private PortableMobileService portableMobileService;
    @Resource
    private UserOperLogService userOperLogService;

    /**
     * 携号转网列表
     */
    @WebAuth({ResourceEnum.PORTABLE_VIEW})
    @ApiOperation("携号转网列表")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),})
    public SuperResult<Page<PortableMobile>> list(int start, int limit, String mobile) {
        Page<PortableMobile> portableMobiles = portableMobileService.findPage(start, limit, mobile);
        return SuperResult.rightResult(portableMobiles);
    }

    /**
     * 新增号码
     */
    @WebAuth({ResourceEnum.PORTABLE_ADD})
    @ApiOperation("新增号码")
    @RequestMapping("/add")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", required = true, dataType = "string"),})
    public Result add(String mobile, String operatorCode) {
        Result result = check(mobile, operatorCode);
        if (!result.getSuccess()) {
            return result;
        }
        // 新增号码
        PortableMobile portableMobile = new PortableMobile();
        portableMobile.setIsDelete(false);
        portableMobile.setMobile(mobile);
        portableMobile.setOperatorCode(operatorCode);
        portableMobileService.save(portableMobile);
        User user = WebUtils.getCurrentUser();
        String context = "新增基础号码:号码为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, mobile));
        log.info("基础信息管理-->用户:" + user.getUsername() + "新增号码:号码为" + mobile);
        return Result.rightResult();
    }

    /**
     * 修改携号转网
     */
    @WebAuth({ResourceEnum.PORTABLE_MODIFY})
    @ApiOperation("修改携号转网")
    @RequestMapping("/modify")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", required = true, dataType = "string"),})
    public Result modify(Long id, String mobile, String operatorCode) {
        if (id == null || id < 1) {
            return Result.badResult("id错误！");
        }
        Result result = check(mobile, operatorCode);
        if (!result.getSuccess()) {
            return result;
        }
        // 修改号码
        PortableMobile portableMobile = portableMobileService.findbyId(id);
        if (portableMobile == null) {
            return Result.badResult("号码有误！");
        }
        portableMobile.setIsDelete(false);
        portableMobile.setMobile(mobile);
        portableMobile.setOperatorCode(operatorCode);
        portableMobileService.update(portableMobile);
        User user = WebUtils.getCurrentUser();
        String context = "携号转网修改:号码为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, mobile));
        log.info("基础信息管理-->用户:" + user.getUsername() + "携号转网修改:号码为" + mobile);
        return Result.rightResult();
    }

    /**
     * 删除携号转网
     */
    @WebAuth({ResourceEnum.PORTABLE_DELETE})
    @ApiOperation("删除携号转网")
    @RequestMapping("/delete")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),})
    public Result delete(Long id) {
        if (id == null || id < 1) {
            return Result.badResult("id错误！");
        }
        // 删除号码
        PortableMobile portableMobile = portableMobileService.findbyId(id);
        if (portableMobile == null) {
            return Result.badResult("号码有误！");
        }
        portableMobileService.delete(portableMobile);
        User user = WebUtils.getCurrentUser();
        String context = "删除基础号码:号码为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, portableMobile.getMobile()));
        log.info("基础信息管理-->用户:" + user.getUsername() + "删除基础号码:号码为" + portableMobile.getMobile());
        return Result.rightResult();
    }

    /**
     * 携号转网导入
     */
    @WebAuth({ResourceEnum.PORTABLE_IMPORT})
    @RequestMapping(value = "/import", headers = "content-type=multipart/form-data")
    @ApiOperation("携号转网导入")
    public SuperResult<Integer> portableImport(@ApiParam(value = "携号转网导入文件", required = true) MultipartFile file) {
        /* 导入文件读 begin */
        FileUploadUtils.FileUpLoadResult result = FileUploadUtils.uploadFile(file, 20, ".xlsx", ".xlx");
        if (!result.isSuccess()) {
            return SuperResult.badResult(result.getErrorMessage());
        }
        List<PortableMobileImport> list = ExcelReader.readFirstSheet(result.getSaveFilePath(),
                PortableMobileImport.class);
        /* 导入文件读 end */
        List<PortableMobile> portableMobiles = new ArrayList<>();
        Set<String> mobileSet = new HashSet<>();
        list.forEach(modle -> {
            if (null != modle.toPortableMobile(modle)) {
                if (!mobileSet.contains(modle.getMobile())) {
                    mobileSet.add(modle.getMobile());
                    portableMobiles.add(modle.toPortableMobile(modle));
                }
            }
        });
        int right = portableMobiles.size();
        if (right < 1) {
            // 全错
            return SuperResult.badResult("导入号码有误！");
        }
        portableMobileService.saveBatch(portableMobiles);
        User user = WebUtils.getCurrentUser();
        log.info("user : " + user.getUsername() + "导入携号转网 ");
        userOperLogService.saveOperLog("基础信息管理", "携号转网导入");
        return SuperResult.rightResult(right);
    }

    private Result check(String mobile, String operatorCode) {
        if (StringUtils.isEmpty(mobile)) {
            return Result.badResult("号码不能为空！");
        }
        if (!CheckUtils.isMobile(mobile)) {
            return Result.badResult("手机号格式有误！");
        }
        if (StringUtils.isEmpty(operatorCode)) {
            return Result.badResult("运营商编码不能为空！");
        }
        return Result.rightResult();
    }

}
