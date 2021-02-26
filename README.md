# ThaiLottery
태국인들을 위한 안드로이드 로또 앱입니다  

## 기술 스택
  + 개발언어 : kotlin
  + 서버 : firebase
  + 데이터베이스 : firebase, realm
  

## 스크린샷
![합친거](https://user-images.githubusercontent.com/48876807/91184222-58561a00-e727-11ea-8be2-0e7785bf5fd2.png)



## 페이지 별 기능 
### 1. 로또 번호 확인 페이지
  + 스피너로 날짜를 선택한 후 당첨 번호와 당첨 금액 확인
  + 번호 검색 기능

### 2. 이벤트 페이지
  + 사용자는 하루 1회 이벤트 참여 가능
  + 이벤트 페이지 하루 내 최초 접속 시 전면 광고
  + 이벤트 페이지 앱 설치 이후 최초 1회 사용자 이름, 전화번호 등록

### 2-2. 전화번호 등록 알고리즘
  + TelephonyManager.line1Number 값이
  "" or null이면 임의의 전화번호 등록 가능
  값이 있으면 그 값과 일치하는 전화번호만 등록 가능
  
### 3. 번호 생성 페이지
  + 스크래치를 긁어 랜덤으로 번호 세개 생성
  + 오른쪽 하단 첫 번째 버튼으로 새로고침
  + 오른쪽 하단 두 번째 버튼으로 공유
  
### 4. 메모장 페이지
  + 생각한 번호등 여러 내용의 메모를 생성
  + 추가, 삭제 기능
  
### 5. 관리자 페이지
관리자 권한을 부여받은 경우 접근 가능한 페이지
   + 이벤트 당첨자 확인
      + 일별로 이벤트 당첨자의 이름, 전화 번호, 트루머니 번호 확인
   + 이벤트 당첨 관리
      + 일별 이벤트 최대 이벤트 당첨자 수와 당첨 비율 설정
   + 이벤트 기능 사용
      + 이벤트 탭 활성화 여부 설정
    
### 6. 오른쪽 상단 메뉴
  + 알림 설정
      + 7일동안 앱 접속 없으면 알림 받을지 여부 설정
  + APP SHARE
      + 플레이스토어 링크 공유
  + 전화번호 재 등록
      + 이벤트 당첨 선물을 받기위한 전화번호 수정
      
## 사용한 라이브러리
  + 이벤트, 번호 생성 페이지의 scratchView  
  https://github.com/myinnos/AndroidScratchCard
  
## 당첨 번호 api
  https://rapidapi.com/limestudio/api/thailand-national-lottery
