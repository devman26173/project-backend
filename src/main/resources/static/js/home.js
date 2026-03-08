document.addEventListener('DOMContentLoaded', function () {
    const japanMapImage = document.getElementById('japanMapImage');
    const homeTitle = document.getElementById('homeTitle');
    const homeQuestionWrapper = document.getElementById('homeQuestionWrapper');
    const homeQuestionInput = document.getElementById('homeQuestionInput');
    const geminiResponse = document.getElementById('geminiResponse');
    const geminiResponseText = document.getElementById('geminiResponseText');
    const geminiLoading = document.getElementById('geminiLoading');
    const searchButton = document.getElementById('searchButton');
    const cancelButton = document.getElementById('cancelButton');
    const errorMessage = document.getElementById('errorMessage');

    // AbortController for canceling requests
    let abortController = null;
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
            homeQuestionWrapper.style.display = 'none';
            geminiResponse.style.display = 'none';
            hideErrorMessage(); 
            resetButtonState();
            return;
        }

        homeTitle.innerHTML = questionTitle;
        homeQuestionWrapper.style.display = 'block';
        homeQuestionInput.focus();
        hideErrorMessage();
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
        
        
        if (isErrorState) {
            hideErrorMessage();
            if (geminiResponse) {
                geminiResponse.style.display = 'none';
            }
            resetButtonState();
            if (geminiResponseText) {
                geminiResponseText.textContent = '';
            }
        } else {
           
            hideErrorMessage();
            if (geminiLoading) {
                geminiLoading.style.display = 'none';
            }
            if (geminiResponse) {
                geminiResponse.style.display = 'none';
            }
            resetButtonState();
            if (geminiResponseText) {
                geminiResponseText.textContent = '';
            }
        }
    }

    function resetButtonState() {
        if (searchButton) searchButton.style.display = 'block';
        if (cancelButton) cancelButton.style.display = 'none';
    }

    function setLoadingState() {
        if (searchButton) searchButton.style.display = 'none';
        if (cancelButton) cancelButton.style.display = 'block';
    }

    function showErrorMessage() {
        if (errorMessage) {
            errorMessage.style.display = 'block';
            isErrorState = true; 
        }
    }

    function hideErrorMessage() {
        if (errorMessage) {
            errorMessage.style.display = 'none';
            isErrorState = false; 
        }
    }

    async function callGeminiAPI(question) {
        try {
            // Create new AbortController for this request
            abortController = new AbortController();
            
            // Hide previous error message
            hideErrorMessage();
            
            // Set loading state
            setLoadingState();
            if (geminiLoading) {
                geminiLoading.style.display = 'flex';
            }
            if (geminiResponseText) {
                geminiResponseText.textContent = '';
            }
            if (geminiResponse) {
                geminiResponse.style.display = 'block';
            }

            const response = await fetch('/api/gemini/ask', {
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

            // Hide loading first
            if (geminiLoading) {
                geminiLoading.style.display = 'none';
            }

            if (response.ok && data.response) {
                // Success case - reset button state and show response
                resetButtonState();
                if (geminiResponseText) {
                    geminiResponseText.textContent = data.response;
                }
            } else {
                // Error case - keep cancel button visible, show error message
                showErrorMessage();
                if (geminiResponse) {
                    geminiResponse.style.display = 'none';
                }
                // Don't call resetButtonState() here - keep cancel button visible
            }

        } catch (error) {
            // Hide loading first
            if (geminiLoading) {
                geminiLoading.style.display = 'none';
            }
            
            if (error.name === 'AbortError') {
                // User cancelled - reset button state
                resetButtonState();
                if (geminiResponseText) {
                    geminiResponseText.textContent = '検索がキャンセルされました。';
                }
            } else {
                // Network or other error - keep cancel button visible, show error message
                showErrorMessage();
                if (geminiResponse) {
                    geminiResponse.style.display = 'none';
                }
                // Don't call resetButtonState() here - keep cancel button visible
            }
        } finally {
            abortController = null;
        }
    }
});