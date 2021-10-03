(ns doxa-re-frame-example.subs
  (:require
   [meander.epsilon :as m]
   [reagent.ratom :as ra]
   [re-frame.core :as rf]
   [ribelo.doxa :as dx]
   [doxa-re-frame-example.events :as evt]))

(comment
  (let [table :fruit/id
        text "apple"]

    (dx/q [:find ?e ?name ?price
           :keys [:id :name :price]
           :where
           [?table ?e :name ?name]
           [?table ?e :price ?price]]
      @re-frame.db/app-db)
    ))

(rf/reg-sub-raw
 ::query
 (fn [db [_eid table text]]
   (ra/make-reaction
    (fn []
      (let [r (dx/q [:find ?e ?name ?price
                     :keys [:id :name :price]
                     :in ?table
                     :where
                     [?table ?e :name (m/re (re-pattern (str "(?i)" text ".*")) ?name)]
                     [?table ?e :price ?price]]
                @db ~table)]
        (tap> [:data r])
        {:meta (meta r) :data r})))))

(rf/reg-sub-raw
 ::cached-query
 (fn [db [_eid table text]]
   (let [kw [_eid table text]]
     (ra/make-reaction
      (fn []
        (let [r
              ^{::dx/cache kw}
              (dx/q [:find ?e ?name ?price
                     :keys [:id :name :price]
                     :in ?table
                     :where
                     [?table ?e :name  (m/re (re-pattern (str "(?i)" text ".*")) ?name)]
                     [?table ?e :price ?price]]
                @db ~table)]
          {:meta (meta r) :data r}))
      :on-dispose
      #(dx/delete-cached-results! @db kw)))))
