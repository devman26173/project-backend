document.addEventListener('DOMContentLoaded', function () {
    const japanMapImage = document.getElementById('japanMapImage');
    const homeTitle = document.getElementById('homeTitle');
    const homeQuestionWrapper = document.getElementById('homeQuestionWrapper');
    const homeQuestionInput = document.getElementById('homeQuestionInput');

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
            return;
        }

        homeTitle.innerHTML = questionTitle;
        homeQuestionWrapper.style.display = 'block';
        homeQuestionInput.focus();
    });

    homeQuestionInput.addEventListener('keydown', function (e) {
        if (e.key !== 'Enter') {
            return;
        }

        e.preventDefault();
        const keyword = homeQuestionInput.value.trim();
        if (!keyword) {
            return;
        }

        window.location.href = '/board?fromHomeKeyword=' + encodeURIComponent(keyword);
    });
});