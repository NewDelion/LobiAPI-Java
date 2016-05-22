package delion.lobiapi.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	public String cover;
	@JsonProperty("default")
	public Integer _default;
	public String description;
	public String icon;
	public String lat;
	public String lng;
	public String located_date;
	public String name;
	public String uid;
	public Long followed_date;
	public Long following_date;
}
