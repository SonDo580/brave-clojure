;; ATOM
(def fred (atom {:cuddle-hunger-level 0
                 :percent-deteriorated 0}))

@fred
; => {:cuddle-hunger-level 0, :percent-deteriorated 0}

(let [zombie-state @fred]
  (if (>= (:percent-deteriorated zombie-state) 50)
    (future (println (:cuddle-hunger-level zombie-state)))))

(swap! fred
       (fn [current-state]
         (merge-with + current-state {:cuddle-hunger-level 1})))
; => {:cuddle-hunger-level 1, :percent-deteriorated 0}

(swap! fred
       (fn [current-state]
         (merge-with + current-state {:cuddle-hunger-level 1
                                      :percent-deteriorated 1})))
; => {:cuddle-hunger-level 2, :percent-deteriorated 1}

(defn increase-cuddle-hunger-level
  [zombie-state increase-by]
  (merge-with + zombie-state {:cuddle-hunger-level increase-by}))

;; Note that this doesn't update 'fred'
(increase-cuddle-hunger-level @fred 10)
; => {:cuddle-hunger-level 12, :percent-deteriorated 1}

(swap! fred increase-cuddle-hunger-level 10)
; => {:cuddle-hunger-level 12, :percent-deteriorated 1}
@fred 
; => {:cuddle-hunger-level 12, :percent-deteriorated 1}

(update-in {:a {:b 3}} [:a :b] inc)
;; => {:a {:b 4}}

(update-in {:a {:b 3}} [:a :b] + 10)
;; => {:a {:b 13}}

(swap! fred update-in [:cuddle-hunger-level] + 10)
; => {:cuddle-hunger-level 22, :percent-deteriorated 1}

(let [num (atom 1)
      s1 @num]
  (swap! num inc)
  (println "State 1:" s1)
  (println "Current state:" @num))
; => State 1: 1
; => Current state: 2

(reset! fred {:cuddle-hunger-level 0
              :percent-deteriorated 0})
; => {:cuddle-hunger-level 0, :percent-deteriorated 0}

;; WATCH
(defn shuffle-speed
  [zombie]
  (* (:cuddle-hunger-level zombie)
     (- 100 (:percent-deteriorated zombie))))

(defn shuffle-alert
  [key watched-atom old-state new-state]
  (let [sph (shuffle-speed new-state)]
    (println "The zombie's SPH is now " sph)
    (println "Key: " key)
    (if (> sph 5000)
      (do
        (println "Danger"))
      (do
        (println "All's well")))))

(add-watch fred :fred-shuffle-alert shuffle-alert)

;; VALIDATORS
(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (and (>= percent-deteriorated 0)
       (<= percent-deteriorated 100)))

(def bobby (atom {:cuddle-hunger-level 0
                  :percent-deteriorated 0}
                 :validator percent-deteriorated-validator))

(swap! bobby update-in [:percent-deteriorated] + 200)
;; => throws "Invalid reference state"
