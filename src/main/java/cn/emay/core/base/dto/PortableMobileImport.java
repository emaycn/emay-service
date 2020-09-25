package cn.emay.core.base.dto;

import cn.emay.core.base.pojo.PortableMobile;
import cn.emay.excel.common.schema.annotation.ExcelColumn;
import cn.emay.excel.common.schema.annotation.ExcelSheet;
import cn.emay.utils.CheckUtils;
import cn.emay.utils.string.StringUtils;

/**
 * 携号转网导入dto
 **/
@ExcelSheet
public class PortableMobileImport {

    private static final String OPERATOR_CMCC = "移动";
    private static final String OPERATOR_CUCC = "联通";
    private static final String OPERATOR_CTCC = "电信";

    private static final String OPERATOR_CMCC_CODE = "CM";
    private static final String OPERATOR_CUCC_CODE = "CU";
    private static final String OPERATOR_CTCC_CODE = "CT";
    /**
     * 用户名
     */
    @ExcelColumn(index = 0, title = "手机号")
    private String mobile;
    @ExcelColumn(index = 1, title = "运营商")
    private String operatorCode;

    public PortableMobile toPortableMobile(PortableMobileImport portableMobileImport) {
        if (!CheckUtils.isMobile(portableMobileImport.getMobile())) {
            return null;
        }
        if (StringUtils.isEmpty(portableMobileImport.getOperatorCode())) {
            return null;
        }
        String opcode = handleOperatorCode(portableMobileImport.getOperatorCode());
        if (StringUtils.isEmpty(opcode)) {
            return null;
        }
        return new PortableMobile(portableMobileImport.getMobile(), opcode, false);
    }


    private String handleOperatorCode(String operatorCode) {
        String type = "";
        if (PortableMobileImport.OPERATOR_CMCC.equals(operatorCode)) {
            type = PortableMobileImport.OPERATOR_CMCC_CODE;
        } else if (PortableMobileImport.OPERATOR_CUCC.equals(operatorCode)) {
            type = PortableMobileImport.OPERATOR_CUCC_CODE;
        } else if (PortableMobileImport.OPERATOR_CTCC.equals(operatorCode)) {
            type = PortableMobileImport.OPERATOR_CTCC_CODE;
        }
        return type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
}
