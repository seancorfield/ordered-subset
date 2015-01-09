(ns ordered-subset.core-test
  (:require [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [ordered-subset.core :refer :all]))


;; Adam's example:
(def adam-set {:one {:alphabet "a",
                     :maori "tahi",
                     :roman "i",
                     :ordinal "first"},
               :two {:alphabet "b",
                     :maori "rua",
                     :roman "ii",
                     :ordinal "second"},
               :three {:alphabet "c",
                       :maori "toru",
                       :roman "iii",
                       :ordinal "third"},
               :four {:alphabet "d",
                      :maori "wha",
                      :roman "iv",
                      :ordinal "fourth"}})

(def adam-result [{:alphabet "d",
                   :maori "wha",
                   :roman "iv",
                   :ordinal "fourth",
                   :label "four"},
                  {:alphabet "b",
                   :maori "rua",
                   :roman "ii",
                   :ordinal "second"
                   :label "two"}])

(deftest adam-test
  (testing "Adam's basic example"
    (is (= adam-result (get-ordered-subset adam-set [:four :two])))))

(deftest empty-intersection-tests
  (testing "Set is empty, subset is non-empty."
    (is (= [{:label "first"} {:label "second"}]
           (get-ordered-subset {} [:first :second]))))
  (testing "Set is non-empty, subset is empty."
    (is (= []
           (get-ordered-subset adam-set []))))
  (testing "Set is non-empty, subset doesn't match."
    (is (= [{:label "first"} {:label "second"}]
           (get-ordered-subset adam-set [:first :second])))))

;; test that selecting bsaed on a random subset of keys works
(defspec subset-has-ordered-labels
  50
  (prop/for-all [m (gen/map gen/keyword (gen/map gen/keyword gen/string))]
                (let [ks (keys m)
                      ;; get a randomly ordered subset of the keys:
                      ;; note: shuffle cannot take nil from (keys {})
                      ordered (take (inc (rand-int (count ks))) (shuffle (or ks [])))]
                  (= (map name ordered)
                     (map :label (get-ordered-subset m ordered))))))

;; test that selecting based on all keys in the default order works
(defspec all-keys-has-all-labels
  50
  (prop/for-all [m (gen/map gen/keyword (gen/map gen/keyword gen/string))]
                (let [ks (keys m)]
                  (= (map name ks)
                     (map :label (get-ordered-subset m ks))))))

;; test that selecting based on all keys in sorted order works
(defspec sorted-keys-has-all-ordered-labels
  50
  (prop/for-all [m (gen/map gen/keyword (gen/map gen/keyword gen/string))]
                (let [ks (sort (keys m))]
                  (= (map name ks)
                     (map :label (get-ordered-subset m ks))))))

;; test that selecting based on a completely random set of potentially unrelated keys works
(defspec random-keys-has-matching-labels
  50
  (prop/for-all [m (gen/map gen/keyword (gen/map gen/keyword gen/string))
                 ks (gen/vector gen/keyword)]
                (= (map name ks)
                   (map :label (get-ordered-subset m ks)))))

;; test that selecting based on known keys returns expected map entries
(defspec known-keys-has-original-entries-plus-label
  50
  (prop/for-all [m (gen/hash-map :a (gen/map gen/keyword gen/string) :b (gen/map gen/keyword gen/string))]
                (= [(assoc (:a m) :label "a")]
                   (get-ordered-subset m [:a]))
                (= [(assoc (:b m) :label "b")]
                   (get-ordered-subset m [:b]))
                (= [(assoc (:a m) :label "a")
                    (assoc (:b m) :label "b")]
                   (get-ordered-subset m [:a :b]))
                (= [(assoc (:b m) :label "b")
                    (assoc (:a m) :label "a")]
                   (get-ordered-subset m [:b :a]))))
