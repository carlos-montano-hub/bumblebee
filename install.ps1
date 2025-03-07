# Java services
Set-Location beehive-nest; ./mvnw clean install -DskipTests; Set-Location ..;
Set-Location beehive-guard; ./mvnw clean install -DskipTests; Set-Location ..;

# Angular App
Set-Location beehive-front; npm i; Set-Location ..;

# Python service
Set-Location beehive-mind;

# Check if the venv exists; if not, create it
if (!(Test-Path -Path ".venv")) {
    python -m venv .venv;
}

# Activate virtual environment
. .venv\Scripts\Activate.ps1;

# Install Rust (for deepfilternet)
choco install -y rust;

# Install dependencies
pip install -r requirements.txt;

Set-Location ..;
