$originalLocation = Get-Location

$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath
Set-Location ..

& ".\venv\Scripts\Activate.ps1"

pytest --capture=no

Set-Location -Path $originalLocation

