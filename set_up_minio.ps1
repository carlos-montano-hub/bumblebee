# set_up_minio.ps1
$scriptPath = Split-Path -Path $MyInvocation.MyCommand.Definition -Parent
Set-Location $scriptPath

# — load .env into Process scope —
Get-Content .env | ForEach-Object {
    if ($_ -match '^(.*?)=(.*)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
    }
}

$alias = 'minio'
$endpoint = $env:S3_ENDPOINT
$rootUser = $env:MINIO_ROOT_USER
$rootSecret = $env:MINIO_ROOT_PASSWORD
$bucket = $env:AUDIO_BUCKET_NAME
$accessKey = $env:S3_ACCESS_KEY
$secretKey = $env:S3_SECRET_KEY

if (-not $bucket) { Write-Error "ERROR: AUDIO_BUCKET_NAME not set"; exit 1 }

# build a single mc invocation
$cmds = @(
    "mc alias set     $alias $endpoint $rootUser $rootSecret",
    "mc mb --ignore-existing $alias/$bucket",
    "mc admin accesskey create $alias --access-key $accessKey --secret-key $secretKey"
) -join " && "

docker run --rm --network host `
    --entrypoint sh `
    minio/mc -c $cmds

