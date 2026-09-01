# The Release Journey — instructor notes for THIS repo

This project is a tiny Spring Boot **Hello World** used to teach production-grade CI/CD.

You are not teaching a list of tools. You are telling the story of how a change earns the right to reach production.

## The one picture (draw this first, keep it on screen all hour)

```
👨‍💻 RAHUL          git push / Pull Request
        ↓
🗂️ GIT              source of truth   (this GitHub repo)
        ↓
🕵️ CI               checkout → test → package     (.github/workflows/ci.yml job: detective)
        ↓
🏭 FACTORY          JAR → Docker image            (Dockerfile + job: factory)
        ↓
📦 REGISTRY         ghcr.io/.../match:7f3a91c     (GitHub Container Registry)
        ↓
🟢 DEV → 🟡 STAGE → 🔴 PROD                       (jobs: deploy-dev / stage / prod)
        ↓
📊 CONTROL ROOM     GET /health
        ↓
👥 USERS            healthy → success   broken → rollback to previous tag
```

Three questions behind the whole architecture:

1. Can we trust the change? → **CI**
2. Can we identify and move the exact thing we tested? → **Artifact + Registry**
3. Can we release and recover safely? → **CD + health check + rollback**

## Cast (say these names all hour)

| Character | Role | In this repo |
|---|---|---|
| Rahul | Developer | `git push` |
| CI | Quality detective | job `detective` |
| Build system | Factory | `mvn package` + `docker build` |
| Registry | Warehouse | GitHub Container Registry (`ghcr.io`) |
| CD | Delivery truck | jobs `deploy-dev` → `deploy-stage` → `deploy-prod` |
| Observability | Control room | `GET /health` |
| Users | Audience | they feel success or failure |

## Files you will open during the demo

| File | What to say |
|---|---|
| `src/main/java/com/corp/match/controller/HelloController.java` | "This is Rahul's change." |
| `src/test/java/com/corp/match/HelloControllerTest.java` | "This is the evidence CI will demand." |
| `pom.xml` | "Maven is the factory for the JAR. `finalName` is `app`, so we get `target/app.jar`." |
| `Dockerfile` | "The JAR is the app. The image is the same app, sealed with a Java runtime." |
| `.github/workflows/ci.yml` | "Trigger → detective → factory → warehouse → truck." |

## Local rehearsal (do this the night before)

From the project folder, in PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
java -jar target\app.jar
```

Then in a browser: [http://localhost:8081/](http://localhost:8081/) should show `Hello World`.  
[http://localhost:8081/health](http://localhost:8081/health) should show `"status":"UP"`.

If Docker Desktop is installed:

```powershell
docker build -t match:1.0.0 .
docker run --rm -p 8081:8081 match:1.0.0
```

Same URLs. Say: "Rahul's laptop, CI, and production now run the same sealed box."

## Live demo runbook (15 minutes)

1. **Open the repository** — "This is our source of truth."
2. **Open** `.github/workflows/ci.yml` — point to `on:` (trigger), `jobs:` (detective then factory), `needs: detective` ("the factory does not ship until the detective approves").
3. **Show a green run** in the Actions tab — "Watch the detective work."
4. **Connect Maven output to gates** — `mvn test` then `mvn package`. If tests fail, no JAR, no image, no deploy.
5. **Show Docker tags** — `match:1.0.0` and `match:<7-char-sha>`. Ask: "What does `latest` tell you?" Answer: almost nothing.
6. **Packages tab** (after the first successful push) — "The warehouse. Git answers what source we have. The registry answers what images we can deploy."
7. **Fail on purpose** (the moment the room remembers):

In `HelloController`, change:

```java
return "Hello World";
```

to:

```java
return "Hello World!";
```

Commit, push, open Actions. The detective goes red. No image is pushed. Then revert, push again, pipeline goes green.

Say: "A red pipeline isn't always bad news. It caught a bad change before production. That's a success."

## GitHub setup (one-time, before class)

1. Push this branch to GitHub.
2. Repo → **Settings → Actions → General** → allow GitHub Actions, and allow read/write for `GITHUB_TOKEN` (needed to push images to GHCR).
3. Repo → **Settings → Environments** and create:
   - `development` — no approval (fast feedback)
   - `staging` — optional reviewers
   - `production` — **Required reviewers** (this is Continuous Delivery: the truck waits at the gate)

If Docker Hub / cloud credentials are missing, the workflow still teaches the story: DEV/STAGE/PROD jobs print which image they would promote. The diagram still makes sense. The demo is evidence, not the lesson.

## What each pipeline job removes

| Job | Uncertainty it removes |
|---|---|
| `detective` | "Does it compile and pass tests?" |
| `factory` | "Do we have one identifiable, sealed image?" |
| `deploy-dev` | "Can developers see it quickly?" |
| `deploy-stage` | "Does it look production-like?" |
| `deploy-prod` | "Are we ready for real users?" |

Build **once**. Promote the **same** image. Never rebuild a different binary per environment.

## Rollback story (Friday 7:15 PM)

PROD is running `match:abc1234` (version 1.0.0 of this commit). Error rate jumps. Do we panic?

No. The previous SHA tag is still in the warehouse. Point PROD back at it. That only works because we tagged images by commit and kept old artifacts.

## Hardening (say this at the end, do not demo all of it)

The basic pipeline is: Build → Test → Package → Image → Deploy.

Production-grade adds layers: secrets and least privilege, image scanning, integration tests, SBOM, approvals, observability, rollback. Teach them as layers on top of this pipeline, not as a second course.

Skip Kubernetes, Helm, Prometheus, and EKS unless someone asks.

## Answers you should have ready

**Why not deploy source code?**  
Production should run a tested, identifiable, reproducible artifact — not Rahul's laptop folder.

**Why Docker if we already have a JAR?**  
The JAR is the application. The image packages it with a predictable runtime.

**Why not rebuild for STAGE and PROD?**  
Rebuilding can produce a different artifact. Promoting the same one removes that uncertainty.

**What if tests pass but production breaks?**  
Tests are not proof of everything. That is why we still need staged rollout, `/health`, and rollback.

**Where do passwords live?**  
In GitHub Actions secrets or a secret manager — never in Git. This repo uses `GITHUB_TOKEN` for GHCR. No passwords in files.

**Does CI/CD mean no humans?**  
No. Automation removes repetitive work. Humans stay at review and risk-based approval gates (the PR + the production environment).

**What makes a pipeline production-grade?**  
Fast feedback, reliable artifacts, security, traceability, safe promotion, observability, recovery.

## Closing (60 seconds)

Forget the tool names for a moment. CI/CD is a story about reducing uncertainty.

Rahul makes a change. A detective verifies it. A factory creates a known artifact. A warehouse stores it. A delivery truck promotes that same artifact through controlled environments. A control room watches the real world. If the release misbehaves, we have an emergency exit.

The goal is not to make releases exciting. It is to make them boring, repeatable, traceable, and safe.
