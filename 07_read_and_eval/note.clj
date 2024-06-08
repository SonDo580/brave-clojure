;; read-string
;; eval

;; MACRO
(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(ignore-last-operand (+ 1 2 10)) ; 3
(macroexpand '(ignore-last-operand (+ 1 2 10))) ; (+ 1 2)

(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))

(infix (1 + 2)) ; 3

;; SYNTAX ABSTRACTION
(defn read-resource
  "Read a resource into a string"
  [path]
  (read-string (slurp (clojure.java.io/resource path))))

(defn read-resource-2
  [path]
  (-> path
      clojure.java.io/resource
      slurp
      read-string))