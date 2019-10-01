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

Einführungen in Clojure findest du natürlich auch massenhaft im NETZ [12].

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
[12] http://hitchhikersclojure.com/blog/hitchhikers-guide-to-clojure/  

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
ausspielen. Es muss also die Farbe der Eröffnungskarte _bedient_
werden. Andernfalls kann er eine beliebige Karte von seiner Hand
spielen (_abwerfen_).

Nachdem alle Spieler ihre Karte in der Runde gespielt haben, erhält
derjenige Spieler die gespielten Karten (den __Stich__), der die Karte
mit der __Farbe der Eröffnungskarte__ mit dem __höchsten Rang__
gespielt hat. Er legt den Stich verdeckt auf seinen __Haufen__. Er
nimmt sie also __nicht__ auf seine __Hand__.

Es kann natürlich vorkommen, dass der eröffnende Spieler selbst den
Stich erhält. Dies ist der Fall, falls alle anderen Spieler
_abgeworfen_ haben oder alle anderen Spieler mit einer Karte bedient
haben, die unter dem Rang der Eröffnungskarte ist.

Damit ist die Runde beendet.

Durch das Erhalten des Stichs sammeln die Spieler über die Runden also
Stiche an, die sie auf ihren __Haufen__ "vor sich auf dem Tisch"
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

Clojure ist eine __funktionale__ __Programmiersprache__ [1]. Was das
genau bedeutet, kann ich leider nicht aufschreiben. Erwähnt werden
sollte aber, dass funktionale Programmiersprachen durchaus auch
Aspekte der __Objekt-orientierten__ __Programmiersprachen__ [2]
enthalten (z.B. Datenkapselung [3]), nur tut sie das mit anderen
Mitteln [4]. Die beiden sind also kein echter Gegensatz .

Somit sind "das Gegenteil" zu funktionalen Programmiersprachen am
ehesten die __imperativen__ Programmiersprachen [5]. In dem
Wikiartikel wird zwar gesagt, dass die "deklarativen
Programmiersprachen" (wie PROLOG) der Gegensatz zu den imperativen
Programmiersprachen seien. Für mich entscheidend ist aber, dass die
imperativen Programmiersprachen im wesentlichen auf Anweisungen
(_Statements_) und __Zustandsänderungen__ (also die Änderung von
Variablen bzw. __Speicherstellen__; von Neumann Rechner) basieren. Die
funktionale Programmierung basiert auf __Funktionen__, __Namen__ und
__Werten__. Das ist ein riesiger Unterschied und wie so ein
funktionales Programm "sich anfühlt", wird hoffentlich durch den
folgenden Text deutlich.

Clojure-Code ist in _Namespaces_ (__Namensräumen__ [6]) organisiert
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
sog. _S-Expressions_ [7]. Dabei handelt es sich um __geschachtelte__
(hierarchische) Klammerausdrücke (__Listen__).

Beispiel: `(str "Hello," "world")`

Die __Elemente__ dieser Listen sind _Forms_ (__Formen__). Es gibt eine
ganze Reihe von Formen (Symbole, _Keywords_, Zahlen, Listen, Vektoren,
Strings, etc.), von denen wir weiter unten einige kennenlernen
werden. Das ist im Prinzip alles, was man zur __Syntax__ von Clojure
sagen kann [8].

Die folgende Liste (erste Codezeile von `core.clj`) hat als erstes
Element das Symbol `ns` und als zweites Element das Symbol
`hearts.core`. Das erste Element einer Liste bezeichnet "etwas
ausführbares" (__Funktor__; z.B. eine Funktion oder ein Makro) und die
folgenden Elemente bilden die __Argumente__ des Funktors.

Die Bedeutung (__Sematik__) der Formen ergibt sich durch Clojures
_Auswertungs-Regeln_ [9]. Die meisten Formen "werten zu sich selbst
aus". Das heißt, dass die __Auswertung__ der __Form__ `2` (Syntax) die
__Zahl__ 2 (Semantik; vom Typ `java.lang.Integer`) ergibt.

Symbole werten zu jenem Wert aus, der an den durch das Symbol
benannten __Namen__ __gebunden__ ist (vgl. unten).

Die Auswertung von Listen erfolgt durch __Auswertung__ der
Listen-Element-Formen (die Listen-Element-Formen können wiederum
Listen sein; __rekursive Auswertung__), wobei das erste Element zu
einem __Funktor__ auswerten __muss__, und der Anwendung des Funktors
auf die restlichen (Auswertungs-)Werte (__Argumente__).

Es gibt einige __Sonderfälle__, die eine andere Auswertungsregel haben
[10]. Und es gibt auch Funktoren, die __Seiteneffekte__ [13] haben,
also im funktionalen Sinn nicht __pure__ sind.

Da Listen sowohl für "ausführbaren Code" als auch als Datenstruktur
verwendet werden (sog. _Homoiconicity_; "code is data is code"
[12, 11]), fällt es Clojure-Neulingen zu Beginn häufig schwer, zu
erkennen, ob eine S-Expression/Form nun als "ausführbarer Code" oder
als Daten-Wert gilt.

Mit `ns` wird das Makro (mehr zu Makros weiter unten)
`clojure.core/ns` benannt. Dieses Makro sorgt dafür, dass der
angegebene Namensraum "aufgemacht" wird. Das Makro kann noch eine
Menge mehr, wie z.B. andere Namensräume "importieren", aber das
brauchen wir hier nicht.

[1] https://de.wikipedia.org/wiki/Funktionale_Programmierung  
[2] https://de.wikipedia.org/wiki/Objektorientierte_Programmierung  
[3] https://de.wikipedia.org/wiki/Datenkapselung_(Programmierung)  
[4] https://de.wikipedia.org/wiki/Closure_(Funktion)  
[5] https://de.wikipedia.org/wiki/Imperative_Programmierung  
[6] https://clojure.org/reference/namespaces  
[7] https://en.wikipedia.org/wiki/S-expression  
[8] https://clojure.org/guides/learn/syntax  
[9] https://clojure.org/reference/evaluation  
[10] https://clojure.org/reference/special_forms  
[11] http://blog.muhuk.com/2014/09/28/is_clojure_homoiconic.html#.XYuwxHtCRhE  
[12] https://de.wikipedia.org/wiki/Homoikonizit%C3%A4t  
[13] https://de.wikipedia.org/wiki/Wirkung_(Informatik)  

---

	(ns hearts.core)

---

