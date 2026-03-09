document.addEventListener('DOMContentLoaded', function () {
    const japanMapImage = document.getElementById('japanMapImage');
    const homeTitle = document.getElementById('homeTitle');
    const homeQuestionWrapper = document.getElementById('homeQuestionWrapper');
    const homeQuestionInput = document.getElementById('homeQuestionInput');
    const geminiResponseText = document.getElementById('geminiResponseText');
    const aiKeywordList = document.getElementById('aiKeywordList');
    const searchButton = document.getElementById('searchButton');
    const cancelButton = document.getElementById('cancelButton');
    const errorMessage = document.getElementById('errorMessage');

    // AbortController for canceling requests
    let abortController = null;
    let isLoading = false;
    // Track error state to keep cancel button visible
    let isErrorState = false;

    if (!japanMapImage || !homeTitle || !homeQuestionWrapper || !homeQuestionInput) {
        return;
    }

    const defaultTitle = 'ようこそ！';
    const questionTitle = '何について<br>質問する？';

    japanMapImage.addEventListener('click', function () {
        const isQuestionMode = homeTitle.innerHTML.replace(/\s+/g, '') === questionTitle.replace(/\s+/g, '');

        if (isQuestionMode) {
            homeTitle.innerHTML = defaultTitle;
            homeQuestionWrapper.hidden = true;
            hideResponseText();
            hideKeywords();
            hideErrorMessage(); 
            resetButtonState();
            return;
        }

        homeTitle.innerHTML = questionTitle;
        homeQuestionWrapper.hidden = false;
        homeQuestionInput.focus();
        hideErrorMessage();
        hideKeywords();
        resetButtonState();
    });

    // Enter key event
    homeQuestionInput.addEventListener('keydown', function (e) {
        if (e.key !== 'Enter') {
            return;
        }

        e.preventDefault();
        performSearch();
    });

    // Search button click event
    if (searchButton) {
        searchButton.addEventListener('click', function () {
            performSearch();
        });
    }

    // Cancel button click event
    if (cancelButton) {
        cancelButton.addEventListener('click', function () {
            cancelSearch();
        });
    }

    function performSearch() {
        if (isLoading) {
            return;
        }
        const keyword = homeQuestionInput.value.trim();
        if (!keyword) {
            return;
        }
        callGeminiAPI(keyword);
    }

    function cancelSearch() {
        if (abortController) {
            abortController.abort();
            abortController = null;
        }
        isLoading = false;
        
        
        if (isErrorState) {
            hideErrorMessage();
            hideResponseText();
            hideKeywords();
            resetButtonState();
            if (geminiResponseText) {
                geminiResponseText.textContent = '';
            }
        } else {
           
            hideErrorMessage();
            hideResponseText();
            hideKeywords();
            resetButtonState();
            if (geminiResponseText) {
                geminiResponseText.textContent = '';
            }
        }
    }

    function resetButtonState() {
        setButtonVisibility(true);
    }

    function setLoadingState() {
        setButtonVisibility(false);
    }

    function setButtonVisibility(showSearchButton) {
        if (searchButton) {
            searchButton.hidden = !showSearchButton;
        }
        if (cancelButton) {
            cancelButton.hidden = showSearchButton;
        }
    }

    function showResponseText() {
        if (geminiResponseText) {
            geminiResponseText.hidden = false;
        }
    }

    function hideResponseText() {
        if (geminiResponseText) {
            geminiResponseText.hidden = true;
        }
    }

    function renderKeywords(keywords) {
        if (!aiKeywordList) {
            return;
        }

        aiKeywordList.className = 'mt-3 ai-keyword-chip-row';
        aiKeywordList.innerHTML = '';

        if (!Array.isArray(keywords) || keywords.length === 0) {
            aiKeywordList.hidden = true;
            return;
        }

        keywords.forEach(function (keyword) {
            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'btn btn-sm btn-outline-secondary ai-keyword-chip';
            button.textContent = keyword;
            button.addEventListener('click', function () {
                window.location.href = '/board/search?keyword=' + encodeURIComponent(keyword);
            });
            aiKeywordList.appendChild(button);
        });

        aiKeywordList.hidden = false;
    }

    function hideKeywords() {
        if (aiKeywordList) {
            aiKeywordList.hidden = true;
            aiKeywordList.innerHTML = '';
        }
    }

    function showErrorMessage() {
        if (errorMessage) {
            errorMessage.hidden = false;
            isErrorState = true; 
        }
    }

    function hideErrorMessage() {
        if (errorMessage) {
            errorMessage.hidden = true;
            isErrorState = false; 
        }
    }

    async function callGeminiAPI(question) {
        try {
            // Create new AbortController for this request
            abortController = new AbortController();
            isLoading = true;
            
            // Hide previous error message
            hideErrorMessage();
            
            // Set loading state
            setLoadingState();
            if (geminiResponseText) {
                geminiResponseText.textContent = '回答を生成しています...';
            }
            showResponseText();
            hideKeywords();

            const response = await fetch('/api/ai/ask', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    question: question
                }),
                signal: abortController.signal
            });

            const data = await response.json();

            if (response.ok && data.answer) {
                // Success case - reset button state and show response
                resetButtonState();
                if (geminiResponseText) {
                    hideResponseText();
                }
                renderKeywords(data.keywords);
            } else {
                // Error case - keep cancel button visible, show error message
                showErrorMessage();
                hideResponseText();
                hideKeywords();
                // Don't call resetButtonState() here - keep cancel button visible
            }

        } catch (error) {
            if (error.name === 'AbortError') {
                // User cancelled - reset button state
                resetButtonState();
                if (geminiResponseText) {
                    showResponseText();
                    geminiResponseText.textContent = '検索をキャンセルしました。';
                }
                hideKeywords();
            } else {
                // Network or other error - keep cancel button visible, show error message
                showErrorMessage();
                hideResponseText();
                hideKeywords();
                // Don't call resetButtonState() here - keep cancel button visible
            }
        } finally {
            isLoading = false;
            abortController = null;
        }
    }
});
