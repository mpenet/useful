(ns useful-test
  (:use clojure.test useful))

(deftest test-assoc-if
  (is (= {:a 1 :b 3}
         (-> {:a 1}
             (assoc-if (even? 3) :a 3)
             (assoc-if (odd?  3) :b 3)))))

(deftest test-assoc-or
  (is (= {:a 1 :b 2 :c 3}
         (-> {:a 1 :b nil}
             (assoc-or :a 2)
             (assoc-or :b 2)
             (assoc-or :c 3)))))

(deftest test-conj-vec
  (is (= (conj-vec nil  5) [5]))
  (is (= (conj-vec '(4) 5) [4 5]))
  (is (= (conj-vec #{3} 5) [3 5])))

(deftest test-conj-set
  (is (= (conj-set nil  5) #{5}))
  (is (= (conj-set '(4) 5) #{4 5}))
  (is (= (conj-set [2]  5) #{2 5})))

(deftest test-into-vec
  (is (= (into-vec nil  [3 4]) [3 4]))
  (is (= (into-vec '(4) [5])   [4 5]))
  (is (= (into-vec [2]  [5 6]) [2 5 6])))

(deftest test-into-map
  (is (= {:foo "1", :bar "2", :bang "3", :baz "4", :blah 5}
         (into-map :foo 1 :bar 2 :bang 3 [:foo "1" :baz "4"] :bar "2" '(:bang "3") {:blah 5}))))

(deftest test-include?
  (is (include? 5 [1 2 3 4 5]))
  (is (include? :bar '(1 4 :bar)))
  (is (not (include? 2 '(1 3 4))))
  (is (not (include? :foo [1 :bar :baz 3]))))

(deftest test-extract
  (is (= [5 '(2 4 6 1 2 7)] (extract odd?     [2 4 6 5 1 2 7])))
  (is (= [2 '(4 6 5 1 2 7)] (extract even?    [2 4 6 5 1 2 7])))
  (is (= [7 '(2 4 6 5 1 2)] (extract #(< 6 %) [2 4 6 5 1 2 7]))))

(deftest test-separate
  (is (= ['(5 1 7) '(2 4 6 2)] (separate odd?  [2 4 6 5 1 2 7])))
  (is (= ['(2 4 6 2) '(5 1 7)] (separate even? [2 4 6 5 1 2 7]))))

(deftest test-if-ns
  (if-ns (:use this-namespace.should-not-exist)
    (is false)
    (is true))
  (if-ns (:require clojure.string)
    (is true)
    (is false)))

(deftest test-tap
  (let [a (atom 0)]
    (is (= 5 (tap #(swap! a + %)
                  (+ 2 3))))
    (is (= 5 @a))))

(deftest test-update
  (is (= {:a 3 :b 3}
         (-> {:a 2 :b 4}
             (update :a inc)
             (update :b dec))))
  (is (= {:a 6 :b 8}
         (-> {:a 3 :b 4}
             (update [:a :b] * 2)))))

(deftest test-append
  (is (= {:a [1 2 3] :b {"foo" [2 3 5] "bar" 7 "bap" 9 "baz" 2} :c #{2 4 6 8}}
         (append
          {:a [1]    :b {"foo" [2 3] "bar" 8 "bap" 9} :c #{2 3 4 6}}
          {:a [2 3]  :b {"foo" [5]   "bar" 7 "baz" 2} :c {3 false 8 true}}))))

(deftest test-merge-in
  (is (= {:a {:b {:c 4} :d 2 :e 3} :e 3 :f 2 :g 1}
         (merge-in {:a {:b {:c 1} :d 2} :e 3 :f 4}
                   {:a {:b {:c 4} :e 3} :f 2 :g 1}))))

(deftest test-while-let
  (let [a (atom '(1 2 3 4 5))]
    (while-let [val (seq @a)]
      (is val)
      (swap! a rest))
    (is (empty? @a))))

(deftest test-queue
  (let [q (queue)]
    (is (instance? clojure.lang.PersistentQueue q))
    (is (empty? q)))
  (let [q (queue [1 2 3 4])]
    (is (= 1 (first q)))
    (is (= 2 (-> q pop first)))
    (is (= 3 (-> q pop pop first)))
    (is (= 4 (-> q pop pop pop first)))
    (is (= 4 (count q)))))

(deftest test-absorb
  (is (= nil  (absorb nil inc)))
  (is (= nil  (absorb nil (+ 8))))
  (is (= 4    (absorb 3   inc)))
  (is (= 11   (absorb 3   (+ 8))))
  (is (= true (absorb false not))))

(deftest test-rescue
  (is (= nil (rescue (/ 9 0) nil)))
  (is (= 3   (rescue (/ 9 3) nil))))

