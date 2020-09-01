package cn.emay.core.base.dto;

import cn.emay.constant.global.Province;
import cn.emay.core.base.pojo.SectionNumber;
import cn.emay.excel.common.schema.annotation.ExcelColumn;
import cn.emay.excel.common.schema.annotation.ExcelSheet;
import cn.emay.utils.string.StringUtils;

/**
 * @description: 携号转网导入dto
 * @author: chang
 * @create: 2019-11-06 10:12
 **/
@ExcelSheet(readDataStartRowIndex = 1)
public class SectionNumberImport {

    private static final String OPERATOR_CMCC = "移动";
    private static final String OPERATOR_CUCC = "联通";
    private static final String OPERATOR_CTCC = "电信";

    private static final String OPERATOR_CMCC_CODE = "CM";
    private static final String OPERATOR_CUCC_CODE = "CU";
    private static final String OPERATOR_CTCC_CODE = "CT";
    /**
     * 用户名
     */
    @ExcelColumn(index = 0, title = "号段")
    private String number;
    @ExcelColumn(index = 1, title = "运营商")
    private String operatorCode;
    @ExcelColumn(index = 2, title = "省份")
    private String provinceName;
    @ExcelColumn(index = 3, title = "城市")
    private String city;

    public SectionNumber toSectionNumber(SectionNumberImport sectionNumberImport) {
        if (StringUtils.isEmpty(sectionNumberImport.getNumber())) {
            return null;
        }
        if (!sectionNumberImport.getNumber().startsWith("1")) {
            return null;
        }
        if (sectionNumberImport.getNumber().length() != 7) {
            return null;
        }
        if (StringUtils.isEmpty(sectionNumberImport.getOperatorCode())) {
            return null;
        }
        String opcode = handleOperatorCode(sectionNumberImport.getOperatorCode());
        if (StringUtils.isEmpty(opcode)) {
            return null;
        }
        if (StringUtils.isEmpty(sectionNumberImport.getProvinceName())) {
            return null;
        }
        if (StringUtils.isEmpty(sectionNumberImport.getCity())) {
            return null;
        }
        Province province = Province.findLikeName(sectionNumberImport.getProvinceName());
        if (province == null) {
            return null;
        }
        SectionNumber sectionNumber = new SectionNumber(sectionNumberImport.getNumber(), sectionNumberImport.getCity(), opcode, province.getCode(), sectionNumberImport.getProvinceName(), false);
        return sectionNumber;
    }


    private String handleOperatorCode(String operatorCode) {
        String type = "";
        if (SectionNumberImport.OPERATOR_CMCC.equals(operatorCode)) {
            type = SectionNumberImport.OPERATOR_CMCC_CODE;
        } else if (SectionNumberImport.OPERATOR_CUCC.equals(operatorCode)) {
            type = SectionNumberImport.OPERATOR_CUCC_CODE;
        } else if (SectionNumberImport.OPERATOR_CTCC.equals(operatorCode)) {
            type = SectionNumberImport.OPERATOR_CTCC_CODE;
        }
        return type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
