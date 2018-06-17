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
	+ "%s LIMIT ? OFFSET ?".intern(); 
	
	private static final String POSTS = 
	"SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, "
	+ "sp.popularity, sp.priority, sp.visibility, sp.commentable, "
	+ "sp.last_change, l.rating, l.id, l.by_user, l.date "
	+ "FROM stream_post AS sp "
	+ "INNER JOIN profile_has_stream_post AS phsp "
	+ "ON phsp.stream_post_id = sp.id "
	+ "INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id "
	+ "LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id AND l.by_user = ? "
	+ "WHERE phsp.profile_id = ? %s LIMIT ? OFFSET ?".intern();
	
	private static final String POSTS_AUTHOR = 
	"SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, "+ 
	"sp.popularity, sp.priority, sp.visibility, sp.commentable, "
	+ "sp.last_change, l.rating, l.id, l.by_user, l.date "
	+ "FROM stream_post AS sp "
	+ "INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id "
	+ "LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id AND l.by_user = ? "
	+ "WHERE sp.author = ? %s LIMIT ? OFFSET ?".intern();
	
	private static final String POSTS_AUTHOR_VISIBILITY = 
	"SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, "+ 
	"sp.popularity, sp.priority, sp.visibility, sp.commentable, "
	+ "sp.last_change, l.rating, l.id, l.by_user, l.date "
	+ "FROM stream_post AS sp "
	+ "INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id "
	+ "LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id AND l.by_user = ? "
	+ "WHERE sp.author = ? AND sp.visibility IN (?, ?, ?)"
	+ " %s LIMIT ? OFFSET ?".intern();
	
	private static final String POSTS_BY_TAG = 
	"SELECT sp.id, sp.author, sp.date, sp.content, sp.rating, "+ 
	"sp.popularity, sp.priority, sp.visibility, sp.commentable, "
	+ "sp.last_change FROM stream_post AS sp "
	+ "INNER JOIN stream_post_has_tag AS spht "
	+ "ON spht.stream_post_id = sp.id AND spht.tag = ? "
	+ "INNER JOIN like_stream_post AS lsp ON lsp.stream_post_id = sp.id "
	+ "LEFT OUTER JOIN _likes AS l ON l.id = lsp.like_id "
	+ "AND l.by_user = ? %s LIMIT ? OFFSET ?".intern();
	
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

	private final String getCorrespondingColumnName(String sortBy) {
		
		if (sortBy.equals("time")) {
			return "id";
		} else if (sortBy.equals("popularity")) {
			return "popularity";
		} else if (sortBy.equals("rating")) {
			return "rating";
		}
		
		throw new IllegalArgumentException(
				"Sorting is not supported for: " + sortBy);
	}
	
	@Override
	public List<StreamPost> getAllPostsAssociatedWithProfileWithLikeInfo(
			int profileId, int requestingId, PageAndSort params) {
		if (params == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		final String sql = params.getSortBy().map((String sortingParam) -> {
			
			return String.format(POSTS, 
					String.format("ORDER BY sp.%s %s", 
							getCorrespondingColumnName(sortingParam), 
							params.getSortingDirection().orElse("ASC")));
			
		}).orElse(String.format(POSTS, ""));
		
		Object[] args = { requestingId, profileId, params.getCount(), 
				params.getStartIndex() };	
		
		return jt.query(sql, args, rowMapper);
	}

	@Override
	public List<StreamPost> getAllPostsAssociatedWithProfileWithLikeInfo(
			int profileId, int requestingId, List<Visibility> visibility,
			PageAndSort params) {
		if (params == null || visibility == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		final String sql = params.getSortBy().map((String sortingParam) -> {
			
			return String.format(POSTS_VISIBILITY, 
					String.format("ORDER BY sp.%s %s", 
							getCorrespondingColumnName(sortingParam), 
							params.getSortingDirection().orElse("ASC")));
			
		}).orElse(String.format(POSTS_VISIBILITY, ""));
		
		return jt.query(sql, (PreparedStatement ps) -> {
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
					
					ps.setInt(6, params.getCount());
					ps.setInt(7, params.getStartIndex());
				}, rowMapper);
		
	}

	@Override
	public List<StreamPost> findAllByAuthorIdWithLikeInfo(int authorId,
			List<Visibility> visibility, int requestingId, PageAndSort params) {
		if (params == null || visibility == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		final String sql = params.getSortBy().map((String sortingParam) -> {
			
			return String.format(POSTS_AUTHOR_VISIBILITY, 
					String.format("ORDER BY sp.%s %s", 
							getCorrespondingColumnName(sortingParam), 
							params.getSortingDirection().orElse("ASC")));
			
		}).orElse(String.format(POSTS_AUTHOR_VISIBILITY, ""));
		
		return jt.query(sql, (PreparedStatement ps) -> {
					ps.setInt(1, requestingId);
					ps.setInt(2, authorId);
					
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
					
					ps.setInt(6, params.getCount());
					ps.setInt(7, params.getStartIndex());
				}, rowMapper);
	}

	@Override
	public List<StreamPost> findAllByAuthorIdWithLikeInfo(int authorId,
			int requestingId, PageAndSort params) {
		if (params == null) {
			throw new IllegalArgumentException("Nulls are disallowed.");
		}
		
		final String sql = params.getSortBy().map((String sortingParam) -> {
			
			return String.format(POSTS_AUTHOR, 
					String.format("ORDER BY sp.%s %s", 
							getCorrespondingColumnName(sortingParam), 
							params.getSortingDirection().orElse("ASC")));
			
		}).orElse(String.format(POSTS_AUTHOR, ""));
		
		Object[] args = { requestingId, authorId, params.getCount(), 
				params.getStartIndex() };	
		
		return jt.query(sql, args, rowMapper);
	}

	@Override
	public List<StreamPost> findAllByTagWithLikeInfo(String tag,
			int requestingId, PageAndSort params) {
		// TODO Auto-generated method stub
		return null;
	}

}
