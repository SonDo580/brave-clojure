(defmacro my-or
  ([] true)
  ([x] x)
  ([x & next]
   `(let [or# ~x]
     (if or# or# (my-or ~@next)))))