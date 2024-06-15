;; EXAMPLE: Sock transactions

(def sock-variaties
  #{"darned" "argyle" "wool" "horsehair" "mulleted"
    "passive-aggressive" "striped" "polka-dotted"
    "athletic" "business" "power" "invisible" "gollumed"})

(defn sock-count
  [sock-variety count]
  {:variety sock-variety
   :count count})

(defn generate-sock-gnome
  [name]
  {:name name
   :socks #{}})

(def sock-gnome (ref (generate-sock-gnome "Gnome")))
(def dryer (ref {:name "Dryer"
                 :socks (set (map #(sock-count % 2)
                                  sock-variaties))}))

(:socks @dryer)

;; TRANSACTION
(defn steal-sock
  [gnome dryer]
  (dosync
   (when-let [pair (some #(if (= (:count %) 2) %)
                         (:socks @dryer))]
     (let [updated-count (sock-count (:variety pair) 1)]
       (alter gnome update-in [:socks] conj updated-count)
       (alter dryer update-in [:socks] disj pair)
       (alter dryer update-in [:socks] conj updated-count)))))

(steal-sock sock-gnome dryer)

(:socks @sock-gnome)

(defn similar-socks
  [target-sock sock-set]
  (filter #(= (:variety %) (:variety target-sock))
          sock-set))

(similar-socks (first (:socks @sock-gnome))
               (:socks @dryer))

;; in-transaction state example
(def counter (ref 0))
(future
  (dosync
   (alter counter inc)
   (println @counter)
   (Thread/sleep 500)
   (alter counter inc)
   (println @counter)))
(Thread/sleep 250)
(println @counter)
; 1 => 0 => 2

;; COMMUTE

;; Safe use
(defn sleep-print-update
  [sleep-time thread-name update-fn]
  (fn [state]
    (Thread/sleep sleep-time)
    (println (str thread-name ": " state))
    (update-fn state)))

(def counter-1 (ref 0))
(future (dosync (commute counter-1 (sleep-print-update 100 "Thread A" inc))))
(future (dosync (commute counter-1 (sleep-print-update 150 "Thread B" inc))))
;; Thread A: 0 | 100ms
;; Thread B: 0 | 150ms
;; Thread A: 0 | 200ms 
;; Thread B: 1 | 300ms

;; Unsafe
(def receiver-a (ref #{}))
(def receiver-b (ref #{}))
(def giver (ref #{1}))
(do (future (dosync (let [gift (first @giver)]
                      (Thread/sleep 10)
                      (commute receiver-a conj gift)
                      (commute giver disj gift))))
    (future (dosync (let [gift (first @giver)]
                      (Thread/sleep 50)
                      (commute receiver-b conj gift)
                      (commute giver disj gift)))))

;; 1 was given to both recercer-a and receiver-b
@receiver-a ; => #{1}
@receiver-b ; => #{1}
@giver ; => #{}

