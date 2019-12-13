(ns example.core-server
  (:require [org.httpkit.server :as server]
            [hiccup.core :as hiccup]
            [clojure.string :as str]))

(defn get-people []
  (let [people (slurp "docs/people.csv")
        people (str/split-lines people)
        header (first people)
        people (rest people)
        header (str/split header #",")
        header (map keyword header)
        people (map
                 (fn [person]
                   (zipmap header (str/split person #",")))
                 people)]
    people))

(defmulti handler :uri)

(defmethod handler "/" [request]
  {:status 200
   :body (hiccup/html
           [:html
            [:body
             [:ol
              (map
                (fn [person]
                  [:li (:first_name person)])
                (get-people))]]])})

(defmethod handler "/philippines" [request]
  {:status 200
   :body (hiccup/html
           [:html
            [:body
             [:ol
              (->> (get-people)
                   (filter
                     (fn [person]
                       (= (:country person)
                          "Philippines")))
                   (map
                     (fn [person]
                       [:li (:first_name person)])))]]])})


(defn -main []
  (server/run-server (var handler) {:port 3000})
  (println "Started server"))

