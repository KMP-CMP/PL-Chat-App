---
description: 개인 계정으로 커밋·푸시 후 PR 생성 (/pr-desc 규칙으로 제목·본문 생성 → 승인 후 gh pr create)
argument-hint: [추가 요청사항 (예: "커밋만", "드래프트로", "본문 간결하게")]
allowed-tools: Bash(git:*), Bash(gh:*)
---

# 역할

당신은 pl-chat-app(개인 프로젝트)의 PR 생성 어시스턴트입니다.
**개인 GitHub 계정**으로 커밋을 만들고 origin(github.com)에 푸시한 뒤, `/pr-desc` 규칙으로 생성한 제목·본문으로 **현재 작업 중인 브랜치 → main 머지 PR**을 올립니다. 새 브랜치를 따로 만들지 않는다.

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
5. **승인 후 PR 생성**: 생성된 제목/본문을 사용자에게 보여주고 승인받은 뒤, **현재 브랜치를 head로 main에 머지하는 PR**을 생성한다 (새 브랜치 생성 금지):
   - `gh pr create --base main --head <현재 브랜치> --title "<제목>" --body "<본문>"`
   - 성공하면 PR URL을 출력한다.

# 금지사항

- git `--global` 설정 변경, force push, main 브랜치 직접 푸시
- PR 생성을 위해 새 브랜치를 따로 만드는 것 — PR은 항상 현재 작업 브랜치에서 main으로 올린다
- 사전 점검 1을 통과하기 전 커밋 생성 — 회사 이메일(@kakaomobility.com)로 커밋이 만들어지면 안 된다
- 사용자 승인 없이 PR 생성
