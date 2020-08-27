package cn.emay.constant.web;

/**
 * 资源枚举值
 *
 * @author Frank
 */
public enum ResourceEnum {

    /**
     * 角色查询
     */
    ROLE_VIEW("ROLE_VIEW", "角色查询", SystemType.OPER),
    /**
     * 角色添加
     */
    ROLE_ADD("ROLE_ADD", "角色添加", SystemType.OPER),
    /**
     * 角色删除
     */
    ROLE_DELETE("ROLE_DELETE", "角色删除", SystemType.OPER),
    /**
     * 角色修改
     */
    ROLE_MODIFY("ROLE_MODIFY", "角色修改", SystemType.OPER),

    /**
     * 用户查询
     */
    USER_VIEW("USER_VIEW", "用户查询", SystemType.OPER),
    /**
     * 用户添加
     */
    USER_ADD("USER_ADD", "用户添加", SystemType.OPER),
    /**
     * 用户删除
     */
    USER_DELETE("USER_DELETE", "用户删除", SystemType.OPER),
    /**
     * 用户修改
     */
    USER_MODIFY("USER_MODIFY", "用户修改", SystemType.OPER),
    /**
     * 用户操作
     */
    USER_OPER("USER_OPER", "用户操作", SystemType.OPER),

    /**
     * 部门查询
     */
    DEPARTMENT_VIEW("DEPARTMENT_VIEW", "部门查询", SystemType.OPER),
    /**
     * 部门添加
     */
    DEPARTMENT_ADD("DEPARTMENT_ADD", "部门添加", SystemType.OPER),
    /**
     * 部门删除
     */
    DEPARTMENT_DELETE("DEPARTMENT_DELETE", "部门删除", SystemType.OPER),
    /**
     * 部门修改
     */
    DEPARTMENT_MODIFY("DEPARTMENT_MODIFY", "部门修改", SystemType.OPER),

    /**
     * 日志查询
     */
    LOG_VIEW("LOG_VIEW", "日志查询", SystemType.OPER),

    /**
     * 客户管理
     */
    CLIENT_VIEW("CLIENT_VIEW", "客户管理", SystemType.OPER),
    /**
     * 新增客户
     */
    CLIENT_ADD("CLIENT_ADD", "新增客户", SystemType.OPER),
    /**
     * 客户修改
     */
    CLIENT_MODIFY("CLIENT_MODIFY", "客户修改", SystemType.OPER),
    /**
     * 客户賬務明細
     */
    CLIENT_CHARGE("CLIENT_CHARGE", "账务明细", SystemType.OPER),
    /**
     * 充值扣费
     */
    RECHARGE_CHARGE("RECHARGE_CHARGE", "充值扣费", SystemType.OPER),

    /**
     * 空号管理
     */
    EMPTY_VIEW("EMPTY_VIEW", "空号管理", SystemType.OPER),
    /**
     * 新增空号
     */
    EMPTY_ADD("EMPTY_ADD", "新增空号", SystemType.OPER),
    /**
     * 删除空号
     */
    EMPTY_DELETE("EMPTY_DELETE", "删除空号", SystemType.OPER),
    /**
     * 空号导入
     */
    EMPTY_IMPORT("EMPTY_IMPORT", "空号导入", SystemType.OPER),
    /**
     * 携号转网管理
     */
    PORTABLE_VIEW("PORTABLE_VIEW", "携号转网", SystemType.OPER),
    /**
     * 携号转网新增
     */
    PORTABLE_ADD("PORTABLE_ADD", "携号转网新增", SystemType.OPER),
    /**
     * 携号转网修改
     */
    PORTABLE_MODIFY("PORTABLE_MODIFY", "携号转网修改", SystemType.OPER),
    /**
     * 携号转网删除
     */
    PORTABLE_DELETE("PORTABLE_DELETE", "携号转网删除", SystemType.OPER),
    /**
     * 携号转网导入
     */
    PORTABLE_IMPORT("PORTABLE_IMPORT", "携号转网导入", SystemType.OPER),
    /**
     * 基础号段管理
     */
    BASENUMBER_VIEW("BASENUMBER_VIEW", "基础号段", SystemType.OPER),
    /**
     * 新增基础号段
     */
    BASENUMBER_ADD("BASENUMBER_ADD", "新增基础号段", SystemType.OPER),
    /**
     * 修改基础号段
     */
    BASENUMBER_MODIFY("BASENUMBER_MODIFY", "修改基础号段", SystemType.OPER),
    /**
     * 删除基础号段
     */
    BASENUMBER_DELETE("BASENUMBER_DELETE", "删除基础号段", SystemType.OPER),
    /**
     * 详细号段管理
     */
    NUMBER_VIEW("NUMBER_VIEW", "详细号段", SystemType.OPER),
    /**
     * 详细号段新增
     */
    NUMBER_ADD("NUMBER_ADD", "详细号段新增", SystemType.OPER),
    /**
     * 详细号段修改
     */
    NUMBER_MODIFY("NUMBER_MODIFY", "详细号段修改", SystemType.OPER),
    /**
     * 详细号段删除
     */
    NUMBER_DELETE("NUMBER_DELETE", "详细号段删除", SystemType.OPER),
    /**
     * 详细号段导入
     */
    NUMBER_IMPORT("NUMBER_IMPORT", "详细号段导入", SystemType.OPER),


