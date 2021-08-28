set app_name sfsadf
set user root
set pass Ygf@337733
set dir /home/application
set ip 1.116.15.151
set port 22
set filen /var/jenkins_home/workspace/rubber_common_utils/common_utils/target/common_utils-1.0-SNAPSHOT.jar
spawn scp -p ${port} ${filen} ${user}@${ip}:${dir}
expect "${user}@${ip}'s password:"
send "${pass}\r"




scp -p 22 /var/jenkins_home/workspace/rubber_common_utils/common_utils/target/common_utils-1.0-SNAPSHOT.jar root@1.116.15.151:/home/application