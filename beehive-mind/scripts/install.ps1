$originalLocation = Get-Location

$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location -Path $scriptPath
Set-Location ..
# Ensure the virtual environment exists
if (-Not (Test-Path -Path ".\.venv")) {
    Write-Host "Virtual environment not found. Creating one..."
    python -m venv .venv
}

# Add the Python of the venv to the PATH
$env:Path = ".\.venv\Scripts;" + $env:Path

# Activate the virtual environment
& .\.venv\Scripts\Activate.ps1

# Install the required packages
pip install -r requirements.txt

Set-Location -Path $originalLocation