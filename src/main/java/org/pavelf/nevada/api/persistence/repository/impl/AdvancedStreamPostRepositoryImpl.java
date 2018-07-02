package org.pavelf.nevada.api.persistence.repository.impl;

import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.pavelf.nevada.api.persistence.repository.AdvancedStreamPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

/**
 * Basic implementation for {@code AdvancedStreamPostRepository}.
 * @author Pavel F.
 * @since 1.0
 * */
@Repository
public class AdvancedStreamPostRepositoryImpl
		implements AdvancedStreamPostRepository {

	private JdbcTemplate jt;
	private Logger log;
	
	@Autowired
	public AdvancedStreamPostRepositoryImpl(JdbcTemplate jt, 
			LoggerFactory loggerFactory) {
		this.jt = jt;
		this.log = loggerFactory.obtain(getClass());
	}
	
	private static final String UPDATE_RATING_FIELD = 
			"UPDATE stream_post AS sp SET rating = COALESCE("
			+ "(SELECT SUM(l.rating) FROM _likes AS l "
			+ "WHERE l.liked_stream_post = sp.id), 0);";

	@Override
	@Scheduled(fixedDelay = 60000)
	public void updateRating() {
		log.debug("Start updating every stream post rating...");
		int affected = jt.update(UPDATE_RATING_FIELD);
		log.debug("End updating stream post rating. Rows affected: "+affected);
	}


}
