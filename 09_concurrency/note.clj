;; FUTURE
;; - define a task and place it on another thread without requiring the result immediately

(future (Thread/sleep 2000)
        (println "I'll print after 4 seconds"))
(println "I'll print immediately")

;; the future's result is cached
(let [result (future (println "this prints once")
                     (+ 1 1))]
  (println "deref: " (deref result))
  (println "@: " @result))

(let [result (future (Thread/sleep 3000)
                     (+ 1 1))]
  (println "Result: " @result)
  (println "It'll be at least 3 seconds before I print"))

;; return 5 if the future doesn't return a value within 10 miliseconds
(deref (future (Thread/sleep 1000) 0) 10 5) ; 5

;; Check if the future's done running
(realized? (future (Thread/sleep 1000))) ; false
(let [f (future)]
  @f
  (realized? f)) ; true

;; DELAY
;; - define a task without having to execute it or requiring the result immediately

(def jackson-delay
  (delay (let [message "Just call my name and I'll be there"]
           (println "First deref: " message)
           message)))

(force jackson-delay)

;; the delay's result is cached
@jackson-delay ; 5

;; One way to use a delay is to fire off a statement 
;; the first time one future out of a group of futures finishes

(def head-shots ["serious.jpg" "fun.jpg" "playful.jpg"])

(defn email-user
  [email-addess]
  (println "Sending headshot notification to" email-addess))

(defn upload-document
  [head-shot]
  true)

(let [notify (delay (email-user "example@x.com"))]
  (doseq [head-shot head-shots]
    (future (upload-document head-shot)
            (force notify))))
;; (force notify) is evaluated 3 times, 
;; but the delay body (email-user "example@x.com") is only evaluated once

;; PROMISE
(def my-promise (promise))
(deliver my-promise (+ 1 2))
@my-promise ; 3
;; You can only deliver a result to a promise once.

;; One use for promises is to find the first satisfactory element in a collection of data.
(def yak-butter-international
  {:store "Yak Butter International"
   :price 90
   :smoothness 90})

(def butter-than-nothing
  {:store "Butter Than Nothing"
   :price 150
   :smoothness 83})

;; budget: $100 for one pound.
;; smoothness rating of 97 or greater. 
;; This is the butter that meets our requirements
(def baby-got-yak
  {:store "Baby Got Yak"
   :price 94
   :smoothness 99})

(defn mock-api-call
  [result]
  (Thread/sleep 1000)
  result)

(defn satisfactory?
  [butter]
  (and (<= (:price butter) 100)
       (>= (:smoothness butter) 97)
       butter))

(time (some (comp satisfactory? mock-api-call)
            [yak-butter-international butter-than-nothing baby-got-yak]))
;; "Elapsed time: 3001.612755 msecs"
;; {:store "Baby Got Yak", :price 94, :smoothness 99}

;; use promises and futures to perform each check on a separate thread
;; If your computer has multiple cores, this could reduce the time
(time 
 (let [butter-promise (promise)]
   (doseq [butter [yak-butter-international butter-than-nothing baby-got-yak]]
     (future (if-let [satisfactory-butter 
                      (satisfactory? (mock-api-call butter))]
               (deliver butter-promise satisfactory-butter))))
   @butter-promise))
;; "Elapsed time: 1002.018239 msecs"
;; {:store "Baby Got Yak", :price 94, :smoothness 99}

;; Include a timeout
(let [p (promise)]
  (deref p 100 "timeout"))

;; Use promises to register callbacks
(let [wisdom-promise (promise)]
  (future (println "Here's some wisdom:"
                   @wisdom-promise))
  (Thread/sleep 100)
  (deliver wisdom-promise
           "Whisper your way to success."))
;; the future begins executing immediately
;; the future's thread is blocking 
;; because it is waiting for a value to be delivered to wisdom-promise
;; after 100ms, deliver the value
;; the println statement in the future runs