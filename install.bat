rem ---------------------------------------------------------------------
rem - installs applino container in "Program Files" directory
rem - creates desktop icon for container and "applini"
rem - creates entry in "startup" folder
rem ---------------------------------------------------------------------
 
@echo off

set APPLINO_HOME=%ProgramFiles%\Applino
mkdir "%APPLINO_HOME%"
mkdir "%APPLINO_HOME%\logs"
mkdir "%APPLINO_HOME%\applini"
mkdir "%APPLINO_HOME%\lib"

copy startup.bat "%APPLINO_HOME%"
copy applino.ico "%APPLINO_HOME%"
copy applini\*.aar "%APPLINO_HOME%\applini"
copy lib\*.jar "%APPLINO_HOME%\lib"
del "%APPLINO_HOME%\lib\examples.jar"

xxmklink "%USERPROFILE%\Desktop\Applino" "%APPLINO_HOME%\startup.bat" "" "%APPLINO_HOME%" "Applino Runner" 1 "%APPLINO_HOME%\applino.ico"
xxmklink "%USERPROFILE%\Start Menu\Programs\Startup\Applino" "%APPLINO_HOME%\startup.bat" "" "%APPLINO_HOME%" "Applino Runner" 1 "%APPLINO_HOME%\applino.ico"
xxmklink "%USERPROFILE%\Desktop\Applini" "%APPLINO_HOME%\applini" "" "%APPLINO_HOME%" "Applini"
