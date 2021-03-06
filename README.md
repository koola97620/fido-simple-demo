# FIDO-SIMPLE-DEMO

Versions
- Spring-boot: 2.6.3
- Java: 17


## 기능구현목록

### 등록 api
- [x] /rp/attestation/options
  - userId 생성
- [x] /fido2/req/challenge
  - challenge 생성
- [x] /rp/attestation/result
- [x] /fido2/req/response

### 인증 api
- [x] /rp/assertion/options
- [x] /fido2/auth/challenge
- [x] /rp/assertion/result
- [x] /fido2/auth/response

### View
- [x] ID 입력화면
- [x] 등록 (Javascript, Web Authentication API)
- [x] 인증 (Javascript, Web Authentication API)

## 실행시 참고

### DB 세팅
- h2 콘솔에 접속이 안될때
  - local db 이용하게 포트 변경
  - window 에서 local h2 포트 다르게 실행하는 법
    - ./h2.bat -webPort 18088 -tcpPort 19099
    - WebConsole 접속은 18088, DB 연결은 19099
- Redis 시작 에러
  - 6379 포트로 실행중인 프로세스가 있는지 확인할 것


## 참고

- FIDO
  - https://github.com/line/line-fido2-server
  - https://fidoalliance.org/fido2/
  - https://fidoalliance.org/specifications/?lang=ko

- WebAuthenticationApi
  - https://developer.mozilla.org/en-US/docs/Web/API/Web_Authentication_API
  - https://developers.yubico.com/WebAuthn/WebAuthn_Developer_Guide/WebAuthn_Client_Registration.html
