# Hearts

Dies ist eine Implementierung des Spiels _Hearts_ [1] in Clojure
[2]. Ich habe nicht alle Regeln des Spiels implementiert. Angeregt hat
mich ein Artikel im Java Magazin 2019/06 [2], in dem eine
Implementation in Haskell vorgestellt wurde.

Um den Code laufen zu lassen, brauchst du erstmal ein Java 8 JDK (eine
JRE sollte auch reichen).

Dann braucht man Clojure: __TODO__

Und schließlich noch den Code. Du kannst entweder das git Repo clonen
oder downloaden oder einfach nur die eine Datei runterladen: __TODO__

Nun kannst du das Programm starten: __TODO__

Falls du mehr über die Möglichkeiten wissen möchtest, wie man Clojure
Programme bauen und ausführen kann, findest du ein paar Hinweise in
Solo [4].

[1] __TODO__ URL Wikipeadi

[2] __TODO__ Clojure

[3] __TODO__ URL Java Magazin

[4] __TODO__ Solo

-----------------------------------------------------------------------

# Spielregeln

Ich versuche, die Regeln des Spiels möglichst knapp zu formulieren und
dabei auch schon Ausdrücke zu benutzen, die sich später im Code
wiederfinden. So entwickelt sich eine _ubiquotious language_ (__TODO__
Schreibung?!) ("allgegenwärtige Sprache"), in der wir uns über unsere
Domäne unterhalten können.

* _Hearts_ ist ein __Karten-Spiel__ mit 52 (= 4 x 13) Spielkarten.


* Es gibt eine __Rangfolge__ von 13 __Karten-Bilder__ (2..10, Bube,
  Dame, König, Ass)


* Es gibt 4 __Karten-Farben__ (Kreuz, Pik, Herz, Karo)


* Jede Karte hat einen __Punktwert__. Diese werden wir später zählen
  und addieren, wenn wir den Gewinner eines Spiels ermitteln:  
    * Pik-Dame     : 13
    * Herz-Karte   : 1
    * alle anderen : 0


* Es gibt __vier Spieler__, die in einem gedachten Kreis sitzen. Es
  gibt somit eine __Reihenfolge__ der Spieler (vgl. __Runden__ unten)

-----------------------------------------------------------------------

## Spielablauf

Das Spiel wird in __Runden__ gespielt, in denen jeweils ein Spieler
einen __Stich__ macht.


### Spielbeginn

__Vor der ersten Runde__ werden die Karten __gemischt__ und je 13
Spielkarten verdeckt auf die Spieler ausgeteilt. Jeder Spieler nimmt
anschließend seine Karten auf. Jeder Spieler hält/kennt also nur seine
Karten (seine _Hand_).

Der Spieler mit der Kreuz-2 __beginnt__ (_eröffnet_) die __erste__
__Runde__. Alle folgenden Runden werden von demjenigen Spieler
__begonnen__, der in der jeweils vorangegangenen Runde den __Stich__
(vgl. unten) __erhalten__ hat.

### Runden

Nun werden die Runden gespielt. Eine Runde beginnt damit, dass der
eröffnende Spieler eine __Eröffnungskarte__ __offen__
__ausspielt__. In der ersten Runde muss die Kreuz-2 ausgespielt werden
(vgl. oben).

Nun spielen die drei anderen Spieler der __Reihe nach__ (vgl. oben
_gedachter Kreis_) auch jeweils eine Karte offen aus. Falls ein
Spieler eine oder mehrere Karte mit der gleichen __Farbe__ wie die
Eröffnungskarte auf der Hand hat, __muss__ er eine dieser Karten
ausspielen (es muss also die Farbe der Eröffnungskarte _bedient_
werden). Andernfalls kann er eine beliebige Karte von seiner Hand
spielen (_abwerfen_).

Nachdem alle Spieler ihre Karte in der Runde gespielt haben, erhält
derjenige Spieler die gespielten Karten (den __Stich__), der die Karte
mit der __Farbe der Eröffnungskarte__ mit dem __höchsten Rang__
gespielt hat. Er legt den Stich verdeckt auf seinen __Haufen__ (er
nimmt sie also __nicht__ auf seine __Hand__).

Es kann natürlich vorkommen, dass der eröffnende Spieler selbst den
Stich erhält. Dies ist der Fall, falls alle anderen Spieler
_abgeworfen_ haben oder alle anderen Spieler mit einer Karte bedient
haben, die unter dem Rang der Eröffnungskarte ist.

Damit ist die Runde beendet.

Durch das Erhalten des Stichs sammeln die Spieler über die Runden also
Stiche an, die sie auf ihren __Haufen__ (vor sich auf dem Tisch)
legen, der zu Beginn leer ist.

### Spielende

Das Spiel endet __immer__ nach der __13. Runde__. Die Runden werden
immer alle gespielt. Es gibt kein vorzeitiges Spielende, selbst wenn
schon während des Spiels feststehen sollte, wer das Spiel gewinnen
wird.

Nach der letzten Runde werden für jeden Spieler die __Punkte__
ermittelt. Die Punkte eines Spielers ergeben sich aus der Summe der
Karten-Punktwerte (vgl. oben) jener Karten, die er über die Stiche
erhalten/gesammelt hat --- also die Karten auf dem __Haufen__
__jeden__ __Spielers__.

__Gewonnen__ hat derjenige Spieler, der nach Spielende die
__wenigsten__ __Punkte__ hat. Es kann auch sein, dass sich mehrere
Spieler bei Punktgleichheit den Sieg teilen.

-----------------------------------------------------------------------

# Implementation

