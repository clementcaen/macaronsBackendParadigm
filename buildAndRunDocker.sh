#sudo systemctl restart docker.socket docker.service

/home/clement/.jdks/openjdk-22.0.1/bin/java -Dmaven.multiModuleProjectDirectory=/home/clement/Documents/project_paradigm/macaronsBackendParadigm -Djansi.passthrough=true -Dmaven.home=/home/clement/.m2/wrapper/dists/apache-maven-3.9.5-bin/2adeog8mj13csp1uusqnc1f2mo/apache-maven-3.9.5 -Dclassworlds.conf=/home/clement/.m2/wrapper/dists/apache-maven-3.9.5-bin/2adeog8mj13csp1uusqnc1f2mo/apache-maven-3.9.5/bin/m2.conf -Dmaven.ext.class.path=/snap/intellij-idea-ultimate/510/plugins/maven/lib/maven-event-listener.jar -javaagent:/snap/intellij-idea-ultimate/510/lib/idea_rt.jar=40375:/snap/intellij-idea-ultimate/510/bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /home/clement/.m2/wrapper/dists/apache-maven-3.9.5-bin/2adeog8mj13csp1uusqnc1f2mo/apache-maven-3.9.5/boot/plexus-classworlds.license:/home/clement/.m2/wrapper/dists/apache-maven-3.9.5-bin/2adeog8mj13csp1uusqnc1f2mo/apache-maven-3.9.5/boot/plexus-classworlds-2.7.0.jar org.codehaus.classworlds.Launcher -Didea.version=2024.1.4 package -DskipTests

sudo docker-compose build spring-boot-app

#For rebuild everything
#sudo docker-compose rm -f
#sudo docker-compose down
sudo docker-compose up --build

sudo docker-compose logs
