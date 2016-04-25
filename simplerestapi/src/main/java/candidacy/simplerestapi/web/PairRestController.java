package candidacy.simplerestapi.web;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import candidacy.simplerestapi.data.PairRepository;
import candidacy.simplerestapi.model.Pair;

/**
 * Controller to accept and process the request for handling pairs
 * @author david
 *
 */
@RestController
@RequestMapping("/rest")
public class PairRestController{

	@Autowired
	private PairRepository pairRepository;
	
	public PairRestController() {
		super();
	}
	
	public PairRestController(PairRepository pairRepository) {
		super();
		this.pairRepository = pairRepository;
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes = "application/json")	
	public @ResponseBody void newPair(@RequestBody Pair pair) throws SQLException{
		pairRepository.insert(pair);
	}
	
}
