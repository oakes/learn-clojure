(defrecord Circle [radius])
(defrecord Square [edge])

(defprotocol Area
  (area [this]))

(extend-protocol Area
  Circle
  (area [{:keys [radius]}]
    (* Math/PI radius radius))
  
  Square
  (area [{:keys [edge]}]
    (* edge edge)))

(area (->Square 5))
