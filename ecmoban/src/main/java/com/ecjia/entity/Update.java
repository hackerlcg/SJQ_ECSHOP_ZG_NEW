package com.ecjia.entity;

import java.io.Serializable;

public class Update implements Serializable{
	
	private int updateFlag;//0_最新版本，无需更新;1_不是最新版本，但旧版本可以继续使用;2_必须更新才能使用
	private String version;
	private String updateUrl;//新版本下载地址
	private String updateMemo;//更新提示，可以为空
	public int getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUpdateUrl() {
		return updateUrl;
	}
	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	public String getUpdateMemo() {
		return updateMemo;
	}
	public void setUpdateMemo(String updateMemo) {
		this.updateMemo = updateMemo;
	}
	
	

}
