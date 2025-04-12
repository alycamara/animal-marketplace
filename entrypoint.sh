#!/bin/sh

echo "The app is starting"
exec java -jar "animalMarketplacePlatformApplication.jar" --spring.profiles.active=${SPRING_ACTIVE_PROFILES}