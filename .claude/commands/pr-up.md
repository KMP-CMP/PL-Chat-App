---
description: 개인 계정으로 커밋·푸시 후 PR 생성 (/pr-desc 규칙으로 제목·본문 생성 → gh pr create까지 자동 진행)
argument-hint: [추가 요청사항 (예: "커밋만", "드래프트로", "본문 간결하게")]
allowed-tools: Bash(git:*), Bash(gh:*)
---

# 역할

당신은 pl-chat-app(개인 프로젝트)의 PR 생성 어시스턴트입니다.
**개인 GitHub 계정**으로 커밋을 만들고 origin(github.com)에 푸시한 뒤, `/pr-desc` 규칙으로 생성한 제목·본문으로 **현재 작업 중인 브랜치 → main 머지 PR**을 올립니다. 새 브랜치를 따로 만들지 않는다.
PR 생성까지가 이 스킬의 완료 지점이다 — 제목/본문을 보여주고 승인을 기다리며 멈추지 않는다.

# 입력

- 입력값: $ARGUMENTS
- 입력이 있으면 **추가 요청사항**으로 간주해 반영한다. (예: "커밋만 하고 PR은 올리지 마", "draft로 올려줘")

# 사전 점검 — 하나라도 실패하면 그 단계에서 중단하고 사용자에게 알린다

1. **커밋 계정**: `git config user.name` / `git config user.email`(effective 값)이 개인 계정(`윤찬` / `vsvx13@naver.com`)인지 확인한다.
   - 아니면 repo-local로만 설정한다: `git config user.name "윤찬"` / `git config user.email "vsvx13@naver.com"`
   - **`--global` 변경 금지** — 회사 레포 설정을 건드리지 않는다.
2. **gh 인증**: `gh auth status --hostname github.com`이 성공하고 활성 계정이 개인 계정 **`Yoon-Chan`**인지 확인한다.
   - `freddie-yc` 등 다른 계정이 활성이면 `gh auth switch --hostname github.com --user Yoon-Chan`으로 전환한다.
   - 로그인이 안 되어 있으면 사용자에게 `! gh auth login --hostname github.com` 실행(브라우저에서 Yoon-Chan 계정으로 인증)을 안내하고 중단한다. (회사 계정 `github.kakaocorp.com` 로그인과는 별개다)
   - 주의: 이 레포 push 권한은 Yoon-Chan에게만 있다. 다른 계정 토큰이 키체인에 남아 있으면 git push도 403이 난다.
3. **브랜치**: 현재 브랜치가 `main`이면 중단하고 feature 브랜치 생성을 제안한다.

# 진행 순서

1. **변경점 확인**: `git status` / `git diff`로 커밋 대상을 파악한다. 커밋할 변경이 없고 이미 푸시할 커밋만 있으면 커밋 단계는 건너뛴다. 작업 주제와 무관한 변경이 섞여 있으면 포함 여부를 사용자에게 확인한다.
2. **커밋**: 프로젝트 컨벤션 `[feat|fix|refactor|chore] <한글 제목>` 형식으로 커밋 메시지를 작성해 커밋한다.
3. **푸시**: `git push -u origin <현재 브랜치>` (force push 금지)
4. **PR 제목/본문 생성**: `/pr-desc` 스킬의 규칙을 그대로 따라 생성한다. 단 개인 프로젝트이므로:
   - base 브랜치는 `develop`이 아닌 `main`
   - 히스토리 섹션의 Jira 링크는 한 줄 요약 불릿으로 대체
5. **Assignee / Label 결정**:
   - **Assignee는 항상 PR을 올린 계정 본인** — `--assignee @me`로 지정한다 (활성 gh 계정 = `Yoon-Chan`).
   - **Label은 변경 성격을 보고 이 레포에 있는 것 중에서** 고른다. 여러 성격이 섞이면 둘 다 붙인다:

     | 변경 성격 | 라벨 |
     | --- | --- |
     | 기능·컴포넌트 추가, 구조 개선 (`[feat]`, `[refactor]`, `[chore]`) | `enhancement` |
     | 문서·주석 위주 변경 (`[docs]`, 학습 주석 추가, README 수정) | `documentation` |
     | 버그 수정 (`[fix]`) | `bug` |

   - 레포의 기본 라벨명은 `documentation`이다(`document` 아님). 확실하지 않으면 `gh label list`로 실제 이름을 확인하고, 없는 라벨을 붙이려다 실패하지 않게 한다.
   - 어느 쪽인지 애매하면 `enhancement`를 기본값으로 붙인다.
6. **PR 생성**: 별도 승인을 묻지 말고 바로 **현재 브랜치를 head로 main에 머지하는 PR**을 생성한다 (새 브랜치 생성 금지):
   - `gh pr create --base main --head <현재 브랜치> --title "<제목>" --body-file <경로> --assignee @me --label <라벨> [--label <라벨2>]`
   - 본문은 셸 이스케이프 사고를 피하기 위해 임시 파일에 쓰고 `--body-file <경로>`로 넘긴다.
   - 이미 같은 브랜치의 열린 PR이 있으면 새로 만들지 말고 기존 PR URL을 알려준 뒤, 본문 갱신이 필요한지 사용자에게 묻는다.
   - 입력값에 "커밋만", "푸시만", "PR은 올리지 마" 같은 요청이 있으면 그 단계에서 멈춘다. "draft" 요청이면 `--draft`를 붙인다.
7. **결과 보고**: PR URL과 함께, 실제로 올라간 제목·본문·Assignee·Label을 요약해 출력한다. 수정이 필요하면 `gh pr edit`로 이어서 고칠 수 있음을 알린다.

# 금지사항

- git `--global` 설정 변경, force push, main 브랜치 직접 푸시
- PR 생성을 위해 새 브랜치를 따로 만드는 것 — PR은 항상 현재 작업 브랜치에서 main으로 올린다
- 사전 점검 1을 통과하기 전 커밋 생성 — 회사 이메일(@kakaomobility.com)로 커밋이 만들어지면 안 된다
- PR 생성 전에 승인을 받겠다고 멈추는 것 — 사전 점검을 통과했다면 PR 생성까지 끝낸다
