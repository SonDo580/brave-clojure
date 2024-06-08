(defn my-reduce
  "A custom implementation of reduce"
  ([f initial collection]
   (loop [result initial
          remaining collection]
     (if (empty? remaining)
       result
       (recur (f result (first remaining))
              (rest remaining)))))
  ([f [head & tail]]
   (my-reduce f head tail)))