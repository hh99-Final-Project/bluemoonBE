# 도커허브 이미지 pull
# 다음 실행할

CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1) TARGET_PORT=0
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

echo 'download docker image from docker hub!!!!!!!!!'
sudo docker pull lion8548/spring-cicd
# 도커 run

echo "start check code deploy env"
echo "${JASYPT_ENCRYPTOR_PASSWORD}"
echo "${JASYPT_ENCRYPTOR_REPEAT}"
echo "finish check code deploy env"

password=$(aws ssm get-parameters --region ap-northeast-2 --names JASYPT_ENCRYPTOR_PASSWORD --with-decryption --query Parameters[0].Value)
echo "start check code deploy env2"
echo "${password}"
repeat=$(aws ssm get-parameters --region ap-northeast-2 --names JASYPT_ENCRYPTOR_REPEAT --with-decryption --query Parameters[0].Value)
echo "${repeat}"
echo "finish check code deploy env3"

echo 'run docker!!!!!!!!!!!!!!!!!'
#sudo docker run -it --name ${TARGET_PORT} -d -p ${TARGET_PORT}:8080 -e active=${TARGET_PORT} lion8548/spring-cicd:latest
sudo docker run -it --name ${TARGET_PORT} -d -p ${TARGET_PORT}:8080 -e active=${TARGET_PORT} -e JASYPT_ENCRYPTOR_PASSWORD=${password:1:-1} -e JASYPT_ENCRYPTOR_REPEAT=${repeat:1:-1} lion8548/spring-cicd:latest
#echo "sudo docker run -it --name ${TARGET_PORT} -d -p ${TARGET_PORT}:8080 -e active=${TARGET_PORT} lion8548/spring-cicd:latest"
echo "sudo docker run -it --name ${TARGET_PORT} -d -p ${TARGET_PORT}:8080 -e active=${TARGET_PORT} -e JASYPT_ENCRYPTOR_PASSWORD=${password:1:-1} -e JASYPT_ENCRYPTOR_REPEAT=${repeat:1:-1} lion8548/spring-cicd:latest"
echo 'good!!!!!!!!!!!!!!!!!'

echo "> Start health check of WAS at 'http://127.0.0.1:${TARGET_PORT}' ..."

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10

do
  echo "> #${RETRY_COUNT} trying..."
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:${TARGET_PORT}/api/health)

  if [ ${RESPONSE_CODE} -eq 200 ]; then
    echo "> New WAS successfully running"
    exit 0
  elif [ ${RETRY_COUNT} -eq 10 ]; then
    echo "> Health check failed."
    exit 1
    fi
    sleep 10
done



