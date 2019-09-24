# Hearts

Dies ist eine Implementierung des Spiels _Hearts_ [1] in Clojure
[2]. Ich habe nicht alle Regeln des Spiels implementiert. Angeregt hat
mich ein Artikel im Java Magazin 2019/09 [3], in dem eine
Implementation in Haskell vorgestellt wurde.

Um den Code laufen zu lassen, brauchst du erstmal ein __Java 8 JDK__
(eine JRE sollte auch reichen).

Dann braucht man __Clojure__ [4, 5]. Alles was man benötigt, ist die
eine JAR Datei. Du kannst sie über deinen Browser runterladen oder
auch per `wget`:

	wget https://repo1.maven.org/maven2/org/clojure/clojure/1.8.0/clojure-1.8.0.jar

Und schließlich noch den __Quelltext__. Du kannst entweder das git
Repo clonen oder downloaden oder einfach nur die eine Datei
runterladen:

    wget https://raw.githubusercontent.com/henrik42/hearts/master/src/hearts/core.clj

Nun kannst du das Programm starten:

	java -jar clojure-1.8.0.jar -i core.clj -e '(hearts.core/spiel)'

Du kannst auch eine __interaktive REPL__ [7] starten und dann Dinge
ausprobieren, während du die folgende Beschreibung durchliest:

    $ java -jar clojure-1.8.0.jar -i core.clj -e "(in-ns 'hearts.core)" -r
    #object[clojure.lang.Namespace 0xc430e6c "hearts.core"]
    hearts.core=> spieler
    [:gabi :peter :paul :sonja]
    hearts.core=> ^D

Über [11] kannst du dich auch über deinen Browser mit einer REPL
verbinden und über ein Web-Interfaces mit dieser interagieren.

Du kannst Clojure bzw. eine REPL aber auch __in__ __deinem__
__Browser__ __ausführen__ [9]. Es gibt nämlich einen
Clojurescript->JavaScript Transpiler, der in deinem Browser läuft und
aus Clojurescript [10] JavaScript macht und das läuft dann in deinem
Browser. So brauchst du weder JDK/Java noch den Code auf deinem
Rechner.

__Hinweis:__ um in [9] den Code aus den folgenden Beispielen via
_copy_ _&_ _paste_ einzufügen, musst du in der GUI über das
Tastatur-Symbol den "input mode" auf `none` oder `indent-mode`
setzen. `paren-mode` funktioniert nicht, weil er dazu führt, dass am
Zeilenende automatisch die schließenden Klamern zugefügt werden und
das führt dann wiederum zu Syntaxfehlern.

Falls du mehr über die Möglichkeiten wissen möchtest, wie man Clojure
Programme bauen und ausführen kann, findest du ein paar Hinweise in [8].

[1] https://de.wikipedia.org/wiki/Hearts  
[2] https://clojure.org/  
[3] https://kiosk.entwickler.de/java-magazin/java-magazin-9-2019/hearts-ist-trumpf/  
[4] https://clojure.org/community/downloads  
[5] https://repo1.maven.org/maven2/org/clojure/clojure/1.8.0/clojure-1.8.0.jar  
[6] https://raw.githubusercontent.com/henrik42/hearts/master/src/hearts/core.clj  
[7] https://clojure.org/guides/repl/introduction  
[8] https://github.com/henrik42/solo  
[9] https://clojurescript.io/  
[10] https://clojurescript.org/  
[11] https://repl.it/languages/clojure

-----------------------------------------------------------------------

# Spielregeln

Ich versuche, die Regeln des Spiels möglichst knapp zu formulieren und
dabei auch schon Ausdrücke zu benutzen, die sich später im Code
wiederfinden. So entwickelt sich eine _ubiquitous language_
("allgegenwärtige Sprache"), in der wir uns über unsere Domäne
unterhalten können.

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

Clojure ist eine __funktionale__ __Programmiersprache__. Was das genau
bedeutet, kann ich leider nicht aufschreiben. Erwähnt werden sollte
aber, dass funktionale Programmiersprachen durchaus auch Aspekte der
__Objekt-orientierten__ __Programmiersprachen__ enthalten. Die beiden
also kein echter Gegensatz sind. Somit sind "das Gegenteil" zu
funktionalen Programmiersprachen am ehesten die __imperativen__
Programmiersprachen.

