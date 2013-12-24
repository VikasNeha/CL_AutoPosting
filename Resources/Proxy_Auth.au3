WinWait("Authentication Required","","20")
If WinExists("Authentication Required") Then
   WinActivate("Authentication Required")
   Send($CmdLine[1])
   Send("{TAB}")
   Send($CmdLine[2])
   Send("{ENTER}")
EndIf