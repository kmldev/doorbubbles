@echo off
setlocal enabledelayedexpansion

echo Updating package names in XML files from com.samyotech.laundry to com.laundry.bubbles

for /r "app\src\main\res" %%f in (*.xml) do (
    echo Processing: %%f
    
    set "file=%%f"
    set "tempfile=%%f.tmp"
    
    powershell -Command "(Get-Content '!file!') -replace 'com\.samyotech\.laundry', 'com.laundry.bubbles' | Set-Content '!tempfile!'"
    
    move /y "!tempfile!" "!file!" > nul
)

echo XML package name update completed!