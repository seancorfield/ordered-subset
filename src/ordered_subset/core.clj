(ns ordered-subset.core)

(defn get-ordered-subset
  "Given a nested map and a sequence of desired keys in order from it,
  return a sequence of the selected nested pieces, with label added to each."
  [set subset]
  (reduce (fn [ordered label]
            (if-let [item (get set label)]
              (conj ordered (assoc item :label (name label)))
              ordered))
          []
          subset))
