(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

;; Use separate function definitions
;; (def c-int (comp :intelligence :attributes))
;; (def c-str (comp :strength :attributes))
;; (def c-dex (comp :dexterity :attributes))

;; (c-int character) ; => 10
;; (c-str character) ; => 4
;; (c-dex character) ; => 5

;; Use 1 macro to define an arbitray number of attribute-retrieving functions 
(defmacro defattrs
  [& args]
  `(do (~@(map (fn [[fn-name attr]]
                 `(def ~fn-name (comp ~attr :attributes)))
               (partition 2 args)))))

(defattrs 
  c-int :intelligence
  c-str :strength
  c-dex :dexterity)

(c-int character) ; => 10
(c-str character) ; => 4
(c-dex character) ; => 5
