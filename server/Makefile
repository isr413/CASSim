default: clean install
	mvn assembly:assembly

clean:
	mvn clean

install:
	cp ../common/target/common-1.0-SNAPSHOT.jar ./lib/common.jar && mvn validate && mvn install

run:
	java -jar target/server-1.0-SNAPSHOT-jar-with-dependencies.jar -p 8080

