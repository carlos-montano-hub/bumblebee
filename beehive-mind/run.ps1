$originalLocation = Get-Location

$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath

& ".\scripts\install.ps1"

$envHost = $env:HOST
# Load environment variables from .env file
if (Test-Path "..\.env") {
    Get-Content "..\.env" | ForEach-Object {
        if ($_ -match "^(.*?)=(.*)$") {
            [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2])
        }
    }
}

$envPort = $env:MIND_SERVICE_PORT

if (-not $envHost) {
    $envHost = "0.0.0.0"
}

if (-not $envPort) {
    $envPort = 8001
}

& python -m uvicorn app.main:app --reload --env-file ../.env --host $envHost --port $envPort

Set-Location -Path $originalLocation

