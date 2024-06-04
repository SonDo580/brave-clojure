(ns functional-programming.note
  (:require [clojure.string :as s]))

(require '[clojure.string :as s])

;; (defn sum
;;   ([vals]
;;    (sum vals 0))
;;   ([vals accumulating-total]
;;    (if (empty? vals)
;;      accumulating-total
;;      (sum (rest vals)
;;           (+ (first vals) accumulating-total)))))

;; Use 'recur' when doing recursion
;; Because Clojure doesn't provide tail call optimization
(defn sum
  ([vals]
   (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals)
            (+ (first vals) accumulating-total)))))

(defn clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})

(def get-intelligence (comp :intelligence :attributes))

;; Without 'comp'
;; (def get-intelligence (fn [c] (:intelligence (:attributes c))))

(def spell-slots (comp int inc #(/ % 2) get-intelligence))

;; Without 'comp'
;; (defn spell-slots
;;   [char]
;;   (int (inc (/ (get-intelligence char) 2))))

(defn two-comp
  "Compose 2 functions"
  [f g]
  (fn [& args]
    (f (apply g args))))

(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)

(def memoized-sleepy-identity (memoize sleepy-identity))
