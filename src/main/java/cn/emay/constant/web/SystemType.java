package cn.emay.constant.web;

public enum SystemType {

    OPER("OPER", "运营系统"),
    CLIENT("CLIENT", "客户系统"),

    ;

    private String type;

    private String name;

    SystemType(String type, String name) {
        this.setType(type);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
