package com.example.join.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.join.entity.Like;
import com.example.join.repository.LikeRepository;
import com.example.join.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	
	@Mock
	private LikeRepository likeRepository;
	
	@InjectMocks
	private PostService postService;
	
    // ──────────────────────────────────────────
    // toggleLike 테스트
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("좋아요가 없으면 새로 추가된다")
	void toggleLike_shouldAddLike_whenNotExists() {
		// given
		when(likeRepository.findByTargetIdAndTargetTypeAndUserId(1L, "POST", "user1"))
				.thenReturn(Optional.empty());
		
		// when
		postService.toggleLike(1L, "POST", "user1");
		
		// then
		verify(likeRepository, times(1)).save(any(Like.class));
		verify(likeRepository, never()).delete(any(Like.class));
	}
	
	@Test
	@DisplayName("좋아요가 이미 있으면 삭제된다")
	void toggleLike_shouldDeleteLike_whenAlreadyExixtx() {
		// given
		Like existingLike = new Like(1L, "POST", "user1");
		when(likeRepository.findByTargetIdAndTargetTypeAndUserId(1L, "POST", "user1"))
				.thenReturn(Optional.of(existingLike));
		
		// when
		postService.toggleLike(1L, "POST", "user1");
		
		// then
		verify(likeRepository, times(1)).delete(existingLike);
		verify(likeRepository, never()).save(any(Like.class));
	}
	
	@Test
	@DisplayName("댓글 좋아요 토글도 targerType COMMENT로 정상 작동한다")
	void toggleLike_comment_shouldAddLike() {
		// given
		when(likeRepository.findByTargetIdAndTargetTypeAndUserId(5L, "COMMENT", "user1"))
				.thenReturn(Optional.empty());
		
		// when
		postService.toggleLike(5L, "COMMENT", "user1");
		
		// then
		verify(likeRepository, times(1)).save(any(Like.class));
	}
	
	@Test
	@DisplayName("대댓글 좋아요 토글도 tartgetType REPLY로 정상 작동한다")
	void toggleLike_reply_shouldAddLike() {
		// given
		when(likeRepository.findByTargetIdAndTargetTypeAndUserId(9L, "REPLY", "user1"))
				.thenReturn(Optional.empty());
		
		// when
		postService.toggleLike(9L, "REPLY", "user1");
		
		// then
		verify(likeRepository, times(1)).save(any(Like.class));
	}
	
    // ──────────────────────────────────────────
    // getLikeCount 테스트
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("좋아요 개수를 정확히 반환한다")
	void getLikeCount_ShouldReturnCorrectCount() {
		// given
		when(likeRepository.countByTargetIdAndTargetType(1L, "POST"))
				.thenReturn(3L);
		
		// when
		int count = postService.getLikeCount(1L, "POST");
		
		// then
		assertThat(count).isEqualTo(3);
	}
	
	@Test
	@DisplayName("좋아요가 없으면 0을 반환한다")
	void getLikeCount_CouldReturnZero_whenNoLikes() {
		// given
		when(likeRepository.countByTargetIdAndTargetType(99L, "POST"))
				.thenReturn(0L);
		
		// when
		int count = postService.getLikeCount(99L, "POST");
				
		// then
		assertThat(count).isEqualTo(0);
	}
	
    // ──────────────────────────────────────────
    // isLiked 테스트
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("좋아요를 했으면 true를 반환한다")
	void isLiked_shouldReturnTrue_whenLikeExists() {
		// given
		when(likeRepository.findByTargetIdAndTargetTypeAndUserId(1L, "POST", "user1"))
				.thenReturn(Optional.of(new Like(1L, "POST", "user1")));
				
		// when
		boolean result = postService.isLiked(1L, "POST", "user1");
		
		// then
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("좋아요를 안 했으면 false를 반환한다")
	void isLiked_sholdReturnFalse_whenLikeNotExists() {
		// given
		when(likeRepository.findByTargetIdAndTargetTypeAndUserId(1L, "POST", "user1"))
			.thenReturn(Optional.empty());
		
		// when
		boolean result = postService.isLiked(1L, "POST", "user1");
		
		// then
		assertThat(result).isFalse();
	}
	
}
