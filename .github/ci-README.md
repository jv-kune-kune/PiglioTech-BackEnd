# Backend CI Pipeline and SonarQube Integration
**Last Updated: 2025-01-16 (subject to change as the CI process evolves)**

## Table of Contents
1. [Overview](#overview)
2. [Key Files and Configuration](#key-files-and-configuration)
3. [Workflow Triggers and Steps](#workflow-triggers-and-steps)
4. [SonarQube Analysis](#sonarqube-analysis)
5. [Validating Checks and Handling Failures](#validating-checks-and-handling-failures)
6. [Commit, Push, and PR Process](#commit-push-and-pr-process)
7. [Further Considerations](#further-considerations)

---

## Overview
This document describes our backend CI pipeline and how SonarQube is integrated to maintain code quality. Our pipeline:
- Compiles, tests, and verifies code using Maven.
- Checks code style with Checkstyle and Spotless.
- Performs static analysis with SpotBugs.
- Produces code coverage reports via JaCoCo.
- Uploads results to SonarQube (SonarCloud in this case).
- Includes CodeQL security scans and dependency reviews on pull requests.

> **Note**: This process is under active development. Expect updates and improvements over time.

---

## Key Files and Configuration
1. **`pom.xml`**
    - Manages project dependencies (Spring Boot, Postgres, etc.).
    - Declares plugins for JaCoCo, Spotless, Checkstyle, SpotBugs, and Sonar Maven Plugin.
    - Sets the Java version to 21.
    - Defines Sonar-related properties (Sonar host, organization, etc.).

2. **`Java CI Pipeline (ci.yml)`**
    - Stored in `.github/workflows/Java CI Pipeline.yml` (or similar).
    - Includes steps for checkout, setting up JDK, caching, analysis, and artifact upload.
    - Integrates SonarQube (SonarCloud) by running the Maven goal `sonar:sonar`.
    - Performs CodeQL checks and dependency review on pull requests.

3. **`README.md` (High-level repository docs)**
    - Contains instructions or references for creating new issues on PR, general usage instructions, and other context.
    - Should be used to guide new contributors on basic steps (commit/push/PR).

---

## Workflow Triggers and Steps
The CI pipeline runs automatically under these conditions:
- **Push Events** on the `main` branch.
- **Pull Requests** targeting the `main` branch.

### High-Level Pipeline Stages

1. **Checkout**
    - Uses `actions/checkout@v3` to fetch the repository code at the requested commit/branch.

2. **JDK Setup**
    - Configures Java 21 using `actions/setup-java@v3`.

3. **Cache Sonar & Maven**
    - Caches SonarQube packages (~/.sonar/cache) and Maven dependencies (~/.m2) to speed up subsequent builds.

4. **Spotless Check**
    - Ensures code formatting is consistent.
    - If formatting issues exist, you’ll need to fix them locally and re-commit.

5. **Checkstyle & Maven Site**
    - Generates Maven site reports, including Checkstyle HTML (`target/site/checkstyle.html`).
    - This report is uploaded as a build artifact.

6. **SpotBugs**
    - Analyzes code for potential bugs, creating an XML report (`target/spotbugsXml.xml`).
    - Uploaded as a build artifact.

7. **Build, Test, and SonarQube Analysis**
    - Invokes `mvn clean verify` followed by the Sonar Maven Plugin (`sonar:sonar`).
    - Publishes coverage and analysis results to SonarCloud.

8. **JaCoCo Coverage Report**
    - Coverage HTML files located under `target/site/jacoco/`.
    - Uploaded as an artifact for review.

9. **CodeQL Analysis**
    - Performs security analysis on the code.
    - After building again (`mvn clean verify`), the CodeQL step scans for vulnerabilities.

10. **Dependency Review**
- Checks if new or updated dependencies introduce vulnerabilities.
- Fails for severities >= `low` for runtime dependencies (in pull requests).

---

## SonarQube Analysis
SonarQube (SonarCloud) integration is handled by the `sonar-maven-plugin`:
- Key properties in `pom.xml`:
    - `sonar.organization` set to **`jv-kune-kune`**.
    - `sonar.host.url` set to **`https://sonarcloud.io`**.
- During the pipeline, Maven runs:
  ```bash
  mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
    -Dsonar.projectKey=jv-kune-kune_PiglioTech-BackEnd \
    -Dsonar.organization=jv-kune-kune \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.login=${{ secrets.SONAR_TOKEN }} \
    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
    -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml \
    -Dsonar.java.spotbugs.reportPaths=target/spotbugsXml.xml
- Ensure SONAR_TOKEN is stored in GitHub Actions secrets.

## Validating Checks and Handling Failures
1. **PR Checks**
    - Pull request checks (e.g., Spotless, Checkstyle, SpotBugs, JaCoCo, SonarQube, CodeQL) must all pass.
    - Failures appear on the PR checks panel.

2. **Rerunning the Pipeline**
    - If a job fails due to intermittent issues, you can manually re-run the workflow from the GitHub Actions tab.
    - Alternatively, push a new commit or rebase to trigger a fresh run.

3. **Addressing Issues**
    - **Formatting/Checkstyle**: Run `mvn spotless:apply` locally to fix formatting, or manually adjust the code.
    - **Sonar/SpotBugs**: Investigate reported issues or code smells and fix them before pushing again.
    - **Test Failures**: Check logs in the Actions tab to see which tests failed. Update tests or code accordingly.

## Commit, Push, and PR Process
1. **Local Development**
    - Create or update a branch off `main`.
    - Implement changes, fix issues, or add features.
    - Test locally with `mvn clean verify`.

2. **Commit & Push**
    - Keep your commit messages clear and descriptive.
    - Push your branch to the remote repository (`git push origin feature/my-changes`).

3. **Open a Pull Request**
    - From your branch, open a PR into `main`.
    - Fill in the PR description, linking any relevant issues or tasks.

4. **Review Pipeline Checks**
    - Verify that the pipeline passes on your PR.
    - If any step fails, fix locally, then push again to re-run.

5. **Merge**
    - Once the pipeline is green and the PR is approved, merge into `main`.

## Further Considerations
- **Local Checks**:
    - Running `mvn spotless:check` and `mvn checkstyle:check` before committing can help catch style issues early.
    - Running `mvn clean verify` ensures tests and analysis pass locally before pushing.

- **Version Control**:
    - Use short-lived feature branches to keep changes isolated.
    - Rebase often to stay updated with `main` to avoid merge conflicts.

- **Scaling Up**:
    - As the project grows, you may add more plugins or refine quality gates in SonarQube.
    - Adjust thresholds for coverage or failing conditions for SpotBugs/Checkstyle if needed.

---

**Remember**:  
This workflow and documentation are subject to change as our CI evolves. Always refer to the latest `ci.yml` and updated project docs for the current state of the build process.
