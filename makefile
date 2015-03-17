console:
	javac -d bin -cp lib/gson-2.3.1.jar src/main_console/ConcreteValues.java src/main_console/IValues.java src/main_console/ServerRunner.java src/messages/ErrorMessage.java src/messages/IMessage.java src/messages/JSONMessage.java src/messages/LoadMessage.java src/messages/LogMessage.java src/messages/TextMessage.java src/messages/UIMessage.java src/components/Component.java src/components/ConsoleOutputComponent.java src/components/IntGenerator.java src/components/LoggingComponent.java src/components/ParserComponent.java src/components/PersistantComponent.java src/components/SocketListenerComponent.java src/components/UIComponent.java src/console_ui/Box.java src/console_ui/MainWindow.java

run:
	java -cp "lib/gson-2.3.1.jar:bin"  main_console/ServerRunner
