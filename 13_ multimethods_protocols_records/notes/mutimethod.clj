(defmulti full-moon-behavior
  (fn [were-creature] (:were-type were-creature)))

(defmethod full-moon-behavior :wolf
  [were-creature]
  (str (:name were-creature) " will howl and murder"))

(defmethod full-moon-behavior :simmons
  [were-creature]
  (str (:name were-creature) " will encourage people and sweat to the oldies"))

(full-moon-behavior {:were-type :wolf
                     :name "Rachel from next door"})
; => "Rachel from next door will howl and murder"

(full-moon-behavior {:name "Andy the baker"
                     :were-type :simmons})
; => "Andy the baker will encourage people and sweat to the oldies"

(defmethod full-moon-behavior nil
  [were-creature]
  (str (:name were-creature) " will stay at home and eat ice cream"))

(full-moon-behavior {:were-type nil
                     :name "Martin the nurse"})
; => "Martin the nurse will stay at home and eat ice cream"

(defmethod full-moon-behavior :default
  [were-creature]
  (str (:name were-creature) " will stay up all night fantasy footballing"))

(full-moon-behavior {:were-type :office-worker
                     :name "Jimmy from sales"})
; => "Jimmy from sales will stay up all night fantasy footballing"

(defmulti types (fn [x y] [(class x) (class y)]))

(defmethod types [java.lang.String java.lang.String]
  [x y]
  "Two strings!")

(types "String 1" "String 2")
; => "Two strings!"