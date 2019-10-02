(ns hearts.core)

(def hr "\n------------------------------------\n")
(def farbe first)
(def bild second)
(def spieler [:gabi :peter :paul :sonja])
(def bilder (concat (range 2 11) [:bube :dame :koenig :ass]))
(def bild->index (->> bilder
                      (map-indexed #(-> [%2 %1]))
                      (into {})))

(def karten->punkte
  (->> (for [f [:kreuz :pik :herz :karo]
             b bilder]
         [f b])
       (map
        (fn [[f b]]
          [[f b] (cond
                   (= [f b] [:pik :dame]) 13
                   (= f :herz) 1
                   :else 0)]))
       (into {})))

(defn beginnt [s-map]
  (->> s-map
       (keep 
        (fn [[s {h :hand}]]
          (when (h [:kreuz 2])
            s)))
       first))

(defn sticht [[eroeffnung & xs]]
  (:spieler
   (reduce
    (fn [{[f1 b1] :karte :as t}
         {[f2 b2] :karte :as s}]
      (if (and (= f1 f2) (> (bild->index b2) (bild->index b1)))
        s
        t))
    eroeffnung
    xs)))

(defn punkte [stiche]
  (->> (flatten stiche)
       (map (comp karten->punkte :karte))
       (apply +)))

(defn rang-liste [s-map]
  (->> s-map
       (map                         
        (fn [[s {stiche :stiche}]]
          {:spieler s :punkte (punkte stiche)}))
       (group-by :punkte)           
       (map (fn [[p xs]]            
              [p (->> xs (map :spieler) (into #{}))]))
       (sort-by first)))            

(defn gewinnt [s-map]
  (->> s-map
       rang-liste
       first                        
       second))

(defn geben! []
  (->> (keys karten->punkte)
       shuffle
       (partition 13)
       (map #(-> [%1 {:hand (into #{} %2)}]) spieler)
       (into {})))

(defn legt [{:keys [hand tisch]}]
  (if (empty? tisch)                 
    (or (hand [:kreuz 2])            
        (-> hand shuffle first))     
    (if-let [k (->> hand
                    (filter #(= (farbe %)
                                (-> tisch first :karte farbe)))
                    shuffle
                    first)]           
      k                              
      (-> hand shuffle first))))   

(defn runde [{s :spieler b :beginnt r :runde}]
  (when-let [xs (when-not (-> s first second :hand empty?)
                  (->> (concat spieler spieler)           
                       (drop-while #(not= b %))           
                       (take 4)))]                        
    (print hr "Runde" r "beginnt: Spieler" b "eröffnet.\nSpieler:" s \newline)
    (loop [[x & xr] xs               
           s s                       
           tisch []]                 
      (if-not x                      
        (let [b (sticht tisch)      
              s {:spieler (update-in s [b :stiche] (fnil conj []) tisch)
                 :sticht b}]
          (println \newline "Runde" r "endet: Spieler" b "sticht:" tisch "\nSpieler:" s hr)
          s)
        (let [{hand :hand} (s x)     
              k     (legt {:hand hand :tisch tisch})   
              tisch (conj tisch {:karte k :spieler x}) 
              s     (update-in s [x :hand] disj k)]    
          (println "     Runde" r ": Spieler" x "legt" k "Tisch:" tisch)
          (recur xr s tisch))))))                      

(defn spiel
  ([] (spiel (geben!)))
  ([gegeben] 
     (println "Gegeben:" gegeben hr)
     (loop [s gegeben
            b (beginnt s)
            r 1]
       (if-let [{s :spieler b :sticht} (runde {:spieler s :beginnt b :runde r})]
         (recur s b (inc r))
         (let [erg {:spieler s
                    :gewinnt (gewinnt s)}]
           (println "Gewinner:" (:gewinnt erg) "\nSpieler:" s hr)
           erg)))))
