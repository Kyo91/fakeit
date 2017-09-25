(ns fakeit.io
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :refer [writer]]
            [fakeit.core :refer [generate-schema]]) )


(defn read-json [json]
  (cheshire/parse-string (slurp json) true))

(defn read-edn [edn])

(defn read-avro [avro])

(defn write-json [schema n out]
  (with-open [w (writer out :append true)]
    (dotimes [i n]
      (let [generation (generate-schema schema)
            as-json (cheshire/generate-string generation)]
        (.write w as-json)))))

(defn write-edn [schema n out])

(defn write-avro [schema n out])
