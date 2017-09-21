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

(def int-schema
  {:type :record
   :name "int"
   :value {:type :int}})

(def float-schema
  {:type :record
   :name "float"
   :value {:type :float}})

(def string-schema
  {:type :record
   :name "string"
   :value {:type :string}})

(defmacro is-fixed-rnd
  "Helper macro to fix RNG generator before each assertion."
  ([form] `(binding [*rnd* (Random. 10)]
            (is ~form)))
  ([form msg] `(binding [*rnd* (Random. 10)]
                    (is ~form ~msg))))

(deftest clamp-tests
  (testing "Make sure custom clamp function works."
    (are [x floor ceiling] (<= floor (clamp x ceiling :min floor) ceiling)
      10 20 30
      20 10 30
      100 10 30
      12.3333 5 8)))



(deftest basic-test
  (testing "Ensure simple-schema and nested-schema work"
    (let [simple {"foo" [6 59 59 26 17 45 54 86]}
          nested {"bar" [{"baz" 6} {"baz" 59} {"baz" 59} {"baz" 26} {"baz" 17} {"baz" 45} {"baz" 54} {"baz" 86}]}]
      (is-fixed-rnd (= (walk-tree generator simple-schema) simple)
          "Failed to generate simple schema.")
      (is-fixed-rnd (= (walk-tree generator nested-schema) nested)
          "Falied to generate nested schema."))))

(deftest type-tests
  (testing "Ensure generators for all types work as expected"
    (let [int {"int" 88}
          float {"float" 0.7304302453994751}
          string {"string" "8%7mCqc;w I\"w{l"}]
      (is-fixed-rnd (= (walk-tree generator int-schema) int)
                  "Failed to generate int schema.")
      (is-fixed-rnd (= (walk-tree generator float-schema) float)
                  "Failed to generate float schema.")
      (is-fixed-rnd (= (walk-tree generator string-schema) string)
                  "Failed to generate string schema."))))
