package com.example.join.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
	CommentRepository commentRepository;
	
	@InjectMocks
	CommentService commentService;
	
	@Test
	void 게시글의_최상위_댓글_조회() {
		Comment comment = new Comment();
		comment.setId(1L);
		
		when(commentRepository.findByPostIdAndParentIdIsNull(1L))
				.thenReturn(List.of(comment));
		
		List<Comment> result = commentService.findByPostId(1L);
		
		assertEquals(1, result.size());
		verify(commentRepository, times(1))
				.findByPostIdAndParentIdIsNull(1L);
	}
	
	@Test
	void 대댓글_조회() {
		Comment reply = new Comment();
		reply.setParentId(1L);
		
		when(commentRepository.findByParentId(1L))
				.thenReturn(List.of(reply));
		
		List<Comment> result = commentService.findRepliesByParentId(1L);
		
		assertEquals(1, result.size());
	}
	
	@Test
	void 댓글_저장() {
		Comment comment = new Comment();
		comment.setContent("테스트");
		
		when(commentRepository.save(comment))
				.thenReturn(comment);
		
		Comment result = commentService.save(comment);
		
		assertEquals("테스트", result.getContent());
		verify(commentRepository).save(comment);
	}
	
	@Test
	void 댓글_삭제() {
		Comment comment = new Comment();

		commentService.delete(comment);
		
		verify(commentRepository, times(1))
				.delete(comment);
	}
	
	@Test
	void 댓글_ID로_조회_성공() {
		Comment comment = new Comment();
		comment.setId(1L);
		
		when(commentRepository.findById(1L))
				.thenReturn(Optional.of(comment));
		
		Comment result = commentService.findById(1L);
		
		assertNotNull(result);
	}
	
	@Test
	void 댓글_ID로_조회_실패() {
		when(commentRepository.findById(1L))
				.thenReturn(Optional.empty());
		
		Comment result = commentService.findById(1L);
		
		assertNull(result);
	}
}