Nun folgt eine detailierte Erläuterung des Codes. Ich möchte damit
verschiedene Aspekte von Clojure erläutern. Dabei werde ich erst immer
jene Dinge erläutern, die für das Verständnis des dann aufgeführten
Code-Abschnitts nötig sind. Ich führe hier wirklich jede Zeile-Code
auf. Es wird nichts ausgelassen.

Los geht's.

Clojure ist eine funktionale Programmiersprache. Clojure-Code ist in
_Namespaces_ (__Namensräumen__) organisiert (ähnlich wie Packages in
Java). I.d.R. entspricht jeder Namensraum einer __Datei__. Diese Datei
muss in einem __Verzeichnis__ liegen, dessen Name zum Namensraum
"passt". Der Namesraum `hearts.core` findet sich in der Datei
`src/hearts/core.clj`. Die Dateien und Namensräume bilden eine
__hierarchische__ __Struktur__. Diese __Hierarchie__ ist für Clojure
jedoch ohne Bedeutung. Sie dient allein der Strukturierung der
Code-Basis und hat keine Auswirkung auf Sichtbarkeit oder
ähnliches. I.d.R. "schneidet" man Namensräume nach
fachlichen/inhaltlichen Gesichtspunkten. Namensräume dienen ebenfalls
als Mittel um __Namenskollisionen__ zu vermeiden und um die
__Sichtbarkeit__ einzuschränken (durch "private Namen").

Clojure-Code (d.h. eine Clojure-Datei) besteht i.d.R. aus Folgen von
sog. _S-Expressions_. Dabei handelt es sich um __geschachtelte__
(hierarchische) Klammerausdrücke (__Listen__).

Beispiel: `(str "Hello," "world")`

Die __Elemente__ dieser Listen sind _Forms_ (__Formen__). Es gibt eine
ganze Reihe von Formen (Symbole, _Keywords_, Zahlen, Listen, Vektoren,
Strings, etc.), von denen wir weiter unten einige kennenlernen
werden. Das ist im Prinzip alles, was man zur __Syntax__ von Clojure
sagen kann.

Die folgende Liste (erste Codezeile von `core.clj`) hat als erstes
Element das Symbol `ns` und als zweites Element das Symbol
`hearts.core`. Das erste Element einer Liste bezeichnet "etwas
ausführbares" (__Funktor__; z.B. eine Funktion oder ein Makro) und die
folgenden Elemente bilden die __Argumente__ des Funktors.

Die Bedeutung (__Sematik__) der Formen ergibt sich durch Clojures
_Auswertungs-Regeln_. Die meisten Formen "werten zu sich selbst
aus". Das heißt, dass die __Auswertung__ der __Form__ `2` (Syntax) die
__Zahl__ 2 (Semantik; vom Typ `java.lang.Integer`) ergibt.

Symbole werten zu jenem Wert aus, der an den durch das Symbol
benannten __Namen__ __gebunden__ ist (vgl. unten).

Die Auswertung von Listen erfolgt durch __Auswertung__ der
Listen-Element-Formen (die Listen-Element-Formen können wiederum
Listen sein; __rekursive Auswertung__), wobei das erste Element zu
einem __Funktor__ auswerten __muss__, und der Anwendung des Funktors
auf die restlichen (Auswertungs-)Werte (Argumente).

Es gibt einige Sonderfälle, die eine andere Auswertungsregel
haben. Und es gibt auch Funktoren, die __Seiteneffekte__ haben, also
im funktionalen Sinn nicht __pure__ sind.

Da Listen sowohl für "ausführbaren Code" als auch als Datenstruktur
verwendet werden (sog. Homoiconicity; "code is data is code"), fällt
es Clojure-Neulingen zu Beginn häufig schwer, zu erkennen, ob eine
S-Expression/Form nun als "ausführbarer Code" oder als Daten-Wert
gilt.

Mit `ns` wird das Makro (mehr zu Makros weiter unten)
`clojure.core/ns` benannt. Dieses Makro sorgt dafür, dass der
angegebene Namensraum "aufgemacht". Das Makro kann noch eine Menge
mehr, wie z.B. andere Namensräume "importieren", aber das brauchen wir
hier nicht.

	(ns hearts.core)

---

`def` (`clojure.core/def`; auch ein Makro) bindet einen __Wert__ (hier
einen String) an einen __Namen__ (hier durch Symbol `hr` angegeben) im
__aktuellen__ __Namensraum__. Die Form `hr` wird also in diesem Fall
__nicht__ zu ihrem gebundenen Wert __ausgewertet__ (vgl. oben),
sondern sie wird als __Name__ für die Bindung verwendet (__TODO__:
Java L-value vs R-value).

Es handelt sich um einen "Java String" vom Typ
`java.lang.String`. Clojure übernimmt komplett die `java.lang`
Datentypen, anders als z.B. Jython, das mit Wrapper-Datentypen
arbeitet. Dadurch lässt sich Clojure sehr elegant mit anderen Java
Klassen/Bibliotheken integrieren; sog. "Java interop".

Als Java-Entwickler denkt man bei diesen "globalen Namen" vielleicht
an __Variablen__. Es ist jedoch unüblich (wenn auch möglich), während
eines Programmlaufs einen Namen nacheinander bzw. __wiederholt__ an
(verschiedene) Werte zu binden (was in einem gewissen Sinne der
Wertzuweisung an eine Variable in Java entspricht; tatsächlich ist es
aber völlig anders, aber das soll hier nicht im Detail erläutert
werden). Diese Bindungen sind also eher wie `static final` Felder in
Java zu verwenden (aber wie schon gesagt: es ist völlig anders!).

---

	(def hr "\n------------------------------------\n")

---

