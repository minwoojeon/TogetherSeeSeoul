# 2018년 서울시 앱 공모전 출품작(가치서울/TogetherSeeSeoul)
2018년 서울시 앱 공모전 출품작(가치서울/TogetherSeeSeoul)

# 기능
 1) 서울시 열린데이터광장의 문화행사 서비스를 통해 일반적인 리스트 조회, 상세조회 기능 제공.
 2) 조회수/공유횟수 (100, 500, 1000 건) 이상일 때, 리본 생성.
 3) 공유하기 기능은 상세화면에서는 URL 첨부 기능, 아닌 경우 화면만 캡쳐하여 첨부하는 기능으로 제공.
 4) 공유기록은 자신(핸드폰번호기준)이 다른 사용자에게 공유한 내역을 제공(앱을 재설치해도 정보는 유지).
 
# 활용 데이터 종류 : 서울시 열린데이터 광장의 문화 행사 서비스(주요기능 API), 구글 Firebase Database 기반으로 외부 DB 설계 및 연동.
 - 활용 빈도 : 
   1) 서울시 열린데이터광장의 문화행사 서비스
      1-1) [문화행사] 탭에서 장르(카테고리) 가져오기
      1-2) [문화행사] 탭에서 장르(카테고리) 선택을 하거나 기간을 정하거나 또는 명칭을 적어 조회를 하는 경우
   2) 구글 Firebase Database 누적 조회수/누적 공유 횟수 매핑 테이블
      2-1) [문화행사] 탭에서 데이터 조회시 검색을 실행(매핑 정보 확인 후, 있는 정보 매칭)
      2-2) [문화행사] 탭에서 데이터 조회시 조회수와 공유횟수(100, 500, 1000)에 따라 좌측 상단에 이미지 리본 출력
      2-3) [문화행사] 탭에서 [상세내용]으로 진입할 때, 누적조회수를 증가 시킨다(없는 매핑정보면 만들고 증가)
      2-4) [상세내용] 탭에서 [공유하기] 탭을 선택하면 공유횟수를 증가 시킨다(2-3과 유사)
   3) 구글 Firebase Database 전화번호별 공유 기록 테이블
      3-1) [상세내용] 탭에서 [공유하기] 탭을 선택하면 공유하는 문화행사정보를 기록
      3-2) [공유기록] 탭에서 내가(핸드폰 번호) 공유한 문화행사 정보리스트 출력에 활용

# 기대효과
    1) 서울시 문화행사의 보다 많은 참여를 유도할 수 있음
      1-1) 아는 사람만 참여하던 기존의 시스템에 단순한 공유기능 하나를 추가하여 다른 사람에게 정보를 전파할 수 있음
      1-2) 카카오톡, 오픈채팅, 등에서 활용될 수 있음
      1-3) 남들이 많이 찾거나(조회) 공유한 문화행사에 관심이 갈 수 있도록 리본을 제공하여 보다 많은 참여를 유도함
   2) 일종의 서울시 시민 정보 분석(데이터 과학, 분석)의 기초가 될 수 있음
      2-1) 실시간 누적조회수, 누적공유횟수를 제공하여 어떤 것은 조회만 되고 공유는 되지 않는지 분별가능하다. 이를 가지고 정부, 주최 측은 해당 지역이나 시간대 선호행사와 비선호행사를 구분하는 척도가 될 수 있음
      2-2) 현재는 저장하고 있지 않지만, 개인별(전화번호기준)로 정보를 가지므로 분석 데이터가 풍부해지면 개인별, 조직별, 성별 문화행사를 추천 할 수 있는 기준이 될 수도 있음
      
# 라이센스 및 사용 기간
  - CCL 3.0 저작권만 표기하면 어떤 방식으로 가져다가 쓰셔도 무방합니다.
  - 현재 파이어베이스 디비는 개인계정이므로, 2018-12-31 또는 서울시 앱 공모전 마감 이후 연결을 끊겠습니다. 개별적으로 세팅해주세요.
  - 공공 디비 API 키 역시 앱 공모전이 마감하면 해당 키 사용할 수 없도록 막을 예정입니다. 다른 키로 키값만 교체해서 사용해주세요.
