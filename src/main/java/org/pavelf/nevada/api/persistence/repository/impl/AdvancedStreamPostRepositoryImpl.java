package org.pavelf.nevada.api.persistence.repository.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.pavelf.nevada.api.domain.MessageDTO;
import org.pavelf.nevada.api.persistence.domain.Like;
import org.pavelf.nevada.api.persistence.domain.Message;
import org.pavelf.nevada.api.persistence.domain.StreamPost;
import org.pavelf.nevada.api.persistence.domain.Visibility;
import org.pavelf.nevada.api.persistence.repository.AdvancedStreamPostRepository;
import org.pavelf.nevada.api.service.PageAndSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
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

	private static final String POSTS_VISIBILITY = 
	"SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, "
	+ "sp.popularity, sp.priority, sp.visibility, sp.commentable, "
	+ "sp.last_change, l.rating, l.id, l.by_user, l.date "
	+ "FROM stream_post AS sp "
	+ "INNER JOIN profile_has_stream_post AS phsp "
	+ "ON phsp.stream_post_id = sp.id "
	+ "INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id "
	+ "LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id AND l.by_user = ? "
	+ "WHERE phsp.profile_id = ? AND sp.visibility IN (?, ?, ?) "
	+ "ORDER BY sp.%s %s LIMIT ? OFFSET ?"; 
	
	private static final String POSTS = 
	"SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, "
	+ "sp.popularity, sp.priority, sp.visibility, sp.commentable, "
	+ "sp.last_change, l.rating, l.id, l.by_user, l.date "
	+ "FROM stream_post AS sp "
	+ "INNER JOIN profile_has_stream_post AS phsp "
	+ "ON phsp.stream_post_id = sp.id "
	+ "INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id "
	+ "LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id AND l.by_user = ? "
	+ "WHERE phsp.profile_id = ? ORDER BY sp.%s %s LIMIT ? OFFSET ?"; 
	
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
			sp.setCurrentUserLike(like);
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

	@Override
	public List<StreamPost> getAllPostsAssociatedWithProfileWithLikeInfo(
			int profileId, int requestingId, Pageable params) {
		if (params == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		Object[] args = { requestingId, profileId, 15, 0 };
		return jt.query(String.format(POSTS, "id", "ASC"), args, rowMapper);
	}

	@Override
	public List<StreamPost> getAllPostsAssociatedWithProfileWithLikeInfo(
			int profileId, int requestingId, List<Visibility> visibility,
			Pageable params) {
		if (params == null || visibility == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		return jt.query(String.format(POSTS_VISIBILITY, "id", "ASC"), 
				(PreparedStatement ps) -> {
					ps.setInt(1, requestingId);
					ps.setInt(2, profileId);
					
					List<String> lvl = visibility.stream()
							.map(Visibility::toString)
							.limit(Visibility.values().length)
							.collect(Collectors.toList());
					
					for (int i = 3; i < 6; i++) {
						
						int index = i - 3; 
						
						if (index < lvl.size()) {
							ps.setString(i, lvl.get(index));
						} else {
							ps.setNull(i, java.sql.Types.NULL);
						}
						
					}
					
					ps.setInt(6, 15);
					ps.setInt(7, 0);
				}, rowMapper);
		
	}

}
