# 🚀 2단계 - 칸반 보드 관리(수정)

## 기능 요구 사항
- 태스크 카드를 클릭하여 수정/삭제할 수 있다.
- 태스크가 수정/삭제되었을 때 스낵바를 노출한다.
- Review 상태를 추가하고, 상태별 태스크의 특징을 적용한다.
- To Do: 태스크 삭제 가능. 담당자 미지정 가능
- In Progress: 태스크 삭제 가능. 담당자 지정 필수
- Review: 태스크 삭제 불가능. 담당자 지정 필수
- Done: 태스크 삭제 불가능. 담당자 지정 필수
- 태스크 상태 전이 규칙을 적용한다. 규칙에 정의되지 않은 상태 전이는 불가능하다.

## 리팩토링
### 0차
- Review State 추가
- ManagerState nullable로 수정
- Board 내 제어로직 및 API 추가
- Card 내 업데이트 로직 추가

### 1차
- [x] BoardTest 내 when 문 분기 중 빈 람다에 따른 거짓 양성 가능성 제거
- [x] Board update 메서드의 책임위임 일관성 이슈 관련 수정
- [ ] BoardScreen 내 행위 주체에 따른 책임 이전 및 확장함수 제거
- [ ] 상태 판단 책임을 UI > CardTaskState 이전 및 UI 상 상태 비교 로직 제거
- [ ] slot API 활용, 기존 if/when UI 분기 로직 수정

### 1차 피드백
- [x] BoardManageResult 개선 및 FailureReason 도입을 통한 Board-Card 간 순환참조 제거
- [x] CardUpdateResult 개선 및 FailureReason 도입을 통한 Board-Card 간 순환참조 제거
- [x] CardTaskState에 전이 로직을 포함시켜, validateTransition의 책임 과중에 따른 부작용 해결
- [x] 보드 UI 로직 내 최상위 함수(createCard, editCard) 제거 및 CardEditorState 확장함수로 변경
- [x] 상태 전이/수정 정책을 CardTaskState 내부로 이동시켜 상태가 자신의 규칙을 직접 판단하도록 변경
- [x] Board Unit 테스트 내 불필요한 테스트 / UI에 의존하는 테스트 제거
- [x] !! 대신 labeled return(local return) 활용
- [x] VIEW 모드 등 확장 가능성 대응을 위한 display state 작성
- 드래그 앤 드롭 테스트 수정 실패 
### UI

- [x] 태스크 카드 클릭 시 수정/삭제 다이얼로그를 노출한다.
- 선택된 상태에 따라 담당자 '없음' 버튼 활성화 여부 변경
  - [x] To Do 단계에서는 '없음' 버튼이 제공된다.
  - [x] In Progress 단계부터는 '없음' 버튼이 제공되지 않는다.
- 수정/삭제 버튼 클릭 시 스낵바를 노출한다.
  - [x] Review / Done 상태에서는 '태스크 삭제가 불가합니다.' snackbar를 노출한다.
  - [x] 태스크가 정상 수정되었을 시 '태스크가 수정되었습니다.' snackbar를 노출한다.
  - [x] 태스크가 정상 삭제되었을 시 '태스크가 삭제되었습니다.' snackbar를 노출한다.
- 불가능한 전이를 시도하면 snackbar를 노출한다.
  - [x] 담당자 없음, To Do 상태의 태스크가 In Progress 상태로 전이를 시도할 시, '담당자를 지정해야 상태를 옮길 수 있습니다.' snackbar를 노출한다.
  - [x] 아래의 경우'해당 상태로 옮길 수 없습니다.' snackbar를 노출한다.
    - [x] 담당자 지정, To Do 상태의 태스크가 In Progress 이외의 상태로 전이를 시도할 시.
    - [x] In Progress 상태의 태스크가 Done 상태로 전이를 시도할 시.
    - [x] Review 상태의 태스크가 To Do 상태로 전이를 시도할 시.
    - [x] Done 상태의 태스크가 To Do 이외의 상태로 전이를 시도할 시.
