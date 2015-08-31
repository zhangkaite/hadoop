package zkt.entity;

import org.apache.solr.client.solrj.beans.Field;

public class UserEntity {
    @Field
	private String type;
    @Field
	private String userName;
  
	private String mobile;
    @Field
	private String email;
	private String password;
    @Field
	private String adminId;
    @Field
    private String reason;
    @Field
    private String nickName;
    @Field
    private String sex;
    @Field
    private String userPhoto;
    @Field
    private String key;
    @Field
    private String enrollTerminal;
    @Field
    private String time;
    @Field
	private String ttnum;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getEnrollTerminal() {
		return enrollTerminal;
	}
	public void setEnrollTerminal(String enrollTerminal) {
		this.enrollTerminal = enrollTerminal;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTtnum() {
		return ttnum;
	}
	public void setTtnum(String ttnum) {
		this.ttnum = ttnum;
	}
	
	
	
	
}
