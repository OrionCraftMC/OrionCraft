# Tell Me I Cheat Now! Client

This is our client-sided module that makes use of tell-me-i-cheat-now-lib's API to communicate with server-sided
anti-cheat plugins.

## Goal

The goal of this module is to prove to the server that the player is not cheating.

## Distrubution

If you are building the client by yourself and want to properly make use of this module, you will have to include a file
in the following location in order for interop with Landia's anti-cheat plugin:

```
tmic-client/src/main/kotlin/io/github/orioncraftmc/orion/assets/orion/data/landia-constants.json
```
