$ErrorActionPreference = 'Stop'

Write-Host "[1/4] Deteniendo daemons de Gradle..."
./gradlew.bat --stop | Out-Null

Write-Host "[2/4] Cerrando procesos Java de tooling que suelen bloquear R.jar..."
$patterns = @(
    'org.gradle.launcher.daemon.bootstrap.GradleDaemon',
    'com.github.badsyntax.gradle.GradleServer'
)

$javaProcs = Get-CimInstance Win32_Process |
    Where-Object { $_.Name -eq 'java.exe' -and $_.CommandLine }

foreach ($proc in $javaProcs) {
    foreach ($pattern in $patterns) {
        if ($proc.CommandLine -match [regex]::Escape($pattern)) {
            try {
                Stop-Process -Id $proc.ProcessId -Force -ErrorAction Stop
                Write-Host ("  - Cerrado PID {0} ({1})" -f $proc.ProcessId, $pattern)
            } catch {
                Write-Warning ("  - No se pudo cerrar PID {0}" -f $proc.ProcessId)
            }
            break
        }
    }
}

Write-Host "[3/4] Intentando limpiar carpeta app/build..."
if (Test-Path "app/build") {
    try {
        Remove-Item "app/build" -Recurse -Force
        Write-Host "  - app/build eliminada"
    } catch {
        Write-Warning "  - app/build no pudo eliminarse completa; se continúa con build sin clean duro"
    }
}

Write-Host "[4/4] Compilando debug sin daemon persistente..."
./gradlew.bat assembleDebug --no-daemon
