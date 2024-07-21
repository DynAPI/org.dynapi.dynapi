# org.dynapi.dynapi

Dynamic API for many Databases

> Successor / Version 2 to [DynAPI/DynAPI](https://github.com/DynAPI/DynAPI). <br>
> Currently under development and not usable!

> See the [development branch](https://github.com/DynAPI/org.dynapi.dynapi/tree/development) to test the experimental Version. Warning: may be broken

<!-- TOC -->
* [org.dynapi.dynapi](#orgdynapidynapi)
  * [Supported Databases](#supported-databases)
  * [Installation](#installation)
  * [Features](#features)
    * [Automated Documentation](#automated-documentation)
    * [Fast](#fast)
    * [Realtime updates when the Database changes](#realtime-updates-when-the-database-changes)
    * [Secure](#secure)
    * [Full Control](#full-control)
  * [developer notes](#developer-notes)
<!-- TOC -->

## Supported Databases

<small>*planned</small>

- ClickHouse
- Microsoft SQL Server
- MySQL
- Oracle
- PostgreSQL
- Redshift
- Snowflake
- SQLite
- Vertica

## Installation

> Installation is currently not possible

## Features

### Automated Documentation

Automatic Documentation is generated for your Database available with ReDoc or Swagger.
This helps you during development or your users to work with the API.

### Fast

In contrary to the [first version of DynAPI](https://github.com/DynAPI/DynAPI), this version is written in a more performant language for better speed.
Additionally, DynAPI doesn't require any complex ORM and archives with this quick response times.

### Realtime updates when the Database changes

The moment you make changes to the database it is reflected in the API.
No need to restart the Server or wait ages for it to synchronise.

### Secure

With an (optional) builtin User-Control-System you can limit the access to your data or make it publicly available.

### Full Control

In case you don't want to reveal your whole database it is possible to configure which parts are available.


## developer notes

Create a build of [org.dynapi.openapispec](https://github.com/DynAPI/org.dynapi.openapispec) and put it as `openapispec.jar` in `/libs/`
