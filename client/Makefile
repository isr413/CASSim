default: clean install
	mvn assembly:assembly

clean:
	mvn clean

install:
	cp ../common/target/common-1.0-SNAPSHOT-jar-with-dependencies.jar ./lib/common.jar && mvn validate && mvn install

run:
	java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar -id Default -h 127.0.0.1 -p 8080 --display -d 0.1

