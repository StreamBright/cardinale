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
  cardinale.core
  (:require 
    [cardinale.hashing  :as hashing                   ]
    [cardinale.indexing :as indexing                  ]
    [cardinale.fileio   :as fileio                    ]
    [cardinale.serde    :as serde                     ]
    [clojure.string     :as str                       ]
    [clojure.set        :refer  [intersection union]  ]
    [bigml.sketchy      [hyper-loglog :as hll]        ]
  )
  (:import
    [com.google.common.primitives Bytes UnsignedLongs UnsignedInts    ]
    [com.google.common.collect    Lists Sets                          ]
    [com.googlecode.javaewah      EWAHCompressedBitmap                ]
    [java.util                    BitSet                              ]
    [java.io                      File                                ]
    )
    (:gen-class))

(defn bitset
  "Set all the bits in the BitSet based on a #{int ... int}"
  [int-set]
  (let [bs (BitSet.)]
    (doseq [h int-set] (.set bs h))
    bs))

(defn bitmap 
  "Set all the bits in the Bitmap based on a #{int ... int}"
  [int-set] 
  (let [bm (EWAHCompressedBitmap.)] 
    (doseq [h int-set] (.set bm h))
    bm))

(defn reading-log-file 
  [file] 
  (let [
          file-lines      (fileio/lazy-lines file)
          split-entries   (map #(str/split % #"\ ") file-lines)
          int-seq         (map #(indexing/get-index (nth % 3)) split-entries)
          int-set         (set int-seq) ]
  ;set
  int-set))

(defn -main 
  [& args]
  (let [
        int-set       (reading-log-file "resources/test_huge.log")
        ;bit-set       (bitset  int-set)
        bit-map        (bitmap  int-set)
        hyper-loglog   (hll/into 
                        (hll/create 0.03) 
                        int-set)
        ]
   ;(serde/serialize-file bit-set     "bs.test.dat") 
   (serde/serialize-file int-set      "set.test.dat")
   (serde/serialize-file bit-map      "bm.test.dat")
   (serde/serialize-file hyper-loglog "hll.test.dat")
  (println (count int-set))
  (println (.cardinality bit-map))
  (println (hll/distinct-count hyper-loglog))
  ))

