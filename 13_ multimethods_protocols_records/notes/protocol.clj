(defprotocol Psychodynamics
   (thoughts [x])
   (feelings-about [x] [x y]))

 (extend-type java.lang.String
   Psychodynamics
   (thoughts [x] (str x " thinks, 'Truly, the character defines the data type'"))
   (feelings-about
     ([x] (str x " is longing for a simpler way of life"))
     ([x y] (str x " is envious of " y "'s simpler way of life"))))
 
 (thoughts "blorb")
 ; => "blorb thinks, 'Truly, the character defines the data type'"
 
 (feelings-about "schmorb")
 ; => "schmorb is longing for a simpler way of life"
 
 (feelings-about "schmorb" 2)
 ; => "schmorb is envious of 2's simpler way of life"

 (extend-type java.lang.Object
   Psychodynamics
   (thoughts [x] "Maybe the Internet is just a vector for toxoplasmosis")
   (feelings-about
     ([x] "meh")
     ([x y] (str "meh about " y))))
 
 (thoughts 3)
 ; => "Maybe the Internet is just a vector for toxoplasmosis"
 
 (feelings-about 3)
 ; => "meh"
 
 (feelings-about 3 "blorb")
 ; => "meh about blorb"

 (extend-protocol Psychodynamics
   java.lang.String
   (thoughts [x] "Truly, the character defines the data type")
   (feelings-about
     ([x] "longing for a simpler way of life")
     ([x y] (str "envious of " y "'s simpler way of life")))
 
   java.lang.Object
   (thoughts [x] "Maybe the Internet is just a vector for toxoplasmosis")
   (feelings-about
     ([x] "meh")
     ([x y] (str "meh about " y))))