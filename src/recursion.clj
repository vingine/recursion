(ns recursion)

(defn
  product
  [coll]
  (if (empty? coll)
    1
    (* (first coll)
       (product (rest coll)))))

(defn
  singleton?
  [coll]
  (cond (empty? coll) false
        (empty? (rest coll)) true
        :else false))

(defn
  my-last
  [coll]
  (cond (empty? coll) nil
        (singleton? coll) (first coll)
        :else (my-last (rest coll))))

(defn
  max-element
  [a-seq]
  (cond (empty? a-seq) nil
        (singleton? a-seq) (first a-seq)
        :else (max
                (first a-seq)
                (max-element (rest a-seq)))))

(defn
  seq-max
  [seq-1 seq-2]
  (if (> (count seq-1) (count seq-2))
    seq-1
    seq-2))

(defn
  longest-sequence
  [a-seq]
  (cond (empty? a-seq) nil
        (singleton? a-seq) (first a-seq)
        :else (seq-max
                (first a-seq)
                (longest-sequence (rest a-seq)))))

(defn
  my-filter
  [pred? a-seq]
  (cond (empty? a-seq) a-seq
        (pred? (first a-seq)) (cons
                                (first a-seq)
                                (my-filter pred? (rest a-seq)))
        :else (my-filter pred? (rest a-seq))))

(defn
  sequence-contains?
  [elem a-seq]
  (cond (empty? a-seq) false
        (= (first a-seq) elem) true
        :else (sequence-contains?
                elem
                (rest a-seq))))

(defn
  my-take-while
  [pred? a-seq]
  (cond (empty? a-seq) a-seq
        (pred? (first a-seq)) (cons
                                (first a-seq)
                                (my-take-while pred? (rest a-seq)))
        :else '()))

(defn
  my-drop-while
  [pred? a-seq]
  (cond (empty? a-seq) a-seq
        (pred? (first a-seq)) (my-drop-while pred? (rest a-seq))
        :else a-seq))

(defn
  seq=
  [a-seq b-seq]
  (cond (empty? a-seq) (empty? b-seq)
        (empty? b-seq) false
        (= (first a-seq) (first b-seq)) (seq= (rest a-seq) (rest b-seq))
        :else false))

(defn
  my-map
  [f seq-1 seq-2]
  (cond (or (empty? seq-1) (empty? seq-2)) '()
        :else (cons (f (first seq-1) (first seq-2)) (my-map f (rest seq-1) (rest seq-2)))))

(defn
  power
  [n k]
  (if (= 0 k)
    1
    (* n (power n (dec k)))))

(defn
  fib
  [n]
  (cond (= n 0) 0
        (= n 1) 1
        :else (+ (fib (- n 1)) (fib (- n 2)))))

(defn
  my-repeat
  [how-many-times what-to-repeat]
  (if (<= how-many-times 0)
    '()
    (cons what-to-repeat (my-repeat (dec how-many-times) what-to-repeat))))

(defn
  my-range
  [up-to]
  (if (<= up-to 0)
    '()
    (cons (dec up-to) (my-range (dec up-to)))))

(defn
  tails
  [a-seq]
  (if (empty? a-seq)
    '(())
    (cons a-seq (tails (rest a-seq)))))

(defn
  inits
  [a-seq]
  (reverse (map reverse (tails (reverse a-seq)))))

(defn
  rotations
  [a-seq]
  (if (empty? a-seq)
    '(())
    (rest (my-map concat (tails a-seq) (inits a-seq)))))

(defn
  my-frequencies-helper
  [freqs a-seq]
  (if (empty? a-seq)
    freqs                                                   ; Sequence has been parsed through
    (let [h (first a-seq)                                   ; Holds the first value in the sequence
          t (rest a-seq)                                    ; Holds the rest of the values in the map
          c (or (freqs h) 0)]                               ; Return either old count or start a new with or
      (my-frequencies-helper
        (assoc freqs h (inc c))                             ; Add or modify key value with new count
        t))))                                               ; The rest of the sequence elements

(defn
  my-frequencies
  [a-seq]
  (my-frequencies-helper {} a-seq))

(defn
  un-frequencies
  [a-map]
  (if (empty? a-map)
    '()
    (let [f (fn
              [[v c]]
              (repeat c v))]
      (apply concat (map f a-map)))))

(defn
  my-take
  [n coll]
  (if (or (= 0 n) (empty? coll))
    '()
    (cons
      (first coll)
      (my-take
        (dec n)
        (rest coll)))))

(defn
  my-drop
  [n coll]
  (if (or (empty? coll) (<= n 0))
    coll
    (my-drop (dec n) (rest coll))))

(defn
  halve
  [a-seq]
  (let [n (int (/ (count a-seq) 2))]
    (cons (my-take n a-seq) (cons (my-drop n a-seq) '()))))

(defn
  seq-merge
  [a-seq b-seq]
  (cond (empty? a-seq) b-seq
        (empty? b-seq) a-seq
        :else (let [ha (first a-seq)
                    hb (first b-seq)
                    ta (rest a-seq)
                    tb (rest b-seq)]
                (if (<= ha hb)
                  (cons ha (seq-merge ta b-seq))            ; if the head of a is smaller than b, then it's the first value
                  (cons hb (seq-merge a-seq tb))))))        ; otherwise the first value is head of b and the rest are larger

(defn
  merge-sort
  [a-seq]
  (if (or
        (empty? a-seq)
        (singleton? a-seq))
    a-seq
    (let [[l, r] (halve a-seq)]
      (seq-merge (merge-sort l) (merge-sort r)))))

(defn
  split-into-monotonics
  [a-seq]
  (if (empty? a-seq)
    '()
    (let [monotonic? (fn [seq] (or (apply <= seq) (apply >= seq)))
          ; check monotonicy from inits of the sequence (leave out the empty seq)
          ; then reverse the passed sequences of inits, so that the longest seq is the first
          mon-seq (first
                    (reverse
                      (take-while
                        monotonic?
                        (rest (inits a-seq)))))]
      ; create a sequence of monotonic sequences
      ; after every monotonic sequence from input seq, check how many numbers were taken
      ;   and then drop them out of the checks. repeats until the function returns empty seq
      (cons
        mon-seq
        (split-into-monotonics
          (drop
            (count mon-seq)
            a-seq))))))

(defn
  permutations
  [a-set]
  (if (empty? a-set)
    [()]
    (apply
      concat
      (map
        (fn [b-set]
          (map
            cons
            ; map repeats the given function on multiple collections at the same time
            ; checks all the variations per rotation based on the first number
            (repeat (first b-set))
            (permutations (rest b-set))))
        (rotations a-set)))))

(defn
  powerset
  [a-set]
  (let [length (count a-set)]
    (cond (== length 0) #{#{}}
          (== length 1) #{#{} #{(first a-set)}}
          :else (let [sub (powerset (rest a-set))
                      union (fn
                              [x-set v]
                              (map
                                (fn [x] (clojure.set/union x #{v}))
                                x-set))
                      concat-sub (union sub (first a-set))]
                  (clojure.set/union sub concat-sub)))))

