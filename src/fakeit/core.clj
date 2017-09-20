(ns fakeit.core
  (:gen-class)
  (:require [clojure.walk :as walk]
            [com.rpl.specter :refer :all ]
            [clojure.data.generators :as gen]))

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
