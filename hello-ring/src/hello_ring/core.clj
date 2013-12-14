(ns hello-ring.core)
;; for ring to work you need a function which takes a request and
;; returns a map representing the response. various handlers for
;; servlet containers exist to pass data to the handlers like jetty
(defn handler [request] 
  {:status 200
   :body "hello ring handler"})

;; this is an example of middleware, which provides a way to
;; manipulate request and responses before and after the handler
(defn wrap-add-header [handler]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "My-Special-Header"] "special/value"))))

;; handler wrapped by middleware
(def wrapped-handler (wrap-add-header handler))
