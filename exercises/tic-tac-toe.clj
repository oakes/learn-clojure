(def board [:x :e :o
            :x :e :e
            :x :e :o])

(let [[a1 b1 c1
       a2 b2 c2
       a3 b3 c3] board
      
      solutions (hash-set
                  [a1 a2 a3]
                  [b1 b2 b3]
                  [c1 c2 c3]
                  [a1 b1 c1]
                  [a2 b2 c2]
                  [a3 b3 c3]
                  [a1 b2 c3]
                  [a3 b2 c1])]
  (cond
    (contains? solutions [:x :x :x])
    :x
    
    (contains? solutions [:o :o :o])
    :o))

