package cc.rinoux.server.model;

public class Cup {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column cup.cid
	 * @mbggenerated
	 */
	private Integer cid;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column cup.mac
	 * @mbggenerated
	 */
	private String mac;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column cup.ip
	 * @mbggenerated
	 */
	private String ip;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column cup.user
	 * @mbggenerated
	 */
	private Integer user;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column cup.cid
	 * @return  the value of cup.cid
	 * @mbggenerated
	 */
	public Integer getCid() {
		return cid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column cup.cid
	 * @param cid  the value for cup.cid
	 * @mbggenerated
	 */
	public void setCid(Integer cid) {
		this.cid = cid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column cup.mac
	 * @return  the value of cup.mac
	 * @mbggenerated
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column cup.mac
	 * @param mac  the value for cup.mac
	 * @mbggenerated
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column cup.ip
	 * @return  the value of cup.ip
	 * @mbggenerated
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column cup.ip
	 * @param ip  the value for cup.ip
	 * @mbggenerated
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column cup.user
	 * @return  the value of cup.user
	 * @mbggenerated
	 */
	public Integer getUser() {
		return user;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column cup.user
	 * @param user  the value for cup.user
	 * @mbggenerated
	 */
	public void setUser(Integer user) {
		this.user = user;
	}
}