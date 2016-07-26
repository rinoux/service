package cc.rinoux.server.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * ������ϢPOJO/JAXB
 * code�������룬0Ϊ�ɹ���1Ϊʧ��
 * msg��������Ϣ
 * data�����ص����ݣ�json��ʽ
 * @author Administrator
 *
 */
@XmlRootElement

public class RespModel {
	public int code;
	public String msg;
	public List<?> data;
	public Object object;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public RespModel(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public RespModel(int code, String msg, List<?> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public RespModel(int code, String msg, Object object) {
		super();
		this.code = code;
		this.msg = msg;
		this.object = object;
	}
	public RespModel() {
	}
}
