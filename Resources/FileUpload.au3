WinWaitActive("File Upload")
if WinExists("File Upload") Then
ControlSetText("File Upload", "", "Edit1", $CmdLine[1])
Sleep(1000)
ControlClick("File Upload", "", "Button1")
Sleep(1000)
ControlFocus("File Upload", "", "DirectUIHWND2")
ControlSend("File Upload", "", "DirectUIHWND2", "^a")
Sleep(1000)
Local $sText = ControlGetText("File Upload", "", "Edit1")
ControlClick("File Upload", "", "Button1")
EndIf