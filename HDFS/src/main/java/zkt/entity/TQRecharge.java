package zkt.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 01002_T卷充值
 **/

public class TQRecharge {

	private BigInteger userID; // 用户ID
	private String time; // 充值时间
	private BigDecimal number; // T劵增加数目
	private Integer type; // 充值类型---- 0:T豆结算零头转换 1：系统赠送
	private Integer TDNumber; // 消耗T豆数目
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

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return this.time;
	}

	public Integer getTDNumber() {
		return TDNumber;
	}

	public void setTDNumber(Integer tDNumber) {
		TDNumber = tDNumber;
	}

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

}
