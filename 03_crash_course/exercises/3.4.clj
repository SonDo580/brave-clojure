(defn mapset
  [fn seq]
  (into #{}
        (map fn seq)))

(mapset inc [1 1 2 2])
;; #{3 2}