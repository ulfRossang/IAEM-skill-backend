Klart. H2-konsolen körs på http://localhost:8082 via en @ApplicationScoped CDI-bean (H2Console.java) som startar org.h2.tools.Server vid appstart.

  Anslut så här i konsolen:
  - JDBC URL: jdbc:h2:mem:epatraktor;MODE=MySQL
  - User: sa
  - Password: (tomt)
  
  OBS: vid mvn clean tas Liberty-installationen bort — använd mvn package liberty:create liberty:deploy liberty:start istället för mvn clean package liberty:deploy liberty:start.


### IAEM-skill-backend (BCE-omstrukturering)

En kopia av IAEM-backend fullständigt ombyggd enligt `microprofile-server` + `java-conventions` skills:

**BCE-paketstruktur** — 10 business components, varje med `boundary/`, `control/`, `entity/`:

| BC | Ansvar |
|---|---|
| `seeding` | `DatabaseSeeder` — seedar testdata vid uppstart |
| `kunder` | Kunduppslag |
| `meddelanden` | Meddelanden + bilagor |
| `utskick` | Utskick + installningar |
| `dokument` | Dokumentsökning |
| `kuvert` | Kuvertsökning |
| `publicering` | Informationssamband + publiceringsjobb |
| `informationsprodukter` | CRUD för informationsprodukter |
| `debiteringsuppgifter` | CRUD för debiteringsuppgifter |
| `massutskick` | CRUD + klarmarkering av massutskick |

**Skill-regler tillämpade:**
- `@Transactional` enbart i boundary-lagret
- Records med `toJSON()` / `fromJSON(JsonObject)` för alla svar och indata
- JSON-P (inte JSON-B) för serialisering — resurser returnerar `JsonObject`/`JsonArray`
- `System.Logger` (statisk `LOGGER`) i stället för `java.util.logging`
- Inga `private` metoder/fält — package-private för testbarhet
- Guard clauses, streams, `var`, method references

**Enhetstester** (JUnit 5 + AssertJ + Mockito, 10 tester):
- `KundTest` — `toJSON()` fältmappning och null → tom sträng
- `MassutskickInputTest` — `fromJSON()` parsing och defaults för saknade fält
- `MassutskickTest` — `klarmarkera()` NOT_FOUND / CONFLICT / OK via mockad `EntityManager` (möjligt tack vare package-private `em`)
- `MeddelandeRowTest` — `toJSON()` booleans, bilagor-lista, null-hantering
