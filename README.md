# match — Hello World used to teach the release journey

Code → CI → Tests → Artifact → Docker → Registry → Deployment → Health → Rollback

Read **[DEMO.md](DEMO.md)** if you are teaching or rehearsing the 60-minute session.

## What this app does

| URL | Response |
|---|---|
| `GET /` | `Hello World` |
| `GET /health` | `{"status":"UP","version":"1.0.0"}` |

Local:

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
java -jar target\app.jar
```

Open http://localhost:8081/

## Pipeline

GitHub Actions file: `.github/workflows/ci.yml`

1. **Detective** — `./mvnw test` then `./mvnw package` → `target/app.jar`
2. **Factory** — build a Docker image from that same JAR
3. **Warehouse** — push `ghcr.io/<owner>/cm-app-for-corporates:<git-sha>`
4. **Truck** — promote the same image through DEV → STAGE → PROD
