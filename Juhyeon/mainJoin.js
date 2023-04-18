// user 아이디,비밀번호,이메일,이름,닉네임,버튼 가져옴
const userId = document.getElementById('userId');
const userPw = document.getElementById('userPw');
const userMail = document.getElementById('userMail');
const userName = document.getElementById('userName');
const userNickName = document.getElementById('userNickName');
const loginBtn = document.getElementById('login_btn');


// 아이디,비밀번호,이메일,이름,닉네임 공백일 때 버튼 비활성화
window.addEventListener('keyup', ()=>{
  if(userPw.value.length > 0 && userId.value.length>0 &&
    userMail.value.length>0 && userName.value.length>0 &&
    userNickName.value.length>0){
    loginBtn.disabled = false;
    loginBtn.classList.add('active');
  }
  else {
    loginBtn.disabled = true;
    loginBtn.classList.remove('active');
  }
});

// 유효성 검사
function checkUserInfo(){
  
  // 아이디
  if(userId.value == ""){
    alert("아이디를 입력해주세요.");
    userId.focus();
    return false;
  }

  // 비밀번호
  if (userPw.value.length == 0 || userPw.value.length < 8 ){
    alert("비밀번호를 8자 이상 입력해주세요.");
    userPw.focus();
    return false;
  }

  // 이메일
  if(userMail.value == ""){
    alert("이메일을 입력해주세요");
    userMail.focus();
    return false;
  }

  // 이름
  if(userName.value == ""){
    alert("이름을 입력해주세요");
    userName.focus();
    return false;
  }

  // 닉네임
  if(userNickName.value == ""){
    alert("닉네임을 입력해주세요");
    userNickName.focus();
    return false;
  }
}



loginBtn.addEventListener("click",checkUserInfo);
