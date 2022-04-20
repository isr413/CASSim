# SEATSim
An implementation of a JSON RPC simulation to support mission critical scenarios, such as search-and-rescue (SAR) missions and intelligence, surveillance, and reconaissance (ISR) missions.

### Setup
Must use Java 1.8 or later:
```sudo apt install openjdk-17-jdk openjdk-17-jre openjdk-17-doc```

Must use Maven version 3.8.5 or later:
    - ```wget https://dlcdn.apache.org/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz -P /tmp```
    - ```sudo tar xf /tmp/apache-maven-*.tar.gz -C /opt```
    - ```sudo ln -s /opt/apache-maven-3.8.5 /opt/maven```
    - ```sudo vim /etc/profile.d/maven.sh```
        - ```export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64```
        - ```export M2_HOME=/opt/maven```
        - ```export MAVEN_HOME=/opt/maven```
        - ```export PATH=${M2_HOME}/bin:${PATH}```
    - ```sudo chmod +x /etc/profile.d/maven.sh```
    - ```source /etc/profile.d/maven.sh```

To build (and test):
```make```

To test (without building):
```make test```

To run:
```make run```
