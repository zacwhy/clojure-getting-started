(ns clojure-getting-started.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as db]
            [environ.core :refer [env]]
            [hiccup.page :as h]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :refer [response]]))

(defn index []
  (h/html5
    [:head
     [:title "Hello World"]
     (h/include-css "/styles/style.css")
     (h/include-js "https://unpkg.com/react@16/umd/react.development.js")
     (h/include-js "https://unpkg.com/react-dom@16/umd/react-dom.development.js")]
    [:body
     [:div {:id "root"}]
     (h/include-js "/scripts/index.js")]))

(defroutes inner-routes
  (GET "/entries" []
       (response
         (db/query (env :database-url)
                   ["select id, transaction_date, amount, from_account, to_account, description, created_on from entries"])))
  (POST "/entries"
        {{:keys [transaction_date amount from_account to_account description]} :params}
        (response
          (db/insert! (env :database-url)
                      :entries {:transaction_date (java.sql.Date/valueOf transaction_date)
                                :amount amount
                                :from_account from_account
                                :to_account to_account
                                :description description}))))

(def api-routes
  (-> inner-routes
      wrap-json-response
      wrap-keyword-params
      wrap-json-params))

(defroutes app
  api-routes
  (GET "/" [] (index))
  (route/resources "/")
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
