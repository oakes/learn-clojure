(def size 10)

(defrecord Room [row col bottom? right?
                 can-visit?])

(def rooms
  (vec
    (for [row (range 0 size)]
      (vec
        (for [col (range 0 size)]
          (map->Room
            {:row row
             :col col
             :bottom? true
             :right? true
             :can-visit? true}))))))

(defn get-neighbors [rooms row col]
  [(get-in rooms [row (dec col)])
   (get-in rooms [row (inc col)])
   (get-in rooms [(dec row) col])
   (get-in rooms [(inc row) col])])

(defn get-random-neighbor [rooms row col]
  (let [neighbors (get-neighbors rooms row col)
        neighbors (filter :can-visit? neighbors)]
    (if (empty? neighbors)
      nil
      (rand-nth neighbors))))

(defn tear-down-wall [rooms room1 room2]
  (let [row1 (:row room1)
        col1 (:col room1)
        row2 (:row room2)
        col2 (:col room2)]
    (cond
      (< col2 col1)
      (assoc-in rooms [row1 col2 :right?] false)
      
      (> col2 col1)
      (assoc-in rooms [row1 col1 :right?] false)
      
      (> row2 row1)
      (assoc-in rooms [row1 col1 :bottom?] false)
    
      (< row2 row1)
      (assoc-in rooms [row2 col1 :bottom?] false))))

(defn make-path [rooms row col]
  (let [neighbor (get-random-neighbor rooms row col)
        rooms (assoc-in rooms [row col :can-visit?] false)]
    (if neighbor
      (let [rooms (tear-down-wall rooms
                    {:row row :col col}
                    neighbor)]
        (loop [old-rooms rooms]
          (let [rooms (make-path old-rooms (:row neighbor) (:col neighbor))]
            (if (= old-rooms rooms)
              rooms
              (recur rooms)))))
      rooms)))

(def rooms (make-path rooms 0 0))

(dotimes [_ size]
  (print " _"))
(println)
(doseq [row rooms]
  (print "|")
  (doseq [room row]
    (print
      (str
        (if (:bottom? room) "_" " ")
        (if (:right? room) "|" " "))))
  (println))

