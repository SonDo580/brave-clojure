(defmulti full-moon-behavior
  (fn [were-creature] (:were-type were-creature)))

(defmethod full-moon-behavior :pitbull
  [were-creature]
  (str (:name were-creature) " will chew"))

(full-moon-behavior {:were-type :pitbull
                     :name "Leo"})