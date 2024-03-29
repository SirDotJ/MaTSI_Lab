; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Тренажёр ИБ"
#define MyAppVersion "1.0"
#define MyAppPublisher "ТОГУ"
#define MyAppURL "https://www.pnu.edu.ru"
#define MyAppExeName "Тренажёр ИБ.exe"

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{624AB3EC-6F7C-4D97-BD49-D9C63DDA734F}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DisableDirPage=yes
DisableProgramGroupPage=yes
; Remove the following line to run in administrative install mode (install for all users.)
PrivilegesRequired=lowest
OutputDir=C:\Users\sirdo\Documents\Programming_Main_Bin\CyberSecurity Training App\out\artifacts\FullApplication_Windows
OutputBaseFilename=Installer
SetupIconFile=C:\Users\sirdo\Documents\Programming_Main_Bin\CyberSecurity Training App\src\main\resources\icons\main_application.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "russian"; MessagesFile: "compiler:Languages\Russian.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Users\sirdo\Documents\Programming_Main_Bin\CyberSecurity Training App\out\artifacts\FullApplication_Windows\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\sirdo\Documents\Programming_Main_Bin\CyberSecurity Training App\out\artifacts\FullApplication_Windows\start.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\sirdo\Documents\Programming_Main_Bin\CyberSecurity Training App\out\artifacts\FullApplication_Windows\src\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

