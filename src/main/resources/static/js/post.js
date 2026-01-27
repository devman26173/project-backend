function toggleCommentSection() {
    const section = document.getElementById('comment-section');
    section.style.display =
        section.style.display === 'none' ? 'block' : 'none';
}

/* 댓글 수정 토글 */
function toggleEdit(commentId) {
    const content = document.getElementById('comment-content-' + commentId);
    const form = document.getElementById('edit-form-' + commentId);

    const isHidden = form.classList.contains('active');

    content.style.display = isHidden ? 'block' : 'none';
    form.classList.toggle('active');
}

/* 대댓글 입력 토글 */
function toggleReply(commentId) {
    const form = document.getElementById('reply-form-' + commentId);
    form.classList.toggle('active');
}

/* 대댓글 수정 토글 */
function toggleReplyEdit(parentId, replyId) {
    const content = document.getElementById(
        `reply-content-${parentId}-${replyId}`
    );
    const form = document.getElementById(
        `reply-edit-form-${parentId}-${replyId}`
    );

    const isHidden = form.classList.contains('active');

    content.style.display = isHidden ? 'block' : 'none';
    form.classList.toggle('active');
}

// 로그인 상태
const isLoggedIn = false;

function checkLoginForClick(event) {
    if (!isLoggedIn) {
        event.preventDefault();
        event.target.blur();
        
        if (confirm('ログインが必要です。ログインページへ移動しますか？')) {
            window.location.href = '/login';
        }
    }
}

function checkLoginForAction(event) {
    if (!isLoggedIn) {
        event.preventDefault();
        
        if (confirm('ログインが必要です。ログインページへ移動しますか？')) {
            window.location.href = '/login';
        }
        return false;
    }
    return true;
}

// 현재 정렬 상태
let currentSort = 'newest';

// ✅ 페이지 로드 시 초기화
window.addEventListener('DOMContentLoaded', function() {
    // 댓글 섹션 상태 복원
    const commentSectionOpen = sessionStorage.getItem('commentSectionOpen');
    if (commentSectionOpen === 'true') {
        document.getElementById('comment-section').style.display = 'block';
    }
    
    // ✅ 최신순 버튼 활성화 표시
    const newestBtn = document.getElementById('sort-newest');
    const oldestBtn = document.getElementById('sort-oldest');
    
    if (newestBtn && oldestBtn) {
        newestBtn.classList.add('active');
        oldestBtn.classList.remove('active');
    }
    
    // 최신순으로 정렬
    if (document.querySelector('.comment-list .comment')) {
        sortComments('newest');
    }
});

function toggleCommentSection() {
    const commentSection = document.getElementById('comment-section');
    if (commentSection.style.display === 'none') {
        commentSection.style.display = 'block';
        sessionStorage.setItem('commentSectionOpen', 'true');
    } else {
        commentSection.style.display = 'none';
        sessionStorage.setItem('commentSectionOpen', 'false');
    }
}

function toggleEdit(commentId) {
    const content = document.getElementById('comment-content-' + commentId);
    const editForm = document.getElementById('edit-form-' + commentId);
    
    content.style.display = content.style.display === 'none' ? 'block' : 'none';
    editForm.classList.toggle('active');
}

function toggleReply(commentId) {
    const replyForm = document.getElementById('reply-form-' + commentId);
    replyForm.classList.toggle('active');
}

function toggleReplyEdit(parentId, replyId) {
    const content = document.getElementById('reply-content-' + parentId + '-' + replyId);
    const editForm = document.getElementById('reply-edit-form-' + parentId + '-' + replyId);
    
    content.style.display = content.style.display === 'none' ? 'block' : 'none';
    editForm.classList.toggle('active');
}
function checkLoginAndToggleEdit(event, commentId) {
    if (!isLoggedIn) {
        event.preventDefault();
        if (confirm('ログインが必要です。ログインページへ移動しますか？')) {
            window.location.href = '/login';
        }
        return;
    }
    toggleEdit(commentId);
}

function checkLoginAndToggleReplyEdit(event, parentId, replyId) {
    if (!isLoggedIn) {
        event.preventDefault();
        if (confirm('ログインが必要です。ログインページへ移動しますか？')) {
            window.location.href = '/login';
        }
        return;
    }
    toggleReplyEdit(parentId, replyId);
}

// ✅ 댓글 정렬 함수 수정
function sortComments(order) {
    // 이미 선택된 정렬이면 아무것도 안 함
    if (currentSort === order) {
        return;
    }
    
    const commentList = document.querySelector('.comment-list');
    const comments = Array.from(commentList.querySelectorAll('.comment'));
    
    const newestBtn = document.getElementById('sort-newest');
    const oldestBtn = document.getElementById('sort-oldest');
    
    if (order === 'newest') {
        // 최신순 (역순)
        comments.reverse();
        newestBtn.classList.add('active');
        oldestBtn.classList.remove('active');
    } else {
        // 시간순 (원래 순서로)
        comments.reverse();
        newestBtn.classList.remove('active');
        oldestBtn.classList.add('active');
    }
    
    // 댓글 다시 추가
    comments.forEach(comment => {
        commentList.appendChild(comment);
    });
    
    currentSort = order;
}