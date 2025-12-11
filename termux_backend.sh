# chmod +x termux_backend.sh

echo "🚀 [Start] Spring Boot Setup & Run for Termux..."

if [ -f "./gradlew" ]; then
    chmod +x gradlew
    echo "✅ gradlew 권한 부여 완료."
else
    echo "❌ 현재 폴더에 gradlew 파일이 없습니다. git clone 된 폴더 내부인지 확인해주세요."
    exit 1
fi

# 빌드
echo "🔨 프로젝트 빌드 중... (시간이 조금 걸릴 수 있습니다)"
./gradlew clean build -x test

# 빌드 실패 시 중단
if [ $? -ne 0 ]; then
    echo "❌ 빌드 실패! 코드를 확인해주세요."
    exit 1
fi

# 빌드된 JAR 파일 찾기
JAR_FILE=$(find build/libs -name "*.jar" ! -name "*plain.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ 빌드된 JAR 파일을 찾을 수 없습니다."
    exit 1
fi

# 실행
echo "▶️  애플리케이션 실행: $JAR_FILE"
# .env는 미리 디렉토리에 생성해야함
java -jar "$JAR_FILE"