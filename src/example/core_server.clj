(ns example.core-server
  (:require [org.httpkit.server :as server]))

(defn handler [request]
  (println "Received request:")
  (println request))

(defn -main []
  (server/run-server (var handler) {:port 3000})
  (println "Started server"))

