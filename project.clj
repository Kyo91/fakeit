(defproject fakeit "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.8.0"]
                 [com.damballa/abracad "0.4.13"]
                 [com.rpl/specter "1.0.3"]]
  :main ^:skip-aot fakeit.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
