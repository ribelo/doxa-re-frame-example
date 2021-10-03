(ns doxa-re-frame-example.core
  (:require
   [reagent.core :as r]
   [reagent.ratom :as ra]
   [reagent.dom :as rd]
   [re-frame.core :as rf]
   [meander.epsilon :as m]
   [ribelo.doxa :as dx]
   [doxa-re-frame-example.fx]
   [doxa-re-frame-example.events :as evt]
   [doxa-re-frame-example.subs :as sub]))

(defn populate-button [{:keys [text populate]}]
  [:div.mx-4.border.border-black.px-2
   {:on-click #(rf/dispatch [::evt/populate {:type populate}])}
   text])

(defn query [{:keys [table cached? placeholder]}]
  (let [v    (r/atom "")
        result
        (ra/reaction (if-not cached?
                       @(rf/subscribe [::sub/query        table @v])
                       @(rf/subscribe [::sub/cached-query table @v])))]
    (fn [_]
      [:div.flex.flex-col.text-sm
       [:input.text-sm
        {:type :text
         :placeholder placeholder
         :default-value @v
         :on-change #(reset! v (-> % .-target .-value))}]
       [:div.flex.flex-col.border.border-black.mt-4.h-64.overflow-auto
        [:div.flex.border-b.justify-around.text-center
         [:div.w-8 "id"]
         [:div.w-16 "name"]
         [:div.w-8 "price"]]
        (when (seq (:data @result))
          (m/rewrite (:data @result)
            [{:id !id :name !name :price !price} ...]
            [:div .
             [:div.flex.justify-around.text-center
              [:div.w-8 !id]
              [:div.w-16 !name]
              [:div.w-8 !price]]
             ...]))]
       [:div.flex.flex-col.px-2
        [:div.flex.justify-between
         [:div "fresh?"]
         [:div (str (some-> (:meta @result) ::dx/fresh?))]]
        [:div.flex.justify-between
         [:div "execution time"]
         [:div (str (some-> (:meta @result) ::dx/execution-time) " ms")]]
        [:div.flex.justify-between
         [:div "count"]
         [:div (str (count (:data @result)))]]]])))

(defn view []
  [:div.flex.flex-col.w-full
   [:div.flex.pt-8.justify-center
    [populate-button {:text "add 100 fruits" :populate :fruit}]
    [populate-button {:text "add 100 vegetables" :populate :vegetable}]
    [populate-button {:text "add 100 cars" :populate :car}]]
   [:div.self-center.mt-4 "uncached"]
   [:div.flex.mt-2.mx-8.justify-around
    [query {:table :fruit/id :placeholder "fruit name"}]
    [query {:table :vegetable/id :placeholder "vegetable name"}]
    [query {:table :car/id :placeholder "car name"}]]
   [:div.self-center.mt-4 "cached"]
   [:div.flex.mt-2.mx-8.justify-around
    [query {:table :fruit/id :cached? true :placeholder "fruit name"}]
    [query {:table :vegetable/id :cached? true :placeholder "vegetable name"}]
    [query {:table :car/id :cached? true :placeholder "car name"}]]])

(defn mount-components []
  (js/console.info "mount-components")
  (rd/render [#'view] (.getElementById js/document "app")))

(defn ^:export init []
  (js/console.info "init")
  (rf/dispatch-sync [::evt/initialize-db])
  (mount-components))
