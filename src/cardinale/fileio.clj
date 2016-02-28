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
    [clojure.java.io            :as io                        ]
    [clojure.tools.logging      :as log                       ]
  )
  (:import 
    [java.io File RandomAccessFile]
  )
  (:gen-class))

(defn lazy-helper
  "Processes a java.io.Reader lazily"
  [reader]
  (lazy-seq
    (if-let [line (.readLine reader)]
      (cons line (lazy-helper reader))
      (do (.close reader) nil))))

(defn lazy-lines
  "Return a lazy sequence with the lines of the file"
  [^String path]
  (let [java-file (File. path) ]
    (try
      (cond
        (.isFile java-file)  
          {:ok (lazy-helper (io/reader java-file)) }
        :else
          (throw (Exception. "Input is not a file")))
    (catch Exception e
      {:error "Exception" :fn "lazy-lines" :exception (.getMessage e) }))))

;; Implementing tail -f in Clojure http://codereview.stackexchange.com/questions/69774/idiomatic-way-to-implement-tail-f-in-clojure
;; Actually I am re-implementing it 
(defn create-rac 
  ^RandomAccessFile [^String filename]
  (RandomAccessFile. filename "r"))

(defn close-rac
  [^RandomAccessFile rac]
  (.close rac))

(defn file-length
  ^Long [^RandomAccessFile rac]
  (.length rac))

(defn read-line-clj 
  ^String [^RandomAccessFile rac]
  (.readLine rac))

(defn get-position
  ^Long [^RandomAccessFile rac]
  (.getFilePointer rac))

(defn seek 
  ^Long [^RandomAccessFile rac ^Long offset]
  (let [  max-offset    (file-length rac)
          offset-or-max (if (>= max-offset offset) offset max-offset) ] 
    (.seek rac offset-or-max)
    offset-or-max))

(defn read-line-with-offset 
  [^RandomAccessFile rac]
  (let [  ^String line    (read-line-clj rac)
          ^Long   offset  (get-position rac) ]
    [line offset]))

(defn continuously-read-file 
  [rac] 
  (let [[line offset] (read-line-with-offset rac)]
    (if line
      (do
        (log/info (str "line: " line " offset: " offset))
        (recur rac))
      ;else
      (do
        (Thread/sleep 10000)
        (log/info "recurring")
        (recur rac)))))


;; TODO 
;; implement truncation detection, re-opening files when needed 
;; support multiple files 
;; back to drawing 




