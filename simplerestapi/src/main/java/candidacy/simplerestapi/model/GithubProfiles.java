package candidacy.simplerestapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Information about a Github user profile, retrieved from github.com
 * @author david
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubProfiles {
	
	@JsonProperty("items")
	private GithubProfile[] items;

	public GithubProfile[] getItems() {
		return items;
	}

	public void setItems(GithubProfile[] items) {
		this.items = items;
	}

}
