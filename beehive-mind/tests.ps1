$originalLocation = Get-Location

$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath

pytest -s

Set-Location -Path $originalLocation

