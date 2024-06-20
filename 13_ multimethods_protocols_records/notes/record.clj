(defrecord WereWolf [name title])

;; use class instantiation interop call
(WereWolf. "David" "London Tourist")

;; use factory functions
(->WereWolf "Jacob" "Lead Shirt Discarder")
(map->WereWolf {:name "Lucian" :title "CEO of Melodrama"})

(def jacob (->WereWolf "Jacob" "Lead Shirt Discarder"))
(.name jacob) ; => "Jacob"
(:name jacob) ; => "Jacob"
(get jacob :name) ; => "Jacob"

(= jacob (->WereWolf "Jacob" "Lead Shirt Discarder")) ; => true
(= jacob (WereWolf. "David" "London Tourist")) ; => false

;; Note that a record is not a map
(= jacob {:name "Jacob" :title "Lead Shirt Discarder"}) ; => false

(assoc jacob :title "Lead Third Wheel")

;; If you dissoc a field, the result's type is a map, not a record 
(dissoc jacob :title)
; => {:name "Jacob"} 

;; Extend protocol
(defprotocol WereCreature
  (full-moon-behavior [x]))

(defrecord WereWolf [name title]
  WereCreature
  (full-moon-behavior [x]
    (str name " will howl and murder")))

(full-moon-behavior 
 (map->WereWolf {:name "Lucian" :title "CEO of Melodrama"}))
; => "Lucian will howl and murder"