package ca.sunlife.web.apps.cmsservice.model;

public class OktaResponse {

    /**
     * Token type. Example Bearer.
     */
    private String tokenType;

    /**
     * Expiry of the token in seconds. Example 3600.
     */
    private String expiresIn;

    /**
     * The Generated Bearer token.
     */
    private String accessToken;

    /**
     * Scope of the token to be used. Refers to .read file.
     */
    private String scope;


	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