Der __Wert__(!!!), der an den Namen `first` gebunden ist, wird an den
Namen `farbe` gebunden.

`first` ist ein Name aus dem Namensraum `clojure.core`. `first` _ist
eine Funktion_ die das erste Element eine Liste liefert.

Genauer: der Name `clojure.core/first` ist an eine Funktion d.h. ein
__Funktionsobjekt__ gebunden (Funktor; vgl. oben). Das ist aber zu
länglich zu schreiben, daher verwendet man einfach beim Sprechen über
den Code den "Namen für den Wert" (also `first`; den __Benenner__)
anstatt den Wert explizit zu benennen (__Benanntes__), falls keine
Gefahr von Unklarheit/Missverständnis besteht.

Dieses Funktionsobjekt und die __zugehörige Klasse__ entsteht durch
die __Kompilierung__ von S-Expressions. Clojure hat __keinen
Interpreter__ sondern einen __Compiler__, der zur __Laufzeit__ läuft;
also __just in time__. Es gibt aber auch __ahead of time__ (AOT), wie
bei Java, um die Startup-Zeit von Clojure-Programmen zu verkürzen.

Wir führen hier also einfach nur einen __Alias__ für eine Funktion ein
(denn wir binden ja einen __zweiten Namen__ an __denselben(!!!!)__
Wert), weil `farbe` Teil unser Domänensprache ist (anders als `first`,
was eher ein technischer/generischer Name ist, der in unserer Domäne
keine besondere Bedeutung hat). Das Gleiche machen wir auch für `bild`
mit der Funktion `clojure.core/second`. Warum wir diese Aliase
einführen, erläutern wir weiter unten.

Mit `first` benennen wir `clojure.core/first` ohne den Namensraum
`clojure.core` angeben zu müssen. Das liegt daran, dass der Namenraum
`clojure.core` via `ns` "importiert" wurde und damit zur Verfügung
steht (mehr sagen wir hier nicht zu Namensräumen).

---

    (def farbe first)
	(def bild second)

---

Wir binden einen Vektor mit den Vornamen unserer Spieler an den Namen
`spieler`. Die Reihenfolge soll beschreiben, in welcher Reihenfolge
unsere Spieler an dem gedachten, runden Tisch sitzen. Dabei ist
unerheblich, welcher Spieler an welcher Position im Vektor steht. Wie
schon gesagt: es ist ein gedachter Kreis.

In Clojure können Vektoren, Listen, Maps und Sets (und Reguläre
Ausdrücke) direkt als __Literal__ aufgeschrieben werden (und man kann
weitere eigene Literaltypen definieren -- sog. _tagged literals_). Die
Elemente dieser _Collections_ brauchen nicht vom gleichen Typ zu sein
und die ganzen Datenstrukturen sind __immutable__ (in Clojure-Sprech
_persistent data structures_). D.h. wir können ihren Wert(!!)
d.h. Inhalt/Zustand nicht ändern. Daher können wir sie auch ohne
Gefahr mit anderen teilen, weil sie niemand _hinterrücks_ ändern kann
(man braucht also keine "Clone" oder "defensive Kopien" und keine
Synchronisation im Race-Conditions zu unterbinden).

Für die Vornamen unserer Spieler verwenden wir Clojure __Keywords__
(natürlich ebenfalls immutable, genau wie die Java Datentypen in
`java.lang`!). Keywords verhalten sich so ähnlich die Java Enums (sie
sind z.B. "identisch" und nicht nur "gleich"), nur dass man sie
nirgends vorab definieren muss/kann (man kann sie also auch nicht
enumerieren/aufzählen). Man schreibt sie einfach hin (und kann sich
dabei auch verschreiben, ohne dass es der Compiler merkt ....)

---

    (def spieler [:gabi :peter :paul :sonja])

---

`bilder` ist eine Liste (genauer eine _Sequenz_, aber dazu später
mehr; man kann sich erstmal eine Liste vorstellen) mit den Elementen
(Zahlen) 2 bis 10 und den Elementen (Keywords) `:bube`, `:dame`,
`:koenig` und `:ass`.

`(range x y)` liefert eine Liste mit den Zahl-Elementen x bis y-1 und
`concat` verbindet die beiden Listen zu einer Liste.

Die Reihenfolge bzw. die Position der Bilder in der Liste bestimmt
später, wenn es darum geht, wer einen Stich bekommt, welche Karte
einen "höheren Rang hat" als die andere.

---

	(def bilder (concat (range 2 11) [:bube :dame :koenig :ass]))

---

Anstatt später immer wieder die "Position" einer Karte in `bilder` zu
ermitteln (also ihren __Rang__), erzeugen wir __einmalig__ eine
Abbildung `bild->index` (eine `java.util.Map`) von den Bildern auf die
jeweilige Position des Bilds in `bilder`. Der Name `bild->index` ist
beliebig gewählt, das `->` in dem Namen hat hier keine besondere
Bedeutung in Clojure.

In dieser S-Expression werden eine Reihe weiterer Funktionen
verwendet.

* `->>` (_thread last_) nimmt das erste Element (hier `bilder`) und
  fügt es an die letzte Argumentposition des zweiten Elements. Dadurch
  entsteht in diesem Fall `(map-indexed #(-> [%2 %1]) bilder)` und
  dann diesen Ausdruck/Form wieder an die letzte Argumentposition des
  dritten Elements. Somit ergibt sich schließlich:
    
  `(into {} (map-indexed #(-> [%2 %1]) bilder))`  

  Der Code wird also __umgestellt__, bevor er überhaupt
  compiliert/ausgeführt/ausgewertet wird. Das nennt man
  __Meta-Programming__ (`->>`ist auch keine Funktion, sondern ein
  Makro).

  `->>` erlaubt es, den Code in der __Reihenfolge__
  __hinzuschreiben__, in der er __ausgeführt__ wird: die
  geschachtelten Funktionsaufrufe werden ja von "innen nach außen"
  ausgeführt/ausgewertet. Die Verwendung von `->>` macht es dem
  __Menschen__ leichter zu verstehen, was geschieht. Die Semantik wird
  nicht verändert -- es handelt sich wirklich nur um eine
  __Umstellung__ __des__ __Codes__, bevor er an den Compiler gegeben
  wird.


