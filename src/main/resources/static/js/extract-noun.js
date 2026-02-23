document.getElementById('newFeatureButton').addEventListener('click', function() {
    const boardId = window.location.pathname.split('/').pop();
    fetch(`/board/token-extract?boardId=${boardId}`,{
        method: 'POST',
    })
    .then(location.reload())
    .catch(err => console.error(err));
    console.log(boardId)
    console.log("clicked")
});