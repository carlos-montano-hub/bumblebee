$originalLocation = Get-Location
$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath

Get-Content .env | ForEach-Object {
    if ($_ -match '^(.*?)=(.*)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], "Process")
    }
}

Set-Location ./beehive-nest
$mvnNest = Start-Process -NoNewWindow -PassThru -FilePath "mvn" -ArgumentList "spring-boot:run"

Set-Location ../beehive-guard
$mvnGuard = Start-Process -NoNewWindow -PassThru -FilePath "mvn" -ArgumentList "spring-boot:run"

Set-Location ../beehive-mind
$pythonMind = Start-Process -NoNewWindow -PassThru -FilePath "powershell" -ArgumentList "-File .\run.ps1"

Start-Sleep -Seconds 10
Set-Location ../beehive-node
$nodeSim = Start-Process -NoNewWindow -PassThru -FilePath "node" -ArgumentList "simulation.js"

Set-Location ..
$mvnNest.Id, $mvnGuard.Id, $nodeSim.Id, $pythonMind.Id | Set-Content "running_pids.txt"

Set-Location -Path $originalLocation
