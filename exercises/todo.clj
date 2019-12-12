
(loop [todos []]
  (let [todo (read-line)]
    (if (not= todo "q")
      (recur (conj todos todo))
      todos)))

