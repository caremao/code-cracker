# June 17

## Using chocolatey as package manager.

Install using powershell as admin.

``` powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; `
[System.Net.ServicePointManager]::SecurityProtocol = `
[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; `
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

Next we verify the installation with `choco -v`

Result: `2.4.3`

## Install curl with choco

``` powershell
choco install curl -y
```

## Push the repo as the first commit

``` shell

git init

```