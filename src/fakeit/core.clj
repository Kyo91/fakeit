(ns fakeit.core
  (:gen-class)
  (:require [clojure.walk :as walk]
            [com.rpl.specter :refer :all ])
  (:import java.util.Random))

;; Schemas are defined as nested trees of records (maps), arrays (vecs), basic primitives (int, long, string, date, etc...)

;; Desired output would be json/avro equiv of {"foo": [1 2 3 4]}
(def simple-schema
  {:type :record
   :name "foo"
   :value {:type :array
           :value {:type :int}}})

;; {"bar" : [{"baz" : 1} {"baz" : 2} {"baz" : 3}]}
(def n-schema
  {:type :record
   :name "bar"
   :value {:type :array
           :value {:type :record
                   :name "baz"
                   :value {:type :int}}}})

(defn generate-array [f]
  (vec (repeatedly (rand-int 10) f)))

;; Add more keys as needed. For now, generation is just off of type.
(defn generator [{:keys [type]}]
  (case type
    :int (rand-int 100)))

(defn walk-tree [f {:keys [type value name] :as leaf}]
  (case type
    :record {name (walk-tree f value)}
    :array (generate-array #(walk-tree f value))
    (f leaf)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
