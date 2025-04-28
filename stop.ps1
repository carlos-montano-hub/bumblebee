$originalLocation = Get-Location
$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath

Write-Host "Stopping Docker Minio service..."
docker compose stop beehive_minio
docker compose rm -f beehive_minio

Set-Location ./beehive-nest
mvn spring-boot:stop

# parar Spring Boot en beehive-guard
Set-Location ../beehive-guard
mvn spring-boot:stop

Set-location ..
# Read stored PIDs and stop the processes

$ports = 8851, 8852

foreach ($port in $ports) {
    $conns = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    if ($conns) {
        foreach ($c in $conns) {
            $_pid = $c.OwningProcess
            Write-Host "Stopping process on port $port (_PID $_pid)"
            Stop-Process -Id $_pid -Force -ErrorAction SilentlyContinue
        }
    }
    else {
        Write-Host "No listener on port $port"
    }
}
if (Test-Path "running_pids.txt") {
    Get-Content "running_pids.txt" | ForEach-Object {
        Write-Host "Stopping process with ID: $_"
        Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue
    }
    Remove-Item "running_pids.txt"
    Write-Host "All running services stopped."
}
else {
    Write-Host "No running services found."
}

Set-Location -Path $originalLocation
