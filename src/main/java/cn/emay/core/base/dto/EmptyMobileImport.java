package cn.emay.core.base.dto;

import cn.emay.core.base.pojo.EmptyMobile;
import cn.emay.excel.common.schema.annotation.ExcelColumn;
import cn.emay.excel.common.schema.annotation.ExcelSheet;
import cn.emay.utils.CheckUtils;

/**
 * 空号导入dto
 **/
@ExcelSheet
public class EmptyMobileImport {

    /**
     * 用户名
     */
    @ExcelColumn(index = 0, title = "手机号")
    private String mobile;

    public EmptyMobile toEmptyMobile(EmptyMobileImport emptyMobileImport) {
        if (!CheckUtils.isMobile(emptyMobileImport.getMobile())) {
            return null;
        }
        return new EmptyMobile(emptyMobileImport.getMobile(), false);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
