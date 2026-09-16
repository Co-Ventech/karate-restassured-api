# Karate + Rest Assured API


 target [Toolshop API](https://api.practicesoftwaretesting.com/api/documentation).

| Tool | Module | What it shows |
|---|---|---|
| Karate | `karate/` | Gherkin API tests, chained calls, CSV + JSON data |
| Rest Assured | `rest-assured/` | Java CRUD, JSON Schema, auth, CSV + JSON data |

## Flows

1. **Auth** — register a unique user, then login (no shared demo customer)
2. **Catalog** — `GET /products` + schema checks
3. **Cart CRUD** — create cart → add item → read → update quantity → delete item → delete cart
4. **Data-driven** — quantities from CSV and JSON

## Setup

Java 17+ and Maven 3.9+.

```bash
mvn test
```

HTML reports are written automatically when the suite finishes:

| Report | Path |
|---|---|
| Landing page | `reports/index.html` |
| Karate summary | `karate/target/karate-reports/karate-summary.html` |
| Cucumber HTML | `karate/target/cucumber-html-reports/overview-features.html` |
| Rest Assured Extent | `rest-assured/target/extent-report/index.html` |

Open the landing page after a run:

```powershell
start reports\index.html
```

Karate only:

```bash
mvn test -pl karate
```

Rest Assured only:

```bash
mvn test -pl rest-assured
```

Environment:

```bash
# PowerShell
$env:TEST_ENV="staging"
$env:API_BASE_URL="https://api.practicesoftwaretesting.com"
mvn test
```

## Layout

```
karate/            Karate features + runner
rest-assured/      Rest Assured JUnit 5 tests
reports/           Links to generated HTML reports
```

## CI

GitHub Actions runs `mvn -B test` on Java 17 and uploads Karate, Cucumber, and Extent reports as the `api-reports` artifact.
