package zkt.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 01001_T币充值
 **/

public class TBRecharge {

	private BigInteger userID; // 用户ID
	private String time; // 充值时间
	private BigDecimal number; // 充值金额
	private String orderId;// 订单编号
	private String clientType;// 设备类型
	private String version;// 版本号

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setUserID(BigInteger userID) {
		this.userID = userID;
	}

	public BigInteger getUserID() {
		return this.userID;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return this.time;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public BigDecimal getNumber() {
		return this.number;
	}
}
