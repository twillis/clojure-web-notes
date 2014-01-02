(ns hello-todo.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [clojure.data.json :as json]))


(def static-path "/Users/twillis/projects/cljweb/hello-todo/resources/public") ;; would be nice to get this from ring


(def entries (atom {})) ;; global variable holding state, obviously not for real apps


(defn list-entries [request]
  {:body (json/write-str @entries)})


(defn create-entry [request]
  (let [new-entry (:entry (:params request))]
    (if (not (nil? new-entry))
      (do 
        (swap! entries merge {(keyword (format "id%s" (inc (count @entries)))) new-entry})
        (list-entries request)))))


(defn delete-entry [request]
  (let [new-list (swap! entries dissoc (keyword (:id (:params request))))]
    (list-entries request)))


(defn update-entry [request]
  (let [new-list (swap! entries merge {(keyword (:id (:params request))) (:entry (:params request))})]
    (list-entries request)))


(defroutes app-routes
  (GET "/list" [] list-entries)
  (POST "/create" [] create-entry)
  (POST "/:id/edit" [id] update-entry)
  (POST "/:id/delete" [id] delete-entry)
  (route/resources "/"))


(def app-handler 
  (handler/site app-routes))


(def app app-handler)
