(ns fakeit.core-test
  (:require [clojure.test :refer :all]
            [fakeit.core :refer :all]
            [clojure.data.generators :refer [*rnd*]])
  (:import java.util.Random))

(def simple-schema
  {:type :record
   :name "foo"
   :value {:type :array
           :value {:type :int}}})

(def nested-schema
  {:type :record
   :name "bar"
   :value {:type :array
           :value {:type :record
                   :name "baz"
                   :value {:type :int}}}})

(deftest a-test
  (testing "Ensure simple-schema and nested-schema work"
    (let [simple {"foo" [6 59 59 26 17 45 54 86]}
          nested {"bar" [{"baz" 6} {"baz" 59} {"baz" 59} {"baz" 26} {"baz" 17} {"baz" 45} {"baz" 54} {"baz" 86}]}]
      (binding [*rnd* (Random. 10)] 
        (is (= (walk-tree generator simple-schema) simple)
            "Failed to generate simple schema."))
      (binding [*rnd* (Random. 10)] 
        (is (= (walk-tree generator nested-schema) nested)
            "Falied to generate nested schema.")))))
