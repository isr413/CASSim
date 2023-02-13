default: clean install
	mvn assembly:assembly

clean:
	mvn clean

install:
	cp ../common/target/common-1.0-SNAPSHOT-jar-with-dependencies.jar ./lib/common.jar && mvn validate && mvn install

run:
	java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar -id BmpExample -h 127.0.0.1 -p 8080 --display -width 1920 -height 1080 -d 0.33

