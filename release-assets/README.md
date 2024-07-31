# DynAPI

## Installation

The recommended installation location is the `/opt/` directory.
You can change it if you prefer.

To download and extract DynAPI Files you can run the following commands.

```shell
cd /opt/
wget "https://github.com/DynAPI/org.dynapi.dynapi/releases/download/latest/dynapi.tgz"
tar -xf dynapi.tgz
```

### Runtime Requirements

DynAPI requires Java 17 or higher to run.
If you don't have this installed you can easily install it with your system package manager.

```shell
sudo apt install openjdk-17-jre
```

Note: This won't replace your `java` command.
To use this newer version you have to search in `/usr/lib/jvm/` for the correct version.
The Java 17 directory that you find there is what you should use for the `$JAVA_HOME` variable in the following steps.

### Configuration

I won't go too much into the details of the configuration file, but you must create a `dynapi.yaml` file.
In this file are all the configurations about how to configure the server, which database to connect to and more.

```text
/opt/dynapi/dynapi.yaml
```

If you use an IDE or other Editor that support json-schemas you can load `json-schema.json` to get autocompletion while editing the config file.

### Execution

To execute DynAPI manually with the help of the `dynapi.sh` script.

```shell
/opt/dynapi/dynapi.sh
```

Optionally you have to specify the `$JAVA_HOME` variable of Java 17 to be able to start DynAPI.

```shell
JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64/" /opt/dynapi/dynapi.sh
```

### System Service

But maybe you don't want to run DynAPI only during development.
In that case it's recommended to create a Service that starts automatically in the background.

`/etc/systemd/system/dynapi.sercice`

> If you want to add DynAPI as a User Service, you should create `$HOME/.config/systemd/user/dynapi.service` instead.
> Later when using the `systemctl` command you have to add the `--user` flag.

```unit file (systemd)
[Unit]
Description=DynAPI
After=syslog.target

[Service]
User=dynapi
WorkingDirectory=/opt/dynapi/
ExecStart=/opt/dynapi/dynapi.sh
Restart=on-failure
SuccessExitStatus=143
# adjust the next line to point to your desired java17
Environment=JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/

[Install]
WantedBy=multi-user.target
```

To enable and start the DynAPI server you can use the following commands

```shell
# enable automatic startup after system boot
systemctl enable dynapi.service
# start DynAPI
systemctl start dynapi.service
```

When you don't need it you can stop DynAPI with the following commands

```shell
# disable automatic startup after system boot
systemctl disable dynapi.service
# stop the currently running instance
systemctl stop dynapi.service
```
