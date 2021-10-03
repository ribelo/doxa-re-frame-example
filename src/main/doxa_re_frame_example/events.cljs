(ns doxa-re-frame-example.events
  (:require
   [re-frame.core :as rf]
   [meander.epsilon :as m]
   [ribelo.doxa :as dx]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ [_eid _]]
   (tap> [_eid])
   (dx/create-dx [] {::dx/with-diff? true})))

(let [next-eid (volatile! 0)]

  (defn random-fruit []
    {:fruit/id (vswap! next-eid inc)
     :name     (rand-nth ["Avocado" "Grape" "Plum" "Apple" "Orange"])
     :price    (rand-int 100)})

  (defn random-vegetable []
    {:vegetable/id (vswap! next-eid inc)
     :name         (rand-nth ["Onion" "Cabbage" "Pea" "Tomatto" "Lettuce"])
     :price        (rand-int 100)})

  (defn random-car []
    {:car/id (vswap! next-eid inc)
     :name   (rand-nth ["Audi" "Mercedes" "BMW" "Ford" "Honda" "Toyota"])
     :price  (rand-int 100)})

  (defn random-animal []
    {:animal/id (vswap! next-eid inc)
     :name      (rand-nth ["Otter" "Dog" "Panda" "Lynx" "Cat" "Lion"])
     :price     (rand-int 100)})

  (defn random-cat []
    {:cat/id (vswap! next-eid inc)
     :name   (rand-nth ["Traditional Persian" "Ocicat" "Munchkin cat" "Persian cat" "Burmese cat"])
     :price  (rand-int 100)}))

(rf/reg-event-fx
 ::populate
 (fn [_ [_eid {t :type}]]
   (let [txs (m/match t
               :fruit
               (mapv (fn [_] [:dx/put (random-fruit)]) (range 100))
               :vegetable
               (mapv (fn [_] [:dx/put (random-vegetable)]) (range 100))
               :car
               (mapv (fn [_] [:dx/put (random-car)]) (range 100)))]
     {:fx [[:commit txs]]})))

(comment
  (tap> [:db @re-frame.db/app-db])
  )

(rf/reg-event-fx
 ::inc-query-count
 (fn [_ [_eid k]]
   {:fx [[:commit [:dx/update [:id :query-counter] k inc]]]}))
