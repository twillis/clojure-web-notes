(ns hello-enlive.core
  (:use net.cgrand.enlive-html))

;; snippets imagine the designer has put some placeholder li's in for
;; the prototype, and you replace all of them with dynamic
;; content. here's a way to do it
;; http://stackoverflow.com/a/19073002/67393 "First, you define
;; snippets with the specific elements that you want to clone with
;; real data, then you can define a template and replace the contents
;; of the elements where you want to put the snippets. This way you
;; don't have to delete the dummy nodes."
(defsnippet t-navigation "index.html" [:#nav [:li (nth-of-type 1)]] [navitems]
  [:li ] (clone-for [i navitems] (content i)))


;; basic example of a layout template
(deftemplate t-layout "index.html" [title navitems page_content footer]
  [:head :title] (content title)
  [:#nav] (content (t-navigation navitems))
  [:#content] (content page_content)
  [:#footer] (content footer))
