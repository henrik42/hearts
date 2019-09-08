(ns hearts.core-test
  (:require [clojure.test :refer :all]
            [hearts.core :refer :all]))

(deftest geben-test
  (testing "geben"
    (let [x (geben!)]
      (is (= spieler (keys x)))
      (is (= (->> (map first karten) (into #{}))
             (->> (vals x)
                  (mapcat :hand)
                  (into #{})))))))

(deftest beginnt-test
  (testing "beginnt"
    (let [x {:gabi {:hand #{[:pik 8]}}
             :paul {:hand #{[:pik 9] [:kreuz 2]}}}]
      (is (= :paul (beginnt x))))))

(deftest legt-test
  (testing "legt"
    ;; kommt mit :kreuz 2 raus
    (is (= [:kreuz 2]
           (legt {:hand #{[:pik 9] [:kreuz 2]} :tisch []})))
    ;; kommt mit irgendeiner Karte raus
    (is (#{[:pik 9] [:kreuz 3]}
         (legt {:hand #{[:pik 9] [:kreuz 3]} :tisch []})))
    ;; bedient :pik
    (is (= [:pik 9] (legt {:hand #{[:pik 9] [:kreuz 4]} :tisch [{:karte [:pik 8]
                                                                 :spieler :paul}]})))
    ;; wirft auf :herz ab.
    (is (#{[:pik 9] [:kreuz 4]} (legt {:hand #{[:pik 9] [:kreuz 4]} :tisch [{:karte [:herz 8]
                                                                             :spieler :paul}]})))
    ))

(deftest sticht-test
  (testing "sticht"
    (is (= :paul (sticht [{:spieler :paul :karte [:herz 9]}
                          {:spieler :gabi :karte [:herz 2]}
                          {:spieler :sonja :karte [:herz 3]}
                          {:spieler :peter :karte [:herz 4]}])))
    (is (= :sonja (sticht [{:spieler :paul :karte [:herz 9]}
                           {:spieler :gabi :karte [:herz 2]}
                           {:spieler :sonja :karte [:herz 10]}
                           {:spieler :peter :karte [:herz 4]}])))
    (is (= :paul (sticht [{:spieler :paul :karte [:herz 9]}
                          {:spieler :gabi :karte [:karo 2]}
                          {:spieler :sonja :karte [:karo 10]}
                          {:spieler :peter :karte [:karo 4]}])))
    ))

(deftest runde-test
  (testing "runde"
    (let [s {:paul {:hand #{[:kreuz :ass]}}
             :gabi {:hand #{[:herz 8]}}
             :peter {:hand #{[:kreuz 2]}}
             :sonja {:hand #{[:herz 10]}}}]
      (is (= (runde {:beginnt :paul :spieler s})
             {:sticht :paul
              :spieler {:paul {:hand #{}
                               :stiche [[{:karte [:kreuz :ass], :spieler :paul}
                                         {:karte [:herz 10], :spieler :sonja}
                                         {:karte [:herz 8], :spieler :gabi}
                                         {:karte [:kreuz 2], :spieler :peter}]]}
                        :gabi {:hand #{}}
                        :peter {:hand #{}}
                        :sonja {:hand #{}}}}))
      (is (= (runde {:beginnt :peter :spieler s})
             {:sticht :paul
              :spieler {:paul {:hand #{}
                               :stiche [[{:karte [:kreuz 2], :spieler :peter}
                                         {:karte [:kreuz :ass], :spieler :paul}
                                         {:karte [:herz 10], :spieler :sonja}
                                         {:karte [:herz 8], :spieler :gabi}]]}
                        :gabi {:hand #{}}
                        :peter {:hand #{}}
                        :sonja {:hand #{}}}}))
      (is (= (runde {:beginnt :gabi :spieler s})
             {:sticht :sonja
              :spieler {:sonja {:hand #{}
                                :stiche [[{:karte [:herz 8], :spieler :gabi}
                                          {:karte [:kreuz 2], :spieler :peter}
                                          {:karte [:kreuz :ass], :spieler :paul}
                                          {:karte [:herz 10], :spieler :sonja}]]}
                        :paul {:hand #{}}
                        :peter {:hand #{}}
                        :gabi {:hand #{}}}}))
      (is (= (runde {:beginnt :sonja :spieler s})
             {:sticht :sonja
              :spieler {:sonja {:hand #{}
                                :stiche [[{:karte [:herz 10], :spieler :sonja}
                                          {:karte [:herz 8], :spieler :gabi}
                                          {:karte [:kreuz 2], :spieler :peter}
                                          {:karte [:kreuz :ass], :spieler :paul}]]}
                        :paul {:hand #{}}
                        :peter {:hand #{}}
                        :gabi {:hand #{}}}}))
      )))


(deftest punkte-test
  (testing "punkte"
    (is (= 2 (punkte [[{:karte [:kreuz :ass]}
                       {:karte [:herz 10]}
                       {:karte [:herz 8]}
                       {:karte [:kreuz 2]}]])))))

(deftest rang-liste-test
  (testing "rang"
    (is (= [[0 #{:gabi}]
            [2 #{:peter :sonja}]
            [3 #{:paul}]]
           (rang-liste
            {:paul {:stiche [[{:karte [:kreuz :ass]} ;; 3
                              {:karte [:herz 10]}
                              {:karte [:herz 8]}
                              {:karte [:herz 2]}]]}
             :gabi {:stiche [[{:karte [:kreuz :ass]} ;; 0
                              {:karte [:kreuz 10]}
                              {:karte [:kreuz 8]}
                              {:karte [:kreuz 2]}]]}
             :peter {:stiche [[{:karte [:kreuz :ass]} ;; 2
                               {:karte [:herz 10]}
                               {:karte [:herz 8]}
                               {:karte [:kreuz 2]}]]}
             :sonja {:stiche [[{:karte [:kreuz :ass]} ;; 2
                               {:karte [:herz 10]}
                               {:karte [:herz 8]}
                               {:karte [:kreuz 2]}]]}})))
    (is (= [[0 #{:gabi :peter}]
            [2 #{:sonja}]
            [3 #{:paul}]]
           (rang-liste
            {:paul {:stiche [[{:karte [:kreuz :ass]} ;; 3
                              {:karte [:herz 10]}
                              {:karte [:herz 8]}
                              {:karte [:herz 2]}]]}
             :gabi {:stiche [[{:karte [:kreuz :ass]} ;; 0
                              {:karte [:kreuz 10]}
                              {:karte [:kreuz 8]}
                              {:karte [:kreuz 2]}]]}
             :peter {:stiche [[{:karte [:kreuz :ass]} ;; 0
                               {:karte [:kreuz 10]}
                               {:karte [:kreuz 8]}
                               {:karte [:kreuz 2]}]]}
             :sonja {:stiche [[{:karte [:kreuz :ass]} ;; 2
                               {:karte [:herz 10]}
                               {:karte [:herz 8]}
                               {:karte [:kreuz 2]}]]}})))
    ))

(deftest gewinnt-test
  (testing "gewinnt"
    (is (= #{:gabi}
           (gewinnt
            {:paul {:stiche [[{:karte [:kreuz :ass]} ;; 3
                              {:karte [:herz 10]}
                              {:karte [:herz 8]}
                              {:karte [:herz 2]}]]}
             :gabi {:stiche [[{:karte [:kreuz :ass]} ;; 0
                              {:karte [:kreuz 10]}
                              {:karte [:kreuz 8]}
                              {:karte [:kreuz 2]}]]}
             :peter {:stiche [[{:karte [:kreuz :ass]} ;; 2
                               {:karte [:herz 10]}
                               {:karte [:herz 8]}
                               {:karte [:kreuz 2]}]]}
             :sonja {:stiche [[{:karte [:kreuz :ass]} ;; 2
                               {:karte [:herz 10]}
                               {:karte [:herz 8]}
                               {:karte [:kreuz 2]}]]}})))
    (is (= #{:gabi :peter}
           (gewinnt
            {:paul {:stiche [[{:karte [:kreuz :ass]} ;; 3
                              {:karte [:herz 10]}
                              {:karte [:herz 8]}
                              {:karte [:herz 2]}]]}
             :gabi {:stiche [[{:karte [:kreuz :ass]} ;; 0
                              {:karte [:kreuz 10]}
                              {:karte [:kreuz 8]}
                              {:karte [:kreuz 2]}]]}
             :peter {:stiche [[{:karte [:kreuz :ass]} ;; 0
                               {:karte [:kreuz 10]}
                               {:karte [:kreuz 8]}
                               {:karte [:kreuz 2]}]]}
             :sonja {:stiche [[{:karte [:kreuz :ass]} ;; 2
                               {:karte [:herz 10]}
                               {:karte [:herz 8]}
                               {:karte [:kreuz 2]}]]}})))
    ))

(deftest spiel-test
  (testing "spiel"
    (let [s {:paul {:hand #{[:kreuz :ass]}}
             :gabi {:hand #{[:herz 8]}}
             :peter {:hand #{[:kreuz 2]}}
             :sonja {:hand #{[:herz 10]}}}]
      (is (= (spiel s)
             {:gewinnt #{:sonja :peter :gabi}
              :spieler {:paul {:hand #{}
                               :stiche [[{:karte [:kreuz 2], :spieler :peter}
                                         {:karte [:kreuz :ass], :spieler :paul}
                                         {:karte [:herz 10], :spieler :sonja}
                                         {:karte [:herz 8], :spieler :gabi}]]}
                        :gabi {:hand #{}}
                        :peter {:hand #{}}
                        :sonja {:hand #{}}}})))))

