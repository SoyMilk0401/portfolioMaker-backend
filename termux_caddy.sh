# chmod +x termux_caddy.sh
# pkg update -y && pkg install caddy -y

DOMAIN="hunpu.asuscomm.com"
SPRING_PORT="8080"
CADDY_HTTP="5412"
CADDY_HTTPS="5413"

echo "🚀 [Start] Caddy Auto-HTTPS Mode..."

# Caddyfile 자동 생성
echo "📝 Caddyfile 생성 중 (자동 발급 설정)..."

cat > Caddyfile <<EOF
{
    http_port $CADDY_HTTP
    https_port $CADDY_HTTPS
}

$DOMAIN {
    reverse_proxy localhost:$SPRING_PORT
}
EOF

echo "✅ Caddyfile 생성 완료."

# 실행
echo "▶️  Caddy 서버 실행 (Auto HTTPS)..."
echo "   - 백엔드 연결: localhost:$SPRING_PORT"
echo "   - Caddy 내부 포트: $CADDY_HTTPS (HTTPS)"

# 포맷팅 후 실행
caddy fmt --overwrite Caddyfile > /dev/null
caddy run