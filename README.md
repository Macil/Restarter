Restarter
=========

The Restarter plugin for Bukkit automatically restarts the server
after a configurable amount of time. It works as a quick fix for any
stability problems some servers have when running for a long time,
lets new plugins and configuration settings you've set take effect,
and clears out those AFK players.

Installing
----------

Place the Restarter.jar file in the plugins/ directory. After the
first run, a Restarter/config.yml file will be generated with default
values.

Note that Restarter actually only stops the server. It does not handle
starting the server up again; refer to the examples/ directory for
example scripts that automatically bring the server up again after it
stops.

Configuration
-------------

The minutesToRestart value is how long in minutes the server will run
between restarts.

The variance value lets you set a maximum amount for the time between
to restart to vary. The server will run for an amount of time in the
range [minutesToRestart-variance, minutesToRestart+variance]. For
example, if minutesToRestart is 90 and variance is 10, then the server
will run for an amount of time between 80 and 100 minutes before
restarting. If you set variance to 0, then the server will always
restart after exactly minutesToRestart minutes.

The warnMessage string is the message displayed to all users one
minute before the restart.

The kickMessage string is the message shown to all users when they're
auto-kicked off of the server for the restart.

Commands
--------

/rsquery

Queries how long until the next scheduled restart from now. Requires
the "restarter.query" permission.

/rsset

Sets the amount of time in minutes until the next restart. This value
is not saved to the configuration file. Giving zero or a negative
number will do an immediate restart. Requires the "restarter.set"
permission.

Compiling
---------

You need to have Maven installed (http://maven.apache.org). Once
installed, simply run:

    mvn package
    
Maven will automatically download dependencies for you.
