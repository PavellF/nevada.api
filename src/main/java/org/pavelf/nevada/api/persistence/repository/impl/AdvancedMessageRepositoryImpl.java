package org.pavelf.nevada.api.persistence.repository.impl;

import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.pavelf.nevada.api.persistence.repository.AdvancedMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

/**
 * Basic implementation for {@code AdvancedMessageRepository}.
 * @author Pavel F.
 * @since 1.0
 * */
@Repository
public class AdvancedMessageRepositoryImpl
		implements AdvancedMessageRepository {

	private JdbcTemplate jt;
	private Logger log;
	
	@Autowired
	public AdvancedMessageRepositoryImpl(JdbcTemplate jt, 
			LoggerFactory loggerFactory) {
		this.jt = jt;
		this.log = loggerFactory.obtain(getClass());
	}
	
	private static final String UPDATE_RATING_FIELD = 
			"UPDATE messages AS m SET m.rating = COALESCE(("
			+ "SELECT SUM(l.rating) FROM _likes AS l "
			+ "WHERE l.liked_message = m.id), 0);";
	
	@Scheduled(fixedDelay = 60000)
	@Override
	public void updateRating() {
		log.debug("Start updating all messages rating...");
		int affected = jt.update(UPDATE_RATING_FIELD);
		log.debug("End updating messages rating. Rows affected: "+affected);
	}

}
