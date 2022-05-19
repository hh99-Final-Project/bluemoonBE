# 현재 실행중인 port 확인, 다음에 사용할 port 확인
# 다음에 사용할 port 번호로 실행중인 container가 있다면 죽임

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

# toDo: 다음에 올라가야 할 도커가 살아있다면 죽여주는 과정이 필요함
CONTAINER_ID=$(docker container ls -f "name=${TARGET_PORT}" -q)

echo "> 컨테이너 ID:  ${CONTAINER_ID}"

if [ -z ${CONTAINER_ID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> docker stop ${TARGET_PORT}"
  sudo docker stop ${TARGET_PORT}
  echo "> docker rm ${TARGET_PORT}"
  # 컨테이너 이름을 지정해서 사용하기 때문에.. 꼭 컨테이너 삭제도 같이 해주셔야 합니다.
  # (나중에 다시 띄울거기 때문에..)
  sudo docker rm ${TARGET_PORT}
  sleep 5
fi