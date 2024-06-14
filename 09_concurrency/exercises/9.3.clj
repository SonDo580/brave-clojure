(def base-url
  {:google "https://www.google.com/search?q="
   :bing "https://www.bing.com/search?q="})

(defn search-url
  [text search-engine]
  (str (get base-url search-engine)
       (java.net.URLEncoder/encode text "UTF-8")))

(defn search
  [text search-engines]
  (let [promises (into {}
                       (map (fn [search-engine] 
                              [search-engine (promise)])
                            search-engines))]
    (doseq [search-engine search-engines]
      (future (deliver (promises search-engine)
                       (slurp (search-url text search-engine)))))
    (reduce (fn [result [search-engine promise]]
              (assoc result search-engine @promise))
            {}
            promises)))
   
(println (search "megaman zero" 
                 [:bing :google]))