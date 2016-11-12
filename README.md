# Java-Fotosortierer
Dieses Programm sortiert Fotos aus mehreren Unterordnern in einen Hauptordner und benannt die Fotos um.

##Programmzweck
Der Nutzer sucht sich einen Ordner seiner Wahl aus und lässt das Programm alle Unterordner nach Dateien durchsuchen. Diese werden mit dem Namen ihres Unterordners und einem Dateizähler als neuem Dateinamen allesamt in den Hauptordner gebracht.

##Konfigurationsmöglichkeiten
Folgende Programmeinstellungen sind möglich:
* Pfad wählen: Der zu sortierende Hauptordner ist wählbar.
* Doppelte Dateien löschen: Das Programm kopiert bei mehrfach vorhandenen Dateien jede nur einmal.
* Kopieren oder Verschieben: Der Nutzer wählt aus, ob alle Dateien nach der Sortierung verschoben oder kopiert werden, also ob die Ursprungsdateien beibehalten werden.
* Dateitypen: Der Nutzer gibt eine Liste von zu sortierenden Dateiendungen ein.

## Programmentwicklung
Das Programm wurde mit Java in Eclipse auf einem macOS 10.12 Rechner programmiert.

##Geprüfte Sonderfälle
Die folgenden Situationen sind bisher auf Korrektheit geprüft worden:

###Betriebssysteme
* macOS 10.12+

###Java-Version
* Java 8

###Sortiersituationen
* Doppelte Ordnernamen
* Doppelte Dateien (bisher jpeg, jpg, png)
* Leer- & Sonderzeichen in Ordner- und Dateinamen
* Leere Ordner
* Dateien im Hauptordner