* Mit `#(....)` wird eine Funktion definiert. Man sagt auch "anonyme
  Funktion", weil sie an keinen Namen gebunden wird. Die Benennung
  "anonym" ist aber irreführend, weil Funktionen __niemals__ einen
  Namen haben und in dem Sinne __immer__ anonym sind. Durch `def` gibt
  man eben einer Funktion auch __keinen__ __Namen__ sondern __bindet__
  die Funktion an einen __Namen__ (und man kann sie eben auch an
  mehrere Namen binden; vgl. oben). In diesem Sinne __besitzt__ eine
  Funktion an sich __keinen__ Namen.

  `#(-> [%2 %1])` ist eine Funktion mit zwei Parameter, die einen
  Vektor liefert, dessen erstes Element das zweite Argument der
  Funktion ist und dessen zweites Element das erste Argument der
  Funktion ist. In Clojure gibt es kein
  `return`-Statement. Stattdessen liefert eine Funktion immer den
  Auswertungswert der "letzten Form" (welche das genau ist, besprechen
  wir im Code). `->` ist das _thread first_ Makro, das ich hier
  verwende, weil der Ausdruck `#([%2 %1])` von Clojure so
  interpretiert wird, als wenn der Vektor eine Funktion (Funktor)
  wäre. `->` tut uns den Gefallen, den Code so umzuformen, dass der
  Vektor als Rückgabewert der anonymen Funktion gilt.


* `map-indexed` ist eine HOF (higher order function): sie erwartet als
  erstes Argument eine __Funktion__ und als zweites eine Sequenz. Sie
  wendet dann die Funktion der Reihe nach auf die Elemente der Sequenz
  an. Dabei ruft sie die übergebene Funktion mit jeweils zwei
  Argumenten auf: das erste Argument ist der 0-basierte Index des
  Elements der Sequenz (und dieser Index ist genau das, was wir als
  "Position" benötigen; vgl. oben), das gerade verarbeitet wird und
  das zweite Argument ist das Element selbst. Das Ergebnis von
  `map-indexed` ist wiederum die Sequenz aus den
  Funktions-Aufruf-Ergebnissen. Beispiel: `(map-indexed str [1 2 3])`
  liefert `("01" "12" "23")` (`str` liefert den String/Konkatenation
  der Argumente). Wir verwenden aber `#(-> [%2 %1])` und erzeugen
  somit für jedes Bild (d.h. Element von `bilder`) ein 2-Tupel
  [Bild, Position].


* `(into {} .....)` sorgt dafür, dass die __Elemente__ des zweiten
  Arguments in die Datenstruktur (erstes Argument) __zugefügt__
  werden. Natürlich wird dabei nichts verändert, sondern es wird ein
  neuer Wert des gleichen Typen erzeugt und diese neue Struktur ist
  dann das Ergebnis von `into`. Bei `{}` handelt es sich um eine
  Map. Und wenn man einer Map 2-elementige Vektoren (2-Tupel) zufügt,
  dann wird das jeweils erste Element als Schlüssel (Key) und das
  jeweils zweite Element als Wert (Value) des Map-Entries
  verwendet. Somit fügt `into` alle [Bild, Position]-Tupel als
  __&lt;Key,Value>__ in die leere Map und liefert das Ergebnis als
  Rückgabewert.

Die ganze Verarbeitung nimmt also `bilder`, macht eine Folge (in
Clojure-Sprech _Sequence_) von 2-Tupeln [Bild, Bild-Position] daraus
und fügt diese als __&lt;Key,Value>__ in eine Map, die wir an den Namen
`bild->index` binden.

Wie man auf diese Map zugreift, sehen wir weiter unten.

