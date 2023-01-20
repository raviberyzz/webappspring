package ca.sunlife.web.apps.cmsservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmsResponse {
	
	private int statusCode;
	
	private String message;
	
	private Object data;
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static ResponseObjBuilder builder() {
		return new ResponseObjBuilder();
		}
	
	public static class ResponseObjBuilder {
		
		private int statusCode = 400;
		
		private String message = "Default";
		
		private Object data = null;
		
		public ResponseObjBuilder ok(Object data) {
			this.statusCode = 200;
			this.message = "Data Stored Successfully";
			this.data= data;
			return this;
			
		}
		
		//public ResponseObj build() {
			//return new ResponseObj(statusCode, message, data);
		//}
	}

}
