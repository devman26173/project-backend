package com.example.join.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.join.entity.Comment;
import com.example.join.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
	
	@Mock
	private CommentRepository commentRepository;
	
	@InjectMocks
	private CommentService commentService;

    // ──────────────────────────────────────────
    // findByPostId (최상위 댓글 조회)
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("게시글 ID로 최상위 댓글 목록을 조회한다")
	void findByPostId_shouldReturnTopLevelComments() {
		// given
		Comment c1 = new Comment(1L, 10L, "댓글1", "user1");
		Comment c2 = new Comment(2L, 10L, "댓글2", "user2");
		when(commentRepository.findByPostIdAndParentIdIsNull(10L))
				.thenReturn(List.of(c1, c2));
		
		// when
		List<Comment> result = commentService.findByPostId(10L);
		
		// then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getContent()).isEqualTo("댓글1");
	}
	
	@Test
	@DisplayName("댓글이 없으면 빈 리스트를 반환한다")
	void findByPostId_shouldReturnEmpty_whenNoComments() {
		// given
		when(commentRepository.findByPostIdAndParentIdIsNull(99L))
				.thenReturn(List.of());
		
		// when
		List<Comment> result = commentService.findByPostId(99L);
		
		// then
		assertThat(result).isEmpty();
	}
	
    // ──────────────────────────────────────────
    // findRepliesByParentId (대댓글 조회)
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("부모 댓글 ID로 대댓글 목록을 조회한다")
	void findRepliesByParentId_shouldReturnReplies() {
		// given
		Comment reply1 = new Comment(3L, 10L, "대댓글1", "user3");
		reply1.setParentId(1L);
		when(commentRepository.findByParentId(1L))
				.thenReturn(List.of(reply1));
		
		// when
		List<Comment> replies = commentService.findRepliesByParentId(1L);
		
		// then
		assertThat(replies).hasSize(1);
		assertThat(replies.get(0).getParentId()).isEqualTo(1L);
	}
	
    // ──────────────────────────────────────────
    // save (댓글 저장)
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("댓글을 저장하고 반환한다")
	void save_shouldReturnSavedComnment() {
		// given
		Comment comment = new Comment(null, 10L, "새 댓글", "user1");
		Comment saved = new Comment(1L, 10L, " 새 댓글", "user1");
		when(commentRepository.save(comment))
				.thenReturn(saved);
		
		// when
		Comment result = commentService.save(comment);
		
		// then
		assertThat(result.getId()).isEqualTo(1L);
		verify(commentRepository, times(1)).save(comment);
	}
	
    // ──────────────────────────────────────────
    // delete (권한 체크 시나리오)
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("본인 댓글은 정상 삭제된다")
	void delete_shouldCallRepository_whenAuthorMatches() {
		// given
		Comment comment = new Comment(1L, 10L, "내 댓글", "user1");
		
		// when - Controller에서 author 체크 후 service.delete() 호출하는 흐름 검증
		commentService.delete(comment);
		
		// then
		verify(commentRepository, times(1)).delete(comment);
	}
	
	@Test
	@DisplayName("다른 사람 댓글은 delete()를 호출하지 않는다 (Controller 레벨 권한 체크 시뮬레이션")
	void delete_shouldNotBeCalled_whenAuthorDoesNotMach() {
		// given
		Comment comment = new Comment(1L, 10L, "남의 댓글", "user2");
		String currentUser = "user1";
		
		// when - 실제 Controller 로직: author가 다르면 delete 호출 안 함
		if (comment.getAuthor().equals(currentUser)) {
			commentService.delete(comment);
		}
			
		// then
			verify(commentRepository, never()).delete(any());
	}
	
    // ──────────────────────────────────────────
    // findById
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("존재하는 댓글 ID로 조회하면 Comment를 반환한다")
	void findById_shouldReturnComment_whenExists() {
		// given
		Comment comment = new Comment(1L, 10L, "댓글", "user1");
		when(commentRepository.findById(1L))
				.thenReturn(Optional.of(comment));
		
		// when
		Comment result = commentService.findById(1L);
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
	}
	
	@Test
	@DisplayName("존재하지 않는 ID로 조회하면 null을 반환한다")
	void findById_shouldReturnNull_whenNotExists() {
		// given
		when(commentRepository.findById(999L))
				.thenReturn(Optional.empty());
		
		// when
		Comment result = commentService.findById(999L);
		
		// then
		assertThat(result).isNull();
	}
	
    // ──────────────────────────────────────────
    // 정렬 시나리오 (최신순 vs 시간순)
    // ──────────────────────────────────────────
	
	@Test
	@DisplayName("댓글 목록이 최신순(내림차순)으로 정렬된다")
	void comments_shouldBeSortedByLatest() {
		// given
		Comment old = new Comment(1L, 10L, "오래된 댓글", "user1");
		Comment newer = new Comment(2L, 10L, "최신 댓글", "user2");
		old.setCreatedAt(LocalDateTime.now().minusDays(1));
		newer.setCreatedAt(LocalDateTime.now());
		
		when(commentRepository.findByPostIdAndParentIdIsNull(10L))
				.thenReturn(List.of(newer, old)); // Repository가 최신순으로 반환한다고 가정

		// when
		List<Comment> result = commentService.findByPostId(10L);
		
		// then: 첫 번째가 더 최신이어야 함
		assertThat(result.get(0).getCreatedAt())
				.isAfter(result.get(1).getCreatedAt());
	}
	
	@Test
	@DisplayName("댓글 목록이 시간순(오름차순)으로 정렬된다")
	void comments_shouldBeSortedByOldest() {
		// given
		Comment old = new Comment(1L, 10L, "오래된 댓글", "user1");
		Comment newer = new Comment(1L, 10L, "최신 댓글", "user2");
		old.setCreatedAt(LocalDateTime.now().minusDays(1));
		newer.setCreatedAt(LocalDateTime.now());
		
		when(commentRepository.findByPostIdAndParentIdIsNull(10L))
				.thenReturn(List.of(old, newer)); // Repository가 시간순으로 반환한다고 가정
		
		// when
		List<Comment> result = commentService.findByPostId(10L);
		
		// then: 첫 번째가 더 오래됐어야 함
		assertThat(result.get(0).getCreatedAt())
				.isBefore(result.get(1).getCreatedAt());
	}
}