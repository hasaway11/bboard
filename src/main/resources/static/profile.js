    async function showProfile() {
      // 1. 이미지가 1MB보다 크면 업로드 중단
      // 2. FormData를 생성해 axios.post()로 요청
      // 3. 서버의 응답을 $('#show-profile')에 출력

      // <input type='file' id='profile'>이 있을 때 사용자가 선택한 이미지는 document.getElementById('profile').files[0]
      const profile = document.getElementById('profile').files[0];
      const maxSize = 1024*1024;
      if(profile.size>maxSize) {
        alert('프로필 사진은 1MB이하여야 합니다');
        return;
      }

      // 자바스크립트에서 파일 업로드를 담당하는 객체인 FormData를 생성하는 방법
      // 1. 폼이 있으면 폼을 가지고 생성 : new FormData(document.getElementById(폼))
      // 2. 폼이 없으면 비어있는 채로 생성한 다음 append로 하나씩 추가
      const formData = new FormData();
      formData.append('profile', profile);
      try {
        const response = await axios.post('/api/profile/new', formData);
        $('#show-profile').css('visibility','visible').attr('src', response.data.profileUrl);
        $('#profile-name').val(response.data.profileName);
      } catch(err) {
        console.log(err);
      }
    }
