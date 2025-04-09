$originalLocation = Get-Location

# Get the script's directory
$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath
Set-Location ..

# Ensure the virtual environment exists
if (-Not (Test-Path -Path ".\venv")) {
    Write-Host "Virtual environment not found. Creating one..."
    python -m venv .\venv
}

# Add the Python of the venv to the PATH
$env:Path = (Resolve-Path ".\venv\Scripts").Path + ";" + $env:Path

# Activate the virtual environment
& .\venv\Scripts\Activate.ps1

# Install the required packages
pip install -r .\requirements.txt

# Return to the original location
Set-Location -Path $originalLocation