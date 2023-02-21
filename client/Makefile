TARGET=Default

default: clean install
	mvn assembly:assembly

clean:
	mvn clean

install:
	cp ../common/target/common-1.0-SNAPSHOT-jar-with-dependencies.jar ./lib/common.jar && mvn validate && mvn install

parse:
	python3 parser.py $(TARGET)

test:
	java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar -id $(TARGET) -h 127.0.0.1 -p 8080 --display -width 1920 -height 1080 -d 0.33

run:
	java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar -id $(TARGET) -h 127.0.0.1 -p 8080

run_thread_%:
	java -jar target/client-1.0-SNAPSHOT-jar-with-dependencies.jar -id $(TARGET) -h 127.0.0.1 -j $* -p $(shell echo $$(( 50000 + $* )))

run_parallel: run_thread_1 run_thread_2 run_thread_3 run_thread_4
