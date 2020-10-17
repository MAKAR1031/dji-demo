# JDI demo

## Информация о проекте

Проект был выполнен в рамках знакомства с [JPDA](https://docs.oracle.com/javase/8/docs/technotes/guides/jpda/index.html)
и [JDI](https://docs.oracle.com/en/java/javase/11/docs/api/jdk.jdi/module-summary.html) в частности.

Проект состоит из двух приложения: основное и приложение-отладчик. Основное приложение выводит некоторое сообщение,
записанное в локальной переменной, раз в секунду. Приложение-отладчик подключается к основному приложению и меняет
выводимое сообщение путем модификации локальной переменной.  

## Сборка и запуск

1. Собрать проект: `mvn clean package`
1. Запустить основное приложение: `java -jar -Xdebug '-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=7007' .\application\target\application.jar`
1. Запустить приложение-отладчик: `java -jar .\debugger\target\debugger.jar`