
// user 이메일,비밀번호 버튼 가져옴
const userId = document.getElementById('userId');
const userPw = document.getElementById('userPw');
const loginBtn = document.getElementById('login_btn');

// 아이디 패스워드 공백일 때 버튼 비활성화
window.addEventListener('keyup', ()=>{
  if(userPw.value.length > 0 && userId.value.length>0){
    loginBtn.disabled = false;
    loginBtn.classList.add('active');
  }
  else {
    loginBtn.disabled = true;
    loginBtn.classList.remove('active');
  }
});

//  비밀번호 8자리 이상
function chkIdPw(){
  
  if(userPw.value.length == 0||userPw.value.length<8){
    alert("비밀번호를 8자리 이상 입력해주세요.");
    userPw.focus();
    loginBtn.disabled = true;
    loginBtn.classList.remove('active');
    return false;
  }
}


loginBtn.addEventListener("click",chkIdPw)
