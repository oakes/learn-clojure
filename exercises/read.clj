(def text "35 1/2 1.2 foo-bar")

(def chars (vec text))

(def digits (set "0123456789"))

(defn digit? [ch]
  (contains? digits ch))

(def char-names {\space :space
                 \. :period
                 \/ :forward-slash})

(defn char-type [ch]
  (cond
    (digit? ch)
    :digit
    
    (contains? char-names ch)
    (get char-names ch)
    
    true
    :other))

(let [first-char (get text 0)
      second-char (get text 1)
      third-char (get text 2)]
  (and (digit? first-char)
       (digit? second-char)
       (digit? third-char)))

(loop [index 0
       result []
       group []
       last-type nil]
  (let [ch (get chars index)
        current-type (char-type ch)]
    (if ch
      (if (= current-type
             (or last-type current-type))
        (recur
          (+ index 1)
          result
          (conj group ch)
          current-type)
        (recur
          (+ index 1)
          (conj result group)
          [ch]
          current-type))
      (conj result group))))

(defn read-token [text]
  (or (re-find #"^\d+\/\d+" text)
      (re-find #"^\d+\.\d+" text)
      (re-find #"^\d+" text)
      (re-find #"^ +" text)
      (re-find #"^[^\d].*" text)))

(loop [result []
       index 0]
  (let [text (subs text index)
        token (read-token text)]
    (if token
      (recur
        (conj result token)
        (+ index (count token)))
      result)))

