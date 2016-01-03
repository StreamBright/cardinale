java \
  -server -Xms512m \
  -Xmx4096m \
  -XX:+UseConcMarkSweepGC -XX:+TieredCompilation -XX:+AggressiveOpts \
  -XX:+UnlockCommercialFeatures -XX:+FlightRecorder \
  -XX:StartFlightRecording=defaultrecording=true,dumponexit=true,settings=profiling.jfc \
  -jar target/cardinale-0.1.0-standalone.jar
