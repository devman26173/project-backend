package com.example.join.controller;

import com.example.join.service.FoodBoardService;
import com.example.join.service.ImageUploadService;
import com.example.join.service.CommentService;
import com.example.join.service.PostService;
import com.example.join.service.TokenExtractService;
import com.example.join.entity.Comment;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.join.entity.FoodBoard;
import com.example.join.entity.User;
import com.example.join.repository.CommentRepository;
import com.example.join.repository.LikeRepository;
import com.example.join.repository.TokenRepository;

import java.util.List;
import java.util.stream.Collectors;

//========== 비동기 API 엔드포인트 ==========

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FoodBoardController {

	private final FoodBoardService foodBoardService;
    private final CommentService commentService;
    private final PostService postService;
    private final ImageUploadService imageUploadService;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final String uploadClientMode;
    private final TokenExtractService tokenExtractService;
    private final TokenRepository tokenRepository;
	
    public FoodBoardController(FoodBoardService foodBoardService, 
    							CommentService commentService,
    							PostService postService,
							ImageUploadService imageUploadService,
    							CommentRepository commentRepository, 
    							LikeRepository likeRepository,
                                @Value("${app.upload.client-mode:server}") String uploadClientMode,
                                TokenExtractService tokenExtractService,
                                TokenRepository tokenRepository) {
    	this.foodBoardService = foodBoardService;
    	this.commentService = commentService;
    	this.postService = postService;
		this.imageUploadService = imageUploadService;
    	this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.uploadClientMode = uploadClientMode;
        this.tokenExtractService = tokenExtractService;
        this.tokenRepository = tokenRepository;
    }

	@GetMapping("/board")
	public String home(@RequestParam(required = false) String region,
	                   @RequestParam(required = false) String prefecture,
	                   HttpSession session,
	                   Model model) {
		
		List<FoodBoard> boards;
	    
	    // 지역 필터링
	    if (prefecture != null && !prefecture.isEmpty()) {
	        boards = foodBoardService.findByPrefecture(prefecture);
	        model.addAttribute("currentCategory", prefecture); // 현재 선택된 카테고리
	    } else if (region != null && !region.isEmpty()) {
	    	boards = foodBoardService.findByRegion(region);
	        model.addAttribute("currentCategory", region); // 현재 선택된 카테고리
	    } else {
	        boards = foodBoardService.findAll();
	        model.addAttribute("currentCategory", "すべて"); // 기본값
	    }
	    
	    //로그인 유저 정보 가져오기
	    User loginUser = (User) session.getAttribute("loginUser");
	    String currentUserId = loginUser != null?loginUser.getUsername() : null;
	    
	    //각 게시글의 좋아요 수와 댓글 수 설정
	    for (FoodBoard board : boards) {
	    	//댓글 수 (부모 댓글만, 대댓글 제외)
	    	long commentCount = commentRepository.countByPostIdAndParentIdIsNull(board.getId());
	    	board.setCommentCount((int) commentCount);
	    	
	    	//좋아요 수 
	    	board.setLikeCount(postService.getLikeCount(board.getId(), "BOARD"));
	    	
	    	//내가 좋아요 눌렀는지 여부 설정
	    	board.setLikedByMe(currentUserId != null &&
	    		postService.isLiked(board.getId(), "BOARD", currentUserId));	
	    }
	    
	    model.addAttribute("boards", boards);
	    
	    
	    return "foodboard";
	}
	//검색 기능 추가
	@GetMapping("/board/search")
	public String searchBoard(@RequestParam String keyword,
							HttpSession session, 
							Model model) {
		//검색어로 게시글 찾기 
		List<FoodBoard> boards = foodBoardService.searchByKeyword(keyword);
		
		//로그인 유저 정보 가져오기
	    User loginUser = (User) session.getAttribute("loginUser");
	    String currentUserId = loginUser != null?loginUser.getUsername() : null;
	    
	    //각 게시글의 좋아요 수와 댓글 수 설정
	    for (FoodBoard board : boards) {
	    	//댓글 수 (부모 댓글만, 대댓글 제외)
	    	long commentCount = commentRepository.countByPostIdAndParentIdIsNull(board.getId());
	    	board.setCommentCount((int) commentCount);
	    	
	    	//좋아요 수 
	    	board.setLikeCount(postService.getLikeCount(board.getId(), "BOARD"));
	    	
	    	//내가 좋아요 눌렀는지 여부 설정
	    	board.setLikedByMe(currentUserId != null &&
	    		postService.isLiked(board.getId(), "BOARD", currentUserId));	
	    }
	    
	    model.addAttribute("boards",boards);
	    model.addAttribute("searchKeyword",keyword);
	    model.addAttribute("currentCategory","検索結果: " + keyword);
	    
	    return "foodboard";
	}
	
    // 게시글 작성 페이지
    @GetMapping("/board/write")
    public String write(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login?returnUrl=/board/write";
        }
        model.addAttribute("uploadClientMode", uploadClientMode);
        return "foodboard-write";
    }
    
    //게시글 저장 
    @PostMapping("/board/write")
    public String saveFood(FoodBoard foodBoard, HttpSession session) {
    	User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login?returnUrl=/board/write";
        }
        foodBoard.setImageUrls(imageUploadService.normalizeImageUrls(foodBoard.getImageUrls()));
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
        
        //조회수 증가(중복 방지를 위한 세션 체크)
        String viewKey = "board_view_" + id; 
        if(session.getAttribute(viewKey) == null) {
        	foodBoardService.increaseViewCount(id);
        	session.setAttribute(viewKey, true);
        }
        
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

    // Add nouns extract feature
    @PostMapping("/board/token-extract")
    public String extractNounsAndApply(@RequestParam Long boardId, RedirectAttributes redirectAttributes) {
        List<String> extractedNouns = tokenExtractService.extractNounsList(foodBoardService.findById(boardId).getContent());
        redirectAttributes.addFlashAttribute("extractedNouns",extractedNouns);
        return "redirect:/board/view/" + boardId;
    }
    
    //게시글 수정 페이지 
    @GetMapping("/board/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("board", foodBoardService.findById(id));
    model.addAttribute("uploadClientMode", uploadClientMode);
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
        foodBoard.setImageUrls(imageUploadService.normalizeImageUrls(foodBoard.getImageUrls()));
        
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
            				 @RequestParam(required = false) String returnUrl,
            				 HttpSession session) {
    	User loginUser = (User) session.getAttribute("loginUser");
    	if (loginUser == null) {
    			return "redirect:/login";
    	}

    	Comment comment = new Comment();
    	comment.setPostId(boardId);
    	comment.setContent(content);
    	comment.setUser(loginUser);
    	comment.setAuthor(loginUser.getUsername());
    	comment.setCreatedAt(java.time.LocalDateTime.now());

    	commentService.save(comment);

    	// returnUrl이 있으면 그쪽으로, 없으면 기본 페이지로
    	if (returnUrl != null && returnUrl.equals("post")) {
    			return "redirect:/post/" + boardId;
    	}
    			return "redirect:/board/view/" + boardId;
    	}
    
    // 댓글 수정
    @PostMapping("/board/comment/edit")
    public String editComment(@RequestParam Long commentId,
                             @RequestParam String content,
                             @RequestParam(required = false) String returnUrl,
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
        
        if (returnUrl != null && returnUrl.equals("post")) {
            return "redirect:/post/" + comment.getPostId();
        }
        return "redirect:/board/view/" + comment.getPostId();
    }
    
    // 댓글 삭제
    @PostMapping("/board/comment/delete")
    public String deleteComment(@RequestParam Long commentId, 
    							@RequestParam(required = false) String returnUrl,	
    							HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment comment = commentService.findById(commentId);
        if (comment != null && comment.getUser().getUserId().equals(loginUser.getUserId())) {
            Long postId = comment.getPostId();
            commentService.delete(comment);
            
        if (returnUrl != null && returnUrl.equals("post")) {
            return "redirect:/post/" + postId;
        }
        	return "redirect:/board/view/" + postId;
        	
        }
        
        return "redirect:/board";
    }
    
    // 댓글 좋아요
    @PostMapping("/board/comment/like")
    public String likeComment(@RequestParam Long commentId, 
    						  @RequestParam(required = false) String returnUrl,
    						  HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment comment = commentService.findById(commentId);
        postService.toggleLike(commentId, "COMMENT", loginUser.getUsername());
        
        if (returnUrl != null && returnUrl.equals("post")) {
            return "redirect:/post/" + comment.getPostId();
        }
        return "redirect:/board/view/" + comment.getPostId() + "#comment-" + commentId;
    }
    
    // 대댓글 추가
    @PostMapping("/board/comment/reply")
    public String addReply(@RequestParam Long parentId,
                          @RequestParam String content,
                          @RequestParam(required = false) String returnUrl,
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
        
        if (returnUrl != null && returnUrl.equals("post")) {
            return "redirect:/post/" + parent.getPostId();
        }
        return "redirect:/board/view/" + parent.getPostId();
    }
    
    // 대댓글 삭제
    @PostMapping("/board/comment/reply/delete")
    public String deleteReply(@RequestParam Long parentId,
                             @RequestParam Long replyId,
                             @RequestParam(required = false) String returnUrl,
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
        
        if (returnUrl != null && returnUrl.equals("post")) {
            return "redirect:/post/" + parent.getPostId();
        }
        return "redirect:/board/view/" + parent.getPostId();
    }
    
    // 대댓글 수정 (누락된 메서드 추가)
    @PostMapping("/board/comment/reply/edit")
    public String editReply(@RequestParam Long parentId,
                           @RequestParam Long replyId,
                           @RequestParam String content,
                           @RequestParam(required = false) String returnUrl,
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
        
        if (returnUrl != null && returnUrl.equals("post")) {
            return "redirect:/post/" + parent.getPostId();
        }
        return "redirect:/board/view/" + parent.getPostId();
    }
    
    // 대댓글 좋아요
    @PostMapping("/board/comment/reply/like")
    public String likeReply(@RequestParam Long parentId,
                           @RequestParam Long replyId,
                           @RequestParam(required = false) String returnUrl,
                           HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Comment parent = commentService.findById(parentId);
        postService.toggleLike(replyId, "COMMENT", loginUser.getUsername());
        
        if (returnUrl != null && returnUrl.equals("post")) {
            return "redirect:/post/" + parent.getPostId();
        }
        return "redirect:/board/view/" + parent.getPostId() + "#comment-" + parentId;
    }

    // ========== 비동기 API ==========
    @PostMapping("/api/board/like")
    @ResponseBody
    public Map<String, Object> likeBoardApi(@RequestParam Long boardId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        postService.toggleLike(boardId, "BOARD", loginUser.getUsername());
        response.put("success", true);
        response.put("liked", postService.isLiked(boardId, "BOARD", loginUser.getUsername()));
        response.put("likeCount", postService.getLikeCount(boardId, "BOARD"));
        return response;
    }

    @PostMapping("/api/board/comment/add")
    @ResponseBody
    public Map<String, Object> addCommentApi(@RequestParam Long boardId,
                                             @RequestParam String content,
                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment comment = new Comment();
        comment.setPostId(boardId);
        comment.setContent(content);
        comment.setUser(loginUser);
        comment.setAuthor(loginUser.getUsername());
        comment.setCreatedAt(java.time.LocalDateTime.now());
        commentService.save(comment);

        response.put("success", true);
        return response;
    }

    @PostMapping("/api/board/comment/like")
    @ResponseBody
    public Map<String, Object> likeCommentApi(@RequestParam Long commentId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            response.put("success", false);
            response.put("message", "댓글을 찾을 수 없습니다.");
            return response;
        }

        postService.toggleLike(commentId, "COMMENT", loginUser.getUsername());
        response.put("success", true);
        response.put("liked", postService.isLiked(commentId, "COMMENT", loginUser.getUsername()));
        response.put("likeCount", postService.getLikeCount(commentId, "COMMENT"));
        return response;
    }

    @PostMapping("/api/board/comment/edit")
    @ResponseBody
    public Map<String, Object> editCommentApi(@RequestParam Long commentId,
                                              @RequestParam String content,
                                              HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            response.put("success", false);
            response.put("message", "댓글을 찾을 수 없습니다.");
            return response;
        }
        if (!comment.getUser().getUserId().equals(loginUser.getUserId())) {
            response.put("success", false);
            response.put("message", "수정 권한이 없습니다.");
            return response;
        }

        comment.setContent(content);
        commentService.save(comment);
        response.put("success", true);
        response.put("content", content);
        return response;
    }

    @PostMapping("/api/board/comment/delete")
    @ResponseBody
    public Map<String, Object> deleteCommentApi(@RequestParam Long commentId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            response.put("success", false);
            response.put("message", "댓글을 찾을 수 없습니다.");
            return response;
        }
        if (!comment.getUser().getUserId().equals(loginUser.getUserId())) {
            response.put("success", false);
            response.put("message", "삭제 권한이 없습니다.");
            return response;
        }

        commentService.delete(comment);
        response.put("success", true);
        return response;
    }

    @PostMapping("/api/board/comment/reply")
    @ResponseBody
    public Map<String, Object> addReplyApi(@RequestParam Long parentId,
                                           @RequestParam String content,
                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment parent = commentService.findById(parentId);
        if (parent == null) {
            response.put("success", false);
            response.put("message", "부모 댓글을 찾을 수 없습니다.");
            return response;
        }

        Comment reply = new Comment();
        reply.setPostId(parent.getPostId());
        reply.setParentId(parentId);
        reply.setContent(content);
        reply.setUser(loginUser);
        reply.setAuthor(loginUser.getUsername());
        reply.setCreatedAt(java.time.LocalDateTime.now());
        commentService.save(reply);

        response.put("success", true);
        return response;
    }

    @PostMapping("/api/board/comment/reply/edit")
    @ResponseBody
    public Map<String, Object> editReplyApi(@RequestParam Long parentId,
                                            @RequestParam Long replyId,
                                            @RequestParam String content,
                                            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment parent = commentService.findById(parentId);
        Comment reply = commentService.findById(replyId);
        if (parent == null || reply == null) {
            response.put("success", false);
            response.put("message", "댓글을 찾을 수 없습니다.");
            return response;
        }
        if (!reply.getUser().getUserId().equals(loginUser.getUserId())) {
            response.put("success", false);
            response.put("message", "수정 권한이 없습니다.");
            return response;
        }

        reply.setContent(content);
        commentService.save(reply);
        response.put("success", true);
        response.put("content", content);
        return response;
    }

    @PostMapping("/api/board/comment/reply/delete")
    @ResponseBody
    public Map<String, Object> deleteReplyApi(@RequestParam Long parentId,
                                              @RequestParam Long replyId,
                                              HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment parent = commentService.findById(parentId);
        Comment reply = commentService.findById(replyId);
        if (parent == null || reply == null) {
            response.put("success", false);
            response.put("message", "댓글을 찾을 수 없습니다.");
            return response;
        }
        if (!reply.getUser().getUserId().equals(loginUser.getUserId())) {
            response.put("success", false);
            response.put("message", "삭제 권한이 없습니다.");
            return response;
        }

        commentService.delete(reply);
        response.put("success", true);
        return response;
    }

    @PostMapping("/api/board/comment/reply/like")
    @ResponseBody
    public Map<String, Object> likeReplyApi(@RequestParam Long parentId,
                                            @RequestParam Long replyId,
                                            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        Comment parent = commentService.findById(parentId);
        Comment reply = commentService.findById(replyId);
        if (parent == null || reply == null) {
            response.put("success", false);
            response.put("message", "댓글을 찾을 수 없습니다.");
            return response;
        }

        postService.toggleLike(replyId, "COMMENT", loginUser.getUsername());
        response.put("success", true);
        response.put("liked", postService.isLiked(replyId, "COMMENT", loginUser.getUsername()));
        response.put("likeCount", postService.getLikeCount(replyId, "COMMENT"));
        return response;
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
        model.addAttribute("post", board);
        
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
    
 // 이용방법 및 규칙 페이지
    @GetMapping("/board/rules")
    public String rules() {
        return "foodboard-rules";
    }

    
}
   
