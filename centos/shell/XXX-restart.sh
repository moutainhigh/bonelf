sh kill.sh XXX
nohup java -jar -Xms256m -Xmx256m -Xmn128m XXX.jar > ./logs/XXX.log &
tail -fn800 logs/XXX.log
