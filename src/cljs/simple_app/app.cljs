(ns simple-app.app
  (:require [ajax.core :refer [GET POST]]
            [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defonce click-count (atom 0))
(def hello (atom nil))

(defn handler [response]
  (reset! hello response)
  (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
  (println (str "something bad happened: " status " " status-text)))

(defn say-hello []
  (GET "/entries"
       {:handler handler
        :error-handler error-handler
        :format :json
        :response-format :json}))

(defn state-ful-with-atom []
  [:div {:on-click #(swap! click-count inc)}
   "I have been clicked " @click-count " times."])

(defn timer-component []
  (let [seconds-elapsed (atom 0)]
    (fn []
      (js/setTimeout #(swap! seconds-elapsed inc) 1000)
      [:div "Seconds Elapsed: " @seconds-elapsed])))

(defn entry-list-item [{:strs [id from_account to_account description]}]
  [:div
   [:span id]
   [:span from_account]
   [:span to_account]
   [:span description]])

(defn hello-world []
  (say-hello)
  (fn []
    [:div
     (for [m @hello]
       ^{:key (get m "id")}
       [entry-list-item m])
     [state-ful-with-atom]
     [timer-component]]))

(reagent/render-component [hello-world]
                          (. js/document (getElementById "root")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
