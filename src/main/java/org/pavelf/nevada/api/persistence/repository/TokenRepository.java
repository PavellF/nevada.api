package org.pavelf.nevada.api.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.pavelf.nevada.api.persistence.domain.Token;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Declares methods used to obtain and modify token related information which is 
 * stored in the database.
 * @since 1.0
 * @author Pavel F.
 */
public interface TokenRepository extends JpaRepository<Token, Integer> {

	public List<Token> findAllByBelongsToProfile(int profileId);
	
	public List<Token> findAllByIssuedBy(int applicationId);
	
	/**
     * Finds token and its associated profile by using the token string as a search criteria.
     * @param token string representing token ,
     * @return  token with owner.
     */
	@Transactional(readOnly = true)
	@EntityGraph(value = "graph.tokens.profile", type = EntityGraphType.LOAD)
	public Optional<Token> findByToken(String token);
	
}
