(ns dangeru-clj.dangeru
  (:require [clj-http.client :as client]
            [cheshire.core :as json]))

;;;;;;;;;;;;;;;;;;;;
;; Board indexing ;;
;;;;;;;;;;;;;;;;;;;;

;; Get a (JSON) string of all the threads on page number [page] of [board]
(defn fetch-index [board page]
  (:body (client/get (str "https://dangeru.us/api/v2/board/" board)
                     {:query-params {:page page}
                      :insecure? false
                      :as :string})))

;; Get all the threads on page number [page] of [board]
(defn real-index [board page]
  (json/parse-string (fetch-index board page) true))
;; Hack for letting page default to 0
(defn index
  ([board] (real-index board 0))
  ([board page] (real-index board page)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Thread info (metadata/replies) ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Get a (JSON) string of either the metadata or replies to a thread
(defn fetch-thread [id which]
  (:body (client/get (str "https://dangeru.us/api/v2/thread/" id "/" which)
                     {:query-params {}
                      :insecure? true
                      :as :string})))

;; Ask for the posts in thread [id] and convert the resulting string to a clojure array
(defn thread-replies [id]
  (json/parse-string (fetch-thread id "replies") false))

;; Ask for the metadata of thread [id] and convert the resulting string to a clojure map
(defn thread-metadata [id]
  (json/parse-string (fetch-thread id "metadata") true))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Unauthenticated posting ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Start a new thread on the given board with the given title and comment
(defn new-thread [board title comment]
  (:status (client/post "https://dangeru.us/post" {:form-params {:board board
                                                        :title title
                                                        :comment comment}})))

;; Reply to thread number [parent] on [board] with the given content
(defn reply [board parent content]
  (:status (client/post "https://dangeru.us/reply" {:form-params {:board board
                                                         :parent parent
                                                         :content content}})))