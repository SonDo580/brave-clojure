;; Use the list function, quoting, and read-string 
;; to create a list that, when evaluated, 
;; prints your first name and your favorite sci-fi movie.

(def name-and-movie "(println \"Son Do - The Matrix\")")
(eval (read-string name-and-movie))

(def name-and-movie-2 (list 'println "Son Do - The Matrix"))
(eval name-and-movie-2)
