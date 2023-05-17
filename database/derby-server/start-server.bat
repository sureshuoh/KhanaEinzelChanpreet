set CLASSPATH=lib\derby.jar;lib\derbynet.jar

for /f "tokens=1-2 delims=:" %%a in ('ipconfig^|find "IPv4"') do set ip=%%b
set ip=%ip:~1%
java -cp lib\derby.jar;lib\derbynet.jar org.apache.derby.drda.NetworkServerControl start -h %ip% -p 51527
pause