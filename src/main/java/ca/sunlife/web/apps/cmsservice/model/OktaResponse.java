package ca.sunlife.web.apps.cmsservice.model;

public class OktaResponse {

    /**
     * Token type. Example Bearer.
     */
    private String token_type;

    /**
     * Expiry of the token in seconds. Example 3600.
     */
    private String expires_in;

    /**
     * The Generated Bearer token.
     */
    private String access_token;

    /**
     * Scope of the token to be used. Refers to .read file.
     */
    private String scope;

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
    
    
}
