import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationToken {
	
	 @JsonProperty("access_token")
	 private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
