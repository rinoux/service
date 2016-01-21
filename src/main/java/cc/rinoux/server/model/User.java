package cc.rinoux.server.model;

import javax.jws.soap.SOAPBinding.Use;

public class User {
    private Integer uid;
    private String telNo;
    private String psw;
    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }
    public String getTelNo() {
        return telNo;
    }
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    public String getPsw() {
        return psw;
    }
    public void setPsw(String psw) {
        this.psw = psw;
    }
    
    public User(Integer uid, String telNo, String psw) {
    	super();
    	this.uid = uid;
    	this.telNo=telNo;
    	this.psw=psw;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
}