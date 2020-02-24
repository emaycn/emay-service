package cn.emay.business.client.dto;

/**
 * 客户
 *
 * @author chang
 */
public class ClientDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	/**
	 * 名称
	 */
	private String clientName;
	/**
	 * 地址
	 */
	private String address;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
