(require '[clojure.string :as str])

(let [people (slurp "docs/people.csv")
      people (str/split-lines people)
      header (first people)
      people (rest people)
      header (str/split header #",")
      header (map keyword header)
      people (map
               (fn [person]
                 (zipmap header (str/split person #",")))
               people)
      people (filter
               (fn [person]
                 (= (:country person)
                    "Philippines"))
               people)]
  people)

(as-> (slurp "docs/people.csv")
      people
      (str/split-lines people)
      (map
        (fn [person]
          (let [header (first people)
                header (str/split header #",")
                header (map keyword header)
                person (str/split person #",")]
            (zipmap header person)))
        (rest people)))

