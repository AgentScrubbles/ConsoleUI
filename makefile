console:
	javac -d bin -cp lib/gson-2.3.1.jar src/main_console/ConcreteValues.java src/main_console/SafeParser.java src/main_console/Box.java src/main_console/IValues.java src/main_console/UpdateArrivedEvent.java src/main_console/SocketListener.java src/main_console/ServerActionHandler.java src/main_console/MainWindow.java src/main_console/ServerRunner.java

run:
	java -cp bin main_console/ServerRunner
