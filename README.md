Restarter
=========

Restarter automatically restarts the server after a configurable
amount of time.

Installing
----------

Place the Restarter.jar file in the plugins/ directory. After the
first run, a Restart/config.yml file will be generated with default
values.

The minutesToRestart value is how long in minutes the server will run
between restarts.

The variance value lets you set a maximum amount for the time between
to restart to vary. The server will run for an amount of time in the
range [minutesToRestart-variance, minutesToRestart+variance]. For
example, if minutesToRestart is 90 and variance is 10, then the server
will run for an amount of time between 80 and 100 minutes before
restarting. If you set variance to 0, then the server will always
restart after exactly minutesToRestart minutes.

Compiling
---------

You need to have Maven installed (http://maven.apache.org). Once
installed, simply run:

    mvn package
    
Maven will automatically download dependencies for you.