    /**
     * 应用列表
     */
    APP_VIEW("APP_VIEW", "应用列表", SystemType.OPER),
    /**
     * 应用新增
     */
    APP_ADD("APP_ADD", "应用新增", SystemType.OPER),
    /**
     * 应用设置单价
     */
    APP_PRICE("APP_PRICE", "应用设置单价", SystemType.OPER),
    /**
     * 应用起停
     */
    APP_ONOFF("APP_ONOFF", "应用起停", SystemType.OPER),


    /**
     * 短信详情列表
     */
    MESSAGE_VIEW("MESSAGE_VIEW", "短信详情列表", SystemType.OPER),

    /**
     * 角色查询
     */
    CLIENTROLE_VIEW("CLIENTROLE_VIEW", "客户角色查询", SystemType.OPER),
    /**
     * 角色添加
     */
    CLIENTROLE_ADD("CLIENTROLE_ADD", "客户角色添加", SystemType.OPER),
    /**
     * 角色删除
     */
    CLIENTROLE_DELETE("CLIENTROLE_DELETE", "客户角色删除", SystemType.OPER),
    /**
     * 角色修改
     */
    CLIENTROLE_MODIFY("CLIENTROLE_MODIFY", "客户角色修改", SystemType.OPER),

    /**
     * 客户用户查询
     */
    CLIENTUSER_VIEW("CLIENTUSER_VIEW", "客户用户查询", SystemType.OPER),
    /**
     * 客户用户添加
     */
    CLIENTUSER_ADD("CLIENTUSER_ADD", "客户用户添加", SystemType.OPER),
    /**
     * 客户用户删除
     */
    CLIENTUSER_DELETE("CLIENTUSER_DELETE", "客户用户删除", SystemType.OPER),
    /**
     * 客户用户修改
     */
    CLIENTUSER_MODIFY("CLIENTUSER_MODIFY", "客户用户修改", SystemType.OPER),
    /**
     * 客户用户操作
     */
    CLIENTUSER_OPER("CLIENTUSER_OPER", "客户用户操作", SystemType.OPER),


    /**
     * 客户端-用户查询
     */
    CLIENT_USER_VIEW("CLIENT_USER_VIEW", "用户查询", SystemType.CLIENT),
    /**
     * 客户端-应用列表
     */
    CLIENT_APP_VIEW("CLIENT_APP_VIEW", "应用列表", SystemType.CLIENT),
    /**
     * 客户端-公司信息查询
     */
    CLIENT_COMMPANY_INFO("CLIENT_COMMPANY_INFO", "公司信息查询", SystemType.CLIENT),
    /**
     * 客户端-短信详情列表
     */
    CLIENT_MESSAGE_VIEW("CLIENT_MESSAGE_VIEW", "短信详情列表", SystemType.CLIENT),

    ;

    /**
     * 编码
     */
    private String code;
    /**
     * 描述
     */
    private String desc;
    /**
     * 所属系统
     */
    private SystemType system;

    ResourceEnum(String code, String desc, SystemType system) {
        this.code = code;
        this.desc = desc;
        this.system = system;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SystemType getSystem() {
        return system;
    }

    public void setSystem(SystemType system) {
        this.system = system;
    }

}
