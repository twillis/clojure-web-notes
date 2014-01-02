(defproject hello-todo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [org.clojure/data.json "0.2.3"]
                 [org.clojure/clojurescript "0.0-2127"]
                 [shoreleave/shoreleave-remote "0.3.0"]
                 [org.clojars.trptcolin/core.async "0.1.242.1"]
                 [enfocus "2.1.0-SNAPSHOT"]]

  :source-paths ["src/clj" "src/cljs"]
  :plugins [[lein-ring "0.8.8"]
            [lein-cljsbuild "1.0.0"]]
  :hooks [leiningen.cljsbuild]
  :ring {:handler hello-todo.handler/app}
  :cljsbuild {
              :builds 
              [{:source-paths ["src/cljs"]
                :compiler {
                           :output-to "resources/public/js/app.js"
                           :optimizations :whitespace
                           :pretty-print true}}]}
  :profiles
  {:dev {:repl-options {:nrepl-middleware 
                        [cemerick.piggieback/wrap-cljs-repl
                         io.aviso.nrepl/pretty-middleware]}
         :plugins [[com.cemerick/austin "0.1.3"]]
         :dependencies [[com.cemerick/piggieback "0.1.2"]
                        [javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [io.aviso/pretty "0.1.8"]]}})
