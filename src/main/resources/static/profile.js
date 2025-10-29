    function showProfile() {
      const profile = $('#profile')[0].files[0];
      const maxSize = 1024*1024;
      if(profile.size>maxSize) {
        alert('프로필 사진은 1MB이하여야 합니다');
        $('#profile')[0].files[0]='';
        return;
      }

      // 로컬에 있는 파일을 서버 업로드 없이 미리보기 : base64 주소 형식으로 변경한 다음 <img src="">에 출력하면 된다
      // 1. 파일 작업 객체를 생성
      let reader = new FileReader();
      // 2. base64 형식으로 변환하는 비동기 작업 수행
      reader.readAsDataURL(profile);
      // 3. 2번 작업이 끝나면 img 태그에 출력 -> 2번 작업이 비동기이므로 콜백으로 작성한다
      reader.onload = function() {
        $('#show-profile').css('visibility','visible').attr('src', reader.result);
      }
    }