;; Copyright 2015 StreamBright LLC
;; Copyright 2015 Istvan Szukacs <istvan@streambrightdata.com>

;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at

;;     http://www.apache.org/licenses/LICENSE-2.0

;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
;;
;; ## cardinale.core 
;; The core module glues the set of libraries together
;;

(ns ^{  :doc "Cardinale, tracking popularity with distributed counters"
        :author "Istvan Szukacs"}
  cardinale.fileio
  (:require 
   [clojure.java.io  :as io ]
  )
    (:gen-class))

(defn lazy-lines 
  "Return a lazy sequence with the lines of the file"
  [^String file]
  (letfn [(helper [rdr]
                  (lazy-seq
                    (if-let [line (.readLine rdr)]
                      (cons line (helper rdr))
                      (do (.close rdr) nil))))]
         (helper (io/reader file))))

