#!/bin/sh
#
#

PUBAPI_MEMORY="-Xms1024M -Xmx1024M -XX:MaxPermSize=128M"
PUBAPI_REMOTE_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8002"

java ${PUBAPI_MEMORY} ${PUBAPI_REMOTE_DEBUG}  -Dlog4j.configuration=file:log4j.xml -jar ru.toxsoft.tslib.pubapi.http.jar  &


