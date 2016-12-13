all: comp

comp:
	javac -d classes src/*.java

kapwing.jar: comp
	cd classes; jar cmf ../src/MANIFEST.MF ../kapwing.jar com 

run: kapwing.jar
	java -jar kapwing.jar

clean:
	rm -rf classes/com
