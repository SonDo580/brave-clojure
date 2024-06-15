(def value (atom 0))

(swap! value inc)
(swap! value inc)
(swap! value inc)

@value ; should be 3