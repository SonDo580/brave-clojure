(defn google-search-url
  [text]
  (str "https://www.google.com/search?q=" 
       (java.net.URLEncoder/encode text "UTF-8")))

(defn bing-search-url
  [text]
  (str "https://www.bing.com/search?q="
       (java.net.URLEncoder/encode text "UTF-8")))

(defn search
  [text]
  (let [search-promise (promise)]
    (future (deliver search-promise 
                     (slurp (google-search-url text))))
    (future (deliver search-promise
                     (slurp (bing-search-url text))))
    @search-promise))

(println (search "megaman zero"))