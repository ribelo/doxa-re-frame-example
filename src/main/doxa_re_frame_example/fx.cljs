(ns doxa-re-frame-example.fx
  (:require
   [re-frame.core :as rf]
   [re-frame.db :refer [app-db]]
   [ribelo.doxa :as dx]))

(rf/reg-fx
 :commit
 (fn [data]
   (tap> [:commit data])
   (if (even? (count data))
     (let [it (iter data)]
       (while (.hasNext it)
         (let [txs   (.next it)
               tx    (nth txs 0)]
           (cond
             (vector?  tx) (dx/commit! app-db txs)
             (keyword? tx) (dx/commit! app-db [txs])))))
     (js/console.error "doxa: \":commit\" number of elements should be even"))))
