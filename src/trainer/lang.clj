;; language data

(def lang-idx {:english 0 :svenska 1 :deutsch 2})
(def lang-struct
  "Holds labels in corresponding language; [english, svenska, deutsch]"
  {:question ["Question" "Fråga" "Frage"]
   :answer ["Answer" "Svar" "Antwort"]
   :prob ["Probability" "Sannolikhet" "Wahrscheinlichkeit"]
   :next ["Next" "Nästa" "Nächstes"]
   :hint ["Hint" "Tips" "Hinweis"]
   :file ["File" "Fil" "Datei"]
   :save-database ["Save database" "Spara databas" "Speichere Daten"]
   :exit ["Exit" "Avsluta" "Beenden"]
   :save ["Save" "Spara" "Übernehmen"]
   :cancel ["Cancel" "Avbryt" "Abrechen"]
   :delete ["Delete" "Radera" "Entfernen"]
   :trainer-add-edit ["Trainer Add/Edit" "Trainer Ny/Ändra" "Trainer Neu/Ändern"]
   :item ["Item" "Fråga" "Frage"]
   :add-item ["Add item" "Ny fråga" "Neue Frage"]
   :edit-item ["Edit item" "Ändra frågan" "Frage änderen"]
   :show-all-items ["Show all items" "Visa alla frågor" "Zeige alle Fragen"]
   :increase-prob ["Increase Probability" "Öka sannolikhet" "Erhöhe Wahrscheinlichkeit"]
   :decrease-prob ["Decrease Probability" "Sänka sannolikhet" "Senke Wahrscheinlichkeit"]
   :options ["Options" "Optioner" "Optionen"]
   :language ["Language" "Språk" "Sprache"]
   :help ["Help" "Hjälp" "Hilfe"]
   :about ["About" "Om" "Über"]
   :about-text (vec
                (map str ["Trainer is a program helping learn something by heart"
                          "Trainer är ett program som hjälper att lära sig något utantill"
                          "Trainer ist ein Programm, welches beim Auswendiglernen hilft"]
                     (repeat "\n
Copyright (C) 2012 Volker Sarodnick\n\n
Distributed under the Eclipse Public License, the same as Clojure.")))
   :all-items ["All items" "Alla frågor" "Alle Fragen"]
   :ask-saving ["Database changed, save it?" "Databas ändrad, spara den?" "Daten geändert, speichern?"]
   :restart-required ["To get the new language at all labels you
have to restart the program.
Don't forget to save the database."
                      "Programmet måste startas om för att byta
språket på alla ställen.
Glöm inte att spara databasen."
                      "Das Programm muß neu gestartet werden, damit
die Spracheinstellungen überall aktiv werden.
Speichern der Daten nicht vergessen!"]
   })
