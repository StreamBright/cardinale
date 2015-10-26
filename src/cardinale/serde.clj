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
  cardinale.serde
  (:import
    [java.io                      File                                ]
    )
  )

;; Serialize and deserialize Java objects the trivial way, in order to write them to disk
;; or being sent over the wire

(defn serialize-file 
  "Serializing input to file on disk"
  [input filename]
  (let [file (File. filename) ]
    (with-open [output-stream (java.io.ObjectOutputStream. 
                                (java.io.FileOutputStream. file))]
      (.writeObject output-stream input))))
 
(defn deserialize-file 
  "Reading a file and deserializing the content of it into the original type"
  [filename]
  (let [file (File. filename) ]
    (with-open [input-stream (java.io.ObjectInputStream. 
                               (java.io.FileInputStream. file))]
      (.readObject input-stream))))

(defn serialize-bytes
  "Serializing input to byte array"
  ^bytes [input]
  (let [buffer (java.io.ByteArrayOutputStream.)]
    (with-open [ output-stream (java.io.ObjectOutputStream. buffer)] 
      (.writeObject output-stream input))
    (.toByteArray buffer)))

(defn deserialize-bytes 
  "Deserializing a byte array into the original type"
  [^bytes input-bytes]
  (with-open [input-stream (java.io.ObjectInputStream. 
                             (java.io.ByteArrayInputStream. input-bytes))]
    (.readObject input-stream)))