`def` (`clojure.core/def` [3]; auch ein Makro) bindet einen __Wert__ (hier
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
(verschiedene) Werte zu binden.

Dies entspricht in einem gewissen Sinne der Wertzuweisung an eine
Variable in Java; tatsächlich ist es aber völlig anders, aber das soll
hier nicht im Detail erläutert werden. Diese Bindungen sind also eher
wie `static final` Felder in Java zu verwenden. Aber wie schon gesagt:
es ist völlig anders! [4]

Wenn man die `def` Zeile in der REPL eingegeben hat (bzw. mit `-i
core.clj` die Datei geladen hat), kann man sich anschließend den
gebundenen Wert ausgeben lassen.

Die REPL liest Formen ein, wertet sie aus und "druckt" das Ergebnis
aus. Die Ausgabe erfolgt in einem Format, in dem diese Ausgabe auch
wieder als __Eingabe__ genutzt werden kann. Daher werden die
Zeilenumbrüche z.B. als `\n` ausgegeben und der ganze String inkl. der
Anführungszeichen ausgegeben. Die Ausgabe erfolgt also im
__Literal__-Format (mit einigen Ausnahmen).

__REPL:__

	hearts.core=> hr
	"\n------------------------------------\n"
	hearts.core=> (type hr)
	java.lang.String
	hearts.core=> (.getClass hr)
	java.lang.String
	hearts.core=> 42
	42
	hearts.core=> (type 42)
	java.lang.Long

[1] https://docs.oracle.com/cd/E19798-01/821-1841/bnahv/index.html  
[2] https://clojure.org/reference/java_interop  
[3] https://clojuredocs.org/clojure.core/def  
[4] https://clojure.org/reference/vars  

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
Interpreter__ sondern einen __Compiler__ [1], der zur __Laufzeit__
läuft; also __just in time__. Es gibt aber auch __ahead of time__
(AOT), wie bei Java, um die Startup-Zeit von Clojure-Programmen zu
verkürzen.

__REPL__:

	hearts.core=> first
	#object[clojure.core$first__4339 0x78997c33 "clojure.core$first__4339@78997c33"]
	hearts.core=> (type first)
	clojure.core$first__4339

Wir führen hier also einfach nur einen __Alias__ für eine Funktion
ein, denn wir binden ja einen __zweiten Namen__ an __denselben(!)__
Wert.

`farbe` ist Teil unser Domänensprache (vgl. oben), anders als `first`,
was eher ein technischer/generischer Name ist. `first` hat in unserer
Domäne keine besondere Bedeutung. Das Gleiche machen wir auch für
`bild` mit der Funktion `clojure.core/second`.

Mit diesen Namen bzw. den an sie gebundenen Funktionen werden wir
später auf unsere Daten, d.h. auf die Farben und die Bilder unserer
Spielkarten, zugreifen.

__REPL__:

	hearts.core=> farbe
	#object[clojure.core$first__4339 0x78997c33 "clojure.core$first__4339@78997c33"]
	hearts.core=> first
	#object[clojure.core$first__4339 0x78997c33 "clojure.core$first__4339@78997c33"]
	hearts.core=> (identical? farbe first)
	true

Mit `first` referenzieren wir `clojure.core/first` __ohne__ den
Namensraum `clojure.core` explizit angeben zu müssen. Das liegt daran,
dass der Namenraum `clojure.core` via `ns` "importiert" [2] wurde und
damit zur Verfügung steht (mehr sagen wir hier nicht zu Namensräumen).

[1] https://clojure.org/reference/compilation  
[2] https://8thlight.com/blog/colin-jones/2010/12/05/clojure-libs-and-namespaces-require-use-import-and-ns.html  

---

    (def farbe first)
	(def bild second)

---

Wir binden einen Vektor [8] mit den Vornamen unserer Spieler an den
Namen `spieler`. Die Reihenfolge der Elemente im Vektor soll
beschreiben, in welcher Reihenfolge unsere Spieler an dem gedachten,
runden Tisch sitzen. Dabei ist unerheblich, welcher Spieler an welcher
Position im Vektor steht. Wie schon gesagt: es ist ein gedachter
__Kreis__.

In Clojure können Vektoren, Listen, Maps, Sets und Reguläre Ausdrücke
direkt als __Literal__ [2] aufgeschrieben werden (und man kann weitere
eigene Literaltypen definieren -- sog. _tagged literals_ [1]). Die
Elemente dieser _Collections_ [4] brauchen nicht vom gleichen Typ zu
sein und die ganzen Datenstrukturen sind __immutable__ (in
Clojure-Sprech _persistent data structures_ [3]). D.h. wir können
ihren Wert(!!)  d.h. Inhalt/Zustand nicht ändern. Daher können wir sie
auch ohne Gefahr mit anderen __teilen__, weil sie niemand
_hinterrücks_ ändern kann. Man braucht also keine "Clone" oder
"defensive Kopien" [5] und keine Synchronisation um Race-Conditions
[6] zu unterbinden.

Für die Vornamen unserer Spieler verwenden wir Clojure __Keywords__
[7]. Diese sind natürlich ebenfalls immutable, genau wie die Java
Datentypen in `java.lang`! Keywords verhalten sich so ähnlich wie Java
Enums. Sie sind z.B. "identisch" und nicht nur "gleich". Man kann sie
jedoch nirgends __vorab__ __definieren__ --- es gibt keine "Klammer"
wie die Enum Klassen in Java. Daher kann man sie auch nicht
enumerieren/aufzählen. Man schreibt sie einfach hin und kann sich
dabei auch verschreiben, ohne dass es der Compiler merkt ....

__REPL__:

	hearts.core=> :foo
	:foo
	hearts.core=> (type :foo)
	clojure.lang.Keyword
	hearts.core=> (identical? :foo (keyword "foo"))
	true
    hearts.core=> spieler
	[:gabi :peter :paul :sonja]
	hearts.core=> (type spieler)
	clojure.lang.PersistentVector

[1] https://clojure.org/reference/reader#tagged_literals  
[2] https://clojure.org/reference/reader#_literals  
[3] https://en.wikipedia.org/wiki/Persistent_data_structure#Clojure  
[4] https://clojure.org/reference/data_structures#Collections  
[5] http://www.javapractices.com/topic/TopicAction.do?Id=15  
[6] https://www.baeldung.com/java-synchronized  
[7] https://clojure.org/reference/data_structures#Keywords  
[8] https://clojure.org/reference/data_structures#Vectors  

---

    (def spieler [:gabi :peter :paul :sonja])

---

`bilder` ist eine Liste [1, 2] mit den Elementen (Zahlen) 2 bis 10 und
den Elementen (Keywords) `:bube`, `:dame`, `:koenig` und `:ass`.

Tatsächlich ist `bilder` eine _Sequenz_ [3], aber dazu später mehr;
man kann sich erstmal eine Liste vorstellen.

__REPL:__

	hearts.core=> bilder
	(2 3 4 5 6 7 8 9 10 :bube :dame :koenig :ass)
	hearts.core=> (type bilder)
	clojure.lang.LazySeq
	hearts.core=> (list? bilder)
	false
	hearts.core=> (seq? bilder)
	true

`(range x y)` liefert eine Liste mit den Zahl-Elementen x bis y-1 und
`concat` verbindet die beiden Listen zu einer Liste.

Die Reihenfolge bzw. die Position der Bilder in der Liste bestimmt
später, wenn es darum geht, wer einen Stich bekommt, welche Karte
einen "höheren Rang hat" als die andere.

__REPL:__

	hearts.core=> (range 3 5)
	(3 4)

[1] https://clojure.org/guides/learn/sequential_colls#_lists  
[2] https://clojure.org/reference/data_structures#Lists  
[3] https://clojure.org/reference/sequences  

---

	(def bilder (concat (range 2 11) [:bube :dame :koenig :ass]))

---

Anstatt später immer wieder die "Position" einer Karte in `bilder` zu
ermitteln (also ihren __Rang__), erzeugen wir __einmalig__ eine
Abbildung `bild->index` (eine `java.util.Map`) von den Bildern auf die
jeweilige Position des Bilds in `bilder`.

__REPL:__

	hearts.core=> (doc instance?)
	-------------------------
	clojure.core/instance?
	([c x])
	  Evaluates x and tests if it is an instance of the class
		c. Returns true or false
	nil
	hearts.core=> (instance? java.util.Map bild->index)
	true

Der Name `bild->index` ist beliebig gewählt, das `->` in dem Namen hat
hier keine besondere Bedeutung in Clojure.

In dieser S-Expression werden eine Reihe weiterer Funktionen
verwendet:


* `->>` (sog. _thread last_) [1] nimmt das erste Element (hier
  `bilder`) und fügt es an die __letzte__ (daher _last_; es gibt auch
  _thread_ _first_ `->`) __Argumentposition__ des zweiten
  Elements. Dadurch entsteht in diesem Fall `(map-indexed #(->
  [%2 %1]) bilder)` und dann diesen Ausdruck/Form wieder an die
  __letzte__ __Argumentposition__ des dritten Elements. Somit ergibt
  sich schließlich:
    
  `(into {} (map-indexed #(-> [%2 %1]) bilder))`  

  Der Code wird also __umgestellt__, __bevor__ er überhaupt
  compiliert/ausgeführt/ausgewertet wird.

  `->>` arbeitet/wirkt auf den __Programmcode__, nicht aber auf den
  __Programmtext__, sondern auf den geparsten Programmtext, der als
  __Datenstruktur/AST__ [3] vorliegt und __formt__ __diese__
  __Datenstruktur__ __um__ (also den AST, der als Listen-Datenstruktur
  vorliegt). Das ist so ähnlich wie "Codegenerierung zur Laufzeit".

  Das nennt man __Meta-Programming__. `->>`ist auch keine Funktion,
  sondern ein __Makro__ [2]. Makros sind "fast" normale Clojure
  Funktionen, die nur eben von Clojure in die __Compilephase__
  eingebunden werden und somit das __zu__ __kompilierende__
  __Programm__ beliebig __umstellen__ können.

  Das ist auch ein Grund dafür, dass man für Clojure keine
  "vorgeschalteten Codegeneratoren" braucht. Man programmiert sich
  seine "Codegenerierung" einfach mit Markos selber [5, 6]. Und zwar
  in der Programmiersprache, mit der man sowieso schon unterwegs
  ist. Keine Template-Sprache oder irgendein anderer
  "Medienbruch". Makros sind also sowas wie eine __interne DSL__ [4]
  für Codegenerierung.

  `->>` erlaubt es, den Code in der __Reihenfolge__
  __hinzuschreiben__, in der er __ausgeführt__ wird: die
  geschachtelten Funktionsaufrufe werden ja von "innen nach außen"
  ausgeführt/ausgewertet. Die Verwendung von `->>` macht es dem
  __Menschen__ leichter zu verstehen, was geschieht. Die Semantik wird
  nicht verändert -- es handelt sich wirklich nur um eine
  __Umstellung__ __des__ __Codes__, bevor er an den Compiler gegeben
  wird.

  Das folgende Beispiel zeigt, wie man sich ausgeben lassen kann, zu
  welchem "Zielausdruck" ein Makro bzw. eine __Makro-Expansion__ (also
  die __Anwendung__ des Makros auf den Programmcode) führt. Das kann
  man auch brauchen, wenn man selber mal ein Makro schreibt.

  __REPL:__

		hearts.core=> (macroexpand '(->> bilder (map-indexed #(-> [%2 %1])) (into {})))
		(into {} (map-indexed (fn* [p1__1436# p2__1435#] (-> [p2__1435# p1__1436#])) bilder))
		hearts.core=> (clojure.walk/macroexpand-all '(->> bilder (map-indexed #(-> [%2 %1])) (into {})))
		(into {} (map-indexed (fn* [p1__1432# p2__1431#] [p2__1431# p1__1432#]) bilder))

* Mit `#(....)` wird eine Funktion definiert. Man sagt auch "anonyme
  Funktion", weil sie an keinen Namen gebunden wird. Die Benennung
  "anonym" ist aber irreführend, weil Funktionen __niemals__ einen
  Namen haben und in dem Sinne __immer__ anonym sind. Durch `def` gibt
  man eben einer Funktion auch __keinen__ __Namen__ sondern __bindet__
  die Funktion an einen __Namen__ und man kann sie eben auch an
  mehrere Namen binden (vgl. oben). In diesem Sinne __besitzt__ eine
  Funktion an sich __keinen__ Namen.

  __REPL:__

		hearts.core=> #(str % "-" %)
		#object[hearts.core$eval1622$fn__1623 0x54ef29c7 "hearts.core$eval1622$fn__1623@54ef29c7"]
		hearts.core=> (#(str %1 "-" %2) "foo" :bar)
		"foo-:bar"

  `#(-> [%2 %1])` ist eine Funktion mit zwei Parametern, die einen
  Vektor liefert, dessen erstes Element das zweite Argument der
  Funktion ist und dessen zweites Element das erste Argument der
  Funktion ist.

  In Clojure gibt es kein `return`-Statement. Stattdessen liefert eine
  Funktion immer den Auswertungswert der "letzten Form". Welche das
  genau ist, besprechen wir im Code.

  `->` ist das _thread first_ Makro, das ich hier verwende, weil der
  Ausdruck `#([%2 %1])` von Clojure so interpretiert wird, als wenn
  der Vektor eine Funktion (Funktor) wäre (vgl. folgende
  __REPL__). `->` tut uns den Gefallen, den Code so umzuformen, dass
  der Vektor als Rückgabewert der anonymen Funktion gilt.

  __REPL:__

		hearts.core=> (clojure.walk/macroexpand-all '#([%2 %1]))
		(fn* [p1__1444# p2__1443#] ([p2__1443# p1__1444#]))
		hearts.core=> (clojure.walk/macroexpand-all '#(-> [%2 %1]))
		(fn* [p1__1448# p2__1447#] [p2__1447# p1__1448#])

* `map-indexed` ist eine HOF (_higher order function_) [7]: sie
  erwartet als erstes Argument eine __Funktion__ und als zweites eine
  Sequenz. Sie wendet dann die Funktion der Reihe nach auf die
  Elemente der Sequenz an.

  Dabei ruft sie die übergebene Funktion mit jeweils zwei Argumenten
  auf: das erste Argument ist der __0-basierte__ __Index__ des
  Elements der Sequenz, das gerade verarbeitet wird und dieser Index
  ist genau das, was wir als "Position" benötigen (vgl. oben).

  Das zweite Argument ist das Element selbst.

  Das Ergebnis von `map-indexed` ist wiederum die Sequenz aus den
  Funktions-Aufruf-Ergebnissen.

  Beispiel: `(map-indexed str [1 2 3])` liefert `("01" "12" "23")`
  (`str` liefert den String/Konkatenation der Argumente). Wir
  verwenden aber `#(-> [%2 %1])` und erzeugen somit für jedes Bild
  (d.h. Element von `bilder`) ein 2-Tupel [Bild, Position].


* `(into {} .....)` sorgt dafür, dass die __Elemente__ des zweiten
  Arguments in die Datenstruktur (erstes Argument) __zugefügt__
  werden.

  Natürlich wird dabei nichts verändert, sondern es wird ein neuer
  Wert des gleichen Typen erzeugt und diese neue Struktur ist dann das
  Ergebnis von `into`.

  Bei `{}` handelt es sich um eine Map. Und wenn man einer Map
  2-elementige Vektoren (2-Tupel) zufügt, dann wird das jeweils
  __erste__ Element des 2-Tupels als __Schlüssel__ (Key) und das
  jeweils __zweite__ Element als __Wert__ (Value) des Map-Entries
  verwendet.

  Somit fügt `into` alle [Bild, Position]-Tupel als __&lt;Key,Value>__
  in die leere Map und liefert das Ergebnis als Rückgabewert.

  __REPL:__

		hearts.core=> (into {} [[:a "A"] [:b "B"]])
		{:a "A", :b "B"}
		hearts.core=> bild->index
		{7 5, 4 2, :koenig 11, 6 4, 3 1, :dame 10, 2 0, :ass 12, 9 7, 5 3, 10 8, :bube 9, 8 6}

Die ganze Verarbeitung nimmt also `bilder`, macht eine Folge (in
Clojure-Sprech _Sequence_) von 2-Tupeln [Bild, Bild-Position] daraus
und fügt diese als __&lt;Key,Value>__ in eine Map, die wir an den Namen
`bild->index` binden.

Wie man auf diese Map zugreift, sehen wir weiter unten.

[1] https://clojure.org/guides/threading_macros  
[2] http://clojure-doc.org/articles/language/macros.html  
[3] https://de.wikipedia.org/wiki/Syntaxbaum#Abstrakte_Syntaxb%C3%A4ume  
[4] https://de.wikipedia.org/wiki/Dom%C3%A4nenspezifische_Sprache#Interne_bzw._eingebettete_DSLs_(internal_DSL)  
[5] https://stackoverflow.com/a/1628255/10546451  
[6] https://clojureverse.org/t/why-is-the-macro-systems-in-lisps-considered-so-valuable/2622  
[7] https://de.wikipedia.org/wiki/Funktion_h%C3%B6herer_Ordnung  

---

    (def bild->index (->> bilder
                          (map-indexed #(-> [%2 %1]))
                          (into {})))

---

`karten->punkte` soll eine Abbildung (Map) von den __Karten__ auf die
__Punkte__ einer jeden Karte sein. Die __Karten__ ergeben sich als
__Kartesisches__ __Produkt__ [6] über die __Farben__ `:kreuz`, `:pik`,
`:herz` und `:karo`, die wiederum als Keywords dargestellt werden, und
den __Bildern__ (`bilder`).

Die Karten sind also einfach nur 2-Tupel/Vektoren
__&lt;Farbe,Bild>__. Wir haben __keinen__ __Datentyp__ definiert. In
Java hätte man wohl eine Klasse eingeführt. Wir haben auch nicht
irgendwie anders ausgedrückt, dass das erste Element des 2-Tupels die
Farbe der Karte ist und dass das zweite Element das Bild der Karte
ist. Die Bedeutung ergibt sich also nur "durch den Code" bzw. die
__Verwendung__ der 2-Tupel.

Wir haben oben aber die Aliase `farbe` und `bild` eingeführt, um diese
Namen für den Zugriff auf die entsprechenden Vektor-Elemente zu
verwenden. Dadurch erhöhen wir die Lesbarkeit und Verständlichkeit des
Codes. Sie sind Teil unserer _ubiquitous language_. Wir hätten
stattdesse ja auch einfach direkt `first` und `second` verwenden
können.

Zu jeder Karte wird dann noch ihr Punktwert ermittelt und damit die
gewünschte Map erzeugt.

* Das Makro `for` __liefert__ für jede Kombination (__Farben__ __x__
  __Bilder__) ihrer Argumente das angegebene Ergebnis `[f b]` (also
  das 2-Tupel) als __Sequenz__. Der Mechanismus wird _list
  comprehension_ (etwa "Listenerzeugung" [1]) genannt.

  In vorliegenden Fall sind dies die Elemente des Farb-Vektors und der
  Bilder, die der Reihe nach --- also "für jeden Durchlauf" --- an die
  Namen `f` und `b` gebunden werden.
  
  __REPL:__

        hearts.core=> (for [f [:kreuz :pik :herz :karo]
             #_=>           b bilder]
             #_=>        [f b])
             ([:kreuz 2] [:kreuz 3] [:kreuz 4] [:kreuz 5] [:kreuz 6] [:kreuz 7] [:kreuz 8] [:kreuz 9] [:kreuz 10] [:kreuz :bube] [:kreuz :dame] [:kreuz :koenig] [:kreuz :ass] [:pik 2] [:pik 3] [:pik 4] [:pik 5] [:pik 6] [:pik 7] [:pik 8] [:pik 9] [:pik 10] [:pik :bube] [:pik :dame] [:pik :koenig] [:pik :ass] [:herz 2] [:herz 3] [:herz 4] [:herz 5] [:herz 6] [:herz 7] [:herz 8] [:herz 9] [:herz 10] [:herz :bube] [:herz :dame] [:herz :koenig] [:herz :ass] [:karo 2] [:karo 3] [:karo 4] [:karo 5] [:karo 6] [:karo 7] [:karo 8] [:karo 9] [:karo 10] [:karo :bube] [:karo :dame] [:karo :koenig] [:karo :ass])


* Diese Sequenz von __&lt;Farbe,Bild>__ "fädeln" wir nun via `->>` durch
  die HOF `map`, die als erstes Argument eine Arity-1-Funktion
  erwartet, die sie der Reihe nach auf die Elemente des zweiten
  Arguments anwendet und als Ergebnis wiederum eine __Sequenz__ mit
  den Funktionswerten liefert.

__REPL:__

	hearts.core=> (doc inc)
	-------------------------
	clojure.core/inc
	([x])
	  Returns a number one greater than num. Does not auto-promote
	  longs, will throw on overflow. See also: inc'
	nil
    hearts.core=> (map inc [1 21 42])
	(2 22 43)

* Wir definieren hier (anonym; also ohne sie an einen Namen zu binden)
  mit `fn` die benötigte Arity-1-Funktion. `fn` ist der _kanonische_
  Weg, eine Funktion zu definieren.

  Die Alternative `#(....)`, die oben vorgestellt wurde, ist eine
  Kurzschreibweise (sog. "reader macro"), in der sich die Arity aus
  der Nennung/Verwendung der "durchnummerierten Parameter-Formen"
  `%<i>` ergibt. Bei `fn` müssen die (_formalen_) Parameter hingegen
  explizit angegeben werden.

  Wir geben als Parameter aber anstatt eines __Namen__ einen
  __Vektor__ mit zwei Elementen/Namen `f` und `b` an. Dieser Vektor
  wird von Clojure als __Muster__ (_pattern_) verwendet. Beim
  Funktionsaufruf wird das Argument (also z.B. ein Vektor oder eine
  Liste) anhand des Musters __zerlegt__ und die Parameternamen `f` und
  `b` werden in diesem Fall an das erste bzw. zweite Element des
  Arguments gebunden. Dieser Mechanismus wird _destructuring_ [2]
  (oder allgemeiner _pattern matching_) genannt. Man könnte es auch
  "muster-basierte rekursive Strukturzerlegung mit Namensbindung"
  nennen.

  Mit einem __Vektor__-Pattern kann man __sequentielle__ Argumente
  zerlegen (also Listen, Vektoren, Strings, Maps) und mit einem
  __Map__-Pattern (kommt weiter unten noch) kann man __assioziative__
  Dinge zerlegen (Maps). Destructuring erspart einem also, den Zugriff
  auf die Elemente explizit über Funktionsaufrufe (z.B. `first` und
  `second`) hinschreiben zu müssen.

  Unsere Arity-1-Funktion liefert als Ergebnis wieder ein
  2-Tupel/Vektor, dessen erstes Element die Karte __&lt;Farbe,Bild>__ ist
  und deren zweites Element der Punkte-Wert der Karte ist.

* `cond` verhält sich fast wie die neue `switch`-__Expression__ (NICHT
  `switch`-__Statement__!) in Java 12 [3]. Die Argument-Paare aus
  _Prädikat_ [7] (Form) und Ergebnis (Form) werden der Reihe nach
  ausgewertet. Dabei wird immer nur die Prädikats-Form ausgewertet!
  Das ist wichtig, weil die Auswertung der Ergebnis-Form ja
  Seiteneffekte haben könnte! Sobald die ausgewertete Prädikats-Form
  _wahrhaftig_ (_truthy_ bzw. logisch wahr; vgl. unten) ist/liefert,
  wird die zugehörige Ergebnis-Form ausgewertet und als Ergebnis von
  `cond` geliefert.

  In Clojure gelten nur `nil` (das ist __dasselbe__ wie Java `null`)
  und `false` (ist __desselbe__ wie Java `java.lang.Boolean/FALSE`)
  als __unwahr__. Alle anderen Werte (inkl. `(new java.lang.Boolean
  false)` gelten als __wahr__. Um _logisch_ _wahr_ von `true` zu
  unterscheiden (denn sie sind __nicht__ __dasselbe__), sagt man
  i.d.R. _truthy_ [4], wenn man "logisch wahr" meint und _falsy_, wenn
  man "logisch unwahr" meint.

  Mit `or` und `and` kann man Wahrheitswerte verknüpfen. Dabei liefern
  diese nicht `true` und `false` sondern konsequenterweise _truthy_
  und _falsy_ Werte.

  __REPL:__

		hearts.core=> (and :foo nil :bar)
		nil
		hearts.core=> (or :foo nil :bar)
		:foo
		hearts.core=> (or false :bar :fred)
		:bar
		hearts.core=> (and 2 3 4)
		4
		hearts.core=> (not (and 2 3 4))
		false


* Schließlich verwenden wir wieder `into`, um die Sequenz von
  __&lt;&lt;Farbe,Bild>,Punkte>__ Tupeln in eine Map
  __&lt;Farbe,Bild>-->&lt;Punkte>__ zu überführen.

  __REPL:__

		hearts.core=> (def xs [[:foo "FOO"] [:bar "BAR"] [:foo "FRED"]])
		#'hearts.core/xs
		hearts.core=> (into [] xs)
		[[:foo "FOO"] [:bar "BAR"] [:foo "FRED"]]
		hearts.core=> (into {} xs)
		{:foo "FRED", :bar "BAR"}
		hearts.core=> (into '() xs)
		([:foo "FRED"] [:bar "BAR"] [:foo "FOO"])
		hearts.core=> (into #{} xs)
		#{[:foo "FOO"] [:foo "FRED"] [:bar "BAR"]}

__Einschub: Sequenzen und Listen__

Oben haben wir mit `for` eine __Sequenz__ erzeugt, obwohl der
Mechanismus _list comprehension_ heißt ....

Eine __Liste__ ist eine __endliche__ __Datenstruktur__. D.h. eine
Liste hat immer eine bekannte Anzahl von Elementen. Da Listen vom
Compiler als __Funktionsaufrufe__, d.h. als __auszuwertende__ __Form__
verstanden werden, muss man Listen-Formen __quoten__ (`quote`) und
damit ausdrücken, dass man diese Liste als Datenstruktur _meint_. Das
einfache Anführungszeichen (`'`) ist die Kurzform von `quote`. Mit
`list` kann man Listen auch aus den Elementen konstruieren.

__REPL:__

	hearts.core=> (str :f \r 3 \d)
	":fr3d"
	hearts.core=> (quote (str :f \r 3 \d))
	(str :f \r 3 \d)
	hearts.core=> '(str :f \r 3 \d)
	(str :f \r 3 \d)
	hearts.core=> (str :f \r (inc 2) "d")
	":fr3d"
	hearts.core=> '(str :f \r (inc 2) "d")
	(str :f \r (inc 2) "d")
	hearts.core=> (list :f \r (inc 2) \d)
	(:f \r 3 \d)

Eine __Sequence__ entspricht eher einer __Folge__ von
__Berechnungsergebnissen__, so wie die Erzeugung des Kartesischen
Produkts via `for`. Diese Folge/Sequenz kann abhängig vom
Berechnungsprozess endlich oder "unendlich" sein.

Natürlich ist nichts in einem Computer wirklich unendlich. Aber mit
Sequenzen kann man __nicht__ __endende__ __Berechnungsprozesse__ so
weit "treiben", wie man möchte. In diesem Sinne sind sie __beliebig__
__lang__.

So berechnet z.B. `(range)` alle natürlichen Zahlen. Man sollte diese
Form __nicht__ in die REPL eingeben, denn die REPL würde versuchen,
den __Wert__ dieses nicht endenden Berechnungsprozesses zu ermitteln,
um ihn dann auszugeben und das geht eben nicht.

Sequenzen werden __lazy__ [5] berechnet. D.h. der Berechnungsprozess
wird gerade so weit getrieben (und manchmal etwas weiter....), wie es
nötig ist, um die geforderte Anzahl von Sequenzelementen zu
liefern. D.h. man kann eine __unendliche__ Sequenz erzeugen, man kann
jedoch immer nur __endlich__, aber __beliebig__ __viele__ dieser
Elemente "konsumieren".

In dem folgenden Beispiel ist wichtig, dass die REPL den Aufruf von
`(range)` durchführt und dieser Aufruf liefert eine __lazy__
__sequence__. Die REPL versucht aber __nicht__, die Elemente dieser
Sequenz zu lesen/konsumieren. `def` bindet den Wert (also die __lazy__
__sequence__) an den Namen `foo`. Das __Ergebnis__ von `def` ist die
_Variable_ (`var`; darauf will ich hier aber nicht weiter eingehen)
mit dem Namen `hearts.core/foo`. Die REPL gibt also wie immer den Wert
der ausgewerteten Form aus und die ist in diesem Fall die `def` Form.

__REPL:__

	hearts.core=> (def foo (range))
	#'hearts.core/foo
	hearts.core=> (take 4 foo)
	(0 1 2 3)
	hearts.core=> (take 4 foo)
	(0 1 2 3)
	hearts.core=> (take 2 foo)
	(0 1)
	hearts.core=> (take 5 foo)
	(0 1 2 3 4)

Listen und Sequenzen werden beide auf die gleiche Weise in der REPL
ausgegeben. Das macht auch Sinn: ausgeben kann man ja nur den
__endlichen__ __Wert__, der den ersten n Elementen einer
möglicherweise unendlichen Sequenz entspricht. Und dieser __Wert__
__ist__ (d.h. verhält sich wie) __eine__ __Liste__.

Das folgende Beispiel zeigt, dass man bei der Umwandlung von Sequenzen
in Strings sorgsam sein muss.

	hearts.core=> (->> (range) (take 3))
	(0 1 2)
	hearts.core=> (->> (range) (take 3) type)
	clojure.lang.LazySeq
	hearts.core=> (->> (range) (take 3) str)
	"clojure.lang.LazySeq@7480"
	hearts.core=> (->> (range) (take 3) seq str)
	"(0 1 2)"
	hearts.core=> (->> (range) (take 3) pr-str)
	"(0 1 2)"

Und wenn du absichtlich einen Seiteneffekt in den Berechnungsprozess
einbaust (hier das `print`), kann du auch erkennen, in welchen
Situationen welche Dinge passieren. Wann also der Berechnungsprozess
wirklich __abläuft__.

__REPL:__

	hearts.core=> (def bar (for [x (range)] (do (print (format "[%s]" x)) x)))
	#'hearts.core/bar
	hearts.core=> (take 2 bar)
	(0 1)[0][1]
	hearts.core=> (take 2 bar)
	(0 1)
	hearts.core=> (take 3 bar)
	(0 1 2)[2]
	hearts.core=> (take 3 bar)
	(0 1 2)
	hearts.core=> (take 10 bar)
	(0 1 2 3 4 5 6 7 8 9)[3][4][5][6][7][8][9]

[1] https://de.wikipedia.org/wiki/List_Comprehension  
[2] https://clojure.org/guides/destructuring  
[3] https://openjdk.java.net/jeps/325  
[4] https://clojure.org/guides/learn/flow#_truth  
[5] http://clojure-doc.org/articles/language/laziness.html  
[6] https://de.wikipedia.org/wiki/Kartesisches_Produkt  
[7] https://de.wikipedia.org/wiki/Pr%C3%A4dikat_(Logik)  

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

Bis jetzt haben wir mit Ausnahme der beiden Aliase nur "Daten"
(d.h. Werte) definiert und sie an Namen gebunden. Der Code, d.h. die
S-Expressions, wird beim Laden des Namensraum `hearts.core`
ausgewertet und führt eben dazu, dass die Werte an die Namen gebunden
werden. Dieser Vorgang findet nur einmalig statt. Also immer nur das
eine Mal, wenn der Namensraum geladen wird. Man kann aber "erzwingen",
dass ein Namensraum wiederholt/mehrfach geladen wird. Dann werden auch
die S-Expressions wiederholt ausgewertet und die Namen werden erneut
an neue Werte gebunden.

Nun wollen wir aber mit `defn` eine __Funktion__ definieren.
`(defn <name> [<parameter>...] <body>)` ist einfach eine Kurzform von
`(def <name> (fn [<parameter>...] <body>))`.

Die Funktionsdefinition mit `fn` haben wir schon oben gesehen. Hier
binden wir diese Funktion aber an einen Namen, so dass wir später über
diesen Namen auf die Funktion zugreifen können.

---

Die Funktion `beginnt` ermittelt, welcher Spieler die __erste__
__Runde__ beginnt. Dies ist jener Spieler mit der Kreuz-2. `beginnt`
liefert diesen Spieler als Keyword (also z.B: `:gabi`).

Das Argument ist eine Map. Diese nenne ich __Alle-Spieler-Map__. Die
Alle-Spieler-Map bildet __&lt;spieler>__ wiederum auf eine Map
ab. Diese nenne ich __Ein-Spieler-Map__. Die Ein-Spieler-Map hat
u.a. den Key `:hand` und der gemappte Wert von `:hand` ist eine
__Menge__ (Set; Hand-Menge). Die Elemente der Hand-Menge sind
__&lt;Farbe,Bild>__-Tupel/Vektoren. Diese könnten wir __Karte-Vektor__
nennen.

Die Alle-Spieler-Map soll als Datenstruktur(-Wert) den Zustand aller
Spieler während des Spiels repräsentieren. Es fehlen noch einige Dinge
, wie z.B. die Stiche, die die Spieler gemacht haben. Diese Dinge
werden aber noch eingeführt (vgl. unten).

Zu Beginn des Spiels werden alle Karten gemischt und auf die Spieler
verteilt. `beginnt` wird also __einmalig__ nach dem Verteilen der
Karten mit diesem _Start-Zustand_ der Alle-Spieler-Map aufgerufen, um
zu ermitteln, wer die erste Runde beginnt.

In Java würde man wohl für Karte-Vektor, Ein-Spieler-Map und
Alle-Spieler-Map separate __Klassen__ einführen, mit Konstruktor,
lesenden und vielleicht schreibenden/mutierenden Methoden
(Getter/Setter) und dann z.B. auch die Methode `beginnt` in die
Alle-Spieler-Klasse tun.

In Clojure kann man auch "Klassen" definieren, d.h. man definiert
"Kontrakte" (sog. __Protokolle__) so wie man Interfaces in Java
definiert. Und man definiert dann __Implementierungen__ zu diesen
Protokollen. Aber man verwendet keine __Ableitung__. Man kann aber
Java-Klassen ableiten und auch Java Interfaces implementieren;
sog. "Java interop".

Es gibt einen endlosen Streit darüber, ob nun OOP oder Funktionale
Sprachen der bessere Weg sind. Hier ein paar Sachen zum Lesen
[1, 2, 3]. Und mehr will ich hier auch nicht dazu sagen.

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

  Diese __Sequenzialisierung__ wird von allen Funktionen vorgenommen,
  die auf Sequenzen arbeiten. Man braucht ihnen also selber gar keine
  Sequenz direkt zu übergeben, sondern falls nötig machen sie sich
  diese aus dem Wert, den man als Argument übergibt.

  Das folgende Beispiel demonstriert dies anhand von `map`, Listen und
  Maps. Die __Sequenzialisierung__ erfolgt immer über `seq`.

  __REPL:__

		hearts.core=> (map inc '(1 2 3))
		(2 3 4)
		hearts.core=> (map inc [1 2 3])
		(2 3 4)
		hearts.core=> (seq [1 2 3])
		(1 2 3)
		hearts.core=> (seq {:foo "FOO" :bar "BAR"})
		([:foo "FOO"] [:bar "BAR"])
		hearts.core=> (map second {:foo "FOO" :bar "BAR"})
		("FOO" "BAR")

* `keep` wendet eine Funktion (erstes Argument) auf die Elemente des
  zweiten Arguments (also der
  __&lt;Spieler,Ein-Spieler-Map>__-Sequenz) an und liefert all jene
  Funktions-Rückgabe-Werte/Ergebnisse, die non-`nil` sind. `keep` ist
  sowas ähnliches wie ein Filter, nur dass es nicht die
  __Eingabelemente__ liefert, sondern deren non-`nil` __"abgebildeten"
  Wert__.

  __REPL:__

		hearts.core=> (filter #(and % (format "[%s]" %)) [:foo true false nil])
		(:foo true)
		hearts.core=> (keep #(and % (format "[%s]" %)) [:foo true false nil])
		("[:foo]" "[true]" false)

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

  Das `when-else`-Fall-`nil` bewirkt zusammen mit `keep`, dass nur
  jene Funktionsergebniswerte durch `keep` geliefert werden, für die
  der `when`-Ausdruck non-`nil` ist.

  In bestimmten Situationen kann man anstatt eines `when` auch `and`
  verwenden. Zu `and` kommen wir noch. Hier nur ein Beispiel:

  __REPL:__

		hearts.core=> (when (#{[:kreuz 2]} [:kreuz 2]) "jupp")
		"jupp"
		hearts.core=> (when (#{[:kreuz 2]} [:kreuz 3]) "jupp")
		nil
		hearts.core=> (and (#{[:kreuz 2]} [:kreuz 3]) "jupp")
		nil
		hearts.core=> (and (#{[:kreuz 2]} [:kreuz 2]) "jupp")
		"jupp"


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

  __REPL:__

		hearts.core=> (#{42 :foo "bar" [:kreuz 2]} 42)
		42
		hearts.core=> (#{42 :foo "bar" [:kreuz 2]} :foo)
		:foo
		hearts.core=> (#{42 :foo "bar" [:kreuz 2]} [:kreuz 2])
		[:kreuz 2]

  Da `[:kreuz 2]` _truthy_ ist, liefert die `fn` die bzw. den Spieler,
  der die Kreuz-2 auf der Hand hat. Und da immer genau ein Spieler
  diese Karte auf der Hand hat, können wir aus der Sequenz via `first`
  einfach das erste (und einzige) Element als Ergebnis liefern.

Fertig.

[1] http://www.smashcompany.com/uncategorized/object-oriented-programming-is-an-expensive-disaster-which-must-end  
[2] http://blog.cleancoder.com/uncle-bob/2019/08/22/WhyClojure.html  
[3] https://8thlight.com/blog/myles-megyesi/2012/04/26/polymorphism-in-clojure.html  

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
  __Verzweigungen__ haben. Bisher haben wir `cond` und `when` als
  Fallunterscheidung/Verzweigung kennengelernt. Viele Dinge, die man
  in Java mit `if-then-else-if-else` machen würde, macht man in
  Clojure mit Sequenzen und den Funktionen auf diesen und HOFs. Ob das
  "einfacher" oder "besser" ist, ist noch eine andere Frage.

  Ich finde, dass Clojure-Programme sehr "kompakt" bzw. "dicht"
  wirken. Als Clojure-Anfänger empfand ich das schon fast als "Schmerz
  beim Lesen", weil die Semanik sich auf so wenig Code "verteilt",
  dass man extrem aufmerksam lesen muss. Es gibt wenig "Raum zwischen
  den wichtigen Teilen" zum Ausruhen --- es ist einfach jede Zeile
  wichtig.

  Das führt meiner Meinung nach auch dazu, dass Clojure-Programm
  einfach "kurz sind". Sie haben i.d.R. weniger Zeilen als Java-Code
  und weniger Verzweigungen. Beides sind Faktoren, die effektiv zu
  einer höheren Testabdeckung (_branch coverage_) führen.

  Und Clojure Programme bestehen meiner Meinung nach nur aus den
  Teilen, die ich wirklich brauche, um mein Problem zu lösen. Nicht
  weniger, aber eben auch nicht mehr. Das führt zu weniger Code und
  weniger Bugs, obwohl Clojure keine statische Typprüfung besitzt
  [3, 4].

  Natürlich müssen auch Clojure Programme automatisch getestet werden
  [2]. Da man aber schon __beim__ __Entwickeln__ ständig in der REPL
  interaktive Ad-Hoc-Tests macht, sind Clojure Programm i.d.R. schon
  sehr gut getestet, wenn man den Code fertig hat.

---

`sticht` liefert den Spieler (Spieler-Keyword), der die Karten, die
auf dem Tisch liegen bzw. ausgespielt wurden, am Ende einer Runde
erhält. Der also __den Stich macht__.

Das Argument ist eine Sequenz. Die Elemente sind Maps mit den Keys
`:spieler` und `:karte`. Diese geben an (also mappen zu Werten), wer
die Karte gespielt hat (Spieler-Keyword) und welche Karte gespielt
wurde (Karten-Vektor).

Die Sequenz wird per Vektor-Destrukturierung zerlegt wird. Das
__erste__ Element (man spricht häufig vom _head_) wird an den Namen
`eroeffnung` gebunden. Durch die `&`-Notation werden die restlichen
__Elemente__ (also auch wieder eine __Sequenz__; man spricht häufig
vom _tail_) an den Namen `xs` gebunden.

Die Reihenfolge der Element in der Sequenz gibt an, in welcher
Reihenfolge die Karten ausgespielt wurden. Daher gibt das __erste__
__Element__ an, mit welcher Karte die betreffende Runde __eröffnet__
wurde.

Die Implementation muss also beginnend von der Eröffnungskarte aus
alle anderen Karten betrachten und entscheiden, welches die
"Rang-höchste" der Karten ist, die die Farbe der Eröffnungskarte
"bedient" hat und denjenigen Spieler liefern, der eben diese Karte
gelegt hat.


* das Keyword `:spieler` steht hier an der __Funktor-Position__ in der
  S-Expression. So wie Mengen Funktionen auf ihren Elementen sind, so
  sind Keywords __Lookup-Funktionen__ auf __assoziativen__
  Datenstrukturen. D.h. `:spieler` verhält sich wie eine Funktion, die
  mit dem Key `:spieler` auf eine Map oder eine Menge/Set zugreift und
  den Ergebniswert (also entweder den Map-Value oder das Set-Element)
  liefert.

  __REPL:__

		hearts.core=> (get {:bar "BAR" :foo "FOO"} :foo)
		"FOO"
		hearts.core=> (get {:bar "BAR" :foo "FOO"} 42 "oops")
		"oops"
		hearts.core=> (:foo {:bar "BAR" :foo "FOO"})
		"FOO"
		hearts.core=> (:fred {:bar "BAR" :foo "FOO"})
		nil
		hearts.core=> (:fred {:bar "BAR" :foo "FOO"} "not-found")
		"not-found"
		hearts.core=> ({:bar "BAR" :foo "FOO"} :bar)
		"BAR"
		hearts.core=> (#{:bar :foo} :foo)
		:foo
		hearts.core=> (:bar #{:bar :foo})
		:bar
		hearts.core=> (:foo [:foo :bar])
		nil

  Mit `:spieler` wird also auf den gemappten Wert der folgenden
  `reduce`-Form zugegriffen und dieser als Ergebnis geliefert. Damit
  soll am Ende eben der gesuchte Spieler geliefert werden.

  __Anmerkung:__ beim Lesen dieser Beschreibung merkt man, dass hier
  __erst__ der Zugrff via `:spieler` beschrieben wird, ob wohl dieser
  __nach__ der Auswertung von `reduce` erfolgt. Das ist ja genau der
  Grund, warum es das _threading macro_ gibt. Denn das erlaubt uns ja,
  den Code in der Reihenfolge hinzuschreiben, in der er auch
  ausgeführt wird. Wie müsste man den Code umstellen, damit er etwa so
  aussieht?

        (... xs .... :spieler)


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
  liefern, den `reduce` beim anschließenden Funktionsaufruf wieder an
  die Funktion gibt. Das ist wie Ping-Pong.

  Nach dem letzten Element von `xs` liefert `reduce` den Rückgabe-Wert
  des letzen Funktionsaufrufs --- also das "finale Aggregat".

  In Java gibt es mittlerweile ja auch ein `reduce` [1]. Früher hätte
  man in Java einfach eine `for` Schleife mit einer lokalen Variable
  benutzt, in der man das Aggregate hält.


* Die Parameter der Aggregats-Funktion werden hier via
  Map-Destrukturierung an lokale Namen gebunden. Das erste Argument
  (das Aggregat) zerlegen wir und binden den Namen `f1` an die Farbe
  des `:karte` Wertes und `b1` an das Bild.

  Hier verwende ich noch ein weiteres Destrukturierungs-Feature: mit
  `:as` bindet man das ganze, unzerlegte Argument an einen Namen --
  hier also `t`.

  Die Namen `f2`, `b2` und `s` binden wir an die Wertes des zweiten
  Arguments (das jeweilige Element von `xs`; vgl. oben).

  Die Funktion prüft nun, ob mit `s` bzw. `f2` die Farbe `f1` bedient
  wurden __und__ ob der Rang von `s` __höher__ ist als der von `t`
  (für den ersten Aufruf wird `t` ja `eroeffnung` sein).

  Falls dem so ist, wird `s` unserer weiterer Aggregationszustand
  sein. Also jene Karte, die "sticht".

  Falls dem aber nicht so ist, "sticht" weiterhin die bisherige
  "stechende Karte" `t`.

Am Ende liefert `reduce` also "die stechende Karte" und mit `:spieler`
liefert `sticht` den Spieler, der diese Karte gespielt hat.

Fertig.

__Hinweis__: man hätte auch `((reduce (fn....) eroeffnung) :spieler)`
schreiben können, also die Map als Funktor und nicht das Keyword, aber
es ist _idiomatisch_, für diesen Use-Case das Keyword als Funktor zu
verwenden. Und außerdem sehen zwei öffnende Klammern direkt
hintereinander auch merkwürdig aus ;-)

[1] https://www.baeldung.com/java-stream-reduce  
[2] https://dev.solita.fi/2017/04/10/making-software-testing-easier-with-clojure.html  
[3] https://dev.to/danlebrero/the-broken-promise-of-static-typing  
[4] https://developers.slashdot.org/story/18/01/01/0242218/which-programming-languages-are-most-prone-to-bugs  

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
  liefert [2, 3, 4]. Wichtig: die Funktionen werden "von rechts nach
  links" angewendet.

  Beispiel: `((comp h g f) x)` entspricht `(h (g (f x)))` oder auch
  `(-> (f x) g h)` bzw. `(-> x f g h)`.

  Hier erzeugen wir eine Funktion, die erst via `:karte` die Karte aus
  der gespielten Karte holt und darauf dann `karten->punkte` anwendet.

  __REPL:__

		hearts.core=> (comp inc second)
		#object[clojure.core$comp$fn__4727 0x7a6fe31f "clojure.core$comp$fn__4727@7a6fe31f"]
		hearts.core=> ((comp inc second) [:foo 41])
		42
		hearts.core=> ((comp pos? inc second) [:foo 41])
		true
		hearts.core=> ((comp str pos? inc second) [:foo 41])
		"true"


* Mit `map` bilden wir die Sequenz von gespielten Karten auf die
  Sequenz der zugehörigen Punkte ab.


* `apply` wendet eine Funktion (erstes Argument) auf die
  __Elemente(!)__ des zweiten Arguments (eine Sequence) an. Dabei
  stellt `apply` die __Elemente__ aber an die __Argumentpositionen__
  der __aufgerufenen__ __Funktion__. D.h. wir übergeben `apply` eine
  Sequenz mit n __Werten/Elementen__ und `apply` ruft die Funktion mit
  n __Argumenten__ auf. Das ist was komplett anderes, als die Funktion
  mit __einem__ __Sequenz-Argument__ mit n Elementen aufzurufen!

  __REPL:__ die erste Form binden eine __Liste__ mit
    __Rest-Argumenten__ an `xs`. In diesem Fall haben wir nur ein
    Argument (den Vektor). Daher hat die Liste auch nur ein
    Element. Die zweite Form ruft die Funktion mit __drei__ Argumenten
    auf (den drei Elementen des Vektors). Daher enthält die
    Rest-Argumenten-Liste in diesem Fall drei Elemente.

		hearts.core=> ((fn [& xs] xs) [:foo :bar :fred])
		([:foo :bar :fred])
		hearts.core=> (apply (fn [& xs] xs) [:foo :bar :fred])
		(:foo :bar :fred)


* `+` ist eine Funktion, die __beliebig__ __viele__ Zahl-Argumente
  addiert. `(+)` ergibt `0`, `(+ 4)` ergibt `4` und `(+ 3 2 1)` ergibt
  `6`.

Mit `apply` können wir also auf einen Schlag (ohne aggregierende
Schleife; vgl. oben) alle gemappten Punktwerte addieren und als
Ergebnis liefern. `(apply + [1 2 3])` ist das gleiche wie `(+ 1 2 3)`
ist das gleiche wie `(reduce + [1 2 3])`.

Fertig.

__Anmerkung__: Clojure-Programme haben schon dadurch weniger Fehler,
  dass es fast unmöglich ist, in Schleifen Off-by-One-Fehler [1] zu
  programmieren. Einfach weil man kaum Schleifen zu programmieren
  braucht und wenn, dann haben sie keine __Schleifen-Varianten__,
  gegen die man ein __Abbruchkriterium__ prüft, sondern man arbeitet
  fast immer auf der __Sequenz-Abstraktion__ und mit den
  API-Funktionen darauf.

[1] https://de.wikipedia.org/wiki/Off-by-one-Error  
[2] https://de.wikipedia.org/wiki/Komposition_(Mathematik)  
[3] https://rosettacode.org/wiki/Function_composition#Clojure  
[4] https://rosettacode.org/wiki/Function_composition#Java_8  

---

    (defn punkte [stiche]
      (->> (flatten stiche)
           (map (comp karten->punkte :karte))
           (apply +)))

---

`rang-liste` soll eine nach Punkten aufsteigend geordnete
Liste/Sequenz von Spielern liefern. Da es auch einen Punktegleichstand
geben kann, sind es Mengen von Spielern.

`rang-liste` wird nur einmal pro Spiel aufgerufen, nämlich wenn nach
der letzten Runde die Stiche der Spieler ausgewertet werden, um
festzustellen, wer gewonnen hat.

* `s-map` ist wieder eine Alle-Spieler-Map. 


* via `map` bilden wir die sequenzialisierte Alle-Spieler-Map (also
  eine Sequenz von __&lt;Spieler,Ein-Spieler-Map>__-Tupel) auf eine
  Sequenz von Maps mit den Schlüsseln `:spieler` und `:punkte`.

  An dieser Stelle hätten wir auch einfach ein
  __&lt;Spieler,Punkte>__-Tupel anstatt der Map nehmen können, genau
  so, wie wir unsere Karten ja als __&lt;Farbe,Bild>__-Tupel
  repräsentieren, anstatt als Map mit `:farbe` und `:bild`. Das ist
  einfach eine Designentscheidung.


* `group-by` ist eine HOF, die die Elemente der Sequenz (zweites
  Argument) nach den Funktionswerten "gruppiert", die sich durch
  Anwendung der Funktion (erstes Argument) auf die Elemente
  ergibt. Das Ergebnis von `group-by` ist eine Map
  __&lt;Gruppierungs-Wert-->Gruppen-Menge>__.

  Hier ist `:punkte` die Funktion, durch die die Gruppierung nach den
  Punkten erfolgt.

  __REPL:__

		hearts.core=> (group-by even? (range 10))
		{true [0 2 4 6 8], false [1 3 5 7 9]}


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
      (->> s-map
           rang-liste
           first
           second))

---

Bis jetzt haben wir nur "einfache Berechnungsregeln" implementiert. Es
wurde noch gar nicht "gespielt". Diese Funktionen kommen jetzt.

Als erstes wird __gegeben__.

`geben!` ist eine "impure function". Sie liefert nämlich nicht immer
bei gleicher Eingabe das gleiche Ergebnis (tatsächlich hat die
Funktion gar keinen Parameter und somit auch keine "Eingabe").

Ein weiterer Umstand, der eine Funktion "impure" macht ist, wenn eine
Funktion "Seiteneffekte" hat --- also z.B. eine globale Variable
ändert (das kann man in Clojure machen, somit ist Clojure auch keine
"reine funktionale Programmiersprache").

In Clojure ist es üblich, für solche Funktionen einen Namen mit einem
`!` am Ende zu verwenden (ist also ein "hallo wach!" für den
Leser). Aber dabei handelt es sich __nur__ __um__ __eine__
__Konvention__. Ansonsten hat das `!` im Namen keine besondere
Bedeutung.

"Impure Functions" zu testen ist schwieriger als "reine Funktionen" zu
testen. Da liegt eben daran, dass man entweder einen
"Funktions-und-Argument-externen Zustand" kontrollieren muss und/oder
"Zufälle kontrollieren muss" (wie z.B. beim Geben). Daher sollte man
immer versuchen, einen möglichst großen Anteil seines Programms "pure
functional" zu machen. Das kann man durchaus auch in Java so machen.


* `keys` liefert die Sequenz mit den Schlüsslen der `karten->punkte`
  Map. Also die Karten.


* `shuffle` ist eine __Zufalls-Misch-Funktion__.


* `partition` ist das Gegenstück zu `flatten`: es macht aus einer
  Sequenz von Elementen, eine Sequenz von Sequenzen von Elementen. Die
  "inneren" Sequenzen haben hier alle die Länge 13. Wir haben damit
  also 4x jeweils 13 Karten.


* die HOF `map` kann nicht nur auf eine Sequenz sondern auch auf
  __mehrere__ __Sequenzen__ angewendet werden. In dem Fall wendet sie
  die Funktion (erstes Argument) auf die jeweils ersten Elemente
  mehrere Sequenzen an (als n Argumente) und dann die jeweils zweiten
  usw.

  Hier wird also die Funktion auf `spieler` und die aufgeteilten
  Karten-Partition angewendet.

  __Übung:__ Schreibe die Funktion `karten->punkte` so um, dass sie
  nur aus einem `map` Aufruf und `into` besteht. D.h. eliminiere das
  `for`.


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
  <hand>)`. Also nicht mit __einer__ __Map__ angegeben sondern mit
  einer Folge von Name-Wert-Paaren.

  __Quizfrage__: wie müssen die Parameter in der Funktionsdefinition
  definiert werden, damit die Angabe an der Aufrufstelle wie gezeigt
  mit mehreren Argumenten erfolgt? Hinweis: die Lösung ist eine
  Kombination auf Vektor- und Map-Destructuring.

  Das Problem, dass __positional parameters__ aufgrund von
  Verwechslungen mit falschen Werten bestückt werden, kennt man auch
  in Java, wenn man z.B. mehrere `null` Werte als Argument
  angibt. Denn in diesem Fall kann auch der Comiler nicht mehr helfen,
  weil `null` ja ein untypisierter Wert ist.

  Frage: was macht der folgende Aufruf?
  `AdressenService.verlegeBeginn(1, 911, null, true, null)`.  Und
  hätte es nicht `AdressenService.verlegeBeginn(911, 1, null, null,
  true)` heißen müssen? Wer weiß. Der Compiler hilft eben nur
  bedingt. Natürlich kann man auch __benannte__ Parameter mit falschen
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

* `hand` ist eine Menge von Karten-Vektoren, also die Karten, die der
  legende Spieler auf der Hand hat, und `tisch` ist eine Liste/Sequenz
  von "bisher in diese Reihenfolge auf den Tisch gelegten Karten":
  eine Map mit `:spieler` und `:karte`.

* `if` wertet das __erste__ __Argument__ aus und falls dieses _truthy_
  ist, liefert `if` als Ergebnis das Ergebnis der Auswertung des
  __zweiten__ Arguments ("then-Fall"). Ansonsten liefert `if` das
  Ergebnis der Auswertung des __dritten__ Elements ("else"-Fall). Das
  dritte Argument ist __optional__. Falls es nicht angegeben ist,
  entspricht das dem Wert `nil`. Es werden also immer nur jene
  __zwei__ Argumente von `if` ausgewertet, die __nötig__ sind, um das
  Ergebnis liefern/berechnen zu können.

  `if` hat also eine besondere Auswertungsregel. Wenn man das mit
  einer Funktion machen würde, würde es nicht das gewünschte Ergebnis
  bringen.

  __REPL:__

		hearts.core=> (if 42 "jupp" "nope")
		"jupp"
		hearts.core=> (if 42 (do (print "JUPP\n") :jupp) (do (print "NOPE!\n") :nope))
		JUPP
		:jupp
		hearts.core=> (if nil (do (print "JUPP\n") :jupp) (do (print "NOPE!\n") :nope))
		NOPE!
		:nope
		hearts.core=> (defn my-if [p a b] (if p a b))
		#'hearts.core/my-if
		hearts.core=> (my-if nil (do (print "JUPP\n") :jupp) (do (print "NOPE!\n") :nope))
		JUPP
		NOPE!
		:nope

* `empty?` liefert _truthy_, falls das Argument "leer ist". `nil` gilt
  auch als leer.

  Wenn der Tisch noch leer ist, muss ja entweder mit der Kreuz-2
  eröffnet werden oder es kann eine beliebige Karte gelegt
  werden. Insbesondere braucht zu Beginn, wenn der Tisch noch leer
  ist, keine Farbe __bedient__ zu werden.

* `or` (ein Makro) liefert das erste seiner n (ausgewerteten)
  __Argumente__(!!!), das _truthy_ ist. Die Argumente werden __der__
  __Reihe__ __nach__ __ausgewertet__, bis ein _truthy_ Wert vorliegt
  und dieser wird dann geliefert.

  Zur Erinnerung: die "normale" Auswertung läuft so an, dass __erst__
  __alle__ Argumente/Elemente einer Liste ausgewertet werden und
  __dann__ wird der Funktor mit eben diesen Werten aufgerufen.

  Bei `or` soll aber die Auswertung __nur__ erfolgen, __falls__
  __alle__ "links daneben stehenden Argument" _falsy_ sind. Mit dieser
  Auswertungssemantik kann man z.B. auch `null`-Checks implementieren.

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
  definiert sein müsste.

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
  "then"-Ausdrucks/Zweig. In __diesem__ __Zweig__ kann man über den
  lokalen Namen auch auf den zuvor gebundenen Wert zugreifen.

  Andernfalls wird der __Namen__ __nicht__ __gebunden__ und das
  `if-let`-Ergebnis ergibt sich aus dem Wert des "else"-Zweiges.


* mit der HOF `filter` erzeugen wir eine Sequenz von Karten (`hand`),
  deren `(farbe %)` der Farbe der ersten Karte auf dem Tisch (`(->
  tisch first :karte farbe)`) entspricht (`=`). Das sind also alle
  Karten, mit denen man die Eröffnungskarte __bedienen__ könnte (und
  dann ja auch __müsste__). Von denen wählen wir uns wieder eine
  beliebige aus (`shuffle first`).

  __Anmerkung__: in `#(...)` Formen kann man `%<i>` benutzen, um die
  Argumente über ihre 1-basierte Position zu "adressieren"
  (vgl. oben). Wenn man nur ein Argument hat, kann man auch einfach
  nur `%` schreiben.

  __REPL:__

		hearts.core=> (filter even? (range 10))
		(0 2 4 6 8)


* Falls es nun eine solche Karte gibt (also `k` _truthy_ ist), liefern
  wir diese Karte.


* Ansonsten liefern wir wie in dem Leerer-Tisch-Fall eine beliebige
  Karte, die der Spieler auf der Hand hat.


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

  __REPL:__

		hearts.core=> (when true (println "x") :foo)
		x
		:foo
		hearts.core=> (println "x")
		x
		nil
		hearts.core=> (if true (println "x") :foo)
		x
		nil


* die zweite Form von `when-let` ist `loop`. Bisher haben wir `reduce`
  verwendet, um __iterative__ __Berechnungen__ durchzuführen. In Java
  hätte man dafür `for` oder `while` Schleifen verwenden.

  `loop` bietet auch die Möglichkeit, eine __Aggregation__
  durchzuführen, jedoch kann man auf einfach Weise am "Ende der
  Schleife" noch einen Verarbeitungsschritt einbauen.

  Mit `reduce` ist das häufig nicht so einfach möglich, weil man nicht
  leicht erkennen kann, wann das letzte Element verarbeitet wird. In
  Java und bei der Verwendung von `reduce` muss man solche "finalen
  Schritte" daher i.d.R. im Anschluss an die Schleife machen. Mit
  `loop` kann man das eleganter und kompakter formulieren.

  `loop` eröffnet auch einen __lokalen__ __Namensraum__ (wie `let` und
  `when-let`) und bindet Werte an lokale Namen. Der _binding_ _vector_
  besteht aus Paaren von `<lokaler-name> <init-wert>`. Diese
  __initiale__ Bindung (inkl. Destructuring) wird __einmalig__ zu
  Beginn der Schleife hergestellt.

  Hier wird `x` an das erste Element von `xs` gebunden und `xr` an den
  _tail_ (Rest) von `xs`. Der lokale Name `s` wird an den Wert von `s`
  gebunden.

  __Achtung__: es handelt sich hierbei um zwei __verschiedene__
  Namen. Das linke `s` ist in der `loop` gebunden, das rechte `s` ist
  in `runde` gebunden und hier wird __der__ __Wert__ von diesem Namen
  an das `s` in `loop` gebunden. Innerhalb von `loop` "sieht" man mit
  `s` immer nur das "innere" `s`. Andere Namen, die in `runde`
  gebunden sind (wie z.B. `r`) können auch in `loop` gesehen
  werden. Das "innere" `s` _verschattet_ also das äußere `s`. Solche
  Mehrfachverwendungen mit Namenverschattung sollte man vermeiden.

  `tisch` wird mit einem leeren Vektor gebunden.

  Die Bindung im _binding_ _vector_ ist die Bindung der Namen für den
  ersten Schleifendurchlauf. Die Schleife wird jedoch mehrfach
  durchlaufen. Wie das passiert, wird gleich gezeigt. Bei diesen
  folgenden Schleifendurchläufen sind die Namen dann an andere Werte
  gebunden (vgl. `recur` unten).

  Der Name `x` wird in jedem Schleifendurchlauf an den Namen
  desjenigen Spielers gebunden, der gerade legen muss. Wenn `x`
  _falsy_ ist (also alle Spieler gelegt haben und der _aktuelle_
  _Spieler_ `nil` ist), ist die Runde "fertig". Diesen Fall erkennen
  wir via `(if-not x ...)`.

  __REPL:__

		hearts.core=> (loop [[h & t] (range 5) r nil]
		                 (if-not h (format ">%s<" r)
		                           (recur t (str h r h))))
		">4321001234<"

  Am Ende der Runde müssen wir via `(sticht tisch)` ermitteln, wer
  diese Runde sticht und wir "geben" die Karten auf dem `tisch` eben
  diesem Spieler `b` in seine `:stiche` (sein Haufen).

  Das machen wir dadurch, dass wir die Alle-Spieler-Map "updaten"
  (natürlich wird eine neue Map erzeugt!), und zwar in dem
  Eintrag/Wert, der über den _Pfad_ `b` und `:stiche` referenziert
  wird.

  Der Wert dieses _Pfades_ ist zu Beginn der Wert `nil`! Wir haben in
  `geben!` nur den `:hand`-Eintrag der Ein-Spieler-Map gesetzt, nicht
  aber den `:stiche`-Eintrag.

  __REPL:__

		hearts.core=> (doc get-in)
		-------------------------
		clojure.core/get-in
		([m ks] [m ks not-found])
		  Returns the value in a nested associative structure,
		  where ks is a sequence of keys. Returns nil if the key
		  is not present, or the not-found value if supplied.
		nil
		hearts.core=> (def a {:foo {:bar "BAR" :fred "FRED"}})
		#'hearts.core/a
		hearts.core=> (get-in a [:foo :bar])
		"BAR"
		hearts.core=> (get-in a [:foo :oops] "oops")
		"oops"
		hearts.core=> (get-in a [:foo :oops])
		nil


  Mit `(fnil conj [])` erzeugen wir eine Funktion, die das erste
  Argument durch `[]` ersetzt, falls es `nil` ist und sich ansonsten
  wie `conj` verhält. Dadurch schafft man eine Funktion, die die
  __Initialisierungslogik__ __mit__ __einschließt__: nämlich als
  Start-Wert einen leeren Vektor zu verwenden. `conj` (_conjoin_) fügt
  ein Element zu einer Collection.

  __REPL:__

		hearts.core=> (doc conj)
		-------------------------
		clojure.core/conj
		([coll x] [coll x & xs])
		  conj[oin]. Returns a new collection with the xs
			'added'. (conj nil item) returns (item).  The 'addition' may
			happen at different 'places' depending on the concrete type.
		nil
		hearts.core=> (conj [] :foo :bar)
		[:foo :bar]
		hearts.core=> (conj '() :foo :bar)
		(:bar :foo)
		hearts.core=> ((fnil conj []) nil :foo :bar)
		[:foo :bar]
		hearts.core=> ((fnil conj []) [:fred :quox] :foo :bar)
		[:fred :quox :foo :bar]



  Das Zusammenwirken von `update` und `fnil` führt dazu, dass der zu
  Beginn nicht vorhandene `:stiche` Wert effektiv zu `[]` wird und
  __diesem__ Wert dann der `tisch` hinzugefügt wird.

  __REPL:__

		hearts.core=> (doc update-in)
		-------------------------
		clojure.core/update-in
		([m [k & ks] f & args])
		  'Updates' a value in a nested associative structure, where ks is a
		  sequence of keys and f is a function that will take the old value
		  and any supplied args and return the new value, and returns a new
		  nested structure.  If any levels do not exist, hash-maps will be
		  created.
		nil
		hearts.core=> (update-in {:foo {}} [:foo :fred] (fnil conj []) :uh-oh :duh!)
		{:foo {:fred [:uh-oh :duh!]}}
		hearts.core=> (update-in {:foo {:fred [:bar]}} [:foo :fred] (fnil conj []) :uh-oh :duh!)
		{:foo {:fred [:bar :uh-oh :duh!]}}


  `runde` liefert also eine Map mit dem Spieler `b`, der `:sticht` und
  der Alle-Spieler-Map, in der `b` den `tisch` in `:stiche` (ein
  Vektor) eingesammelt hat. `let` kann genau wie `when-let` mehrere
  Formen auswerten und liefert den Wert der letzten Form.

  __REPL:__

		hearts.core=> (let [x 1] (println x) (inc x))
		1
		2


* falls wir in `x` aber noch einen Spieler haben (`if-not`
  "else"-Zweig) , der nun legen muss, ermitteln wir erst was er auf
  der `hand` hat und dann welche Karte `k` er legen will.

  Der `tisch` ist entweder noch leer (leerer Vektor `[]`; erster
  Schleifendurchlauf) oder hat schon Karten. Nun fügen wir dem `tisch`
  eine neue "gelegte Karte" mit `:karte k` und `:spieler b` zu und
  binden diesen Wert an einen neuen lokalen Namen `tisch`.

  Schließlich "updaten" wir noch die `:hand` des Spielers `x` und
  "entziehen" (`disj`) ihm die Karte `k`.

* damit haben wir das "Delta" an unserem Spielzustand berechnet und in
  Alle-Spieler-Map `s` und den `tisch` __eingebaut__ und können nun
  mit dem nächsten Spieler in dieser Runde fortfahren.

  __Wichtig:__ wir haben __nichts__ geändert, so wie man ein Objekt in
  Java ändern würde. Wir haben ausschließlich __neue__
  __unveränderliche__ __Objekte__ aus __bestehenden__
  __unveränderlichen__ __Objekten__ konstruiert. Die __neuen__ `s` und
  `tisch` sind unser neuer Aggregatszustand.

  Nun rufen wir die `loop` Schleife via `recur` mit den neuen
  Argumentwerten für die `loop`-Bindungen auf: also den verbleibenden
  Spielern `xs`, der neuen Alle-Spieler-Map `s` und dem aktualisierten
  `tisch`.

  Der Aufruf sieht so aus, als wenn hier eine "Rekursion" [1] erfolgen
  würde. Und Rekursion "kostet Stack-Space" und führt irgendwann zum
  Stackoverflow.

  In Funktionalen Programmiersprachen wird aber häufig eine
  Optimierung durchgeführt, die sich __Endrekursion__ [2]
  nennt. Endrekursion führt zu einem __iterativen__
  __Berechnungsprozess__ [3] (nicht zu einem __rekurisven__) und
  dieser benötigt __keinen__ __Stackspace__.

  `recur` ist so ein __endrekursiver__ __Rekursionsaufruf__. Er kann
  nur erfolgen, wenn die `recur`-S-Expression die letzte ist, die
  ausgewertet wird. Andernfalls kommmt es zu einem Compile-Fehler.

Fertig.

__Anmerkung__: in imperativen Programmiersprachen kann man
versehentlich Endlosschleifen programmieren, weil man das
Abbruchkriterium nicht richtig formuliert. Das kann in funktionalen
Programmiersprachen auch passieren. Die `loop` Schleife unten
terminiert garantiert, weil wir von `xs` immer ein Element
__entfernen__ (und `xs` zu Beginn vier Elemente hat) und damit
schließlich in einen Zustand kommen, in dem `x` _falsy_ (hier `nil`)
ist. Und damit laufen wir nicht mehr in das `recur`.

[1] https://de.wikipedia.org/wiki/Rekursion  
[2] https://de.wikipedia.org/wiki/Endrekursion  
[3] https://de.wikipedia.org/wiki/Iterative_Programmierung  

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

`defn` unterstützt auch die Definition von mehreren Arities. Die
Funktion `spiel` hat die Arity-0 und die Arity-1.

Die Arity-0-Variante ruft die Arity-1-Variante mit `(spiel (geben!))`
auf. D.h. die Arity-1-Variante hat als Argument `gegeben` die
Alle-Spieler-Map zu Spielbeginn nach dem Geben. Diese Variante habe
ich eingeführt, um die Funktion besser testen zu können.

Auch das `spiel` läuft in `loop`-`recur`-Schleifen, nämlich über die
Runden. `s` wird zu Beginn an die Alle-Spieler-Map `gegeben` gebunden,
`b` an den Spieler, der `beginnt` und der Rundenzähler `r` bekommt den
Wert `1`.

* `if-let` wird benutzt, um die `runde` mit dem aktuellen Spielzustand
  aufzurufen. Falls `runde` _truthy_ ist, werden in `if-let` die
  lokalen Namen `s` und `b` an die neuen Werte aus der `runde`
  gebunden und es wird sofort mit diesen Werten via `recur` der
  nächste Schleifendurchlauf gemacht.

  __Wichtig:__ `recur` ist kein "imperatives goto", sonder es verhält
  sich wie ein "Funktionsaufruf ohne Stackverbrauch", der Argumente
  und einen Rückgabe-Wert hat und genau dieser Rückgabe-Wert wird
  durch die `recur`-Form "geliefert".

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
