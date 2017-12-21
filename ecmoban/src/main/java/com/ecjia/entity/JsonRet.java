package com.ecjia.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsonRet<Entity> implements Serializable {

	private static final long serialVersionUID = -6662556845549122666L;

	@SerializedName("status")
	private boolean status;
	@SerializedName("data")
	private Entity data;
	@SerializedName("errortext")
	private String errortext;
	//错误代码
	private int errorno;

	private String totalGoods="";
	private long qryTime=0L;

	public String getErrortext() {
		return errortext;
	}

	public void setErrortext(String errortext) {
		this.errortext = errortext;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Entity getData() {
		return data;
	}

	public void setData(Entity data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getErrorno() {
		return errorno;
	}

	public void setErrorno(int errorno) {
		this.errorno = errorno;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public boolean isStatus() {
		return status;
	}

	public String getTotalGoods() {
		return totalGoods;
	}

	public void setTotalGoods(String totalGoods) {
		this.totalGoods = totalGoods;
	}

	public long getQryTime() {
		return qryTime;
	}

	public void setQryTime(long qryTime) {
		this.qryTime = qryTime;
	}
}
