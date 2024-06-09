(macroexpand '(when boolean-expression
                expression-1
                expression-2
                expression-3))
;; =>
;; (if boolean-expression 
;;   (do expression-1 
;;       expression-2 
;;       expression-3))

(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))

(infix (1 + 1)) ; 2
(macroexpand '(infix (1 + 1))) ; (+ 1 1)

(defmacro infix-2
  [[operand1 op operand2]]
  (list op operand1 operand2))

(defmacro my-and
  ([] true)
  ([x] x)
  ([x & next]
   (let [and# ~x]
     (if and# (my-and ~@next) and#))))

;; use quoting to turn off evaluation
(defmacro my-print
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

(defmacro my-when
  [test & body]
  (list 'if test (cons 'do body)))

(defmacro my-unless
  [test & branches]
  (conj (reverse branches) test 'if))

;; Syntax quoting
(list '+ 1 (inc 1)) ; (+ 1 2)
`(+ 1 ~(inc 1)) ; (clojure.core/+ 1 2)

(defmacro code-critic
  [bad good]
  (list 'do
        (list 'println
              "Bad code:"
              (list 'quote bad))
        (list 'println
              "Good code:"
              (list 'quote good))))

(code-critic (1 + 1) (+ 1 1))
; Bad code (1 + 1)
; Good code (+ 1 1)

(defmacro code-critic-2
  [bad good]
  `(do (println "Bad code:"
                (quote ~bad))
       (println "Good code:"
                (quote ~good))))

(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))

(defmacro code-critic-3
  [bad good]
  `(do ~(criticize-code "Bad code:" bad)
       ~(criticize-code "Good code:" good)))

;; This's gonna lead to NullPointerException
;; Because 'map' return a list of 'println' calls, each returns nil
(defmacro code-critic-4-error
  [bad good]
  `(do ~(map #(apply criticize-code %)
             [["Bad code:" bad]
              ["Good code:" good]])))

;; Unquote splicing
(defmacro code-critic-4
  [bad good]
  `(do ~@(map #(apply criticize-code %)
             [["Bad code:" bad]
              ["Good code:" good]])))

;;;;;;;;;;;;
;; GOTCHAS
;;;;;;;;;;;;

;; VARIABLE CAPTURING
(def message "Good job")

;; The macro introduce a binding that eclipses an existing binding
(defmacro with-mischief
  [& stuff-to-do]
  (concat (list 'let ['message "Bad job"])
          stuff-to-do))

(with-mischief (println message)) ; Bad job

;; systax quoting prevents you from accidentally capturing variables 
(defmacro with-mischief-2
  [& stuff-to-do]
  `(let [message "Bad job"]
     ~@stuff-to-do))

(with-mischief-2 (println message)) ; => error

;; Use 'gensym' to introduce let bindings
(defmacro with-mischief-3
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Bad job"]
       ~@stuff-to-do
       (println ~macro-message))))

(with-mischief-3 (println message))
; Good job
; Bad job

;; Use auto-gensym
(defmacro with-mischief-4
  [& stuff-to-do]
  `(let [message# "Bad job"]
     ~@stuff-to-do
     (println message#)))

(with-mischief-4 (println message))
; Good job
; Bad job

;; DOUBLE EVALUATION
(defmacro report-double
  [to-try]
  `(if ~to-try
     (println (quote ~to-try)
              "was successful:"
              ~to-try)
     (println (quote ~to-try)
              "wasn't successful:"
              ~to-try)))

;; This would sleep for 2 seconds
(report-double (do (Thread/sleep 1000)
                   (+ 1 1)))

;; Fix: place to-try in a let expression
(defmacro report
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try)
                "was successful:"
                result#)
       (println (quote ~to-try)
                "wasn't successful:"
                result#))))

;; This would sleep for 1 second
(report (do (Thread/sleep 1000)
            (+ 1 1)))

;; MACROS ALL THE WAY DOWN
;; you can end up having to write more and more of them to get anything done

(doseq [code ['(= 1 1)]]
  (report code))
; code was successful: (= 1 1)

;; Expansion of 1 iteration
;; (if code
;;   (clojure.core/println 'code "was successful:" code)
;;   (clojure.core/println 'code "was not successful:" code))

;; Resolve
(defmacro doseq-macro
  [macroname & args]
  `(do ~@(map (fn [arg]
                (list macroname arg))
              args)))

(doseq-macro report (= 1 1) (= 1 2))
; (= 1 1) was successful: true
; (= 1 2) wasn't successful: false