Clojure-Code ist in _Namespaces_ (__Namensräumen__) organisiert
(ähnlich wie Packages in Java). I.d.R. entspricht jeder Namensraum
einer __Datei__. Diese Datei muss in einem __Verzeichnis__ liegen,
dessen Name zum Namensraum "passt". Der Namesraum `hearts.core` findet
sich in der Datei `src/hearts/core.clj`. Die Dateien und Namensräume
bilden eine __hierarchische__ __Struktur__. Diese __Hierarchie__ ist
für Clojure jedoch ohne Bedeutung. Sie dient allein der Strukturierung
der Code-Basis und hat keine Auswirkung auf Sichtbarkeit oder
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
auf die restlichen (Auswertungs-)Werte (__Argumente__).

Es gibt einige __Sonderfälle__, die eine andere Auswertungsregel
haben. Und es gibt auch Funktoren, die __Seiteneffekte__ haben, also
im funktionalen Sinn nicht __pure__ sind.

Da Listen sowohl für "ausführbaren Code" als auch als Datenstruktur
verwendet werden (sog. Homoiconicity; "code is data is code"), fällt
es Clojure-Neulingen zu Beginn häufig schwer, zu erkennen, ob eine
S-Expression/Form nun als "ausführbarer Code" oder als Daten-Wert
gilt.

Mit `ns` wird das Makro (mehr zu Makros weiter unten)
`clojure.core/ns` benannt. Dieses Makro sorgt dafür, dass der
angegebene Namensraum "aufgemacht" wird. Das Makro kann noch eine
Menge mehr, wie z.B. andere Namensräume "importieren", aber das
brauchen wir hier nicht.

	(ns hearts.core)

---

`def` (`clojure.core/def`; auch ein Makro) bindet einen __Wert__ (hier
einen String) an einen __Namen__ (hier durch Symbol `hr` angegeben) im
__aktuellen__ __Namensraum__. Die Form `hr` wird also in diesem Fall
__nicht__ zu ihrem gebundenen Wert __ausgewertet__ (vgl. oben),
sondern sie wird als __Name__ für die Bindung verwendet (so ähnlich
wie ein __lvalue__ -- sprich "el-value" -- in Java [1]).

Es handelt sich um einen "Java String" vom Typ
`java.lang.String`. Clojure übernimmt komplett die `java.lang`
Datentypen, anders als z.B. Jython, das mit Wrapper-Datentypen
arbeitet. Dadurch lässt sich Clojure sehr elegant mit anderen Java
Klassen/Bibliotheken integrieren; sog. "Java interop" [2].

Als Java-Entwickler denkt man bei diesen "globalen Namen" vielleicht
an __Variablen__. Es ist jedoch unüblich (wenn auch möglich), während
eines Programmlaufs einen Namen nacheinander bzw. __wiederholt__ an
(verschiedene) Werte zu binden (was in einem gewissen Sinne der
Wertzuweisung an eine Variable in Java entspricht; tatsächlich ist es
aber völlig anders, aber das soll hier nicht im Detail erläutert
werden). Diese Bindungen sind also eher wie `static final` Felder in
Java zu verwenden (aber wie schon gesagt: es ist völlig anders!).

[1] https://docs.oracle.com/cd/E19798-01/821-1841/bnahv/index.html  
[2] https://clojure.org/reference/java_interop  

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

  Diese zu Mengen zugehörige Arity-1-__Lookup-Funktion__ bildet die
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
  Funktionsobjekt. `comp` ("compose") liefert eine Arity-1-Funktion,
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

`rang-liste` soll eine nach Punkten aufsteigend geordnete
Liste/Sequenz von Spielern liefern. Da es auch einen Punktegleichstand
geben kann, sind es Mengen von Spielern.

* `s-map` ist wieder eine Alle-Spieler-Map. `rang-liste` wird nur
  einmal pro Spiel aufgerufen, nämlich wenn nach der letzten Runde die
  Stiche der Spieler ausgewertet werden, um festzustellen, wer
  gewonnen hat.


