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
git add .
git commit -m "message"
git remote add origin git@github.com:caremao/code-cracker-core.git
branch -M main
push -u origin main
```

## Adding lombok dependencyt to project because I'm lazy

Installing mvn with choco: `choco install maven -y`

Adding dependency
``` gradle

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

Testing new dependency:

```shell

mvn clean install
```