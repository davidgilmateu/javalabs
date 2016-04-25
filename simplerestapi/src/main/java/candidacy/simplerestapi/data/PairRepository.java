package candidacy.simplerestapi.data;

import candidacy.simplerestapi.model.Pair;
import candidacy.simplerestapi.web.exceptions.PairNotFoundException;

/**
 * Retrieves and stores information on database
 * @author david
 *
 */
public interface PairRepository{

	/**
	 * Returns a pair with this email or throws a PairNotFoundException
	 * @param email of the pair to be fetched
	 * @return the pair with this email
	 * @throws PairNotFoundException if the pair cannot be found
	 */
	public Pair findByEmail(String email) throws PairNotFoundException;
	
	/**
	 * Inserts a new pair into the database. If a pair with the same
	 * email exists, it is just updated
	 * @param pair to be inserted
	 */
	public void insert(Pair pair);
	
	/**
	 * Updates the data of the pair. Obviously the email cannot be changed
	 * as it is used as the Primary Key
	 * @param pair to be updated
	 * @throws PairNotFoundException when there is no pair to be updated
	 */
	public void update(Pair pair) throws PairNotFoundException;
	
	/**
	 * Deletes the pair with this email
	 * @param email of the pair to be deleted
	 * @throws PairNotFoundException if no pair with this email exists
	 */
	public void delete(String email) throws PairNotFoundException;
		
}
