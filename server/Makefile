SHELL=/bin/bash
PORT=50000

default: clean install
	mvn compile assembly:single

clean:
	mvn clean

install:
	cp ../common/target/common-1.0-SNAPSHOT-jar-with-dependencies.jar ./lib/common.jar && mvn validate && mvn install

run:
	java -jar target/server-1.0-SNAPSHOT-jar-with-dependencies.jar -p 8080

run_thread_%:
	java -jar target/server-1.0-SNAPSHOT-jar-with-dependencies.jar -p $(shell echo $$(( $(PORT) + $* )))

run_parallel: run_thread_1 run_thread_2 run_thread_3 run_thread_4
