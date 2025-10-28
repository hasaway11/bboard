      const patterns = {
        username: /^[a-z0-9]{6,10}$/,
        password: /^[A-Za-z0-9]{6,10}$/,
        email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      };

      const errors = {
        username: '아이디는 소문자와 숫자 6~10자입니다',
        password: '비밀번호는 영숫자 6~10자입니다',
        email: '이메일을 올바르게 입력해주세요'
      };

      // 입력여부 + 입력한 경우 패턴체크를 수행
      function check(engName, korName, $element) {
        const $errorElement = $element.next();
        const value = $element.val();

        $errorElement.text('');
        if(value==='') {
          $errorElement.text(korName + ': 필수입력입니다');
          return false;
        }
        if(patterns[engName].test(value)===false) {
          $errorElement.text(errors[engName]);
          return false;
        }
        return true;
      };

      async function usernameCheck() {
        const checkResult = check('username', '아이디', $('#username'));
        if(checkResult===false)
          return false;
        try {
          // try 내부에서 어떤 오류라도 발생하면 catch로 간다. 오타 안나게 매우 주의
          const username = $('#username').val();
          await axios.get(`/api/member/check-username?username=${username}`);
          return true;
        } catch(err) {
          $('#username').next().text('사용중인 아이디입니다');
          return false;
        }
      }

      function passwordCheck() {
        return check('password', '비밀번호', $('#password'));
      }

      function emailCheck() {
        return check('email', '이메일', $('#email'));
      }