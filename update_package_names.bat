@echo off
setlocal enabledelayedexpansion

echo Updating package names from com.samyotech.laundry to com.laundry.bubbles

for /r "app\src\main\java" %%f in (*.java) do (
    echo Processing: %%f
    
    set "file=%%f"
    set "tempfile=%%f.tmp"
    
    powershell -Command "(Get-Content '!file!') -replace 'package com\.samyotech\.laundry', 'package com.laundry.bubbles' -replace 'import com\.samyotech\.laundry', 'import com.laundry.bubbles' | Set-Content '!tempfile!'"
    
    move /y "!tempfile!" "!file!" > nul
)

echo Package name update completed!