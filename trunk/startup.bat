@echo off
set APPLINO_HOME=%ProgramFiles%\Applino
set APPLINO_CLASSPATH=%APPLINO_HOME%\lib\applino.jar;%APPLINO_HOME%\lib\jetty.jar;%APPLINO_HOME%\lib\jetty-util.jar;%APPLINO_HOME%\lib\servlet-api.jar

start "Applino Runner" javaw -Dapplino.home="%APPLINO_HOME%" -cp "%APPLINO_CLASSPATH%" com.applino.ApplinoContainer
