$originalLocation = Get-Location
$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath
# Load environment variables from .env file
Get-Content .env | ForEach-Object {
    if ($_ -match '^(.*?)=(.*)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], "Process")
    }
}

# Start beehive-nest
Set-Location ./beehive-nest
$mvnNest = Start-Process -NoNewWindow -PassThru -FilePath "mvn" -ArgumentList "spring-boot:run"

# Start beehive-guard
Set-Location ../beehive-guard
$mvnGuard = Start-Process -NoNewWindow -PassThru -FilePath "mvn" -ArgumentList "spring-boot:run"

Set-Location ../beehive-mind
$pythonMind = Start-Process -NoNewWindow -PassThru -FilePath "powershell" -ArgumentList "-File .\run.ps1"

# Start beehive-node simulation
Start-Sleep -Seconds 10
Set-Location ../beehive-node
$pythonSim = Start-Process -NoNewWindow -PassThru -FilePath "python" -ArgumentList "simulation.py"

# Store PIDs in a file
Set-Location ..
$mvnNest.Id, $mvnGuard.Id, $pythonSim.Id, $pythonMind.Id | Set-Content "running_pids.txt"

Set-Location -Path $originalLocation
