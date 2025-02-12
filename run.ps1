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

# Start beehive-node simulation
Start-Sleep -Seconds 10
Set-Location ../beehive-node
$pythonSim = Start-Process -NoNewWindow -PassThru -FilePath "python" -ArgumentList "simulation.py"

# Store PIDs in a file
Set-Location ..
$mvnNest.Id, $mvnGuard.Id, $pythonSim.Id | Set-Content "running_pids.txt"
