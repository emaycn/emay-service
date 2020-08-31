package cn.emay.api.oper.base;

import cn.emay.constant.web.OperType;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.base.pojo.BaseSectionNumber;
import cn.emay.core.base.service.BaseSectionNumberService;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import cn.emay.utils.string.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础号段API
 *
 * @author chang
 */
@RestController
@Api(tags = {"基础号段"})
@RequestMapping(value = "/o/basenumber", method = RequestMethod.POST)
public class BaseSectionNumberApi {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BaseSectionNumberService baseSectionNumberService;
    @Autowired
    private UserOperLogService userOperLogService;

    /**
     * 基础号段列表
     *
     * @return
     */
    @WebAuth({ResourceEnum.BASENUMBER_VIEW})
    @ApiOperation("基础号段列表")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "number", value = "号段", dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", dataType = "string"),
            @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),})
    public SuperResult<Page<BaseSectionNumber>> list(int start, int limit, String number, String operatorCode) {
        Page<BaseSectionNumber> baseSectionNumberPage = baseSectionNumberService.findPage(start, limit, number,
                operatorCode);
        return SuperResult.rightResult(baseSectionNumberPage);
    }

    /**
     * 新增基础号段
     */
    @WebAuth({ResourceEnum.BASENUMBER_ADD})
    @ApiOperation("新增基础号段")
    @RequestMapping("/add")
    @ApiImplicitParams({@ApiImplicitParam(name = "number", value = "基础号段", required = true, dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", required = true, dataType = "string"),})
    public Result add(String number, String operatorCode) {
        Result result = check(number, operatorCode);
        if (!result.getSuccess()) {
            return result;
        }
        // 新增号段
        BaseSectionNumber baseSectionNumber = new BaseSectionNumber();
        baseSectionNumber.setIsDelete(false);
        baseSectionNumber.setNumber(number);
        baseSectionNumber.setOperatorCode(operatorCode);
        List<BaseSectionNumber> mobileList = new ArrayList<>();
        mobileList.add(baseSectionNumber);
        baseSectionNumberService.saveBatchByAutoNamed(mobileList);
        User user = WebUtils.getCurrentUser();
        String context = "新增基础号段:号段为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[]{number}), OperType.ADD);
        log.info("基础信息管理-->用户:" + user.getUsername() + "新增基础号段:号段为" + number);
        return Result.rightResult();
    }

    /**
     * 修改基础号段
     */
    @WebAuth({ResourceEnum.BASENUMBER_MODIFY})
    @ApiOperation("修改基础号段")
    @RequestMapping("/modify")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),
            @ApiImplicitParam(name = "number", value = "基础号段", required = true, dataType = "string"),
            @ApiImplicitParam(name = "operatorCode", value = "运营商编码", required = true, dataType = "string"),})
    public Result modify(Long id, String number, String operatorCode) {
        if (id == null || id < 1) {
            return Result.badResult("id错误！");
        }
        Result result = check(number, operatorCode);
        if (!result.getSuccess()) {
            return result;
        }
        // 修改号段
        BaseSectionNumber baseSectionNumber = baseSectionNumberService.findbyId(id);
        if (baseSectionNumber == null) {
            return Result.badResult("基础号段有误！");
        }
        baseSectionNumber.setIsDelete(false);
        baseSectionNumber.setNumber(number);
        baseSectionNumber.setOperatorCode(operatorCode);
        baseSectionNumberService.update(baseSectionNumber);
        User user = WebUtils.getCurrentUser();
        String context = "修改基础号段:号段为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[]{number}), OperType.MODIFY);
        log.info("基础信息管理-->用户:" + user.getUsername() + "修改基础号段:号段为" + number);
        return Result.rightResult();
    }

    /**
     * 删除基础号段
     */
    @WebAuth({ResourceEnum.BASENUMBER_DELETE})
    @ApiOperation("删除基础号段")
    @RequestMapping("/delete")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long"),})
    public Result delete(Long id) {
        if (id == null || id < 1) {
            return Result.badResult("id错误！");
        }
        // 删除号段
        BaseSectionNumber baseSectionNumber = baseSectionNumberService.findbyId(id);
        if (baseSectionNumber == null) {
            return Result.badResult("基础号段有误！");
        }
        baseSectionNumberService.delete(baseSectionNumber);
        User user = WebUtils.getCurrentUser();
        String context = "删除基础号段:号段为{0}";
        String module = "基础信息管理";
        userOperLogService.saveOperLog(module,
                MessageFormat.format(context, new Object[]{baseSectionNumber.getNumber()}), OperType.MODIFY);
        log.info("基础信息管理-->用户:" + user.getUsername() + "删除基础号段:号段为" + baseSectionNumber.getNumber());
        return Result.rightResult();
    }

    private Result check(String number, String operatorCode) {
        if (StringUtils.isEmpty(number)) {
            return Result.badResult("基础号段不能为空！");
        }
        if (!number.startsWith("1")) {
            return Result.badResult("基础号段首位不正确！");
        }
        if (3 != number.length() && 4 != number.length()) {
            return Result.badResult("基础号段长度有误！");
        }
        if (StringUtils.isEmpty(operatorCode)) {
            return Result.badResult("运营商编码不能为空！");
        }
        return Result.rightResult();
    }

}
