(ns dangeru-clj.core-test
  (:require [clojure.test :refer :all]
            [dangeru-clj.dangeru :as dangeru :refer :all]))

(deftest index-test
  (testing "index" 
    ;; There are multiple pages on /u/, so these should be different
    (is (not= (dangeru/index "u")(dangeru/index "u" 1)))
    ;; However, the board's name and url shouldn't change
    (is (= (:board (nth (dangeru/index "u") 0)) (:board (nth (dangeru/index "u" 6) 0))))
    ;; Make sure that we're getting /u/ when we ask for /u/
    (is (= (:board (nth (dangeru/index "u" 0) 0)) "u"))
    ;; Make sure we can request other boards
    (is (not= (dangeru/index "u")(dangeru/index "new")))
    ;; Make sure we're actually getting other boards when we request them
    (is (not= (dangeru/index "new") nil))))
(deftest thread-test
  (testing "thread-replies, thread-metadata"
    ;; Make sure different threads yield different results
    (is (not= (dangeru/thread-replies 25295) (dangeru/thread-replies 25662)))
    ;; Make sure we're getting good metadata
    (is (= (:post_id (dangeru/thread-metadata 25662)) 25662))))
(deftest new-thread-test
  (testing "new-thread"
    ;; Make sure we can start a thread on test
    (is (= 200 (dangeru/new-thread "test" "Testing clojure wrapper" "Ignore this thread (or reply, whatever floats your boat)")))))
(deftest reply-test
  (testing "reply, index"
    ;; Grab the newest thread on test (probably the one we just made) and make sure we can reply (note: grabbing second thread because the first is the dashchan sticky (here's hoping test only ever has one sticky))
    (is (= 200 (dangeru/reply "test" (:post_id (nth (dangeru/index "test") 1)) "More clojure wrapper testing")))))
