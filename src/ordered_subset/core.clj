(ns ordered-subset.core)

(defn get-ordered-subset
  "Given a nested map and a sequence of desired keys in order from it,
  return a sequence of the selected nested pieces, with label added to each."
  [set subset]
  (map (fn [label] (assoc (get set label) :label (name label)))
       (filter (fn [label] (get set label)) subset)))
