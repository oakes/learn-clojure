(require
  '[markdown-to-hiccup.core :as m]
  '[hiccup.core :as h]
  '[clojure.walk :as walk])

(defn add-tags [hiccup]
  (-> hiccup
      (update 2 conj [:link {:rel :stylesheet :type "text/css" :href "paren-soup-light.css"}])
      (update 2 conj [:link {:rel :stylesheet :type "text/css" :href "style.css"}])
      (update 3 conj [:script {:type "text/javascript" :src "paren-soup-with-compiler.js"}])
      (update 3 conj [:script {:type "text/javascript"} "paren_soup.core.init_all({\"disable-timeout?\":true});"])))

(def chapters
  [{:prefix "01.read"
    :title "The Reader"}
   {:prefix "02.eval"
    :title "The Evaluator"}
   {:prefix "03.vec"
    :title "Vectors and Sets"}
   {:prefix "04.fn"
    :title "Functions and Lists"}
   {:prefix "05.cond"
    :title "Conditions"}
   {:prefix "06.hash-map"
    :title "Hash Maps"}
   {:prefix "07.let"
    :title "Local Bindings"}
   {:prefix "08.loop"
    :title "Loops"}
   {:prefix "09.loop"
    :title "Loops Continued"}
   {:prefix "10.regex"
    :title "Regular Expressions"}
   {:prefix "11.require"
    :title "Namespaces"}])

(defn add-links [i hiccup]
  (let [prev-page (get chapters (dec i))
        next-page (get chapters (inc i))]
    (cond-> hiccup
            prev-page
            (update 3 conj [:a {:href (str (:prefix prev-page) ".html")
                                :style "float:left;"}
                            "Previous: " (:title prev-page)])
            next-page
            (update 3 conj [:a {:href (str (:prefix next-page) ".html")
                                :style "float:right;"}
                            "Next: " (:title next-page)]))))

(defn md->html [_ i {:keys [prefix title]}]
  (->> (str "docs/" prefix ".md")
       slurp
       m/md->hiccup
       add-tags
       (add-links i)
       (walk/prewalk (fn [x]
                       (if (and (vector? x)
                                (= (first x) :code)
                                (-> x second :class (= "clojure")))
                         [:div {:class "paren-soup"}
                          [:div {:class "instarepl"}]
                          (into [:div {:class "content"
                                       :contenteditable "true"}]
                                (drop 2 x))]
                         x)))
       h/html
       (spit (str "docs/" prefix ".html"))))

(reduce-kv md->html nil chapters)

