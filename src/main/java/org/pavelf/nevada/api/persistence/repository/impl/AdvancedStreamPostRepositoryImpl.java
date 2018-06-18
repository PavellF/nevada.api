package org.pavelf.nevada.api.persistence.repository.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.AdvancedStreamPostRepository;
import org.pavelf.nevada.api.service.PageAndSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
	private final RowMapper<StreamPost> rowMapper = 
			(ResultSet rs, int rowNum) -> {
		StreamPost sp = new StreamPost();
		sp.setId(rs.getInt(1));
		sp.setAuthorId(rs.getInt(2));
		sp.setDate(rs.getTimestamp(3).toInstant());
		sp.setContent(rs.getString(4));
		sp.setRating(rs.getInt(5));
		sp.setPopularity(rs.getInt(6));
		sp.setPriority(rs.getShort(7));
		sp.setVisibility(Visibility.valueOf(rs.getString(8)));
		sp.setCommentable(Visibility.valueOf(rs.getString(9)));
		
		Timestamp lastChange = rs.getTimestamp(10);
		if (lastChange != null) {
			sp.setLastChange(lastChange.toInstant()); 
		}
		
		Integer likeId =  rs.getInt(12);
		if (likeId != null) {
			Like like = new Like();
			//sp.setCurrentUserLike(like);
			like.setId(likeId);
			like.setRating(rs.getShort(11));
			like.setLikedById(rs.getInt(13));
			like.setDate(rs.getTimestamp(14).toInstant());
		}
		
		return sp;
	};
	
	@Autowired
	public AdvancedStreamPostRepositoryImpl(JdbcTemplate jt) {
		this.jt = jt;
	}

	@Override
	public void updateRating() {
		// TODO Auto-generated method stub

	}


}
