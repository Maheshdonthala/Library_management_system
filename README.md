# Library System (Spring Boot)

This is a simple Spring Boot application that manages books. It serves both server-rendered pages (Thymeleaf) and JSON REST APIs under `/api/books`.

Key features:
- Thymeleaf templates: `/books` and `/add-book` for UI
- REST API: `/api/books` for public access (CORS enabled)
- MongoDB for persistence (configure via MONGODB_URI or spring.data.mongodb.uri)

## Run locally

1. Ensure you have Java 17 and Maven installed.
2. Start a MongoDB instance (local or Atlas). Note the connection URI.
3. Set the environment variable `MONGODB_URI` (or edit `src/main/resources/application.properties`):

Windows PowerShell example:

```powershell
$env:MONGODB_URI = 'mongodb://localhost:27017/librarydb'; mvn spring-boot:run
```

On other environments set `MONGODB_URI` accordingly.

## MongoDB Compass

Open MongoDB Compass and connect to the same URI you used above. The database `librarydb` (or the one you used) will contain a `books` collection.

## Docker

Build the jar and image:

```powershell
mvn -DskipTests package; docker build -t librarysystem:latest .
```

Run with MongoDB URI passed in:

```powershell
docker run -e MONGODB_URI='mongodb://host.docker.internal:27017/librarydb' -p 8080:8080 librarysystem:latest
```

Note: `host.docker.internal` works on Docker Desktop for Windows to reference the host machine.

## Deploying publicly

- Vercel is primarily for frontends. For a backend API consider providers like Render, Railway, Fly.io, Google Cloud Run, or deploy a container on Docker Hub + a host.
- Provide `MONGODB_URI` as an environment variable in the chosen host's configuration.

## CORS and public API

The project includes a global CORS config allowing `*` for `/api/**` endpoints. For production, change this to the specific frontend origin(s) for security.

## Connect to MongoDB Atlas (step-by-step)

1. Create an Atlas account at https://www.mongodb.com/cloud/atlas if you don't already have one.
2. Create a free cluster (Shared Cluster) and wait for it to provision.
3. Network Access: Add your IP address (or add `0.0.0.0/0` for testing only).
4. Database Access: Create a database user with a username and strong password. Give it the `readWrite` role for your application database (e.g., `librarydb`).
5. Click "Connect" → "Connect your application" → Select the driver (Java) and copy the provided connection string. It will look like:

	mongodb+srv://<username>:<password>@cluster0.xyz.mongodb.net/librarydb?retryWrites=true&w=majority

6. Use that connection string as the `MONGODB_URI` environment variable for your app. Example (PowerShell):

```powershell
$env:MONGODB_URI = 'mongodb+srv://atlasUser:AtlasPass@cluster0.xyz.mongodb.net/librarydb?retryWrites=true&w=majority'
mvn spring-boot:run
```

7. In MongoDB Compass paste the same SRV connection string in the connection field and connect (Compass will enable TLS automatically for SRV URLs).

Security reminder: Never commit credentials to version control. Use environment variables or a secrets manager in production.

## .env.sample

Create a `.env` file in your project root (do not commit it) with your Atlas connection string:

```
MONGODB_URI=mongodb+srv://atlasUser:AtlasPass@cluster0.xyz.mongodb.net/librarydb?retryWrites=true&w=majority
```

Then load it into PowerShell before running (example using a simple dot-source technique if you have a local script to import .env variables), or set the env var directly as shown above.

## Deployment recommendations

This is a Java Spring Boot backend that serves server-side rendered pages and JSON APIs. Here are recommended hosts and the tradeoffs:

- Render (recommended for beginners): simple, supports Docker or native Maven build, easy environment variable management, free tier available for testing.
- Railway: easy to use, quick deployments from GitHub, supports environment variables; watch free-tier limits.
- Fly.io: great for low-latency global deployment and Docker-based deploys.
- Google Cloud Run: highly scalable, pay-as-you-go, deploy using the Dockerfile.
- Heroku: classic PaaS but free tier retired; still usable with paid plans.
- Vercel: primarily for frontends and serverless functions. You can deploy this backend on Vercel only via a Docker container (requires a paid plan for full container support) — not ideal unless you need everything in one Vercel project.

### Quick Render deployment (Docker)

1. Push your repo to GitHub.
2. In Render dashboard -> New -> Web Service -> Connect to GitHub repo.
3. Choose Docker as the environment (Render will use your `Dockerfile`).
4. Add environment variables in Render -> Environment -> Add `MONGODB_URI` with your Atlas URI.
5. Deploy — Render will build the image and run the container. The service will use the `PORT` environment variable automatically.

### Quick Google Cloud Run (container) deployment

1. Build and push the Docker image to Google Container Registry or Artifact Registry.
2. Example (gcloud):

```powershell
gcloud builds submit --tag gcr.io/<PROJECT-ID>/librarysystem:latest
gcloud run deploy librarysystem --image gcr.io/<PROJECT-ID>/librarysystem:latest --platform managed --region <REGION> --allow-unauthenticated --set-env-vars MONGODB_URI="${MONGODB_URI}"
```

Replace `<PROJECT-ID>` and `<REGION>`, and make sure `MONGODB_URI` is set in your Cloud Run service.

### Notes for Vercel

- Vercel favors serverless functions (Node.js, etc.). For a Spring Boot app, prefer deploying to Render/Cloud Run/Heroku/Fly and host the frontend on Vercel if desired.
- If you must deploy to Vercel, use a Docker deployment (paid plan) and set `MONGODB_URI` in Vercel's Environment Variables.

### Environment variables

Make sure to set at least:
- `MONGODB_URI` — the full Atlas connection string.
- (Optional) `PORT` — most platforms set this automatically; `application.properties` falls back to 8080.

