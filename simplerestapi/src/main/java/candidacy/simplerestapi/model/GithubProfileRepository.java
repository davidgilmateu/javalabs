package candidacy.simplerestapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubProfileRepository {

	@JsonProperty("name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
}
