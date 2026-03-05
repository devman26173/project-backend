document.addEventListener('DOMContentLoaded', function () {
    const japanMapImage = document.getElementById('japanMapImage');
    const homeTitle = document.getElementById('homeTitle');

    if (!japanMapImage || !homeTitle) {
        return;
    }

    const defaultTitle = 'ようこそ！';
    const questionTitle = '何について質問する？';

    japanMapImage.addEventListener('click', function () {
        homeTitle.textContent =
            homeTitle.textContent.trim() === questionTitle ? defaultTitle : questionTitle;
    });
});