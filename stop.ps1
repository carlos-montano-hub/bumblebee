# Read stored PIDs and stop the processes
if (Test-Path "running_pids.txt") {
    Get-Content "running_pids.txt" | ForEach-Object {
        Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue
    }
    Remove-Item "running_pids.txt"
    Write-Host "All running services stopped."
} else {
    Write-Host "No running services found."
}
