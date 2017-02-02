# VarangianRus server - client setup

If you want to run the server on your machine you need to first download the code.

Before you proceed, make sure that you have [Node.js](https://nodejs.org/en/) installed.

When everything is prepared, you have to install the required node-modules
for this setup. Navigate to `VarangianRus/src/serverClientInteraction`
and then do the follwing command:

```shell
$ npm install
```

Wait until the installation is finished and then start the server via:

```shell
$ node index.js
```

This will start the server and from this point on you can access the 
user interface for this project by typing in your favorite browser
the following url:

`http://localhost:8080/` or `http://your.ip.address:8080`

The second variant is more reliable, as you can access it from any
laptop/pc. Consider this example:

Lets say your ip address is the following: `192.168.0.11`.

When you start the server from your machine, anyone can access
the ui via `http://192.168.0.11:8080/`.

If you change the code you have to restart the server via pressing `ctrl+c`
and using the same command for starting the server above.

When you restart the server make sure that you reload any open tabs
with the url above, so that changes are being applied.