* via `map` bilden wir die sequenzialisierte Alle-Spieler-Map (also
  eine Sequenz von __&lt;Spieler,Ein-Spieler-Map>__-Tupel) auf eine
  Sequenz von Maps mit den Schlüsseln `:spieler` und `:punkte`.

  An dieser Stelle hätten wir auch einfach ein
  __&lt;Spieler,Punkte>__-Tupel anstatt der Map nehmen können (genau so,
  wie wir unsere Karten ja als __&lt;Farbe,Bild>__-Tupel repräsentieren,
  anstatt als Map mit `:farbe` und `:bild`. Das ist einfach eine
  Designentscheidung.


* `group-by` ist eine HOF, die die Elemente der Sequenz (zweites
  Argument) nach den Funktionswerten "gruppiert", die sich durch
  Anwendung der Funktion (erstes Argument) auf die Elemente
  ergibt. Das Ergebnis von `group-by` ist eine Map
  __&lt;Gruppierungs-Wert-->Gruppen-Menge>__.

  Hier ist `:punkte` die Funktion, durch die die Gruppierung nach den
  Punkten erfolgt.


* via `map` erzeugen wir eine Sequenz von
  __&lt;Punkte,Spieler-Menge>__-Tupel.


* und diese sortieren wir nach dem ersten (`first`) Element dieser
  Tupel: also den Punkten.


`rang-liste` liefert also eine nach Punkten aufsteigend sortierte
Sequenz von __&lt;Punkte,Spieler-Menge>__-Tupel.

Fertig.

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

`gewinnt` liefert zu der Alle-Spieler-Map den bzw. die Gewinner. Hier
wird die `rang-liste` Funktion aufgerufen und dann auf das erste
Element der Sequenz (also jene Gruppe mit den niedrigsten Punkten) und
dann auf das zweite Element (also die Spieler-Menge) zugegriffen.

Das Ergebnis ist also eine Menge mit den Gewinnern (Menge von
Keywords).

---

    (defn gewinnt [s-map]
      (->> (rang-liste s-map)
           first
           second))

---

Bis jetzt haben wir nur "einfache Berechnungsregeln" implementiert. Es
wurde noch gar nicht "gespielt". Diese Funktionen kommen jetzt.

Als erstes wird __gegeben__.

`geben!` ist eine "impure function". Sie liefert nämlich nicht immer
bei gleicher Eingabe das gleiche Ergebnis (tatsächlich hat die
Funktion gar keinen Parameter und somit auch keine "Eingabe"). Ein
weiterer Umstand, der eine Funktion "impure" macht ist, wenn eine
Funktion "Seiteneffekte" hat --- also z.B. eine globale Variable
ändert (das kann man in Clojure machen, somit ist Clojure auch keine
"reine funktionale Programmiersprache").

In Clojure ist es üblich, für solche Funktionen einen Namen mit einem
`!` am Ende zu verwenden (ist also ein "hallo wach!" für den
Leser). Aber dabei handelt es sich __nur__ __um__ __eine__
__Konvention__. Ansonsten hat das `!` im Namen keine besondere
Bedeutung.


* `keys` liefert die Sequenz mit den Schlüsslen der `karten->punkte`
  Map. Also die Karten.


* `shuffle` ist eine Zufalls-Misch-Funktion.


* `partition` ist das Gegenstück zu `flatten`: es macht aus einer
  Sequenz von Elementen, eine Sequenz von Sequenzen von Elementen. Die
  "inneren" Sequenzen haben hier alle die Länge 13. Wir haben damit
  also 4x jeweils 13 Karten.


* die HOF `map` kann nicht nur auf eine Sequenz sondern auch auf
  mehrere Sequenzen angewendet werden. In dem Fall wendet sie die
  Funktion (erstes Argument) auf die jeweils ersten Elemente mehrere
  Sequenzen an (als n Argumente) und dann die jeweils zweiten usw.

  Hier wird also die Funktion auf `spieler` und die aufgeteilten
  Karten-Partition angewendet.


* die Arity-2-Funktion liefert ein Tupel mit `%1` (dem Spieler; erstes
  Argument) und einer Map mit dem Schlüssel `:hand` und (via `into`)
  dem Wert "Karten-Menge" (`%2` ist ja das zweite Argument, das von
  `map` mit den Elementen aus der Karten-Partition bestückt wird).

  Diese Map ist die anfängliche __Ein-Spieler-Map__. Sie hat nur den
  Schlüssel `:hand`. In `rang-liste` wird auf den Schlüssel `:stiche`
  der Ein-Spieler-Map zugegriffen. Ich habe mich dazu entschlossen,
  die `:stiche` hier an dieser Stelle nicht zu
  "initialisieren". Später werden die `:stiche` zu der Ein-Spieler-Map
  zugefügt werden, ohne dass wir dafür jetzt schon einen "Leereintrag"
  bräuchten. Das kann man so machen, muss man aber nicht. Es ist eine
  Designentscheidung.

* durch `into` in eine Map wird aus der
  __<Spieler,Ein-Spieler-Map>__-Sequenz die __Alle-Spieler-Map__.


`geben!` liefert also die Alle-Spieler-Map, die "den Zustand der
Spieler zu Spielbeginn" repräsentiert.

---

    (defn geben! []
      (->> (keys karten->punkte)
           shuffle
           (partition 13)
           (map #(-> [%1 {:hand (into #{} %2)}]) spieler)
           (into {})))

---

`legt` liefert jene Karte, die ein Spieler auf den Tisch legt, wenn er
an der Reihe ist. `legt` ist eine Arity-1-Funktion, die als
__einziges__ __Argument__ eine __Map__ mit den Schlüsseln `:hand` und
`:tisch` erwartet. Die inhaltliche Zuordnung der Argument-Teil-Werte
(also die Werte der Map) zu den Namen in der Funktion (also `hand` und
`tisch`) erfolgt (via Destructuring) über die (__benannten__)
Schlüssel.

  Wir hätten die Funktion auch als Arity-2-Funktion mit
  `[hand tisch]`-Parametern implementieren können. In diesem Fall
  würde man von __positional__ __parameters__ sprechen. D.h. die
  Zuordnung der __Argumente__ zu den formalen __Parametern__ der
  Funktion erfolgt aufgrund der __Reihenfolge__, in der die
  __Argumente__ __angegeben__ sind. Also z.B. `(legt <hand>
  <tisch>)`. So macht man es z.B. auch in Java.
  
  __Positional Parameter__ haben aber den Nachteil, dass man an der
  __Aufrufstelle__ nicht direkt sieht, dass das erste Argument "die
  Hand" ist (bzw. sein muss) und das zweite Argument "der Tisch". Und
  da Clojure aufgrund der __fehlenden__ __Typinformation__ das auch
  nicht weiß bzw. prüfen kann, ist es durchaus möglich, dass jemand
  die Argumente in der Reihenfolge versehentlich __vertauscht__.

  Wenn wir aber eine Map als Argument haben, dann sieht die
  Aufrufstelle eher so aus: `(legt {:tisch <tisch> :hand <hand>})`. In
  diesem Fall sind die __Argumente__ an der Aufrufstelle __benannt__,
  daher spricht man auch von __named parameters__.

  __Anmerkung__: von __named parameter__ spricht man eigentlich, wenn
  die Funktion so aufgerufen würde: `(legt :tisch <tisch> :hand
  <hand>)` (also nicht mit __einer__ __Map__ sondern mit einer Folge
  von Name-Wert-Paaren). __Quizfrage__: wie müssen die Parameter in
  der Funktionsdefinition definiert werden, damit die Angabe an der
  Aufrufstelle wie gezeigt mit mehreren Argumenten erfolgt? Hinweis:
  die Lösung ist eine Kombination auf Vektor- und Map-Destructuring.

  Das Problem, dass __positional parameters__ aufgrund von
  Verwechslungen mit falschen Werten bestückt werden, kennt man auch
  in Java, wenn man z.B. mehrere `null` Werte als Argument
  angibt. Denn in diesem Fall kann auch der Comiler nicht mehr helfen,
  weil `null` ja ein untypisierter Wert ist.

  Frage: was macht der folgende Aufruf? `AdressenService.verlegeBeginn(1, 911, null, true, null)`.
  Und hätte es nicht `AdressenService.verlegeBeginn(911, 1, null, null, true)`
  heißen müssen? Wer weiß. Der Compiler hilft eben nur
  bedingt. Natürlich kann man auch __benannt__ Parameter mit falschen
  Werten belegen, aber man bekommt zumindest beim Lesen eine Ahnung
  davon, was der Code wohl tuen wird bzw. tuen soll.

  Aber __positional parameters__ haben auch ihre Stärken. In der
  Funktionalen Programmierung und auch in Clojure benutzt man
  "partielle Funktionsauswertung" (sowas ähnliches wie __currying__
  [1, 2]; z.B. `(partial str "LOG:")`), um aus Funktionen neue
  Funktionen zu erzeugen, deren "ersten n" Parameter schon mit
  Argumenten besetzt sind. Aus diesem Grund verwenden die Funktionen,
  die in `clojure.core` geliefert werden, wohl alle __positional
  parameter__, damit man sie eben zusammen mit `partial` verwenden
  kann. Außerdem ist die Gefahr der Verwechslung bei 1 oder 2
  Parametern noch nicht so groß wie bei 4 oder 5.

Aber jetzt zum Code:

* `hand` ist eine Menge von Karten-Vektoren (also die Karten, die der
  legende Spieler auf der Hand hat) und `tisch` ist eine Liste/Sequenz
  von "bisher in diese Reihenfolge auf den Tisch gelegten Karten"
  (eine Map mit `:spieler` und `:karte`).

* `if` wertet das erste Argument aus und falls dieses _truthy_ ist,
  liefert `if` als Ergebnis das Ergebnis der Auswertung des zweiten
  Arguments ("then-Fall"). Ansonsten liefert `if` das Ergebnis der
  Auswertung des dritten Elements ("else"-Fall). Das dritte Argument
  ist __optional__. Falls es nicht angegeben ist, entspricht das dem
  Wert `nil`. Es werden also immer nur jene zwei Argumente von `if`
  ausgewertet, die nötig sind, um das Ergebnis liefern/berechnen zu
  können.

* `empty?` liefert _truthy_, falls das Argument "leer ist" (`nil` gilt
  auch als leer). Wenn der Tisch noch leer ist, muss ja entweder mit
  der Kreuz-2 eröffnet werden oder es kann eine beliebige Karte gelegt
  werden. Insbesondere braucht zu Beginn, wenn der Tisch noch leer
  ist, keine Farbe __bedient__ zu werden.

* `or` (ein Makro) liefert das erste seiner n (ausgewerteten)
  __Argumente__(!!!), das _truthy_ ist. Die Argumente werden der Reihe
  nach ausgewertet, bis ein _truthy_ Wert vorliegt und dieser wird
  dann geliefert. Nochmal zu Erinnerung: die "normale" Auswertung
  läuft so an, dass __erst__ __alle__ Argumente/Elemente einer Liste
  ausgewertet werden und __dann__ wird der Funktor mit eben diesen
  Werten aufgerufen. Bei `or` soll aber die Auswertung nur erfolgen,
  falls alle "links daneben stehenden Argument" _falsy_ sind. Mit
  dieser Auswertungssemantik kann man z.B. auch `null`-Checks
  implementieren.

* `(hand [:kreuz 2])` liefert `[:kreuz 2]`, falls diese Karte in der
  Menge `hand` ist (der Spieler also mit dieser Karte eröffnen
  __muss__) und `(-> hand shuffle first)` liefert die erste Karte der
  "gemischten Hand" --- also eine __beliebige__, zufällig aus der Hand
  gewählten Karte.

  An dieser Stelle merkt man, dass unsere Spieler nicht sonderlich
  "intelligent" handeln. Denn diese "zufällige Legestrategie" ist
  suboptimal. Falls es dir Spaß macht, kannst du an dieser Stelle ja
  eine "schlauere" Strategie implementieren.

  Ein "echter" (d.h. menschlicher) Spieler würde auf alle Fälle auch
  die bisherigen Stiche aller Spieler (nicht nur der eigenen!) in
  seine Entscheidung einbeziehen, so dass die Funktion wohl eher als
  `(defn legt [{:keys [hand tisch stiche-aller-spieler]}] ...)`
  definiert sein sollte.

  Der "then"-Zweig für den leeren Tisch liefert also entweder die
  Kreuz-2 oder eine beliebige Karte des eröffnenden Spielers.

* `if-let` ist eine Kombination aus `let` und `if`: mit `let` wird ein
  __lokaler__ (geschachtelter; _nested_) __Namensraum__ aufgemacht, in
  dem Werte an Namen gebunden werden. Diese Name-Wert-Paare stehen in
  einem Vektor (_binding vector_). Ein `if-let` hat immer genau ein
  Name-Wert-Paar.

  Der Wert-Ausdruck/Form hinter dem lokalen Namen wird ausgewertet und
  __falls__ er _truthy_ ist, wird er an den __Namen__ __gebunden__ und
  das `if-let`-Ergebnis ist der Wert des folgenden
  "then"-Ausdrucks/Zweig. In diesem Zweig kann man über den lokalen
  Namen auch auf den zuvor gebundenen Wert zugreifen.

  Andernfalls wird der __Namen__ __nicht__ __gebunden__ und das
  `if-let`-Ergebnis ergibt sich aus dem Wert des "else"-Zweiges.

* mit der HOF `filter` erzeugen wir eine Sequenz von Karten (`hand`),
  deren `(farbe %)` der Farbe der ersten Karte auf dem Tisch (`(->
  tisch first :karte farbe)`) entspricht (`=`). Das sind also alle
  Karten, mit denen man die Eröffnungskarte __bedienen__ könnte (und
  dann ja auch __müsste__). Von denen währen wir uns wieder eine
  beliebige aus (`shuffle first`).

  __Anmerkung__: in `#(...)` Formen kann man `%<i>` benutzen, um die
  Argumente über ihre 1-basierte Position zu "adressieren"
  (vgl. oben). Wenn man einfach nur `%` verwendet, so "zählt" Clojure
  die Indexe selbständig hoch. D.h. die erste `%`-Form wird zu `%1`,
  die zweite zu `%2` usw. 

* Falls es nun eine solche Karte gibt (also `k` _truthy_ ist), liefern
  wir diese Karte.

* Ansonsten liefern wir eine beliebige Karte (wie in dem
  Leerer-Tisch-Fall).

Fertig.

__Aufgabe__: Könnte man das `if-let` durch ein `or` ersetzen?

[1] https://de.wikipedia.org/wiki/Currying  
[2] https://practicalli.github.io/clojure/thinking-functionally/partial-functions.html  

---

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

---

`runde` bekommt als __benannte__ Argumente die Alle-Spieler-Map
`:spieler`, den Spieler `:beginnt`, der diese Runde beginnt und die
Zahl, welche Runde gerade gespielt wird. Diese Zahl brauchen wir aber
nur für eine Ausgabe, die wir während des Spiels erzeugen möchten. Für
die Spiellogik ist `:runde` völlig irrelevent.

`runde` liefert als Ergebnis eine Map mit der (aktualisierten ---
jemand hat ja den Stich bekommen) Alle-Spieler-Map (`:spieler`) und
der Angabe, welcher Spieler (Keyword) diese Runde __sticht__
(`:sticht`). Falls aber gar keine Runde mehr gespielt wird (weil die
Spieler keine Karte mehr auf der Hand haben; vgl. unten), liefert
`runde` den Wert `nil`.

* mit `(-> s first second :hand empty?)` wird geprüft, ob der erste
  Spieler überhaupt noch eine Karte auf der Hand hat. Damit prüfen wir
  einfach, ob die letzte Runde bereits in der Runde zuvor gespielt
  wurde, denn dann wird _diese_ Runde nicht mehr gespielt. Wir können
  hier einfach nur den ersten Spieler betrachten, weil alle Spieler
  gleich viele Karten auf der Hand haben.

* falls dem __nicht__ so ist (`when-not`), ermitteln wir die
  Reihenfolge, in der die Spieler in dieser Runde ihre Karten legen
  müssen. Das machen wir, indem wir die Liste der `spieler`
  __zweimal__ hintereinander zusammenfügen (`(concat spieler
  spieler)`) und diese Liste dann von Beginn an durchlaufen und alle
  Elemente __löschen__ (bzw. vernachlässigen), die nicht der
  `:beginnt` Spieler sind (`(drop-while #(not= b %))`). Damit haben
  wir dann eine Liste/Sequenz, die mit dem `:beginnt` Spieler
  __beginnt__ (denn an genau der Stelle haben wir mit dem
  Vernachlässigen ja aufgehört) und die __folgenden__ __drei__
  __Elemente__ (also insgesamt `(take 4)`) sind die Spieler in jener
  Reihenfolge, in der sie ausspielen/legen müssen.

* falls `when-not` also _truthy_ ist (nämlich die Liste der Spieler),
  wird diese Liste via `when-let` an den lokalen Namen `xs`
  (gesprochen "ix-es") gebunden und es geht mit dem "then"-Zweig von
  `when-let` weiter. Andernfalls (wenn also keine Runde mehr gespielt
  wird) liefert die Funktion `runde` den Wert `nil`.

  __Anmerkung__: `when` und `when-let` unterscheiden sich von `if`
  auch dadurch, dass sie nicht nur __eine__ "then"-Form haben, sondern
  sie können __beliebig__ __viele__ solcher Formen/Argumente
  haben. Der Wert des `when` Ausdrucks ist im _truthy_-Fall dann der
  Wert der __letzen__ __Form__.

  In einer __rein__ __funktionalen__ Programmiersprache macht sowas
  keinen Sinn, denn was sollten schon die Formen/Ausdrücke tun, die
  vor der letzten Form stehen? Sie können ja __nichts__ __zum__
  __Rückgabewergebnis__ __beitragen__. In Clojure gibt es aber
  Ausdrücke, die __Seiteneffekte__ haben, wie z.B. die Ausgabe nach
  STDOUT. Dadurch machen solche "Multi-Form-When-Ausdrücke" doch
  Sinn. Wir nutzen hier die Möglichkeit, `(when-let [...] <form-1> <form-2>)`  
  schreiben zu können, um so via `print` Ausgaben auf STDOUT zu
  erzeugen.

* die zweite Form von `when-let` ist `loop`. Bisher haben wir `reduce`
  verwendet, um __iterative__ __Berechnungen__ durchzuführen. In Java
  hätte man dafür `for` oder `while` Schleifen verwenden. `loop`
  bietet auch die Möglichkeit, eine Aggregation durchzuführen, jedoch
  kann man auf einfach Weise am "Ende der Schleife" noch einen
  Verarbeitungsschritt einbauen. Und genau das machen wir unten.

  `loop` eröffnet auch einen __lokalen__ __Namensraum__ (wie `let` und
  `when-let`) und bindet Wert an lokale Namen. Der _binding_ _vector_
  besteht aus Paaren von `<lokaler-name> <init-wert>`. Diese Bindung
  (inkl. Destructuring) wird einmalig (zu Beginn der Schleife)
  hergestellt.

  Hier wird `x` an das erste Element von `xs` gebunden und `xr` an den
  _tail_ (Rest) von `xs`. Der lokale Name `s` wird an den Wert von `s`
  gebunden.

  __Achtung__: es handelt sich hierbei um zwei __verschiedene__
  Name. Das linke `s` ist in der `loop` gebunden, das rechte `s` ist
  in `runde` gebunden und hier wird __der__ __Wert__ von diesem Namen
  an das `s` in `loop` gebunden. Innerhalb von `loop` "sieht" man mit
  `s` immer nur das "innere" `s`. Andere Namen, die in `runde`
  gebunden sind (wie z.B. `r`) können auch in `loop` gesehen
  werden. Das "innere" `s` _verschattet_ also das äußere `s`.

  `tisch` wird mit einem leeren Vektor gebunden.

  Die Bindung im _binding_ _vector_ ist die Bindung der Namen für den
  ersten Schleifendurchlauf. Die Schleife wird jedoch mehrfach
  durchlaufen (wie das passiert, wird gleich gezeigt). Bei diesen
  folgenden Schleifendurchläufen sind die Namen dann an andere Werte
  gebunden.

  Der Name `x` wird in jedem Schleifendurchlauf an den Namen
  desjenigen Spielers gebunden, der gerade legen muss. Wenn `x`
  _falsy_ ist (also alle Spieler gelegt haben), ist die Runde
  "fertig". Diesen Fall erkennen wir via `(if-not x ...)`.

  Am Ende der Runde müssen wir via `(sticht tisch)` ermitteln, wer
  diese Runde sticht und wir "geben" die Karten auf dem `tisch` eben
  diesem Spieler `b` in seine `:stiche` (sein Haufen). Das machen wir
  dadurch, dass wir die Alle-Spieler-Map "updaten" (natürlich wird
  eine neue Map erzeugt!), und zwar in dem Eintrag/Wert, der über `b`
  und `:stiche` referenziert wird.

  Dies ist zu Beginn der Wert `nil`! Wir haben in `geben!` nur den
  `:hand`-Eintrag der Ein-Spieler-Map gesetzt, nicht aber den
  `:stiche`-Eintrag. Mit `(fnil conj [])` erzeugen wir eine Funktion,
  die das erste Argument durch `[]` ersetzt, falls es `nil` ist und
  sich ansonsten wie `conj` verhält. Dadurch schafft man eine
  Funktion, die ihre eigene Initialisierung (nämlich ein leerer
  Vektor) durchführt. `conj` (_conjoin_) fügt ein Element zu einer
  Collection. 

  `runde` liefert also eine Map mit dem Spieler `b`, der `:sticht` und
  der Alle-Spieler-Map, in der `b` den `tisch` in `:stiche` (ein
  Vektor) eingesammelt hat. `let` kann genau wie `when-let` mehrere
  Formen auswerten und liefert den Wert der letzten Form.

* falls wir in `x` aber noch einen Spieler haben, der nun legen muss,
  ermitteln wir erst was er auf der `hand` hat und dann welche Karte
  `k` er legen will. Der `tisch` ist entweder noch leer (erster
  Schleifendurchlauf) oder hat schon Karten. Nun fügen wir dem `tisch`
  eine neue "gelegte Karte" mit `:karte k` und `:spieler b` zu (und
  binden diesen Wert an einen neuen lokalen Namen
  `tisch`). Schließlich "updaten" wir noch die `:hand` des Spielers
  `x` und "entziehen" (`disj`) ihm die Karte `k`.

* damit haben wir das "Delta" an unserem Spielzustand
  (Alle-Spieler-Map `s` und dem `tisch`) berechnet und können nun mit
  dem nächsten Spieler in dieser Runde fortfahren.

  Dazu rufen wir die `loop` Schleife via `recur` mit den neuen
  Argumentwerten für die `loop`-Bindungen auf: also den verbleibenden
  Spielern `xs`, der neuen Alle-Spieler-Map `s` und dem aktualisierten
  `tisch`.

  Der Aufruf sieht so aus, als wenn hier eine "Rekursion" erfolgen
  würde. Und Rekursion "kostet Stack-Space" und führt irgendwann zum
  Stackoverflow. In Funktionalen Programmiersprachen wird aber häufig
  eine Optimierung durchgeführt, die sich __Endrekursion__
  nennt. Endrekursion führt zu einem __iterativen__
  __Berechnungsprozess__ (nicht zu einem __rekurisven__) und dieser
  benötigt __keinen__ __Stackspace__. `recur` ist so ein
  __endrekrsiver__ __Rekursionsaufruf__. Er kann nur erfolgen, wenn
  die `recur`-S-Expression die letzte ist, die ausgewertet
  wird. Andernfalls kommmt es zu einem Compile-Fehler.

Fertig.

__Anmerkung__: in imperativen Programmiersprachen kann man
versehentlich Endlosschleifen programmieren, weil man das
Abbruchkriterium nicht richtig formuliert. Das kann in funktionalen
Programmiersprachen auch passieren. Die `loop` Schleife unten
terminiert garantiert, weil wir von `xs` immer ein Element
__entfernen__ (und `xs` zu Beginn vier Elemente hat) und damit
schließlich in einen Zustand kommen, in dem `x` _falsy_ (hier `nil`)
ist. Und damit laufen wir nicht mehr in das `recur`.

---

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

---

`defn` unterstützt auch die Definition von mehreren Arities. `spiel`
hat die Arity-0 und die Arity-1. Die Arity-0-Variante ruft die
Arity-1-Variante mit `(spiel (geben!))` auf. D.h. die Arity-1-Variante
hat als Argument `gegeben` die Alle-Spieler-Map zu Spielbeginn nach
dem Geben. Diese Variante habe ich eingeführt, um die Funktion besser
testen zu können.

Auch das `spiel` läuft in Schleifen, nämlich über die Runden. `s` wird
zu Beginn an die Alle-Spieler-Map `gegeben` gebunden, `b` an den
Spieler, der `beginnt` und der Rundenzähler `r` bekommt den Wert `1`.

* `if-let` wird benutzt, um die `runde` mit dem aktuellen Spielzustand
  aufzurufen. Falls `runde` _truthy_ ist, werden (die lokalen Namen
  von `if-let`) `s` und `b` an die neuen Werte aus der `runde`
  gebunden und es wird sofort mit diesen Werten via `recur` der
  nächste Schleifendurchlauf gemacht.

  Falls `runde` _falsy_ ist, werden die Namen ja __nicht__ neu
  gebunden, so dass in `let` die Alle-Spieler-Map `s` nach der letzten
  Runde (in `loop` gebunden!) verwendet werden kann, um zu ermitteln,
  wer das Spiel `gewinnt`.

Der Gewinner wird im Ergebnis `erg` als `:gewinnt` geliefert und der
finale Zustand der Spieler mit ihren Stichen wird durch `:spieler` als
Alle-Spieler-Map im Ergebnis geliefert.

Fertig.

---

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

---

Die Funktion `-main` ist der "Programmeinstieg von außen", so wie man
es von Java mit der `static void main(String[] args)` kennt. Die
Funktion hat aber mit dem Spiel an sich nichts zu tun.

---

    (defn -main [& args]
      (spiel))

---