---

    (def bild->index (->> bilder
                          (map-indexed #(-> [%2 %1]))
                          (into {})))

---

`karten->punkte` soll eine Abbildung (Map) von den Karten auf die
Punkte einer jeden Karte sein. Die Karten ergeben sich als
__Kreuzprodukt__ über die Farben `:kreuz`, `:pik`, `:herz` und `:karo`
(die wiederum als Keywords dargestellt werden) und den Bildern
(`bilder`).

Die Karten sind also einfach nur 2-Tupel/Vektoren
__&lt;Farbe,Bild>__. Wir haben weder explizit einen __Datentyp__
definiert (in Java hätte man wohl eine Klasse eingeführt) noch
irgendwie anders ausgedrückt, dass das erste Element des 2-Tupels die
Farbe der Karte ist oder dass das zweite Element das Bild der Karte
ist. Die Bedeutung ergibt sich also nur "durch den Code" bzw. die
__Verwendung__ der 2-Tupel. Zu jeder Karte wird dann noch ihr
Punktwert ermittelt und damit die gewünschte Map erzeugt.

* Das Makro `for` __liefert__ für jede Kombination/Permutation
  (Kreuzprodukt) ihrer Argumente (hier die Elemente des Farb-Vektors
  und der Bilder, die der Reihe nach --- also "für jeden Durchlauf"
  --- an die Namen `f` und `b` gebunden werden) das angegebene
  Ergebnis `[f b]` (also das 2-Tupel) als __Sequenz__. Der Mechanismus
  wird _list comprehension_ (etwa "Listenerzeugung") genannt.


* Diese Sequenz von __&lt;Farbe,Bild>__ "fädeln" wir nun via `->>` durch
  die HOF `map`, die als erstes Argument eine Arity-1-Funktion
  erwartet, die sie der Reihe nach auf die Elemente des zweiten
  Arguments anwendet und als Ergebnis wiederum eine __Sequenz__ mit
  den Funktionswerten liefert.


* Wir definieren hier ("in place"; also ohne sie an einen Namen zu
  binden) mit `fn` die benötigte Arity-1-Funktion. `fn` ist der
  _kanonische_ Weg, eine Funktion zu definieren. Die Alternative
  `#(....)`, die oben vorgestellt wurde, ist eine Kurzschreibweise
  (sog. "reader macro"), in der sich die Arity aus der
  Nennung/Verwendung der "durchnummerierten Parameter-Formen" `%<i>`
  ergibt. Bei `fn` müssen die (_formalen_) Parameter hingegen explizit
  angegeben werden.

  Wir geben als Parameter aber anstatt eines __Namen__ einen
  __Vektor__ mit zwei Elementen/Namen `f` und `b` an. Dieser Vektor
  wird von Clojure als __Muster__ (_pattern_) verwendet. Beim
  Funktionsaufruf wird das Argument (also z.B. ein Vektor oder eine
  Liste) anhand des Musters __zerlegt__ und die Parameternamen `f` und
  `b` werden in diesem Fall an das erste bzw. zweite Element des
  Arguments gebunden. Dieser Mechanismus wird _destructuring_ (oder
  allgemeiner _pattern matching_) genannt. Man könnte es auch
  "muster-basierte rekursive Strukturzerlegung mit Namensbindung"
  nennen.

  Mit einem Vektor-Pattern kann man __sequentielle__ Argumente
  zerlegen (also Listen, Vektoren, Strings, Maps) und mit einem
  Map-Pattern (kommt weiter unten noch) kann man __assioziative__
  Dinge zerlegen (Maps). Destructuring erspart einem also, den Zugriff
  auf die Elemente explizit über Funktionsaufrufe (z.B. `first` und
  `second`) hinschreiben zu müssen.

  Unsere Arity-1-Funktion liefert als Ergebnis wieder ein
  2-Tupel/Vektor, dessen erstes Element die Karte __&lt;Farbe,Bild>__ ist
  und deren zweites Element der Punkte-Wert der Karte ist.

* `cond` verhält sich wie die (neue) `switch`-__Expression__ (NICHT
  `switch`-__Statement__!) in Java 12. Die Argument-Paare aus
  _Prädikat_ (Form) und Ergebnis (Form) werden der Reihe nach
  ausgewertet. Dabei wird immer nur die Prädikats-Form ausgewertet!
  (das ist wichtig, weil die Auswertung der Ergebnis-Form ja
  Seiteneffekte haben könnte!) Sobald die ausgewertete Prädikats-Form
  _wahrhaftig_ (_truthy_ bzw. logisch wahr; vgl. unten) ist/liefert,
  wird die zugehörige Ergebnis-Form ausgewertet und als Ergebnis von
  `cond` geliefert.

  In Clojure gelten nur `nil` (das ist __dasselbe__ wie Java `null`)
  und `false` (ist __desselbe__ wie Java `java.lang.Boolean/FALSE`)
  als __unwahr__. Alle anderen Werte (inkl. `(new java.lang.Boolean
  false)` gelten als __wahr__. Um _logisch_ _wahr_ von `true` zu
  unterscheiden (denn sie sind __nicht__ __dasselbe__), sagt man
  i.d.R. _truthy_, wenn man "logisch wahr" meint und _falsy_, wenn man
  "logisch unwahr" meint.

* Schließlich verwenden wir wieder `into`, um die Sequenz von
  __&lt;<Farbe,Bild>,Punkte>__ Tupeln in eine Map
  __&lt;Farbe,Bild>--><Punkte>__ zu überführen.

---

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

---

Bis jetzt haben wir (mit Ausnahme der beiden Aliase) nur "Daten"
(d.h. Werte) definiert und sie an Namen gebunden. Der Code (also die
S-Expressions) wird beim Laden des Namensraum `hearts.core`
ausgewertet und führt eben dazu, dass die Werte an die Namen gebunden
werden. Dieser Vorgang findet nur einmalig statt. Also immer nur das
eine Mal, wenn der Namensraum geladen wird. Man kann aber "erzwingen",
dass ein Namensraum wiederholt/mehrfach geladen wird. Dann werden auch
die S-Expressions wiederholt ausgewertet und die Namen werden erneut
an (neue) Werte gebunden.

Nun wollen wir aber mit `defn` eine __Funktion__ definieren.
`(defn <name> [<parameter>...] <body>)` ist einfach eine Kurzform von
`(def <name> (fn [<parameter>...] <body>))`.

Die Funktionsdefinition mit `fn` haben wir schon oben gesehen. Hier
binden wir diese Funktion aber an einen Namen, so dass wir später über
diesen Namen auf die Funktion zugreifen können.

---

Die Funktion `beginnt` ermittelt, welcher Spieler die __erste__
__Runde__ beginnt (nämlich jener mit der Kreuz-2) und liefert diesen
als Keyword (also z.B: `:gabi`).

Das Argument ist eine Map. Diese nenne ich __Alle-Spieler-Map__. Die
Alle-Spieler-Map bildet __&lt;spieler>__ wiederum auf eine Map
ab. Diese nenne ich __Ein-Spieler-Map__. Die Ein-Spieler-Map hat
u.a. den Key `:hand` und der (gemappte) Wert von `:hand` ist eine
__Menge__ (Set; Hand-Menge). Die Elemente der Hand-Menge sind
__&lt;Farbe,Bild>__-Tupel/Vektoren (könnten wir __Karte-Vektor__ nennen).

Die Alle-Spieler-Map soll als Datenstruktur(-Wert) den Zustand aller
Spieler während des Spiels repräsentieren. Es fehlen noch einige Dinge
, wie z.B. die Stiche, die die Spieler gemacht haben. Diese Dinge
werden aber noch eingeführt (vgl. unten).

Zu Beginn des Spiels werden alle Karten gemischt und auf die Spieler
verteilt. `beginnt` wird also __einmalig__ (nach dem Verteilen der
Karten) mit diesem _Start-Zustand_ der Alle-Spieler-Map aufgerufen, um
zu ermitteln, wer die erste Runde beginnt.

In Java würde man wohl für Karte-Vektor, Ein-Spieler-Map und
Alle-Spieler-Map separate __Klassen__ einführen, mit Konstruktor,
lesenden und vielleicht schreibenden/mutierenden Methoden
(getter/setter) und dann z.B. auch die Methode `beginnt` in die
Alle-Spieler-Klasse tun.

In Clojure kann man auch "Klassen" definieren, d.h. man definiert
"Kontrakte" (sog. __Protokolle__) so wie man Interfaces in Java
definiert. Und man definiert dann __Implementierungen__ zu diesen
Protokollen. Aber man verwendet keine __Ableitung__ (man kann aber
Java-Klassen ableiten und auch Java Interfaces implementieren;
sog. "Java interop").

In der Implementation, die ich hier vorstelle, habe ich bewusst auf
diese Konstrukte verzichtet, weil ich zeigen wollte, wie man in
Clojure mit den "eingebauten Datentypen" arbeiten kann. In Clojure ist
dies in vielen Situationen "idiomatisch", also ein akzeptiertes und
erwartetes Vorgehen. Das heißt aber nicht, dass es falsch wäre,
Protokolle einzuführen und diese zu implementieren. Ganz im Gegenteil:
durch eine explizite "Typisierung" kann man viel "Information
transportieren" und damit trägt sie zur Verständlichkeit, Wartbarkeit
und Fehlerminimierung/Qualität bei.

Nun aber zur Umsetzung von `beginnt`:

* `->>` fädelt den Argument-Wert (Alle-Spieler-Map) durch die HOF
  `keep`, die aus der Map eine Sequenz von Map-Entries macht. Wenn
  eine Map auf diese Weise "sequenziallisiert" wird, dann besteht die
  entstehende Sequenz aus __&lt;Key,Value>__ der Map-Entries. Hier also
  __&lt;Spieler,Ein-Spieler-Map>__.


* `keep` wendet eine Funktion (erstes Argument) auf die Elemente des
  zweiten Arguments (also der __&lt;Spieler,Ein-Spieler-Map>__-Sequenz)
  an und liefert all jene Funktions-Rückgabe-Werte/Ergebnisse, die
  _truthy_ (vgl. oben) sind. `keep` ist sowas ähnliches wie ein
  Filter, nur dass es nicht die __Eingabelemente__ liefert, sondern
  deren truthy __"abgebildeten" Wert__.


* Für die Definition der Parameter unserer "Abbildungsfunktion" (`fn`)
  verwenden wir diesmal ein komplexeres (geschachteltes) "Muster": wir
  benutzen ein __Vektor-Destructuring__, um jeweils den __&lt;Spieler>__
  des 2-Tupels an `s` zu binden. Für das zweite Element des 2-Tupels
  (die __&lt;Ein-Spieler-Map>__) benutzen wir ein __Map-Destructuring__
  und binden `h` an den Wert des Ein-Spieler-Map-Eintrags mit dem
  Schlüssel `:hand` (also die Hand-Menge).


* `when` funktioniert wie ein `if` ohne `else`-Zweig. Der `else`-Zweig
  ist dann automatisch immer der Wert `nil`. Das muss man erstmal
  verdauen: in Java ist `if` ein (imperatives) __Statement__, in
  Clojure ist es eine (funktionale) __Expression__, die einen
  (Rückgabe-)Wert hat.

  `when` wertet das erste Argument aus und falls es _truthy_ ist, wird
  das zweite Argument ausgewertet und dieser Wert als Ergebnis
  geliefert.

  Das `when-else`-Fall `nil` (`nil` ist _falsy_; nicht _truthy_)
  bewirkt zusammen mit `keep`, dass nur jene Funktionsergebniswerte
  durch `keep` geliefert werden, für die der `when`-Ausdruck _truthy_
  ist.


* `h` wird an die Hand-Menge gebunden (vgl. oben). In der Form `(h
  [:kreuz 2])` ist diese Menge der __Funktor__ (weil erstes Element
  der Liste), also muss sie etwas "ausführbares" sein --- eine
  Funktion!

  Diese zu Mengen zugehörige 1-Arity-__Lookup-Funktion__ bildet die
  Elemente der Menge wiederum auf das jeweilige Element ab (also auf
  "sich selbst"). Alle anderen Argumente werden auf `nil`
  abgebildet. D.h. eine Menge ist "ihre eigene
  __Lookup-Funktion__". Somit wertet `(h [:kreuz 2])` zu `[:kreuz 2]`
  aus, falls `[:kreuz 2]` Element von `h` ist oder zu `nil`
  andernfalls.

  Da `[:kreuz 2]` _truthy_ ist, liefert die `fn` die bzw. den Spieler,
  der die Kreuz-2 auf der Hand hat. Und da immer genau ein Spieler
  diese Karte auf der Hand hat, können wir aus der Sequenz via `first`
  einfach das erste (und einzige) Element als Ergebnis liefern.

Fertig.

---

    (defn beginnt [s-map]
      (->> s-map
           (keep 
            (fn [[s {h :hand}]]
              (when (h [:kreuz 2])
                s)))
           first))

---

__Anmerkung__: es fällt auf, dass Clojure-Programme wenig
  __Verzeigungen__ haben. Bisher haben wir `cond` und `when` als
  Fallunterscheidung/Verzweigung kennengelernt. Viele Dinge, die man
  in Java mit `if-then-else-if-else` machen würde, macht man in
  Clojure mit Sequenzen (und den Funktionen) auf diesen und HOFs. Ob
  das "einfacher" oder "besser" ist, ist noch eine andere Frage.

  Ich finde, dass Clojure-Programme sehr "kompakt" bzw. "dicht"
  wirken. Als Clojure-Anfänger empfand ich das schon fast als "Schmerz
  beim Lesen", weil die Semanik sich auf so wenig Code "verteilt",
  dass man extrem aufmerksam lesen muss. Es gibt wenig "Raum zwischen
  den wichtigen Teilen" zum Ausruhen --- es ist einfach jede Zeile
  wichtig.

  Das führt meiner Meinung nach auch dazu, dass Clojure-Programm
  einfach "kurz sind". Sie haben i.d.R. weniger Zeilen als Java-Code
  und weniger Verzeigungen. Beides sind Faktoren, die effektiv zu
  einer höheren Testabdeckung (branch coverage) führen.

  Aber das ist nur meine subjktive Meinung.

---

`sticht` liefert den Spieler (Spieler-Keyword), der die Karten, die
auf dem Tisch liegen bzw. ausgespielt wurden, erhält. Der also
__den Stich macht__.

Das Argument ist eine Sequenz. Die Elemente sind Maps mit den Keys
`:spieler` und `:karte`. Diese geben an (also mappen zu Werten), wer
die Karte gespielt hat (Spieler-Keyword) und welche Karte gespielt
wurde (Karten-Vektor).

Die Sequenz wird per Vektor-Destrukturierung zerlegt wird. Das
__erste__ Element (man spricht häufig vom _head_) wird an den Namen
`eroeffnung` gebunden. Durch die `&`-Notation werden die restlichen
__Elemente__ (also auch wieder eine __Sequenz__; man spricht häufig
vom _tail_) an den Namen `xs` gebunden.

D.h. die Reihenfolge der Element in der Sequenz gibt an, in welcher
Reihenfolge die Karten ausgespielt wurden. Daher gibt das erste
Element an, mit welcher Karte die betreffende Runde __eröffnet__
wurde.

Die Implementation muss also beginnend von der Eröffnungskarte aus
alle anderen Karten betrachten und entscheiden, welches die
"Rang-höchste" der Karten ist, die die Farbe der Eröffnungskarte
"bedient" hat und denjenigen Spieler liefern, der eben diese Karte
gelegt hat.


* das Keyword `:spieler` steht hier an der __Funktor__ Position in der
  S-Expression. So wie Mengen Funktionen auf ihren Elementen sind, so
  sind Keywords __Lookup-Funktionen__ auf __assoziativen__
  Datenstrukturen. D.h. `:spieler` verhält sich wie eine Funktion, die
  mit dem Key `:spieler` auf eine Map oder eine Menge/Set zugreift und
  den Ergebniswert (also entweder den Map-Value oder das Set-Element)
  liefert.

  Mit `:spieler` wird also auf den gemappten Wert der folgenden
  `reduce`-Form zugegriffen und dieser als Ergebnis geliefert. Damit
  soll am Ende eben der gesuchte Spieler geliefert werden.


* `reduce` ist eine HOF, mit der man Werte (z.B. die Element einer
  Sequenz) __aggregieren__ kann. Die Aggregierungslogik steckt
  wiederum in einer Funktion, die man an `reduce` übergibt.

  `reduce` ruft diese Funktion (erstes Argument) für jedes Element des
  "sequenzialisierten" dritten Arguments (`xs`) einmal auf.

  Dabei ruft `reduce` die Funktion beim ersten Mal mit `eroeffnung`
  (dem "initialen" Aggregat) und dem ersten Element von `xs`
  auf. Anschließend ruft `reduce` die Funktion mit dem Rückgabe-Wert
  des ersten Funktionsaufrufs (also dem Ergebnis der ersten
  Aggregation) und dem zweiten Element von `xs` auf.

  Mit der Funktion können wir also einen Aggregatswert an `reduce`
  liefern, den `reduce` beim anschließeden Funktionsaufruf wieder an
  die Funktion gibt (das ist wie Ping-Pong).

  Nach dem letzten Element von `xs` liefert `reduce` den Rückgabe-Wert
  des letzen Funktionsaufrufs (also das "finale Aggregat").

  In Java gibt es mittlerweile ja auch ein `reduce` [1]. Früher hätte
  man in Java einfach eine `for` Schleife mit einer lokalen Variable
  benutzt, in der man das Aggregate hält.


* Die Parameter der Aggregats-Funktion werden hier via
  Map-Destrukturierung an lokale Namen gebunden. Das erste Argument
  (das Aggregat) zerlegen wir und binden den Namen `f1` an die Farbe
  des `:karte` Wertes und `b1` an das Bild.

  Hier verwende ich noch ein weiteres Destrukturierungs-Feature: mit
  `:as` bindet man das ganze (unzerlegte) Argument an einen Namen --
  hier also `t`.

  Die Namen `f2`, `b2` und `s` binden wir an die Wertes des zweiten
  Arguments (das jeweilige Element von `xs`; vgl. oben).

  Die Funktion prüft nun, ob mit `s` bzw. `f2` die Farbe `f1` bedient
  wurden __und__ ob der Rang von `s` __höher__ ist als der von `t`
  (für den ersten Aufruf wird `t` ja `eroeffnung` sein).

  Falls dem so ist, wird `s` unser weiterer Aggregationszustand
  sein. Also jene Karte, die "sticht".

  Falls dem aber nicht so ist, "sticht" weiterhin die bisherige
  "stechende Karte" `t`.

Am Ende liefert `reduce` also "die stechende Karte" und mit `:spieler`
liefert `sticht` den Spieler, der diese Karte gespielt hat.

Fertig.

__Hinweis__: man hätte auch `((reduce (fn....) eroeffnung) :spieler)`
schreiben können (also die Map als Funktor und nicht das Keyword),
aber es ist _idiomatisch_, für diesen Use-Case das Keyword als Funktor
zu verwenden (und zwei öffnende Klammern direkt hintereinander sehen
auch merkwürdig aus).

[1] https://www.baeldung.com/java-stream-reduce

---

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

---

`punkte` liefert die Summe der Karten-Punkte zu einer Sequenz von
Stichen. Ein Stich ist wiederum eine Sequenz von "gespielten" Karten
(also eine Sequenz von Maps jeweils mit `:spieler` und `:karte`;
vgl. `sticht`). Wir haben also zwei ineinander geschachtelte
Sequenzen!


* `flatten` macht aus der Sequenz von Sequenzen (von Elementen) eine
  Sequenz von Elementen (die äußere Sequenz wird also "ausgepackt"
  oder auch "flachgeklopft"). Damit erhalten wir also eine Sequenz von
  gespielten Karten (eine Sequenz von Maps).


* `comp` ist eine __Funktion__, die __eine Funktion erzeugt__!
  D.h. das Ergebnis von `comp` ist eine Funktion oder ein
  Funktionsobjekt. `comp` ("compose") liefert eine 1-Arity-Funktion,
  die die angegebenen Funktionen der Reihe nach auf das Argument
  anwenden und als Ergebnis das Funktionsergebnis der letzten Funktion
  liefert. Wichtig: die Funktionen werden "von rechts nach links"
  angewendet.

  Beispiel: `((comp h g f) x)` entspricht `(h (g (f x)))` oder auch
  `(-> (f x) g h)` bzw. `(-> x f g h)`.

  Hier erzeugen wir eine Funktion, die erst via `:karte` die Karte aus
  der gespielten Karte holt und darauf dann `karten->punkte` anwendet.


* Mit `map` bilden wir die Sequenz von gespielten Karten auf die
  Sequenz der zugehörigen Punkte ab.


* `apply` wendet eine Funktion (erstes Argument) auf die Elemente des
  zweiten Arguments an. Dabei stellt `apply` die Elemente aber an die
  Argumentpositionen der aufgerufenen Funktion. D.h. wir übergeben
  `apply` eine Sequenz mit n Werten und `apply` ruft die Funktion mit
  n Argumenten auf. Das ist was komplett anderes, als die Funktion mit
  einem Sequenz-Argument mit n Elementen aufzurufen!

* `+` ist eine Funktion, die beliebig viele Zahl-Argumente
  addiert. `(+)` ergibt `0`, `(+ 4)` ergibt `4` und `(+ 3 2 1)` ergibt
  `6`.

Mit `apply` können wir also auf einen Schlag (ohne aggregierende
Schleife; vgl. oben) alle gemappten Punktwerte addieren und als
Ergebnis liefern. `(apply + [1 2 3])` ist das gleiche wie `(+ 1 2 3)`.

Fertig.

__Anmerkung__: Clojure-Programme haben schon dadurch weniger Fehler,
  dass es fast unmöglich ist, in Schleifen Off-by-One-Fehler [1] zu
  programmieren. Einfach weil man kaum Schleifen zu programmieren
  braucht und wenn, dann haben sie keine __Schleifen-Varianten__,
  gegen die man ein __Abbruchkriterium__ prüft, sondern man arbeitet
  fast immer auf der __Sequenz-Abstraktion__ und mit den
  API-Funktionen darauf.

[1] https://de.wikipedia.org/wiki/Off-by-one-Error

---

    (defn punkte [stiche]
      (->> (flatten stiche)
           (map (comp karten->punkte :karte))
           (apply +)))

---

__TODO__: `rang-liste`

---

	(defn rang-liste [s-map]
	  (->> s-map
		   (map
			(fn [[s {stiche :stiche}]]
			  {:spieler s :punkte (punkte stiche)}))
		   (group-by :punkte)
		   (map (fn [[p xs]]
				  [p (->> xs (map :spieler) (into #{}))]))
		   (sort-by first)))

---

---

	(defn gewinnt [s-map]
	  (->> (rang-liste s-map)
		   first
		   second))

	(defn geben! []
	  (->> (keys karten->punkte)
		   shuffle
		   (partition 13)
		   (mapv #(-> [%1 {:hand (into #{} %2)}]) spieler)
		   (into {})))

	(defn legt [{:keys [hand tisch]}]
	  (if (empty? tisch)                 
		(or (hand [:kreuz 2])            
			(-> hand shuffle first))     
		(if-let [k (-> (filter #(= (-> (first tisch) :karte farbe) (farbe %)) hand)
					   shuffle
					   first)]           
		  k                              
		  (-> (shuffle hand) first))))   

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

	(defn -main [& args]
	  (spiel))

