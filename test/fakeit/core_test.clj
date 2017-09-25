(ns fakeit.core-test
  (:require [clojure.test :refer :all]
            [fakeit.core :refer :all]
            [clojure.data.generators :refer [*rnd*]])
  (:import java.util.Random))

(def simple-field
  {:type "field"
   :name "foo"
   :value {:type "array"
           :value {:type "int"}}})

(def nested-schema
  {:type "record"
   :value [{:type "field"
            :name "bar"
            :value {:type "array"
                    :value {:type "field"
                            :name "baz"
                            :value {:type "int"}}}}]})

(def two-fields
  {:type "record"
   :value [{:type "field"
            :name "field1"
            :value {:type "int"}}
           {:type "field"
            :name "field2"
            :value {:type "array"
                    :value {:type "date"}}}]})

(def int-schema
  {:type "field"
   :name "int"
   :value {:type "int"}})

(def float-schema
  {:type "field"
   :name "float"
   :value {:type "float"}})

(def string-schema
  {:type "field"
   :name "string"
   :value {:type "string"}})

(def date-schema
  {:type "field"
   :name "date"
   :value {:type "date"}})

(defmacro is-fixed-rnd
  "Helper macro to fix RNG generator before each assertion."
  ([form] `(binding [*rnd* (Random. 10)]
            (is ~form)))
  ([form msg] `(binding [*rnd* (Random. 10)]
                    (is ~form ~msg))))

(deftest clamp-tests
  (testing "Make sure custom clamp function works."
    (are [x floor ceiling] (<= floor (#'fakeit.core/clamp x ceiling :min floor) ceiling)
      10 20 30
      20 10 30
      100 10 30
      12.3333 5 8)))

(deftest basic-test
  (testing "Ensure general schemas work"
    (let [simple {"foo" [6 59 59 26 17 45 54 86]}
          nested {"bar" [{"baz" 6} {"baz" 59} {"baz" 59} {"baz" 26} {"baz" 17} {"baz" 45} {"baz" 54} {"baz" 86}]}
          double {"field1" 88, "field2" [#inst "1976-10-28T17:19:44.572-00:00" #inst "1973-04-15T21:37:08.910-00:00" #inst "1977-07-22T04:08:54.565-00:00" #inst "2007-07-06T08:24:06.243-00:00" #inst "1975-11-12T12:18:44.626-00:00" #inst "1982-09-05T15:29:16.014-00:00"]}]
      (is-fixed-rnd (= (walk-tree generator simple-field) simple)
          "Failed to generate simple schema.")
      (is-fixed-rnd (= (walk-tree generator nested-schema) nested)
                    "Falied to generate nested schema.")
      (is-fixed-rnd (= (walk-tree generator two-fields) double)
                    "Falied to generate nested schema."))))

(deftest type-tests
  (testing "Ensure generators for all types work as expected"
    (let [int {"int" 88}
          float {"float" 0.7304302453994751}
          string {"string" "8%7mCqc;w I\"w{l"}
          date {"date" #inst "1981-11-14T15:35:16.326-00:00"}]
      (is-fixed-rnd (= (walk-tree generator int-schema) int)
                  "Failed to generate int schema.")
      (is-fixed-rnd (= (walk-tree generator float-schema) float)
                  "Failed to generate float schema.")
      (is-fixed-rnd (= (walk-tree generator string-schema) string)
                    "Failed to generate string schema.")
      (is-fixed-rnd (= (walk-tree generator date-schema) date)
                    "Failed to generate date schema."))))
