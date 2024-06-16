(def asym-body-parts [{:name "head" :size 3}
                      {:name "first-eye" :size 1}
                      {:name "first-ear" :size 1}
                      {:name "mouth" :size 1}
                      {:name "nose" :size 1}
                      {:name "neck" :size 2}
                      {:name "first-shoulder" :size 3}
                      {:name "first-upper-arm" :size 3}
                      {:name "chest" :size 10}
                      {:name "back" :size 10}
                      {:name "first-forearm" :size 3}
                      {:name "abdomen" :size 6}
                      {:name "first-kidney" :size 1}
                      {:name "first-hand" :size 2}
                      {:name "first-knee" :size 2}
                      {:name "first-thigh" :size 4}
                      {:name "first-lower-leg" :size 3}
                      {:name "first-achilles" :size 1}
                      {:name "first-foot" :size 2}])

(defn matching-parts
  [part]
  (map (fn [order]
         {:name (clojure.string/replace (:name part) #"^first" order)
          :size (:size part)})
       ["second" "third" "fourth" "fifth"]))

(defn symmetrize-body-parts
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts
                  (set (conj (matching-parts part) part))))
          []
          asym-body-parts))

(symmetrize-body-parts asym-body-parts)