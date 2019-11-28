Should I use a vector or set?

* Pre-flight checklist
* List of blocked phone numbers
* Notes in a chord
* Christmas list
* Spell checker

A tic-tac-toe checker:

```clojure
(def board [:x :e :o
            :x :e :e
            :x :e :o])

(defn solve [board]
  (let [[a b c
         d e f
         g h i] board
        solutions (hash-set
                    [a b c]
                    [d e f]
                    [g h i]
                    [a d g]
                    [b e h]
                    [c f i]
                    [a e i]
                    [c e g])]
    (cond
      (contains? solutions [:x :x :x]) :x
      (contains? solutions [:o :o :o]) :o
      :else nil)))
```

Find all flushes in a deck of cards:

```clojure
(def suits [:clubs :spades :hearts :diamonds])
(def ranks (range 1 14))
(def rank-names {1 :ace, 11 :jack, 12 :queen, 13 :king})

(defonce deck
  (set (for [suit suits
             rank ranks]
         {:suit suit
          :rank (get rank-names rank rank)})))

(defonce hands
  (set (for [c1 deck
             c2 (disj deck c1)
             c3 (disj deck c1 c2)
             c4 (disj deck c1 c2 c3)]
         #{c1 c2 c3 c4})))

(defn flush? [hand]
  (= 1 (count (set (map :suit hand)))))

(count (filter flush? hands))
```

Write the following functions:

* `straight-flush?` returns true if the given cards have the same suit and the ranks are in a sequence
* `straight?` returns true if the given cards' ranks are in a sequence but are not a straight flush
* `four-of-a-kind?` returns true if the given cards have the same rank
* `three-of-a-kind?` returns true if only 3 of the given cards have the same rank
* `two-pair?` returns true if 2 of the given cards have the same rank, and the other 2 have the same rank

Parse a CSV file:

```clojure
(let [people (slurp "people.csv")
      people (str/split-lines people)
      people (map (fn [line]
                    (str/split line #","))
               people)
      header (first people)
      people (rest people)
      people (map (fn [line]
                    (zipmap header line))
               people)
      people (filter (fn [line]
                       (= (get line "country") "Brazil"))
               people)]
  people)
```

Generate a maze:

```clojure
(def size 10)

(def rooms
  (vec (for [row (range 0 size)]
         (vec (for [col (range 0 size)]
                {:row row, :col col, :visitable? true,
                 :bottom? true, :right? true})))))

(defn possible-neighbors [rooms row col]
  [(get-in rooms [(- row 1) col])
   (get-in rooms [(+ row 1) col])
   (get-in rooms [row (- col 1)])
   (get-in rooms [row (+ col 1)])])

(defn random-neighbor [rooms row col]
  (let [neighbors (possible-neighbors rooms row col)
        neighbors (filter :visitable? neighbors)]
    (first (shuffle neighbors))))

(defn tear-down-wall [rooms old-row old-col new-row new-col]
  (cond
    ; going up
    (< new-row old-row)
    (assoc-in rooms [new-row new-col :bottom?] false)
    ;going down
    (> new-row old-row)
    (assoc-in rooms [old-row old-col :bottom?] false)
    ; going left
    (< new-col old-col)
    (assoc-in rooms [new-row new-col :right?] false)
    ; going right
    (> new-col old-col)
    (assoc-in rooms [old-row old-col :right?] false)))

(defn create-maze [rooms row col]
  (let [rooms (assoc-in rooms [row col :visitable?] false)
        next-room (random-neighbor rooms row col)]
    (if next-room
      (let [rooms (tear-down-wall rooms row col (:row next-room) (:col next-room))]
        (loop [old-rooms rooms]
          (let [new-rooms (create-maze old-rooms (:row next-room) (:col next-room))]
            (if (= old-rooms new-rooms)
              old-rooms
              (recur new-rooms)))))
      rooms)))

(let [rooms (create-maze rooms 0 0)]
  ; print top walls
  (doseq [row rooms]
    (print " _"))
  (println)
  ; print grid
  (doseq [row rooms]
    (print "|")
    (doseq [room row]
      (print (str (if (:bottom? room) "_" " ")
                  (if (:right? room) "|" " "))))
    (println)))
```

A to-do list:

```clojure
(def to-dos (atom []))

(defn add-to-do [input]
  (reset! to-dos (conj (deref to-dos) input)))

(println "Type a to-do and hit enter.")
(loop []
  (let [input (read-line)]
    (when (not= input "q")
      (add-to-do input)
      (recur))))
(println (deref to-dos))
```

Barebones web app:

```clojure
(ns example.server
  (:require [org.httpkit.server :as server]
            [hiccup.core :as h]))

(defn app [request]
  {:status 200
   :body (h/html
           [:html
            [:body
             "Hello, world!"]])})

(defn -main []
  (server/run-server (var app) {:port 3000}))
```
