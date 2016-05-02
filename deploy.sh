mvn install
cd /usr/local/apache-tomcat-7.0.34/webapps/
rm -rf webapp
cp -r /home/phpfrep/reports/webapp/target/webapp /usr/local/apache-tomcat-7.0.34/webapps/webapp
cd /home/phpfrep
/usr/local/apache-tomcat-7.0.34/bin/shutdown.sh

