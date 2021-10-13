#define MyAppName "MemView"
#define MyAppVersion "1.0-SNAPSHOT"
#define MyAppPublisher "ACME"
#define MyAppURL ""
#define MyAppExeName "MemView.exe"
#define MyAppFolder "MemView"
#define MyAppLicense ""
#define MyAppIcon "D:\Programming\Java\MemView3\target\assets\MemView.ico"

[Setup]
AppId={{{#MyAppName}}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppFolder}
DisableDirPage=yes
DisableProgramGroupPage=yes
DisableFinishedPage=yes
PrivilegesRequired=admin
PrivilegesRequiredOverridesAllowed=commandline
LicenseFile={#MyAppLicense}
SetupIconFile={#MyAppIcon}
UninstallDisplayIcon={app}\{#MyAppExeName}
Compression=lzma
SolidCompression=yes
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Registry]

[Files]
Source: "D:\Programming\Java\MemView3\target\MemView\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\MemView.ico"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\MemView.ico"; Tasks: desktopicon

[Run]
