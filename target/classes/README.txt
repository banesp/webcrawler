# Grunnleggende funksjonalitet
Initialiseres på normalt hvis, enten med tom konstruktør eller en verdi med antall ordforekomster.
Ordene fra 'words.txt', som ikke forekommer i 'stopwords.txt' hentes fra fil(Tar utgangspunkt i at disse filene ligger på root).
Web-crawlingen startes med crawlFrom(param. Start-URL). Default søkealgoritme er breadthfirst, dette kan endres
ved tilgangsmetodene. Selve algoritmen for søkene ligger i crawl().
Interfacet 'Datastructure' brukes som en wrapper, hvorav klassene 'WrappedQueue' og 'WrappedStack' implementerer interfacet
slik at begge strukturene kan bruke crawl().
HashMapet lagrer alle ordforekomster med value av alle URLene.
While-loopen i crawl() vil genere en ny no.woact.banesp14.webcrawler.WebPageReader for hver iterasjon, legge nye linker i queue/stack,
lagre besøkte linker i 'visitedLinks'.
Når crawlet er avsluttet på grunn av tom queue/stack eller max ordforekomster nådd, vil collectGarbage() nulle objekter som
ikke lenger er i bruk.
Deretter kan en altså bruke searchHits() for å sjekke forekomster.

# Begrensning av indekseringen
Begrensningen settes i fetchAllowedWordsFromFile(). Her brukes princeton-biblioteket for å scanne fil.
Lagrer først alle stopwords i et lokalt Set. Om ordene fra words.txt ikke forekommer i 'stopwordSet' legges de da
til i allowedWords(Som brukes til å legge til ord i HashMapet).

# Traverseringsorden
Algoritmen er tilpasset å fungere med både breadthFirst og depthFirst gjennom samme metode.
Dette velges da med tilgangsmetodene som setter en boolean som velger hvilken datastruktur som skal holde lenkene
(breadFirst = Queue, depthFirst = Stack).

# Effektivisering
En rekke tiltak er gjort for effektivisering.
Det er brukt  HashSets for values i HashMapet, 'allowedWords', 'visitedLinks' og 'stopwordSet'.
Denne strukturen tillater ikke duplikater, slik at det ikke tas opp unødvendig med minne underveis. Samt søk på O(1) er
bra.
Datastructure med tilhørende klasser som implementerer interfacet, er en praktisk måte å holde antall kodelinjer nede i
MyEngine.
Her får en heller en crawl-metode. Istedet for kanskje 2 forskjellige metoder som gjør nesten det samme, men
bare bruker forskjellige hent ut og legge til(enqueue/dequeue, push/pop) metoder.
collectGarbage() kjøres etter crawlet er gjennomført og frigjør brukte ressurser som ikke lenger er nødvendige, men
som tar opp minneplass.

Jeg har også lagt til et filter som hindrer å legge til lange URLer i datastrukturen. Dette er fordi spesielt
depthSearch har en tendens til å grave seg ned på URLer som inneholder veldig få ord samt lange parameteriserte URLer.
Lagring av slike linker tar opp kolosale mengder minne, men inneholder ofte veldig få ord(Dermed er det ønskelig
å ikke kaste bort ressurser på de).Om du ønsker å kjøre uten, eller med lengre URLer er det bare å endre 'MAX_SIZE_FOR_LINKS'.