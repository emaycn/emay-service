package cn.emay.business.base.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 携号转网
 * 
 * @author Frank
 *
 */
@Entity
@Table(name = "base_mobile_portable")
public class PortableMobile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 手机号码
	 */
	@Column(name = "mobile")
	private String mobile;
	/**
	 * 运营商CODE[CM-移动，CU-联通，CT-电信]
	 */
	@Column(name = "operator_code")
	private String operatorCode;
	/**
	 * 是否删除 0:未删除,1:已删除
	 */
	@Column(name = "is_delete")
	private Boolean isDelete;

	public PortableMobile() {
	}

	public PortableMobile(String mobile, String operatorCode, Boolean isDelete) {
		this.mobile = mobile;
		this.operatorCode = operatorCode;
		this.isDelete = isDelete;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

}
