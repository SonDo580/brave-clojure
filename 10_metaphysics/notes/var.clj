;; DYNAMIC BINDING
(def ^:dynamic *notification-address* "dobby@elf.org")

(binding [*notification-address* "test@elf.org"]
  *notification-address*)
; => "test@elf.org"

(binding [*notification-address* "tester-1@elf.org"]
  (println *notification-address*)
  (binding [*notification-address* "tester-2@elf.org"]
           (println *notification-address*))
  (println *notification-address*))
; => tester-1@elf.org
; => tester-2@elf.org
; => tester-1@elf.org

(defn notify
  [message]
  (str "TO: " *notification-address* "\n"
       "MESSAGE: " message))

(notify "I fell.")
;; => "TO: dobby@elf.org\nMESSAGE: I fell."

(binding [*notification-address* "test@elf.org"]
  (notify "I fell."))
;; => "TO: test@elf.org\nMESSAGE: I fell."

;; Re-bind built-in *out* to write to a file
(binding [*out* (clojure.java.io/writer "print-output")]
  (println "Content"))

(println ["Print" "all" "the" "things!"])
; => [Print all the things!]

(binding [*print-length* 1]
  (println ["Print" "all" "the" "things!"]))
; => [Print ...]

(def ^:dynamic *troll-thought* nil)

(defn troll-riddle
  [your-answer]
  (let [number "man meat"]
    (when (thread-bound? #'*troll-thought*)
      (set! *troll-thought* number))
    (if (= number your-answer)
      "TROLL: You can cross the bridge!"
      "TROLL: Time to eat you, succulent human!")))

(binding [*troll-thought* nil]
  (println (troll-riddle 2)))

;; ALTERING THE VAR ROOT
(def power-source "hair")

(alter-var-root #'power-source (fn [_] "7-eleven parking lot"))
power-source
; => "7-eleven parking lot"