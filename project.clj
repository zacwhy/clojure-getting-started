(defproject clojure-getting-started "1.0.0-SNAPSHOT"
  :description "Demo Clojure web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [environ "1.0.0"]
                 [org.clojure/java.jdbc "0.3.5"]
                 [org.postgresql/postgresql "9.4-1201-jdbc4"]

                 ;; ClojureScript
                 [org.clojure/clojurescript "1.9.229"]
                 [cljs-ajax "0.5.1"]
                 [reagent "0.7.0"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]
            [lein-environ "1.1.0"]
            [lein-ring "0.9.7"]

            ;; ClojureScript
            [lein-figwheel "0.5.14"]]
  :ring {:handler clojure-getting-started.web/app}
  :hooks [environ.leiningen.hooks]
  :uberjar-name "clojure-getting-started-standalone.jar"
  :cljsbuild {:builds [{:id "simple-app"
                        :source-paths ["src/cljs"]
                        :figwheel true
                        :compiler {:main "simple-app.app"
                                   :asset-path "js/out"
                                   :output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"}}]}
  :profiles {:production {:env {:production true}}})
