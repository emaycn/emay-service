package cn.emay.api.oper.base;

import cn.emay.constant.global.Province;
import cn.emay.constant.global.ProvinceDTO;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.base.dto.SectionNumberImport;
import cn.emay.core.base.pojo.SectionNumber;
import cn.emay.core.base.service.SectionNumberService;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.excel.read.ExcelReader;
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
 * 详细号段API
 *
 * @author chang
 */
@RestController
@Api(tags = {"详细号段"})
@RequestMapping(value = "/o/sectionnumber", method = RequestMethod.POST)
public class SectionNumberApi {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private SectionNumberService sectionNumberService;
    @Resource
    private UserOperLogService userOperLogService;

    /**
     * 详细号段列表
     */
    @WebAuth({ResourceEnum.NUMBER_VIEW})
    @ApiOperation("详细号段列表")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "number", value = "号段", dataType = "string"),
            @ApiImplicitParam(name = "provinceCode", value = "省份编码", dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", dataType = "string"),
            @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),})
    public SuperResult<Page<SectionNumber>> list(int start, int limit, String number, String operatorCode,
                                                 String provinceCode) {
        Page<SectionNumber> sectionNumberPage = sectionNumberService.findPage(start, limit, number, operatorCode,
                provinceCode);
        return SuperResult.rightResult(sectionNumberPage);
    }

    /**
     * 新增详细号段
     */
    @WebAuth({ResourceEnum.NUMBER_ADD})
    @ApiOperation("新增详细号段")
    @RequestMapping("/add")
    @ApiImplicitParams({@ApiImplicitParam(name = "number", value = "详细号段", required = true, dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "provinceCode", value = "省份编码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, dataType = "string"),})
    public Result add(String number, String operatorCode, String provinceCode, String city) {
        Result result = check(number, operatorCode);
        if (!result.getSuccess()) {
            return result;
        }
        // 新增号段
        SectionNumber sectionNumber = new SectionNumber();
        sectionNumber.setIsDelete(false);
        sectionNumber.setNumber(number);
        sectionNumber.setOperatorCode(operatorCode);
        sectionNumber.setProvinceCode(provinceCode);
        String provinceName = Province.findNameByCode(provinceCode);
        sectionNumber.setProvinceName(provinceName);
        sectionNumber.setCity(city);
        sectionNumberService.save(sectionNumber);
        User user = WebUtils.getCurrentUser();
        String context = "新增详细号段:号段为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, number));
        log.info("基础信息管理-->用户:" + user.getUsername() + "新增详细号段:号段为" + number);
        return Result.rightResult();
    }

    /**
     * 修改详细号段
     */
    @WebAuth({ResourceEnum.NUMBER_MODIFY})
    @ApiOperation("修改详细号段")
    @RequestMapping("/modify")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "number", value = "详细号段", required = true, dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "provinceCode", value = "省份编码", required = true, dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, dataType = "string"),})
    public Result modify(Long id, String number, String operatorCode, String provinceCode, String city) {
        if (id == null || id < 1) {
            return Result.badResult("id错误！");
        }
        Result result = check(number, operatorCode);
        if (!result.getSuccess()) {
            return result;
        }
        // 修改详细号段
        SectionNumber sectionNumber = sectionNumberService.findbyId(id);
        if (sectionNumber == null) {
            return Result.badResult("详细号段有误！");
        }
        sectionNumber.setIsDelete(false);
        sectionNumber.setNumber(number);
        sectionNumber.setOperatorCode(operatorCode);
        sectionNumber.setProvinceCode(provinceCode);
        String provinceName = Province.findNameByCode(provinceCode);
        sectionNumber.setProvinceName(provinceName);
        sectionNumber.setCity(city);
        sectionNumberService.update(sectionNumber);
        User user = WebUtils.getCurrentUser();
        String context = "修改详细号段:号段为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, number));
        log.info("基础信息管理-->用户:" + user.getUsername() + "修改详细号段:号段为" + number);
        return Result.rightResult();
    }

    /**
     * 删除详细号段
     */
    @WebAuth({ResourceEnum.NUMBER_DELETE})
    @ApiOperation("删除详细号段")
    @RequestMapping("/delete")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),})
    public Result delete(Long id) {
        if (id == null || id < 1) {
            return Result.badResult("id错误！");
        }
        // 删除详细号段
        SectionNumber sectionNumber = sectionNumberService.findbyId(id);
        if (sectionNumber == null) {
            return Result.badResult("详细号段有误！");
        }
        sectionNumberService.delete(sectionNumber);
        User user = WebUtils.getCurrentUser();
        String context = "删除详细号段:号段为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, sectionNumber.getNumber()));
        log.info("基础信息管理-->用户:" + user.getUsername() + "删除详细号段:号段为" + sectionNumber.getNumber());
        return Result.rightResult();
    }

    /**
     * 获取省份列表
     */
    @ApiOperation("获取省份列表")
    @RequestMapping("/provincelist")
    public SuperResult<List<ProvinceDTO>> provinceList() {
        return SuperResult.rightResult(Province.toDtos());
    }

    private Result check(String number, String operatorCode) {
        if (StringUtils.isEmpty(number)) {
            return Result.badResult("详细号段不能为空！");
        }
        if (!number.startsWith("1")) {
            return Result.badResult("详细号段首位不正确！");
        }
        if (7 != number.length()) {
            return Result.badResult("详细号段长度必须为7位！");
        }
        if (StringUtils.isEmpty(operatorCode)) {
            return Result.badResult("运营商编码不能为空！");
        }
        return Result.rightResult();
    }

    /**
     * 详细号段导入
     */
    @WebAuth({ResourceEnum.NUMBER_IMPORT})
    @RequestMapping(value = "/import", headers = "content-type=multipart/form-data")
    @ApiOperation("详细号段导入")
    public SuperResult<Integer> sectionNumberImport(@ApiParam(value = "详细号段导入文件", required = true) MultipartFile file) {
        /* 导入文件读 begin */
        FileUploadUtils.FileUpLoadResult result = FileUploadUtils.uploadFile(file, 20, ".xlsx", ".xlx");
        if (!result.isSuccess()) {
            return SuperResult.badResult(result.getErrorMessage());
        }
        List<SectionNumberImport> list = ExcelReader.readFirstSheet(result.getSaveFilePath(),
                SectionNumberImport.class);
        /* 导入文件读 end */
        List<SectionNumber> sectionNumbers = new ArrayList<>();
        Set<String> mobileSet = new HashSet<>();
        list.forEach(modle -> {
            if (null != modle.toSectionNumber(modle)) {
                if (!mobileSet.contains(modle.getNumber())) {
                    mobileSet.add(modle.getNumber());
                    sectionNumbers.add(modle.toSectionNumber(modle));
                }
            }
        });
        int right = sectionNumbers.size();
        if (right < 1) {
            // 全错
            return SuperResult.badResult("导入号码有误！");
        }
        sectionNumberService.saveBatch(sectionNumbers);
        User user = WebUtils.getCurrentUser();
        log.info("user : " + user.getUsername() + "导入详细号段 ");
        userOperLogService.saveOperLog("基础信息管理", "详细号段导入");
        return SuperResult.rightResult(right);
    }
}
