package com.github.ulwx.aka.dbutils.database.spring.boot;
import com.github.ulwx.aka.dbutils.tool.support.ObjectUtils;


public class Address  implements java.io.Serializable {

	private String name;/*;len:20*/
	private Integer addressId;/*;len:10*/
	private Long custormerId;/*;len:19*/

	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setAddressId(Integer addressId){
		this.addressId = addressId;
	}
	public Integer getAddressId(){
		return addressId;
	}
	public void setCustormerId(Long custormerId){
		this.custormerId = custormerId;
	}
	public Long getCustormerId(){
		return custormerId;
	}

	public String toString(){
		return  ObjectUtils.toString(this);
	}

	private static final long serialVersionUID =556120941L;

}