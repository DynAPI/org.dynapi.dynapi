# Build Dependencies

DynAPI depends on some own packages that are available via the GitHub package registry.
Cause GitHub is a bitch we need to authenticate us to use these.

`/gradle.properties`
```properties
gpr.user=<username>
gpr.token=<personal-access-token>
```

`gpr.user` can be your username or `DynAPI`. Don't ask why.

`gpr.token` is a personal access token.
You can [create a new token](https://github.com/settings/tokens/new) that requires `packages:read`.
