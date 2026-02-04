package com.example.join.controller;

import com.example.join.service.FoodBoardService;
import com.example.join.service.CommentService;
import com.example.join.service.PostService;
import com.example.join.entity.Comment;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.join.entity.FoodBoard;
import com.example.join.entity.User;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FoodBoardController {

	private final FoodBoardService foodBoardService;
    private final CommentService commentService;
    private final PostService postService;
	
    public FoodBoardController(FoodBoardService foodBoardService, 
    							CommentService commentService,
    							PostService postService) {
    	this.foodBoardService = foodBoardService;
    	this.commentService = commentService;
    	this.postService = postService;
    }

	@GetMapping("/board")
	public String home(@RequestParam(required = false) String region,
	                   @RequestParam(required = false) String prefecture,
	                   Model model) {
	    
	    // 지역 필터링
	    if (prefecture != null && !prefecture.isEmpty()) {
	        model.addAttribute("boards", foodBoardService.findByPrefecture(prefecture));
	        model.addAttribute("currentCategory", prefecture); // 현재 선택된 카테고리
	    } else if (region != null && !region.isEmpty()) {
	        model.addAttribute("boards", foodBoardService.findByRegion(region));
	        model.addAttribute("currentCategory", region); // 현재 선택된 카테고리
	    } else {
	        model.addAttribute("boards", foodBoardService.findAll());
	        model.addAttribute("currentCategory", "すべて"); // 기본값
	    }
	    
	    return "foodboard";
	}
    // 게시글 작성 페이지
    @GetMapping("/board/write")
    public String write(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login?returnUrl=/board/write";
        }
        return "foodboard-write";
    }
    
    //게시글 저장 
    @PostMapping("/board/write")
    public String saveFood(FoodBoard foodBoard, HttpSession session) {
    	User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login?returnUrl=/board/write";
        }
        foodBoard.setUser(loginUser);
        foodBoardService.saveFood(foodBoard);
        return "redirect:/board";
    }
    
    
    
    // ✅ 추가: 게시글 댓글 전용 페이지로 이동
    @GetMapping("/board/view/{id}")
    public String viewBoard(@PathVariable Long id, 
                           @RequestParam(required = false) String sort,
                           HttpSession session,
                           Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        String currentUserId = loginUser != null ? loginUser.getUsername() : null;
        
        // 게시글 정보
        FoodBoard board = foodBoardService.findById(id);
        
        // 게시글 좋아요 정보
        board.setLikeCount(postService.getLikeCount(id, "BOARD"));
        board.setLikedByMe(currentUserId != null && 
            postService.isLiked(id, "BOARD", currentUserId));
        
        model.addAttribute("board", board);
        
        // 댓글 목록 
        List<Comment> comments = commentService.findByPostId(id);
        
        // 정렬 처리
        if ("newest".equals(sort)) {
            comments = comments.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
        }
        // "oldest"는 기본 정렬 (createdAt 오름차순)
        
        // 각 댓글에 대댓글, 좋아요 정보 추가
        for (Comment comment : comments) {
            // 대댓글 가져오기
            List<Comment> replies = commentService.findRepliesByParentId(comment.getId());
            
            // 대댓글에 좋아요 정보 추가
            for (Comment reply : replies) {
                reply.setLikeCount(postService.getLikeCount(reply.getId(), "COMMENT"));
                reply.setLikedByMe(currentUserId != null && 
                    postService.isLiked(reply.getId(), "COMMENT", currentUserId));
            }
            
            // 댓글 좋아요 정보
            comment.setReplies(replies);
            comment.setLikeCount(postService.getLikeCount(comment.getId(), "COMMENT"));
            comment.setLikedByMe(currentUserId != null && 
                postService.isLiked(comment.getId(), "COMMENT", currentUserId));
        }
        
        model.addAttribute("post", board);  // post.html에서는 'post'로 받음
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("isLoggedIn", loginUser != null);
        model.addAttribute("sortOrder", sort);  // ✅ 추가
        
        return "foodboard-view";  // ✅ 게시글 상세 페이지로
    }
    
    //게시글 수정 페이지 
    @GetMapping("/board/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("board", foodBoardService.findById(id));
    return "foodboard-edit";
    }
    
    //게시글 수정 처리 
    @PostMapping("/board/edit/{id}")
    public String updateBoard(@PathVariable Long id, FoodBoard foodBoard, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        FoodBoard existingBoard = foodBoardService.findById(id);
        foodBoard.setUser(existingBoard.getUser());
        
        foodBoardService.updateBoard(id, foodBoard);
        return "redirect:/board/view/" + id;
    }
    
    //게시글 삭제 처리 
    @PostMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
    	foodBoardService.deleteBoard(id);
    	return "redirect:/board";
    }
    
    // ========== 댓글 관련 기능 ==========
    
 // ✅ 댓글 추가 - 게시글 상세 페이지로
    @PostMapping("/board/comment/add")
    public String addComment(@RequestParam Long boardId,
                            @RequestParam String content,
                            HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
        	return "redirect:/board/view/" + boardId;  // ✅ 게시글 상세 페이지로
        }
        
        Comment comment = new Comment();
        comment.setPostId(boardId);
        comment.setContent(content);
        comment.setUser(loginUser);
        comment.setAuthor(loginUser.getUsername());
        comment.setCreatedAt(java.time.LocalDateTime.now());
        
        commentService.save(comment);
        
        return "redirect:/board/view/" + boardId;
    }
    
    // 댓글 수정
    @PostMapping("/board/comment/edit")
    public String editComment(@RequestParam Long commentId,
                             @RequestParam String content,
                             HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment comment = commentService.findById(commentId);
        if (comment != null && comment.getUser().getUserId().equals(loginUser.getUserId())) {
            comment.setContent(content);
            commentService.save(comment);
        }
        
        return "redirect:/board/view/" + comment.getPostId();  // ✅ 게시글 상세 페이지로
    }
    
    // 댓글 삭제
    @PostMapping("/board/comment/delete")
    public String deleteComment(@RequestParam Long commentId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment comment = commentService.findById(commentId);
        if (comment != null && comment.getUser().getUserId().equals(loginUser.getUserId())) {
            Long postId = comment.getPostId();
            commentService.delete(comment);
            return "redirect:/board/view/" + postId;
        }
        
        return "redirect:/board";
    }
    
    // 댓글 좋아요
    @PostMapping("/board/comment/like")
    public String likeComment(@RequestParam Long commentId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment comment = commentService.findById(commentId);
        postService.toggleLike(commentId, "COMMENT", loginUser.getUsername());
        
        return "redirect:/board/view/" + comment.getPostId();
    }
    
    // 대댓글 추가
    @PostMapping("/board/comment/reply")
    public String addReply(@RequestParam Long parentId,
                          @RequestParam String content,
                          HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment parent = commentService.findById(parentId);
        
        Comment reply = new Comment();
        reply.setPostId(parent.getPostId());
        reply.setParentId(parentId);
        reply.setContent(content);
        reply.setUser(loginUser);
        reply.setAuthor(loginUser.getUsername());
        reply.setCreatedAt(java.time.LocalDateTime.now());
        
        commentService.save(reply);
        
        return "redirect:/board/view/" + parent.getPostId();
    }
    
    // 대댓글 삭제
    @PostMapping("/board/comment/reply/delete")
    public String deleteReply(@RequestParam Long parentId,
                             @RequestParam Long replyId,
                             HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment reply = commentService.findById(replyId);
        Comment parent = commentService.findById(parentId);
        
        if (reply != null && reply.getUser().getUserId().equals(loginUser.getUserId())) {
            commentService.delete(reply);
        }
        
        return "redirect:/board/view/" + parent.getPostId();
    }
    
    // 대댓글 수정 (누락된 메서드 추가)
    @PostMapping("/board/comment/reply/edit")
    public String editReply(@RequestParam Long parentId,
                           @RequestParam Long replyId,
                           @RequestParam String content,
                           HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment reply = commentService.findById(replyId);
        Comment parent = commentService.findById(parentId);
        
        if (reply != null && reply.getUser().getUserId().equals(loginUser.getUserId())) {
            reply.setContent(content);
            commentService.save(reply);
        }
        
        return "redirect:/board/view/" + parent.getPostId();
    }
    
    // 대댓글 좋아요
    @PostMapping("/board/comment/reply/like")
    public String likeReply(@RequestParam Long parentId,
                           @RequestParam Long replyId,
                           HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment parent = commentService.findById(parentId);
        postService.toggleLike(replyId, "COMMENT", loginUser.getUsername());
        
        return "redirect:/board/view/" + parent.getPostId();
    }
    
    // ✅ 추가: 댓글 전용 페이지 (POST 페이지)
    @GetMapping("/post/{boardId}")
    public String postPage(@PathVariable Long boardId,
                          @RequestParam(required = false) String sort,
                          HttpSession session,
                          Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        String currentUserId = loginUser != null ? loginUser.getUsername() : null;
        
        // 게시글 정보
        FoodBoard board = foodBoardService.findById(boardId);
        board.setLikeCount(postService.getLikeCount(boardId, "BOARD"));
        board.setLikedByMe(currentUserId != null && 
            postService.isLiked(boardId, "BOARD", currentUserId));
        
        model.addAttribute("board", board);
        
        // 댓글 목록 가져오기
        List<Comment> comments = commentService.findByPostId(boardId);
        
        // 정렬 처리
        if ("newest".equals(sort)) {
            comments = comments.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
        }
        
        // 각 댓글에 대댓글, 좋아요 정보 추가
        for (Comment comment : comments) {
            List<Comment> replies = commentService.findRepliesByParentId(comment.getId());
            
            for (Comment reply : replies) {
                reply.setLikeCount(postService.getLikeCount(reply.getId(), "COMMENT"));
                reply.setLikedByMe(currentUserId != null && 
                    postService.isLiked(reply.getId(), "COMMENT", currentUserId));
            }
            
            comment.setReplies(replies);
            comment.setLikeCount(postService.getLikeCount(comment.getId(), "COMMENT"));
            comment.setLikedByMe(currentUserId != null && 
                postService.isLiked(comment.getId(), "COMMENT", currentUserId));
        }
        
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("isLoggedIn", loginUser != null);
        model.addAttribute("sortOrder", sort);
        
        return "post";  // post.html로 이동
    }

    // ✅ 추가: 게시글 좋아요
    @PostMapping("/board/like")
    public String likeBoard(@RequestParam Long boardId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        postService.toggleLike(boardId, "BOARD", loginUser.getUsername());
        
        return "redirect:/board/view/" + boardId;
    }
    
}
   
