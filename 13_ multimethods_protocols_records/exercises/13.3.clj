(defprotocol Hunter
  (run [x])
  (skill [x] [x y]))

(extend-type java.lang.String
  Hunter
  (run [x] (str "run from " x))
  (skill
    ([x] (str "I can " x))
    ([x y] (str "I can " x " and " y))))

(run "tiger")
(skill "shoot")
(skill "kick" "punch")

(extend-protocol Hunter
  java.lang.Number
  (run [x] (str "Run from number " x))
  (skill
    ([x] (str "Skill number " x))
    ([x y] (str "Skill total " (+ x  y))))

  java.lang.Object
 (run [x] "run")
 (skill
  ([x] "single skill")
  ([x y] "double skills")))

(run 1)
(skill 1)
(skill 1 2)