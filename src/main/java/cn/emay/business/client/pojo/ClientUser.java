package cn.emay.business.client.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客户
 *
 * @author chang
 */
@Entity
@Table(name = "client_user_assign")
public class ClientUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 客戶id
     */
    @Column(name = "client_id")
    private Long clientId;
    /**
     * 用戶id
     */
    @Column(name = "user_id")
    private Long userId;

    public ClientUser() {

    }

    public ClientUser(Long clientId, Long userId) {
        this.clientId = clientId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
