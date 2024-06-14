(def base-url 
  {:google "https://www.google.com/search?q="
   :bing "https://www.bing.com/search?q="})

(defn search-url
  [text search-engine]
  (str (get base-url search-engine)
       (java.net.URLEncoder/encode text "UTF-8")))

(defn search
  [text search-engine]
  (let [search-promise (promise)]
    (future (deliver search-promise
                     (slurp (search-url text search-engine))))
    @search-promise))

(println (search "megaman zero" :bing))
(println (search "megaman zero" :google))