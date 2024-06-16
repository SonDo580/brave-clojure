(defn dec-maker
  [num]
  #(- % num))

(def dec9 (dec-maker 9))

(dec9 10)
;; 1
