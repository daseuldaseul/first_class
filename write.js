
/*  하트눌렀을때 숫자증가 */
function onClickLike() {
    const likeNum = document.getElementById('like-num');
    let number = likeNum.innerText;
    // 이미지 변경을 위한 likeBtn 변수 선언
    const likeBtn = document.getElementById('like-btn');

    if (likeBtn.classList.contains('active')) {
        // 이미 클릭되어 활성화된 경우, 함수 종료
        return;
    }
    // likeNum 증가
    number = parseInt(number) + 1;
    // likeNum에 결과값 출력
    likeNum.innerText = number;
    // 이미지 변경 코드
    likeBtn.src = 'img/like-on.png';
    likeBtn.classList.add('active');
}


