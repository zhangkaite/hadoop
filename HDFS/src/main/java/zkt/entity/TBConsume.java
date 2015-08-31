package zkt.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 01004_T币消费
 **/

public class TBConsume {

	private BigInteger userID; // 用户ID
	private BigInteger destinationUserID; // 消费对象
	private String time; // 消费时间
	private String productID; // 商品编号
	private Integer productCount; // 商品数量
	private BigDecimal productPrice; // 商品单价
	private BigDecimal number; // 消费总额
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

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

}
