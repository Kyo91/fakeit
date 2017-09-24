(ns fakeit.core
  (:gen-class)
  (:require [clojure.walk :as walk]
            [com.rpl.specter :refer :all ]
            [clojure.data.generators :as gen])
  (:import  [java.util.Date]))

;; Important TODO. Come up with a syntax for multi-value records.
;; Maybe record :value [{:type foo...} {:type bar...}]

(defn clamp [n max & {:keys [min] :or {min 0}}]
  (let [modspace (- max min)]
    (+ min
       (mod n modspace))))

(defn- generate-array [f]
  (vec (repeatedly (clamp (gen/int) 10) f)))

(defn- date-generator [{:keys [startDate endDate meanTime]
                        :or {endDate #inst "2020-01-01T01:00:00.000-00:00"}}]
  (let [generated (if (nil? meanTime) (gen/date) (gen/date meanTime))
        generated-time (.getTime generated)
        endTime (.getTime endDate)]
    (java.util.Date. (clamp generated-time endTime
                            :min (if (nil? startDate)
                                   0
                                   (.getTime startDate)
                                   )))))

;; Add more keys as needed.
;; TODO change this to instead be a multimethod based on :type
;; (defmulti generate :type ...)
(defn generator [{:keys [type min max] :or {min 0 max 100} :as leaf}]
  (case type
    :int (clamp (gen/int) max :min min)
    :float (clamp (gen/float) max :min min)
    :string (gen/string)
    :date (date-generator leaf)))

(defn walk-tree [f {:keys [type value name] :as leaf}]
  (case type
    :record (into {} (mapv #(walk-tree f %) value))
    :field {name (walk-tree f value)}
    :array (generate-array #(walk-tree f value))
    (f leaf)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
