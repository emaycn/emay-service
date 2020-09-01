package cn.emay.core.base.dto;

import cn.emay.core.base.pojo.EmptyMobile;
import cn.emay.excel.common.schema.annotation.ExcelColumn;
import cn.emay.excel.common.schema.annotation.ExcelSheet;
import cn.emay.utils.CheckUtils;

/**
 * @description: 空号导入dto
 * @author: chang
 * @create: 2019-11-06 10:12
 **/
@ExcelSheet(readDataStartRowIndex = 1)
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
        EmptyMobile emptyMobile = new EmptyMobile(emptyMobileImport.getMobile(), false);
        return emptyMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
