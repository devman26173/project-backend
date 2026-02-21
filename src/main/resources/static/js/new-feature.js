document.getElementById('japanMapImage').addEventListener('mouseover', function() {
    console.log('japanMapImage mouseover');
    this.insertAdjacentHTML('beforebegin', tooltip('北海道'));
});

document.getElementById('japanMapImage').addEventListener('mouseout', function() {
    console.log('japanMapImage mouseout');
    this.previousElementSibling?.remove();
});

const tooltip = (text) => {
    return `<div class="tooltip">${text}</div>`;
};