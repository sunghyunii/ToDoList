echo "> 애플리케이션 실행"
JAR_NAME=$(ls /home/ubuntu/app/build/*.jar | tail -n 1)
nohup java -jar "$JAR_NAME" > /home/ubuntu/app/nohup.out 2>&1 &
