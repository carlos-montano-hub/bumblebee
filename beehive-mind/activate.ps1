$originalLocation = Get-Location

$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath

& "$scriptPath\.venv\Scripts\Activate.ps1"

Set-Location -Path $originalLocation

