package candidacy.simplerestapi.data;

import candidacy.simplerestapi.model.GithubProfile;

/**
 * The user of this interface has power to query against github
 * about users and repositories
 * @author david
 *
 */
public interface GithubRepository {

	/**
	 * Returns a list of profiles with information taken from
	 * github.com. The profiles must be sorted by number of
	 * repositories (as returned by github) in descending order.
	 * The number of repositories returned is set in github.properties
	 * but remember it depends as well on github
	 * @param city
	 * @return N profiles with the information available when asking
	 * to github/search with type=users. N is the maximum between
	 * your configuration and what github gives
	 */
	public GithubProfile[] getTopProfilesByRepositories(String city);
	
	/**
	 * As long as (at the moment of the implementation of this interface)
	 * in one call you cannot retrieve information about
	 * profiles and repositories, a new call for each users is made
	 * against github to complete this information on each profile
	 * @param profiles the same profiles having completed the repositories
	 * information
	 */
	public void populateRepositories(GithubProfile[] profiles);
}
