# FIDO-SIMPLE-DEMO

Versions
- Spring-boot: 2.6.3
- Java: 17


## 기능구현목록

### 등록 api
- [ ] /rp/attestation/options
  - userId 생성
- [ ] /fido2/req/challenge
  - challenge 생성
- [ ] /rp/attestation/result
- [ ] /fido2/req/response

### 인증 api
- [ ] /rp/assertion/options
- [ ] /fido2/auth/challenge
- [ ] /rp/assertion/result
- [ ] /fido2/auth/response

### View
- [x] ID 입력화면
- [x] 등록 (Javascript, Web Authentication API)
- [ ] 인증 (Javascript, Web Authentication API)


## 참고

- FIDO
  - https://github.com/line/line-fido2-server
  - https://fidoalliance.org/fido2/
  - https://fidoalliance.org/specifications/?lang=ko

- WebAuthenticationApi
  - https://developer.mozilla.org/en-US/docs/Web/API/Web_Authentication_API
  - https://developers.yubico.com/WebAuthn/WebAuthn_Developer_Guide/WebAuthn_Client_Registration.html