package zkt.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 01005_T卷消费
 **/

public class TQConsume {

	private BigInteger userID; // 用户ID
	private String time; // 消费时间
	private BigDecimal number; // 消费总额

	private BigInteger destinationUserID; // 消费对象
	private String productID; // 商品编号
	private Integer productCount; // 商品数量
	private BigDecimal productPrice; // 商品单价
	private String equipID; // 道具编号
	private Integer userType; // 消费类型

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

	public void setDestinationUserID(BigInteger destinationUserID) {
		this.destinationUserID = destinationUserID;
	}

	public BigInteger getDestinationUserID() {
		return this.destinationUserID;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return this.time;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductID() {
		return this.productID;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public Integer getProductCount() {
		return this.productCount;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getProductPrice() {
		return this.productPrice;
	}

	public void setEquipID(String equipID) {
		this.equipID = equipID;
	}

	public String getEquipID() {
		return this.equipID;
	}

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

}
