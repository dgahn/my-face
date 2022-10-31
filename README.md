# MyFace 서버

고객의 소비내역을 기록하는 서버를 구현합니다.

## 기능 요구사항

1. 고객은 `이메일`과 `비밀번호`입력을 통해서 회원 가입을 할 수 있습니다.
2. 고객은 회원 가입이후, 로그인과 로그아웃을 할 수 있습니다.
3. 고객은 로그인 이후 가계부 관련 아래의 행동을 할 수 있습니다.
   - 가계부에 오늘 사용한 돈의 `금액`과 관련된 `메모`를 남길 수 있습니다. 
   - 가계부에서 수정을 원하는 내역은 `금액`과 `메모`를 수정 할 수 있습니다.
   - 가계부에서 삭제를 원하는 내역은 삭제 할 수 있습니다. 
   - 삭제한 내역은 언제든지 `다시 복구` 할 수 있어야 한다. 
   - 가계부에서 이제까지 기록한 `가계부 리스트`를 볼 수 있습니다. 
   - 가계부에서 `상세한 세부 내역`을 볼 수 있습니다.
4. 로그인하지 않은 고객은 `가계부 내역에 대한 접근 제한` 처리가 되어야 합니다.

## API 명세

### 회원가입 API
```
POST /v1/sign-up
```

### 로그인 API
```
POST /v1/sign-in
```

### 로그아웃 API
```
POST /v1/sign-out
```
