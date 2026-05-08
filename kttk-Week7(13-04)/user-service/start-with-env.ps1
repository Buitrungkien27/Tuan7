$envFile = Join-Path $PSScriptRoot '.env'
if (!(Test-Path $envFile)) {
  Write-Error '.env file not found.'
  exit 1
}

Get-Content $envFile | ForEach-Object {
  if ($_ -match '^\s*#' -or $_ -match '^\s*$') { return }
  $parts = $_ -split '=', 2
  if ($parts.Count -eq 2) {
    [Environment]::SetEnvironmentVariable($parts[0].Trim(), $parts[1].Trim(), 'Process')
  }
}

& "$PSScriptRoot\mvnw.cmd" spring-boot:run
