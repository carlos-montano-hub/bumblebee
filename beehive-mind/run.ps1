$originalLocation = Get-Location

$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath

& "$scriptPath\install.ps1"

& uvicorn app.main:app --reload

Set-Location -Path $originalLocation

