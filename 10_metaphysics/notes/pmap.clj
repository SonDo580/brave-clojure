;; perform a parallel map with pmap. 
;; with pmap, each application of the mapping function runs on a separate thread.

(def alphabet-length 26)

(def letters (mapv (comp str char (partial + 65))
                   (range alphabet-length)))

(defn random-string
  [length]
  (apply str (take length (repeatedly #(rand-nth letters)))))

(defn random-string-list
  [list-length string-length]
  (doall (take list-length 
               (repeatedly (partial random-string string-length)))))

(def orc-names (random-string-list 3000 7000))
(time (dorun (map clojure.string/lower-case orc-names)))
(time (dorun (pmap clojure.string/lower-case orc-names)))

;; pmap may take longer to run (because of parallelization overhead)
(def orc-name-abbrevs (random-string-list 20000 300))
(time (dorun (map clojure.string/lower-case orc-name-abbrevs)))
(time (dorun (pmap clojure.string/lower-case orc-name-abbrevs)))

;; Increase the grain size may improve performance
(def numbers [1 2 3 4 5 6 7 8 9 10])

(partition-all 3 numbers)
; => ((1 2 3) (4 5 6) (7 8 9) (10))

;; grain size: 1 (each thread applies inc to 1 element)
(pmap inc numbers)

;; grain size: 3
;; - doall: force the lazy sequence returned by (map inc number-group) 
;; to be relized within the thread
;; - concat: to ungroup the result 
(apply concat
       (pmap (fn [number-group]
               (doall (map inc number-group)))
             (partition-all 3 numbers)))

(time
 (dorun
  (apply concat
         (pmap (fn [name]
                 (doall (map clojure.string/lower-case name)))
               (partition-all 1000 orc-name-abbrevs)))))