- [x] 태스크가 수정되면 카드 UI에 변경사항이 즉시 반영된다.
- [x] 태스크가 삭제되면 카드 UI가 즉시 목록에서 제거된다.
- [x] 태스크 수정/삭제 시 완료율 텍스트와 프로그레스 바가 즉시 갱신된다.

### Domain
- 태스크 수정 기능을 제공한다.
  - [x] 기존 태스크의 제목, 내용, 태그, 담당자, 상태를 새로운 태스크 정보로 갱신할 수 있다.
  - [x] 수정 시에도 기존 생성 규칙과 동일한 유효성 검증을 적용한다.
- 태스크 삭제 기능을 제공한다.
  - [x] To Do, In Progress 상태의 태스크는 삭제할 수 있다.
  - [x] Review, Done 상태의 태스크는 삭제할 수 없다.
- 상태별 담당자 지정 규칙을 적용한다.
  - [x] To Do 상태는 담당자 미지정을 허용한다.
  - [x] In Progress, Review, Done 상태는 담당자 지정이 필수다.
- 태스크 상태 전이 규칙을 적용한다.
  - [x] 담당자 없음, To Do 상태의 태스크는 In Progress 상태로 전이할 수 없다.
  - [x] 담당자 지정, To Do 상태의 태스크는 In Progress 상태로만 전이할 수 있다.
  - [x] In Progress 상태의 태스크는 Done 상태로 전이할 수 없다.
  - [x] Review 상태의 태스크는 To Do 상태로 전이할 수 없다.
  - [x] Done 상태의 태스크는 To Do 상태로만 전이할 수 있다.
- [x] 불가능한 상태 전이 시 실패 결과를 반환한다.
- [x] 수정 / 삭제 / 상태 전이 시 Board의 상태 별 태스크 개수가 즉시 반영된다.
- [x] 태스크 수정 / 삭제 시 변경사항이 즉시 반영된다.

## 테스트

### UI

- [x] 태스크 카드를 클릭하면 수정 / 삭제 다이얼로그가 노출된다.
- [x] 다이얼로그에서 To Do 상태 선택 시 담당자 '없음' 버튼이 노출된다.
- [x] 다이얼로그에서 To Do 이외의 상태를 선택 시 담당자 '없음' 버튼이 노출되지 않는다.
- [x] 태스크 수정 성공 시 '태스크가 수정되었습니다.' snackbar가 노출된다.
- [x] 태스크 삭제 성공 시 '태스크가 삭제되었습니다.' snackbar가 노출된다.
- [x] Review / Done 상태 태스크 삭제 시도 시 '태스크 삭제가 불가합니다.' snackbar가 노출된다.
- [x] 태스크 수정 후 카드 정보가 즉시 변경된다.
- [x] 태스크 삭제 후 카드가 목록에서 제거된다.

### Domain

- [x] To Do / In Progress 상태 태스크는 삭제할 수 있다.
- [x] Review / Done 상태 태스크는 삭제할 수 없다.
- [x] 담당자 없는 To Do 상태 태스크는 In Progress 상태로 전이할 수 없다.
- [x] 담당자 있는 To Do 상태 태스크는 In Progress 상태로 전이할 수 있다.
- [x] 담당자 있는 To Do 상태 태스크는 Review 상태로 전이할 수 없다.
- [x] 담당자 있는 To Do 상태 태스크는 Done 상태로 전이할 수 없다.
- [x] In Progress 상태 태스크는 Review 상태로 전이할 수 있다.
- [x] In Progress 상태 태스크는 Done 상태로 전이할 수 없다.
- [x] Review 상태 태스크는 Done 상태로 전이할 수 있다.
- [x] Review 상태 태스크는 To Do 상태로 전이할 수 없다.
- [x] Done 상태 태스크는 To Do 상태로 전이할 수 있다.
- [x] Done 상태 태스크는 In Progress 상태로 전이할 수 없다.
- [x] Done 상태 태스크는 Review 상태로 전이할 수 없다.
- [x] 태스크 수정 / 삭제 시 Board의 상태별 태스크 개수가 변경된다.
- [x] 태스크 수정 / 삭제 시 완료율이 변경된다.
- [x] 태스크 수정 / 삭제 시 프로그레스 계산값이 변경된다.