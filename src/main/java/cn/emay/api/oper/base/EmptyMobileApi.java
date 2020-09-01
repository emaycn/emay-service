package cn.emay.api.oper.base;

import cn.emay.constant.web.OperType;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.base.dto.EmptyMobileImport;
import cn.emay.core.base.pojo.EmptyMobile;
import cn.emay.core.base.service.EmptyMobileService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 空号API
 *
 * @author chang
 */
@RestController
@Api(tags = {"空号"})
@RequestMapping(value = "/o/empty", method = RequestMethod.POST)
public class EmptyMobileApi {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmptyMobileService emptyMobileService;
    @Autowired
    private UserOperLogService userOperLogService;

    /**
     * 空号列表
     *
     * @return
     */
    @WebAuth({ResourceEnum.EMPTY_VIEW})
    @ApiOperation("空号列表")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),})
    public SuperResult<Page<EmptyMobile>> list(int start, int limit, String mobile) {
        Page<EmptyMobile> emptyMobilePage = emptyMobileService.findPage(start, limit, mobile);
        return SuperResult.rightResult(emptyMobilePage);
    }

    /**
     * 新增空号
     */
    @WebAuth({ResourceEnum.EMPTY_ADD})
    @ApiOperation("新增空号")
    @RequestMapping("/add")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobiles", value = "空号", required = true, dataType = "string")})
    public SuperResult<Integer> add(String mobiles) {
        if (StringUtils.isEmpty(mobiles)) {
            return SuperResult.badResult("手机号为空！");
        }
        String[] mobileArr = mobiles.split(",");
        // 新增空号
        List<String> mobileStrList = new ArrayList<>(Arrays.asList(mobileArr));
        SuperResult<List<String>> result = check(mobileStrList);
        if (!result.getSuccess()) {
            return SuperResult.badResult(result.getMessage());
        } else {
            mobileStrList.clear();
            mobileStrList.addAll((List<String>) result.getResult());
            if (mobileStrList.size() < 1) {
                return SuperResult.badResult("手机号格式不正确！");
            }
        }
        List<EmptyMobile> emptyMobiles = new ArrayList<>();
        mobileStrList.forEach(mobile -> {
            EmptyMobile emptyMobile = new EmptyMobile(mobile, false);
            emptyMobiles.add(emptyMobile);
        });
        emptyMobileService.saveBatch(emptyMobiles);
        User user = WebUtils.getCurrentUser();
        String context = "新增空号:空号个数为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[]{mobileArr.length}),
                OperType.ADD);
        log.info("基础信息管理-->用户:" + user.getUsername() + "新增空号:空号个数为：" + mobileStrList.size());
        return SuperResult.rightResult(emptyMobiles.size());
    }

    /**
     * 删除空号
     */
    @WebAuth({ResourceEnum.EMPTY_DELETE})
    @ApiOperation("删除空号")
    @RequestMapping("/delete")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),})
    public Result delete(Long id) {
        if (id == null || id < 1) {
            return Result.badResult("id错误！");
        }
        // 删除空号
        EmptyMobile emptyMobile = emptyMobileService.findbyId(id);
        if (emptyMobile == null) {
            return Result.badResult("空号有误！");
        }
        emptyMobileService.delete(emptyMobile);
        User user = WebUtils.getCurrentUser();
        String context = "删除空号:空号为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[]{emptyMobile.getMobile()}),
                OperType.MODIFY);
        log.info("基础信息管理-->用户:" + user.getUsername() + "删除基础空号:空号为" + emptyMobile.getMobile());
        return Result.rightResult();
    }

    /**
     * 空号导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @WebAuth({ResourceEnum.EMPTY_IMPORT})
    @RequestMapping(value = "/import", headers = "content-type=multipart/form-data")
    @ApiOperation("空号导入")
    public SuperResult<Integer> emptyImport(
            @ApiParam(name = "file", value = "空号导入文件", required = true) MultipartFile file) throws Exception {
        /* 导入文件读 begin */
        FileUploadUtils.FileUpLoadResult result = FileUploadUtils.uploadFile(file, 20, ".xlsx", ".xlx");
        if (!result.isSuccess()) {
            return SuperResult.badResult(result.getErrorMessage());
        }
        List<EmptyMobileImport> list = ExcelReader.readFirstSheet(result.getSaveFilePath(), EmptyMobileImport.class);
        /* 导入文件读 end */
        List<EmptyMobile> emptyMobiles = new ArrayList<>();
        Set<String> mobileSet = new HashSet<>();
        list.stream().forEach(modle -> {
            if (null != modle.toEmptyMobile(modle)) {
                // 判断重复
                if (!mobileSet.contains(modle.getMobile())) {
                    mobileSet.add(modle.getMobile());
                    emptyMobiles.add(modle.toEmptyMobile(modle));
                }
            }
        });
        int right = emptyMobiles.size();
        if (right < 1) {
            // 全错
            return SuperResult.badResult("导入号码有误！");
        }
        emptyMobileService.saveBatch(emptyMobiles);
        User user = WebUtils.getCurrentUser();
        log.info("user : " + user.getUsername() + "导入空号 ");
        userOperLogService.saveOperLog("基础信息管理", "空号导入", OperType.ADD);
        return SuperResult.rightResult(right);
    }

    private SuperResult<List<String>> check(List<String> mobiles) {
        // 校验手机号合法性
        List<String> rightMobiles = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        mobiles.forEach(mobile -> {
            if (!CheckUtils.isMobile(mobile)) {
                i.getAndIncrement();
            } else {
                rightMobiles.add(mobile);
            }
        });
        if (i.get() == mobiles.size()) {
            return SuperResult.badResult("手机号不正确！");
        }
        return SuperResult.rightResult(rightMobiles);
    }

}
