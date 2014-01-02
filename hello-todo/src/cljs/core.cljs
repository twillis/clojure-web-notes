;; TODO: need client side templating solution TODO: need more complex
;; dom interaction ex: replace element with another element wire-up
;; event handlers and such....in other words... components


;; *** cljs-repl ***
;; (require 'cljs.repl.browser) (cemerick.piggieback/cljs-repl :repl-env (cljs.repl.browser/repl-env :port 9000))

;; or ...

;; (cemerick.austin.repls/cljs-repl (reset! cemerick.austin.repls/browser-repl-env (cemerick.austin/repl-env)))
(ns hello-todo.core
  (:require 
   [clojure.browser.repl]
   [enfocus.core :as ef]
   [enfocus.events :as events]
   [cljs.core.async :refer [put! chan <!]]
   [shoreleave.remotes.request :refer [request]])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [enfocus.macros :as em]))


(defn attach-to-repl [port]
  "utility function to attach the page to a brepl, when you start a
brepl it usually tells you what port/path it's running on."
  (clojure.browser.repl/connect (str "http://localhost:" port "/repl")))


(def todo-list (atom {}))
(def list-todo "#list-todo")
(def info-todo "#info")
(def new-todo "#new-todo")
(def server-base-url "http://localhost:3000")

(defn get-todo-list [e]
  (request [:get (str server-base-url "/list")]
           :on-success 
           (fn [r] (reset! todo-list 
                           (js->clj 
                            (JSON/parse 
                             (:body r)))))))
(defn enter-pressed? [e]
  (= 13 (.-keyCode e)))

(defn on-enter-create [e]
  (do 
    (.log js/console e)
    (if (enter-pressed? e) 
      (let [target (.-target e) 
            entry (.-value target)]
        (request [:post (str server-base-url "/create")]
                 :content {:entry entry}
                 :on-success 
                 (fn [r] 
                   (get-todo-list nil)
                   (set! (.-value target) "")))))))


(defn on-enter-update [e]
  (do 
    (.log js/console e)
    (if (enter-pressed? e) 
      (let [target (.-target e) 
            entry (.-value target)
            item-id (.getAttribute target "data")
            url (str server-base-url "/" item-id "/edit")]
        (request [:post url]
                 :content {:entry entry}
                 :on-success 
                 (fn [r] 
                   (get-todo-list nil)))))))

;;new todo event
(ef/at new-todo 
       (events/listen :keypress 
                      (fn [e]
                        (on-enter-create e))))


;; edit event
(ef/at list-todo 
       (events/listen-live  :dblclick ".item" 
                           (fn [e]
                             (let [target (.-target e)
                                   item-id (.getAttribute target "data")
                                   entry (@todo-list item-id)]
                               (.log js/console (str "editting " item-id " " entry))
                               (ef/at target (ef/content (ef/html (todo-edit-template [item-id entry]))))))))


;; delete event
(ef/at list-todo 
       (events/listen-live :click ".delete" 
                           (fn [e]
                             (let [target (.-target e)
                                   item-id (.getAttribute target "data")
                                   url (str server-base-url "/" item-id "/delete" )]
                               (request [:post url]
                                        :on-success
                                        (fn[r]
                                          (get-todo-list nil)))))))

;; save event
(ef/at list-todo
       (events/listen-live :keypress ".edit-item"
                           (fn [e]
                             (on-enter-update e))))




(defn todo-edit-template [[id entry] todo-item]
  [:input {:data id :id id :type "text" :class "edit-item" :value entry}])

(defn todo-view-template [[id entry] todo-item]
  [:span {:data id :class "item"}  entry])

(defn todo-item-template [[id entry] todo-item]
  [:li {:data id} 
   [:span {:data id :class "delete"} "x"] 
   " " 
   (todo-view-template [id entry])])


(defn todo-list-template [val]
  [:ul (map todo-item-template val)])


(defn render-todo-list [val]
  (ef/at list-todo (ef/content (ef/html (todo-list-template val)))))


(add-watch todo-list "update" (fn [key identity oldv newv]
                                (if (not (= oldv newv))
                                  (render-todo-list @identity))))


(set! (.-onload js/window) get-todo-list)
