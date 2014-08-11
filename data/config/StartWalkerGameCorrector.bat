@echo off
title xMlex: Server Console
:start
echo %DATE% %TIME% Login server is running !!! > server_is_running.tmp
echo Starting xMlex Server.
echo.
java -server -Xms32m -Xmx64m -cp libs/*;libs/slik2d/*; fw.connection.socks.SOCKS4
if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end
:restart
echo.
echo Admin Restart ...
echo.
goto start
:error
echo.
echo Server terminated abnormaly
echo.
:end
echo.
echo server terminated
echo.
del server_is_running.tmp
pause