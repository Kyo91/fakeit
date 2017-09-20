(ns fakeit.core
  (:gen-class)
  (:require [clojure.walk :as walk]
            [com.rpl.specter :refer :all ]
            [clojure.data.generators :as gen])
  )

;; Schemas are defined as nested trees of records (maps), arrays (vecs), basic primitives (int, long, string, date, etc...)

;; Desired output would be json/avro equiv of {"foo": [1 2 3 4]}
(def simple-schema
  {:type :record
   :name "foo"
   :value {:type :array
           :value {:type :int}}})

;; {"bar" : [{"baz" : 1} {"baz" : 2} {"baz" : 3}]}
(def nested-schema
  {:type :record
   :name "bar"
   :value {:type :array
           :value {:type :record
                   :name "baz"
                   :value {:type :int}}}})

(defn clamp [n max & {:keys [min] :or {min 0}}]
  (let [modspace (- max min)]
    (+ min
       (mod n modspace))))

(defn generate-array [f]
  (vec (repeatedly (clamp (gen/int) 10) f)))

;; Add more keys as needed. For now, generation is just off of type.
(defn generator [{:keys [type]}]
  (case type
    :int (clamp (gen/int) 100)))

(defn walk-tree [f {:keys [type value name] :as leaf}]
  (case type
    :record {name (walk-tree f value)}
    :array (generate-array #(walk-tree f value))
    (f leaf)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
