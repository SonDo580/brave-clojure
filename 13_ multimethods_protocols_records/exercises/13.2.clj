(defprotocol WereCreature
  (full-moon-behavior [x]))

(defrecord WereSimons [name skill]
  WereCreature
  (full-moon-behavior [x]
    (str name " can " skill)))

(full-moon-behavior
 (map->WereSimons {:name "Zero" :skill "slice"}))