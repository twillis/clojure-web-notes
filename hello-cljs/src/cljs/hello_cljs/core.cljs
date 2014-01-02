;;; If this namespace requires macros, remember that ClojureScript's
;;; macros are written in Clojure and have to be referenced via the
;;; :require-macros directive where the :as keyword is required. Even
;;; if you can add files containing macros and compile-time only
;;; functions in the :source-paths setting of the :builds, it is
;;; strongly suggested to add them to the leiningen :source-paths.
(ns hello-cljs.core
  (:use [jayq.core :only [$ css html]])
  (:require [cljsbinding :as binding]
            [goog.dom :as dom]))


;; todo:
;; - 2 way binding
;; bind atom
;; bind map
;; bind sequence


;; - events
;; - routing
;; - client side templates


(defn foo [greeting]
  (if greeting 
    (str greeting "ClojureScript!")
    (str "Hello, ClojureScript!")))

(.write js/document (foo "Welcome to "))

;; get element and write to console
(.log js/console (dom/getElement "test"))

(.log js/console ($ "#test"))
