package org.pavelf.nevada.api.persistence.repository.impl;

import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.pavelf.nevada.api.persistence.repository.AdvancedProfileReporitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

/**
 * Basic implementation for {@code AdvancedProfileReporitory}.
 * @author Pavel F.
 * @since 1.0
 * */
@Repository
public class AdvancedProfileReporitoryImpl
		implements AdvancedProfileReporitory {

	private JdbcTemplate jt;
	private Logger log;
	
	@Autowired
	public AdvancedProfileReporitoryImpl(JdbcTemplate jt, 
			LoggerFactory loggerFactory) {
		this.jt = jt;
		this.log = loggerFactory.obtain(getClass());
	}
	
	private static final String UPDATE_RATING_FIELD = 
			"UPDATE profiles AS p SET p.rating=( COALESCE("
			+ "(SELECT SUM(sp.rating) FROM stream_post AS sp "
			+ "WHERE sp.author = p.id), 0) +  COALESCE("
			+ "(SELECT SUM(m.rating) FROM messages AS m "
			+ "WHERE m.author = p.id), 0) )";
	
	private static final String UPDATE_POPULARITY = "UPDATE profiles AS p "
			+ "SET p.rating= COALESCE((SELECT COUNT(*) FROM guests AS g "
			+ "WHERE g.to_profile = p.id), 0);";
	
	@Override
	@Scheduled(fixedDelay = 600000)
	public void updateRating() {
		log.debug("Start updating every profile rating...");
		int affected = jt.update(UPDATE_RATING_FIELD);
		log.debug("End updating profile rating. Rows affected: "+affected);

	}

	@Override
	@Scheduled(fixedDelay = 6000000)
	public void updatePopularity() {
		log.debug("Start updating every profile popularity...");
		int affected = jt.update(UPDATE_POPULARITY);
		log.debug("End updating profile popularity. Rows affected: "+affected);
	}

}
