(defproject cardinale "0.1.0"
  :description "Merging distributed counters, representing users for analytical use (tracking popularity)"
  :url "https://github.com/StreamBright/cardinale"
  :license {:name "Apache License 2.0 (Apache-2.0)" :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
      [org.clojure/clojure                "1.7.0"   ]
      [com.google.guava/guava             "18.0"    ]
      [com.googlecode.javaewah/JavaEWAH   "1.0.8"   ]
      [bigml/sketchy                      "0.3.1"   ]
      [org.clojure/tools.logging          "0.3.1"   ]
      [org.slf4j/slf4j-log4j12            "1.7.12"  ]
      [log4j/log4j                        "1.2.17"  ]
      [org.clojure/tools.cli              "0.3.3"   ]
  ]
  :exclusions [
    javax.mail/mail
    javax.jms/jms
    com.sun.jdmk/jmxtools
    com.sun.jmx/jmxri
    jline/jline
  ]
  :plugins [
            [lein-ancient "0.6.7"]
            [michaelblume/lein-marginalia "0.9.0"]]
  :profiles {
    :uberjar {
      :aot :all
    }
  }
  :jvm-opts [
    "-Xms128m" "-Xmx256m" 
    "-server"  "-XX:+UseConcMarkSweepGC" 
    "-XX:+TieredCompilation" "-XX:+AggressiveOpts"
    ;"-Dcom.sun.management.jmxremote"
    ;"-Dcom.sun.management.jmxremote.port=8888"
    ;"-Dcom.sun.management.jmxremote.local.only=false"
    ;"-Dcom.sun.management.jmxremote.authenticate=false"
    ;"-Dcom.sun.management.jmxremote.ssl=false"
    ;"-XX:+UnlockCommercialFeatures" "-XX:+FlightRecorder"
    ;"-XX:StartFlightRecording=duration=60s,filename=myrecording.jfr"
    ;"-Xprof" "-Xrunhprof"
  ]
  :repl-options {:init-ns cardinale.core}
  :main cardinale.core)

