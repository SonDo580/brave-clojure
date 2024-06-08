(ns divine-cheese-code.visualization.svg
  (:require [clojure.string :as s])
  (:refer-clojure :exclude [min max]))

(defn comparator-over-maps
  [comparison-fn keys]
  (fn [maps]
    (zipmap keys
            (map (fn [key]
                   (apply comparison-fn (map key maps)))
                 keys))))

;; Used to find top-left corner
(def min (comparator-over-maps clojure.core/min [:lat :lng]))

;; Used to find bottom-right corner
(def max (comparator-over-maps clojure.core/max [:lat :lng]))

;; (min [{:a 1 :b 3} {:a 5 :b 0}]) 
; => {:a 1 :b 0}
;; (max [{:a 1 :b 3} {:a 5 :b 0}]) 
; => {:a 5 :b 3}

(defn translate-to-00
  [locations]
  (let [min-coords (min locations)]
    (map #(merge-with - % min-coords) locations)))

(defn scale
  [width height locations]
  (let [max-coords (max locations)
        ratio {:lat (/ height (:lat max-coords))
               :lng (/ width (:lng max-coords))}]
    (map #(merge-with * % ratio) locations)))

(defn latlng->point
  "Convert lat/lng map to comma-separated string"
  [latlng]
  (str (:lat latlng) "," (:lng latlng)))

(defn points
  "Given a seq of lat/lng maps, return string of points joined by space"
  [locations]
  (clojure.string/join " " (map latlng->point locations)))

(defn line
  [points]
  (str "<polyline points=\"" points "\" />"))

(defn transform
  [width height locations]
  (->> locations
       translate-to-00
       (scale width height)))

(defn xml
  "svg 'template', which also flips the coordinate system"
  [width height locations]
  (str "<svg height=\"" height "\" width=\"" width "\">"
       "<g transform=\"translate(0," height ")\">"
       "<g transform=\"rotate(-90)\">"
       (-> (transform width height locations)
           points
           line)
       "</g></g>"
       "</svg>"))