#!/bin/bash

BINDIR=$(dirname "$(readlink -fn "$0")")
cd "$BINDIR"

while true
do
    java -Xincgc -Xms820M -Xmx820M -jar craftbukkit-1.0.1-R2-SNAPSHOT.jar

    echo -e 'If you want to completely stop the server process now, press ctrl-C before the\ntime is up!'
    echo "Rebooting in:"
    for i in {5..1}
    do
        echo "$i..."
        sleep 1
    done
    echo 'Restarting now!'
done
