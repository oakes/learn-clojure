(def suits [:spades :hearts :clubs :diamonds])
(def ranks (range 1 14))

(def deck
  (vec
    (for [suit suits
          rank ranks]
      {:suit suit
       :rank rank})))

(comment
  (filter
    (fn [card]
      (= (:suit card) :clubs))
    deck)
  
  (map :suit deck))

(defonce hands
  (for [index1 (range 0 (dec (count deck)))
        index2 (range (inc index1) (count deck))
        index3 (range (inc index2) (count deck))
        index4 (range (inc index3) (count deck))]
    #{(get deck index1)
      (get deck index2)
      (get deck index3)
      (get deck index4)}))

(comment
  (defonce hands
    (set
      (for [card1 deck
            card2 (disj deck card1)
            card3 (disj deck card1 card2)
            card4 (disj deck card1 card2 card3)]
        #{card1 card2 card3 card4}))))

(defn flush? [hand]
  (= 1 (count (set (map :suit hand)))))

(defn straight? [hand]
  (let [ranks (sort (map :rank hand))
        [r1 _ _ r4] ranks]
    (and (= 3 (- r4 r1))
         (= 4 (count (set ranks))))))   

(defn straight-flush? [hand]
  (and (flush? hand)
       (straight? hand)))

(comment
  (defn two-pair? [hand]
    (let [groups (group-by :rank hand)
          ranks (keys groups)
          [r1 r2] ranks]
      (and (= 2 (count ranks))
           (= 2 (count (get groups r1)))))))

(defn two-pair? [hand]
  (let [ranks (sort (map :rank hand))
        [r1 r2 r3 r4] ranks]
    (and (= r1 r2)
         (= r3 r4)
         (not= r2 r3))))

(count (filter two-pair? hands))

