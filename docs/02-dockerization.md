# Чекор 2 — Докеризација (10%)

## Цел и поими

Dockerfile е рецепт за градење Docker имиџ. Имиџот ги содржи апликацијата и нејзината извршна околина. Контејнер е извршена инстанца на имиџот. За Tourism App се подготвени два Dockerfile-а: за Spring Boot веб-апликацијата и за FastAPI сервисот. За PostgreSQL ќе се користи официјалниот имиџ, без сопствен Dockerfile.

## Додадени датотеки

- `backend/Dockerfile`
- `backend/.dockerignore`
- `recommendation-service/Dockerfile`
- `recommendation-service/.dockerignore`

## Spring Boot Dockerfile

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN groupadd --system tourism && useradd --system --gid tourism tourism
COPY --from=build --chown=tourism:tourism /build/target/tourism_app-0.0.1-SNAPSHOT.jar ./app.jar
USER tourism
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

Ова е градење во две фази. Првата содржи Maven и Java 17 и го компајлира проектот во извршна JAR датотека. Прво се копира `pom.xml` и се преземаат зависностите; потоа се копира кодот. Docker може повторно да го користи слојот со зависности кога се менува само кодот.

Втората фаза содржи Java 17 JRE и го презема само извршниот JAR од првата фаза. Maven и изворниот код не се потребни за извршување. `USER tourism` ја стартува апликацијата со системски корисник без root привилегии. `ENTRYPOINT` го дефинира процесот што се извршува при стартување на контејнерот.

`-DskipTests` ги прескокнува тестовите при пакувањето на имиџот: постојниот Spring context тест бара база. Оваа build команда не е доказ дека тестовите поминале; проверките ќе се организираат посебно со достапна база.

## FastAPI Dockerfile

```dockerfile
FROM python:3.12-slim
ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1
WORKDIR /app
COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
RUN groupadd --system tourism && useradd --system --gid tourism tourism
COPY --chown=tourism:tourism app ./app
USER tourism
EXPOSE 8000
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
```

Основата е Python 3.12 со Debian slim околина. Зависностите се инсталираат од постојниот `requirements.txt`, а потоа се копира кодот. `PYTHONDONTWRITEBYTECODE` го исклучува создавањето `.pyc` датотеки, а `PYTHONUNBUFFERED` овозможува веднаш да се прикажуваат Python логовите.

Uvicorn ја извршува FastAPI апликацијата на портата 8000. `--host 0.0.0.0` овозможува пристап преку мрежниот интерфејс на контејнерот. `--reload` не се користи бидејќи имиџот е наменет за извршување на изградена верзија.

## .dockerignore и EXPOSE

`.dockerignore` ги исклучува локалните build папки, виртуелни околини, Git метаподатоци и `.env` датотеки од build контекстот. Така тие не се испраќаат до Docker builder.

`EXPOSE` ја документира портата на сервисот. Не ја објавува автоматски на компјутерот; тоа се прави со `-p` кај `docker run` или со `ports` во Docker Compose.

## Градење на имиџите

Со вклучен Docker Desktop во режим за Linux контејнери, од коренот на проектот:

```bash
docker build -t tourism-backend:local ./backend
docker build -t tourism-recommendations:local ./recommendation-service
docker image ls
```

`-t` доделува име и tag. Последниот аргумент го одредува build контекстот: папката од која Docker ги чита Dockerfile и датотеките за COPY.

## Зависност од базата

Градењето на имиџите не бара активна база, но нивното извршување бара PostgreSQL. FastAPI ги чита табелите уште при стартување, па базата и Flyway миграциите треба да бидат подготвени пред него. Целосното поврзување и редоследот на стартување ќе се обработат во Docker Compose чекорот.

Во контејнер, `localhost` се однесува на самиот контејнер. Постојните локални адреси ќе се заменат преку environment variables: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `RECOMMENDATION_SERVICE_BASE_URL` и `DATABASE_URL`.

## Статус и докази

Dockerfile-овите и `.dockerignore` датотеките се подготвени. При проверката Docker клиентот е инсталиран, но Docker engine не е достапен. Градењето и стартувањето не се потврдени. Потребно е да се стартува Docker Desktop и да се извршат build командите.

За елаборатот, по успешно градење зачувајте слики од двата Dockerfile-а и од `docker image ls` со двата имиџа. Текстот подолу е нацрт за подготовката; успешното градење се додава само по проверка.

## Текст за елаборатот

За докеризација на Tourism App се подготвени посебни Dockerfile датотеки за Spring Boot веб-апликацијата и FastAPI сервисот за препораки. Spring Boot Dockerfile користи две фази: Maven со Java 17 за градење на извршен JAR и Java 17 JRE за негово извршување. FastAPI Dockerfile користи Python 3.12 slim, ги инсталира зависностите од requirements.txt и ја стартува апликацијата со Uvicorn на портата 8000. Двата сервиси се конфигурирани да се извршуваат со корисник без root привилегии. Со .dockerignore се исклучуваат локални и генерирани датотеки од build контекстот. За PostgreSQL се предвидува користење на официјалниот Docker имиџ.
