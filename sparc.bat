@ECHO OFF
REM ------------------------------------------------------------------------------------
REM This is a Windows batch file. It is a command-line script for running SPARC.
REM PRERQUISITE: You must have DLV installed and it must be in the PATH.
REM If you wish to change the default behavior, include options (e.g., "-A").
REM ------------------------------------------------------------------------------------
java -jar sparc.jar %*
