package cn.emay.core.system.pojo;

import javax.persistence.*;

/**
 * 资源
 *
 * @author frank
 */
@Entity
@Table(name = "system_resource")
public class Resource implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 资源CODE
     */
    @Column(name = "resource_code")
    private String resourceCode;
    /**
     * 资源名称
     */
    @Column(name = "resource_name")
    private String resourceName;
    /**
     * 资源类型 SystemType
     */
    @Column(name = "resource_type")
    private String resourceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
