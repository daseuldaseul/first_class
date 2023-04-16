
/*  하트눌렀을때 숫자증가 */
function onClickLike() {
    const likeNum = document.getElementById('like-num');
    let number = likeNum.innerText;
    const likeBtn = document.getElementById('like-btn');

    if (likeBtn.classList.contains('active')) {
        // 이미 클릭되어 활성화된 경우, active 클래스 제거하고 likeNum 감소
        likeBtn.classList.remove('active');
        likeNum.innerText = parseInt(number) - 1;
        // 이미지 변경
        likeBtn.src = 'img/like.png';
        return;
    }
    // likeNum 증가
    number = parseInt(number) + 1;
    // likeNum에 결과값 출력
    likeNum.innerText = number;
    // 이미지 변경
    likeBtn.src = 'img/like-on.png';
    likeBtn.classList.add('active');
}




