;; Copyright 2015 StreamBright LLC
;; Copyright 2015 Istvan Szukacs <istvan@streambrightdata.com>
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
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
  cardinale.hashing
  (:require
    [clojure.string :as str                       ]
  )
  (:import
    [com.google.common.primitives Bytes UnsignedLongs UnsignedInts    ]
    [com.google.common.hash       Hashing HashFunction HashCode       ]
    [java.nio.charset             Charset                             ]
    )
    (:gen-class))

;; ## Definitions
;;
;; Defining hash functions ([HashFunction](http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/hash/HashFunction.html)), using [Murmur3](https://code.google.com/p/smhasher/wiki/MurmurHash3) 32 and 128 bit
;; [Hashing.murmur3_32()](http://docs.guava-libraries.googlecode.com/git-history/master/javadoc/com/google/common/hash/Hashing.html#murmur3_32())
;;
(def ^:private ^HashFunction murmur-fun-32 (Hashing/murmur3_32))
;; [Hashing.murmur3_128()](http://docs.guava-libraries.googlecode.com/git-history/master/javadoc/com/google/common/hash/Hashing.html#murmur3_128())
(def ^:private ^HashFunction murmur-fun-128 (Hashing/murmur3_128))
;; Defining UTF8 character set 
(def ^:private ^sun.nio.cs.UTF_8 utf8-chr-set (Charset/forName "UTF-8"))
;; The returned [HashCode](http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/hash/HashCode.html) can be converted to any data type that is supported later:
;;
;; * byte[] asBytes() - Returns the value of this hash code as a byte array.
;; * int  asInt() - Returns the first four bytes of this hashcode's bytes, converted to an int value in little-endian order.
;; * long asLong() - Returns the first eight bytes of this hashcode's bytes, converted to a long value in little-endian order.
;; * int  bits() - Returns the number of bits in this hash code; a positive multiple of 8.
(defn gen-hash
  "Returns the hash object for a string"
  ^HashCode [^HashFunction hash-fn ^String string]
  (.hashString hash-fn string utf8-chr-set))

;; 32 bit
(defn murmur-32-signed-int
  "Returns an Integer (+/-)"
  ^Integer [^String string]
    (.asInt (gen-hash murmur-fun-32 string)))

(defn murmur-32-unsigned-int
  "Returns an Integer (only positive values)"
  ^Long [^String string]
  (read-string
    (UnsignedInts/toString
      (.asInt
        (.hashString murmur-fun-32 string utf8-chr-set)))))

;; 128 bit
(defn murmur-128-signed-int
  "Returns an Integer (+/-)"
  ^Integer [^String string]
  (.asInt (gen-hash murmur-fun-128 string)))

(defn murmur-128-unsigned-int
  "Returns an Integer (only positive values)"
  [^String string]
  (read-string 
    (UnsignedLongs/toString 
      (.asLong 
        (.hashString murmur-fun-128 string utf8-chr-set)))))
